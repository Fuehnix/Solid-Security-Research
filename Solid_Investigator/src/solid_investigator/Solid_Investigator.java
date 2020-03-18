/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package solid_investigator;


import java.io.File;
import java.io.FileWriter;
import java.io.BufferedWriter;
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
import java.util.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
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
 * <i> Solid Spintax Investigator</i>
 * <p>
 * Command-line utility designed to trace Solid Spintax document Leaks to their source.
 * <p>
 * For command-line arguments, run with --help.
 * <p>
 * For additional documentation, check the
 * <a href="https://github.com/SolidSecurity/Solid-Spintax-Spinner">GitHub</a>
 * repository.
 * <p>
 Uses the
 * <a href="https://github.com/SolidSecurity/Solid-Spintax-Specification">Solid Spintax</a>
 * standard from <i>Solid Security</i>.
 * @author Solid Security
 * @author Jacob Fuehne
 * @author Vivek Nair
 * @author Thomas Quig
 * @version 1.0.1
 * @since 1.0.0
 */
public class Solid_Investigator {
    private static final String INVESTIGATOR_VERSION = "1.0.1";
    public static final String SPINTAX_VERSION = "1.0.1";

    /**
     * Main method, runs upon program execution.
     * @param args the command line arguments
     */
    public static void main(String[] args) throws IOException{
        printHeader();
        ArrayList<String> algorithms = 
        new ArrayList<String>(Arrays.asList("cosine", "hamming", "jaccard", 
                            "jarowinkler", "levenshteindetailed",
                            "levenshtein", "longestcommonsubsequence"));
        

        ArgumentParser parser = ArgumentParsers.newFor("investigator")
                .addHelp(false)
                .defaultFormatWidth(80)
                .build()
                .description("Command-line utility to match tag and leaked document to eachother.");

        parser.addArgument("spintaxfile")
                .metavar("<FILE>")
                .nargs("?")
                .help("spintax file that the spinner used to generate the leaked document");
        parser.addArgument("leakedfile")
                .metavar("<FILE>")
                .nargs("?")
                .help("the text file of the leaked document");
        parser.addArgument("tagdatabasefile")
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
                .type(Integer.class)
                .help("sets the cost per substitution when computing edit distance");
        costs.addArgument("-ic", "--insertion-cost")
                .metavar("<#>")
                .type(Integer.class)
                .help("sets the cost per insertion when computing edit distance");
        costs.addArgument("-dc", "--deletion-cost")
                .metavar("<#>")
                .type(Integer.class)
                .help("sets the cost per deletion when computing edit distance");

        ArgumentGroup output = parser.addArgumentGroup("output")
                .description("specify where output files should be stored");
        output.addArgument("-d", "--data")
                .metavar("<FILE>")
                .help("outputs the modelling data to the provided file");
        
        ArgumentGroup modes = parser.addArgumentGroup("modes")
                .description("specify in which way the investigator determines the leak source.");
        modes.addArgument("-a", "--algorithm")
                .metavar("<ALGO>")
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
        
        int substitution_cost, insertion_cost, deletion_cost;
        if(res.get("substitution-cost") != null){
            substitution_cost = res.get("substitution_cost");
        } else{
            substitution_cost = 1;
        }
        if(res.get("insertion-cost") != null){
            insertion_cost = res.get("insertion_cost");
        } else{
            insertion_cost = 1;
        }
        if(res.get("deletion-cost") != null){
            deletion_cost = res.get("deletion_cost");
        } else{
            deletion_cost = 1;
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
                System.out.println("Solid Spintax Investigator Version: " + INVESTIGATOR_VERSION);
            }
            System.exit(0);
        }

        if (res.get("tagdatabasefile") == null || res.get("spintaxfile") == null || res.get("leakedfile") == null){
            if(res.get("spintaxfile") == null)
                System.out.println("Error: Spintax File Missing");
            if(res.get("leakedfile") == null)
                System.out.println("Error: Leaked File Missing");
            if(res.get("tagdatabasefile") == null)
                System.out.println("Error: Tag Database File Missing");
            System.out.println("One or more files that are required are missing, exiting...");
            System.exit(1);
        }
        String tag_database_filename = res.get("tagdatabasefile");
        String spintax_filename = res.get("spintaxfile");
        String leaked_filename = res.get("leakedfile");

        String spintax_content = null;
        try{
            spintax_content = readFileAsString(spintax_filename);
        } catch (Exception e){
            System.out.println("Reading the spintax file encountered an error, does it exist? Exiting...");
        }

        String tag_database_content = null;
        try{
            tag_database_content = readFileAsString(tag_database_filename);
        } catch (Exception e){
            System.out.println("Reading the tag database file encountered an error, does it exist? Exiting...");
        }

        String leaked_content = null;
        try{
            leaked_content = readFileAsString(leaked_filename);
        } catch (Exception e){
            System.out.println("Reading the leaked file encountered an error, does it exist? Exiting...");
        }
        
        String algorithm = null;
        if(res.get("algorithm") != null){
            algorithm = res.get("algorithm");
            if(!algorithms.contains(algorithm)) {
                System.out.println("Algorithm '" + algorithm + "'does not exist, please select from the following.");
                for(String algo : algorithms){
                    System.out.println("\t"+ algo);
                }
                System.out.println("\nExiting...");
                System.exit(1);
            }
            algorithm = algorithm.toLowerCase();
        }
        
        
        
        String modelling_filename = null;
        if (res.get("data") != null) {
            modelling_filename = res.get("data");
        }
        
        Map<String, String> tagDatabase = new HashMap<String,String>();
        if(tag_database_content != null){
            String[] lines = tag_database_content.split("\n");
            for(String line: lines){
                String[] values = line.split(",");
                tagDatabase.put(values[0],values[1]);
            }
        } else{
            System.out.println("Tag database ecountered an unknown error, Exiting...");
            System.exit(1);
        }
        
        String[] identArgs = {spintax_filename, leaked_filename, algorithm, modelling_filename,
                                "" + insertion_cost, "" + deletion_cost, "" + substitution_cost};
        String leaker = identify(leaked_content,spintax_content,tagDatabase,identArgs);
        System.out.print(tagDatabase.size() + " permutations have been analyzed\nThe suspected leaker is: "); 
        System.out.println(leaker.split(",")[1]);
    }
    
    /**
     * 
     * @param leak The leak text that will be investigated. This is the leak text
     *      and not the leak filename, as the text is processed in main.
     * @param spintax The spintaxt text that will be investigated. This is also the
     *      data itself and not the filename.
     * @param tagDatabase The tag database is a map that exists with key as tag,
     *      and value as user. This is so users can be identified based on their tags.
     * @param args Args consists of several argument values to be used in some algorithmic
     *      or optional cases. They are as follows.
     *      [0] spintax_filename: used with -d, included in log metadata.
     *      [1] leaked_filename: used with -d, included in log metadata.
     *      [2] algorithm: determines the algorithm used in distance calculation.
     *                      the default is Levenshtein distance. Additionally, this sets
     *                      the type of distance. For the purposes of logging however, all
     *                      distances are in doubles. (Important for later type conformity)
     *      [3] modelling_filename: the name of the file that the model is updated into.
     *      [4-6] insertion/deletion/substitution cost. Used with LevenshteinDetailedDistance
     *              determines the cost for each individual distance factor. By default these
     *              are set to 1 and LevenshteinDetailedDistance will behave as a Levenshtein
     *              distance.
     * @return The name of the primary suspect of the leaks, if two are equal, 
     * then the latter is chosen. If the log option exists, all outputs are stored
     * in the log file.
     */
    public static String identify(String leak, String spintax, 
        Map<String, String> tagDatabase, String[] args){
        String algorithm = args[2];
        SolidSpintaxElement spintaxE;
        spintaxE = SolidSpintaxSpinner.parse(spintax);
        
        Map<String, String> permutations = createPermutationMap(spintaxE, tagDatabase);
        int numPermutations = permutations.size();
        int numSwitches = spintaxE.countSwitches();
        Map<String,Double> tagToDist = new HashMap<String,Double>();
        
        String mostLikelyLeaker = "";
        double minimumDistance = Double.POSITIVE_INFINITY;
        
        
        EditDistance<Integer> edistInt = null;
        EditDistance<Double> edistDouble = null;
        EditDistance<LevenshteinResults> edistDetail = null;

        boolean useDetail = false;
        boolean useDouble = false; 
        if(algorithm == null){
            edistInt = new LevenshteinDistance();
        }
        else if(algorithm.equals("cosine")){
            edistDouble = new CosineDistance();
            useDouble = true;
        }
        else if(algorithm.equals("hamming")){ //TODO Figure out what to do if not same strlen
            edistInt = new HammingDistance();
        }
        else if(algorithm.equals("jaccard")){
            edistDouble = new JaccardDistance();
            useDouble = true;
        }
        else if(algorithm.equals("jarowinkler")){
            edistDouble = new JaroWinklerDistance();
            useDouble = true;
        }
        else if(algorithm.equals("longestcommonsubsequence")){
            edistInt = new LongestCommonSubsequenceDistance();
        }
        else if(algorithm.equals("levenshteindetaileddistance")){
            edistDetail = new LevenshteinDetailedDistance();
            useDetail = true;
        }
        else{
            edistInt = new LevenshteinDistance();
        }
           
        for(Map.Entry<String,String> entry : permutations.entrySet()){
            String docPerm = entry.getValue();
            String currTag = entry.getKey();
            
            double currDist;
            if(useDouble)
                currDist = edistDouble.apply(leak,docPerm);
            else if(useDetail){
                LevenshteinResults levres = edistDetail.apply(leak,docPerm);
                currDist = (double)( (levres.getInsertCount() * Integer.parseInt(args[4])) +
                                     (levres.getDeleteCount() * Integer.parseInt(args[5])) +
                                     (levres.getSubstituteCount() * Integer.parseInt(args[6]))
                         );
            }
            else
                currDist = (double)edistInt.apply(leak,docPerm);
            
            tagToDist.put(currTag + "," + tagDatabase.get(currTag), currDist);
            if(mostLikelyLeaker.equals("")){
                mostLikelyLeaker = currTag + "," + tagDatabase.get(currTag);
                minimumDistance = currDist;
            } else{
                minimumDistance = tagToDist.get(mostLikelyLeaker);
                if(minimumDistance > currDist){
                    mostLikelyLeaker = currTag + "," + tagDatabase.get(currTag);
                }
            }
            /*
            System.out.println("[LOG: " + getCurrentDate() + ")]\nSuspect: " + tagDatabase.get(currTag) +
                "\n Permutation: \"" + docPerm.replaceAll("\n","\\\\n") +
                "\"\n Distance: " + currDist + "\n");
            */
        }
        
        if(args[3] != null){
            String metadata = args[0] + "," + args[1] + ","  + numSwitches + "," + numPermutations + 
            "," + "something" + "," + ((args[2] != null) ? args[2] : "Levenshtein");
            try{
                writeModelToFile(args[3],metadata,tagToDist);
            }
            catch(Exception e){
                System.out.println("Model data was unable to be written to file, errmsg: " + e);
            }
        }
        else
        {
            System.out.println("Model not found");
        }
        
        return mostLikelyLeaker;
    }
    
    
    /**
     * Creates a permutation map of all the tags that exist in the tag database.
     * @param spintax The Spintax that will be used to generate all permutations
     * @param tagDatabase The database of tags that are to be permuted on.
     * @return The Map of Tag to permutation.
     */
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
    
    
    /**
     * Read the entirety of the file into a string.
     * @param fileName the name of the file to be read as a string.
     * @return  The data content as a string.
     * @throws Exception File not found and IOException.
     */
    private static String readFileAsString(String fileName) throws Exception {
        String data = new String(Files.readAllBytes(Paths.get(fileName)));
        return data;
    }
    /**
     * Writes the model (including metadata) to the file at filename.
     * @param filename The filename where the model will be stored.
     * @param metadata The metadata associated with the model file.
     * @param modelTable The model table, username and tag -> distance.
     * @throws Exception IOException in case IO fails for some reason.
     */
    private static void writeModelToFile(String filename, String metadata,
        Map<String,Double> modelTable) throws Exception {
        BufferedWriter writer = new BufferedWriter(new FileWriter(filename));
        String[] metaArray = metadata.split(",");
        if(metaArray.length == 6){
            writer.write("# Solid Spintax Investigator\n");
            writer.write("# spintax: " + metaArray[0] + "\n");
            writer.write("# document: " + metaArray[1] + "\n");
            writer.write("# switches: " + metaArray[2] + "\n");
            writer.write("# permutations: " + metaArray[3] + "\n");
            writer.write("# characters: " + metaArray[4] + "\n");
            writer.write("# algorithm: " + metaArray[5] + "\n");
            writer.write("# date: " + getCurrentDate() + "\n");
        }
        
        writer.newLine();
        
        for(Map.Entry<String,Double> entry  : modelTable.entrySet()){
            writer.write(entry.getKey() + ":" + entry.getValue());
            writer.newLine();
        }
        writer.close();
    }
    
    
    /**
     * Gets the current date and time in proper format.
     * @return The current time.
     */
    private static String getCurrentDate(){
    DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
	Date date = new Date();
        return dateFormat.format(date);
    }
    private static void printHeader() {
        System.out.print("\n"
                + "       /MMM/ yMMm .MMMs                                                         \n"
                + "       /MMMmdNMMMddMMMs                                                         \n"
                + "+ ++++++ssssdMMMMMMMMMo    -\\\\\\\\\\\\\\\\\\   `:///////-  `/-         -/` -/////////. \n"
                + " `/`++++smNNNNNNMMMMy-    oMy          /NhoooooooNh .My         sM: sMsoooooosMs\n"
                + "           syyyyMMM.      +My++++++oo. hM.       yM .My         sM: sM-       NM\n"
                + "    `/.++++sssssMMM`               sMy hM.       yM .My         sM: sM-       NM\n"
                + "     `/.+++mNNNNMMM`      +My++++++yMs /NhoooooosNh .Md+++++++: sM: sMsooooooyMo\n"
                + "         :syyyyhMMMh/      -////////-   `:///////.  `/////////- -/` -////////:` \n"
                + "  `/`+++ssssmMMMMMMMMN`                                                         \n"
                + " :./++++NMMMMMMMMMMMMM.                                                         \n"
                + "================================================================================\n\n"
                + "Solid Spinner v" + INVESTIGATOR_VERSION + " (Solid Spintax Standard v" + SPINTAX_VERSION + ")\n");
    }
}
