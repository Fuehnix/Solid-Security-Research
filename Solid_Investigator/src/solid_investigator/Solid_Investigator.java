/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package solid_investigator;


import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Dictionary;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;
import java.util.Scanner;
import java.util.Arrays;
import solid.spintax.spinner.*;
import solid.spintax.spinner.SolidSpintax.*;
import org.apache.commons.text.similarity.*;
import net.sourceforge.argparse4j.ArgumentParsers;
import net.sourceforge.argparse4j.impl.Arguments;
import net.sourceforge.argparse4j.inf.ArgumentGroup;
import net.sourceforge.argparse4j.inf.ArgumentParser;
import net.sourceforge.argparse4j.inf.ArgumentParserException;
import net.sourceforge.argparse4j.inf.Namespace;

/**
 *
 * @author jacob
 */
public class Solid_Investigator {
    private static StringBuilder output = new StringBuilder();

    /**
     * NOTES FOR WHEN YOU COME BACK TO THIS:
     * LOOK INTO CS 440 LECTURE, UNDER VECTOR SEMANTICS
     * ALSO, LOOK INTO WHAT IS THE BEST METHOD FOR IMPARTIAL DISTANCE
     * @param args the command line arguments
     */
    public static void main(String[] args) throws IOException{
        ArrayList<String> algorithms = 
        new ArrayList<String>(Arrays.asList("Cosine", "Hamming", "Jaccard", 
                            "JaroWinkler", "LevenshteinDetailed",
                            "Levenshtein", "LongestCommonSubsequence"));
        

        ArgumentParser parser = ArgumentParsers.newFor("investigator")
                .addHelp(false)
                .defaultFormatWidth(80)
                .build()
                .description("Command-line utility to match tag and leaked document to eachother.");

        parser.addArgument("spintax-file")
                .metavar("<FILE>")
                .nargs("?")
                .help("spintax file that the spinner used to generate the leaked document");
        parser.addArgument("leaked-file")
                .metavar("<FILE>")
                .nargs("?")
                .help("the text file of the leaked document");
        parser.addArgument("tag-database-file")
                .metavar("<FILE>")
                .nargs("?")
                .help("the file of the tag database that is checked against the leaked document");

        ArgumentGroup information = parser.addArgumentGroup("information")
                .description("information about this program");
        information.addArgument("-v", "--version")
                .action(Arguments.storeTrue())
                .help("prints the current spintax and spinner version");
        information.addArgument("-h", "--help")
                .action(Arguments.storeTrue())
                .help("shows this help message");
        
        ArgumentGroup costs = parser.addArgumentGroup("costs")
                .description("sets the cost for each difference when computing edit distance");
        costs.addArgument("-sc", "--substitution-cost")
                .metavar("<#>")
                .type(BigInteger.class)
                .help("sets the cost per substitution when computing edit distance");
        costs.addArgument("-ic", "--insertion-cost")
                .metavar("<#>")
                .type(BigInteger.class)
                .help("sets the cost per insertion when computing edit distance");
        costs.addArgument("-dc", "--deletion-cost")
                .metavar("<#>")
                .type(BigInteger.class)
                .help("sets the cost per deletion when computing edit distance");

        ArgumentGroup output = parser.addArgumentGroup("output")
                .description("specify where output files should be stored");
        output.addArgument("-d", "--data")
                .metavar("<FILE>")
                .help("outputs the modelling data to the provided file");
        output.addArgument("-l", "--log")
                .metavar("<FILE>")
                .help("uses the provided log file");
        
        ArgumentGroup modes = parser.addArgumentGroup("modes")
                .description("specify in which way the investigator determines the leak source.");
        modes.addArgument("-a", "--algorithm")
                .metavar("<FILE>")
                .help("sets the algorithm that will be run to determine edit distance");

        Namespace res = null;
        try{
            res = parser.parseArgs(args);
        } catch (ArgumentParserException e) {
            parser.handleError(e);
            System.exit(1);
        }
        if(res == null){
            System.out.println("Args could not be parsed, Exiting...");
        }
        
        BigInteger substitution_cost, insertion_cost, deletion_cost;
        if(res.get("substitution-cost") != null){
            substitution_cost = BigInteger.ONE;
        }
        if(res.get("insertion-cost") != null){
            insertion_cost = BigInteger.ONE;
        }
        if(res.get("deletion-cost") != null){
            deletion_cost = BigInteger.ONE;
        }

        if (res.get("help")) {
            if (args.length > 1) {
                System.out.println("\nNOTE: --help specified; all other arguments ignored\n");
            }
            parser.printHelp();
            System.exit(0);
        }

        if (res.get("version")) {
            if (args.length > 1) {
                System.out.println("\nNOTE: --version specified; all other arguments ignored\n");
            }
            System.exit(0);
        }

        if (res.get("tag-database-file") == null || res.get("spintax-file") == null || res.get("leaked-file") == null){
            if(res.get("spintax-file") == null)
                System.out.println("Error: Spintax File Missing");
            if(res.get("leaked-file") == null)
                System.out.println("Error: Leaked File Missing");
            if(res.get("tag-database-file") == null)
                System.out.println("Error: Tag Database File Missing");
            System.out.println("One or more files that are required are missing, exiting...");
            System.exit(1);
        }
        String tag_database_filename = res.get("tag-database-file");
        String spintax_filename = res.get("spintax-file");
        String leaked_filename = res.get("leaked-file");

        String spintax_content = null;
        try{
            spintax_content = readFileAsString(spintax_filename);
        } catch (Exception e){
            System.out.println("Reading the spintax file encountered an error, does it exist? Exiting...");
        }

        String tag_database_content = null;
        try{
            tag_database_content = readFileAsString(spintax_filename);
        } catch (Exception e){
            System.out.println("Reading the tag database file encountered an error, does it exist? Exiting...");
        }

        String leaked_content = null;
        try{
            leaked_content = readFileAsString(spintax_filename);
        } catch (Exception e){
            System.out.println("Reading the leaked file encountered an error, does it exist? Exiting...");
        }

        String algorithm = null;
        if(res.get("algorithm") != null){
            if(!algorithms.contains(algorithm))
            {
                System.out.print("Algorithm '" + algorithm + "'does not exist, please select from: ");
                for(String algo : algorithms){
                    System.out.print(algo);
                }
                System.out.println("\nExiting...");
                System.exit(1);
            }
        }

        String modelling_filename = null;
        if (res.get("data") != null) {
            modelling_filename = res.get("data");
        }

        String log_filename = null;
        if (res.get("log") != null){
            log_filename = res.get("log");
        }

        Map<String, String> tagDatabase = new HashMap<String,String>();
        if(tag_database_content != null){
            String[] lines = tag_database_content.split("\n");
            for(String line: lines){
                String[] values = line.split(",");
                tagDatabase.put(values[0],values[1]);
            }
        }
        else{
            System.out.println("Tag database ecountered an unknown error, Exiting...");
            System.exit(1);
        }
        System.out.print("The Leaker is: "); 
        String leaker = identify(leaked_content,spintax_content,tagDatabase);
        System.out.println(leaker);
    }
    
    public static String identify(String leak, String spintax, Map<String, String> tagDatabase){
        String leaker = "";
        SolidSpintaxElement spintaxE;
        spintaxE = SolidSpintaxSpinner.parse(spintax);
        Map<String, String> permutations = createPermutationMap(spintaxE, tagDatabase);
        Map<String,Integer> tagToDist = new HashMap<String,Integer>();
        String mostLikelyLeaker = "";
        for(Map.Entry<String,String> entry : permutations.entrySet()){
            String docPerm = entry.getValue();
            String currTag = entry.getKey();
            LevenshteinDistance lev = new LevenshteinDistance(); //Where did you get this? Which library
            int dist = lev.apply(leak,docPerm);
            tagToDist.put(currTag,dist);
            int mostLikelyLeakerDist;
            if(mostLikelyLeaker.equals("")){
                mostLikelyLeaker = currTag;
                mostLikelyLeakerDist = dist;
            } else{
                mostLikelyLeakerDist = tagToDist.get(mostLikelyLeaker);
                if(mostLikelyLeakerDist > dist){
                    mostLikelyLeaker = currTag;
                }
            }
            System.out.println(dist);
        }
        return mostLikelyLeaker;
    }
    
    public static Map<String, String> createPermutationMap(SolidSpintaxElement spintax, Map<String, String> tagDatabase){
        Map<String, String> permutations = new HashMap<String,String>();
        for (Map.Entry<String,String> entry : tagDatabase.entrySet()){
            String currTag = entry.getKey();
            BigInteger perm = new BigInteger(currTag,36);
            String docPerm = spintax.spin(perm);
            permutations.put(currTag, docPerm);
        }
        return permutations;
    }
    
    private static String readFileAsString(String fileName) throws Exception {
        String data = new String(Files.readAllBytes(Paths.get(fileName)));
        return data;
    }
    
}
