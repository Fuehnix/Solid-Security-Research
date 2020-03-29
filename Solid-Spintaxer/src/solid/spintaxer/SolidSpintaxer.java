/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package solid.spintaxer;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.sourceforge.argparse4j.ArgumentParsers;
import net.sourceforge.argparse4j.impl.Arguments;
import net.sourceforge.argparse4j.inf.ArgumentGroup;
import net.sourceforge.argparse4j.inf.ArgumentParser;
import net.sourceforge.argparse4j.inf.ArgumentParserException;
import net.sourceforge.argparse4j.inf.Namespace;

/**
 * <i> Solid Spintaxer</i>
 * <p>
 * Command-line utility to create Solid Spintax from a regex ruleset
 * <p>
 * For command-line arguments, run with --help.
 * <p>
 * @author Solid Security
 * @author Jacob Fuehne
 * @author Vivek Nair
 * @version 1.0.0
 * @since 1.0.0
 */
public class SolidSpintaxer {
    private static final String SPINTAXER_VERSION = "1.0.0";
    public static final String SPINTAX_VERSION = "1.0.0";
    
    /**
     * Main method, runs upon program execution.
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        printHeader();
        
        ArgumentParser parser = ArgumentParsers.newFor("spintaxer")
                .addHelp(false)
                .defaultFormatWidth(80)
                .build()
                .description("Command-line utility to create Solid Spintax from a regex ruleset");
        parser.addArgument("inputfile")
                .metavar("<FILE>")
                .nargs("?")
                .help("The file that should be modified with Solid Spintax");
        parser.addArgument("inputfolderdirectory")
                .metavar("<FOLDER>")
                .nargs("?")
                .help("the folder of input regex rules");
        parser.addArgument("outputfolderdirectory")
                .metavar("<FOLDER>")
                .nargs("?")
                .help("the folder of output regex rules");

        ArgumentGroup information = parser.addArgumentGroup("information")
                .description("information about this program");
        information.addArgument("-h", "--help")
                .action(Arguments.storeTrue())
                .help("shows this help message");
        information.addArgument("-v", "--version")
                .action(Arguments.storeTrue())
                .help("show version information");

        Namespace res = null;
        
        try{
            res = parser.parseArgs(args);
        } catch (ArgumentParserException e) {
            parser.handleError(e);
            return;
        }
        
        if(res == null){
            System.out.println("Args could not be parsed, Exiting...");
        }
        
        if (res.get("help")) {
                if (args.length > 1) {
                    System.out.println("\nNOTE: --help specified; all other arguments ignored\n");
                }
                parser.printHelp();
                return;
        }

        if (res.get("version")) {
            if (args.length > 1) {
                System.out.println("\nNOTE: --version specified; all other arguments ignored\n");
                System.out.println("Solid Spintax Modeller Version: " + SPINTAXER_VERSION);
            }
        }
        
        if (res.get("inputfile") == null) {
            System.out.println("\nERROR: Please specify an input file.");
            return;
        }
        
        if (res.get("inputfolderdirectory") == null) {
            System.out.println("\nERROR: Please specify a folder of input rule files.");
            return;
        }
        
        if (res.get("outputfolderdirectory") == null) {
            System.out.println("\nERROR: Please specify a folder of output rule files");
            return;
        }
        
        String inputFile = res.get("inputfile");
        String inputRuleDirectory = res.get("inputfolderdirectory");
        String outputRuleDirectory = res.get("outputfolderdirectory");
        ArrayList<String> inputRules = new ArrayList<>();
        ArrayList<String> outputRules = new ArrayList<>();
        ArrayList<SolidSpintaxerRule> ruleset = new ArrayList<>();
        String inputString = "";
        try {
            inputString = readFileAsString(inputFile);
        } catch(Exception ex){
            System.out.println("readInputString");
            System.out.println(ex);
        }
        try {
            inputRules = readFilesInFolder(inputRuleDirectory);
        } catch (Exception ex) {
            System.out.println("readInputDirectory");
            System.out.println(ex);
            return;
        }
        try {
            outputRules = readFilesInFolder(outputRuleDirectory);
        } catch (Exception ex) {
            System.out.println("readOutputDirectory");
            System.out.println(ex);
            return;
        }
        try {
            ruleset = createRulesFromFiles(inputRules,outputRules);
        } catch (Exception ex) {
            System.out.println("createRulesFromFiles");
            System.out.println(ex);
            return;
        }
        try {
            for(int i = 0; i < ruleset.size();i++){
//                System.out.println(ruleset.get(i).toString());
                inputString = ruleset.get(i).applyOutputRule(inputString);
//                System.out.println("new input string is: "+ inputString);
            }
        } catch (Exception ex){
            System.out.println("applyingRuleset");
            System.out.println(ex);
        }
        System.out.println("Finished applying ruleset");
        System.out.println(inputString);
        
    }
    
    private static ArrayList<String> readFilesInFolder(String folderPath) throws Exception{
//        System.out.println("readFilesInFolder");
//        System.out.println(folderPath);
        File folder = new File(folderPath);
        File[] listOfFiles = folder.listFiles();
        ArrayList<String> fileNameList = new ArrayList<>();
        for (File file : listOfFiles) {
            if (file.isFile()) {
//                System.out.println(file.getName());
                fileNameList.add(folderPath + "/" + file.getName());
            }
        }
        return fileNameList;
    }
    
    private static ArrayList<SolidSpintaxerRule> createRulesFromFiles(ArrayList<String> inputRules, ArrayList<String> outputRules) throws Exception{
//        System.out.println("createRulesFromFiles");
        ArrayList<SolidSpintaxerRule> ruleset = new ArrayList<>();
        if(inputRules.size() != outputRules.size()){
            System.out.println("Error: there are " + inputRules.size() + 
                    "input rules and " + outputRules.size() +
                    "output rules. They must be equal.");
            throw new Exception(); //THIS IS A PLACEHOLDER, REPLACE WITH SPECIFIC ERROR IN FINAL VERSION
        }
        for(int i = 0; i < inputRules.size(); i++){
            String inputRule = "";
            String outputRule = "";
            inputRule = readFileAsString(inputRules.get(i));
            outputRule = readFileAsString(outputRules.get(i));
            String inputRuleName = (inputRules.get(i).toString().split("/")[1].toString()).split("\\.")[0];
            String outputRuleName = (outputRules.get(i).toString().split("/")[1].toString()).split("\\.")[0];
            if(!inputRuleName.equals(outputRuleName)){
                System.out.println("Error: input rule: "+ inputRuleName +
                        " does not match output rule: " + outputRuleName);
                throw new Exception(); //THIS IS A PLACEHOLDER, REPLACE WITH SPECIFIC ERROR IN FINAL VERSION
            }
            SolidSpintaxerRule rule = new SolidSpintaxerRule(inputRule,outputRule);
            ruleset.add(rule);
        }
        return ruleset;
    }
    
    /**
     * Read the entirety of the file into a string.
     * @param fileName the name of the file to be read as a string.
     * @return  The data content as a string.
     * @throws Exception File not found and IOException.
     */
    private static String readFileAsString(String fileName) throws Exception {
//        System.out.println("readFileAsString");
        String data = new String(Files.readAllBytes(Paths.get(fileName)));
//        System.out.println(data);
        return data;
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
                + "SolidModeller v" + "1.0.0" + " (Solid Spintax Standard v" + SPINTAX_VERSION + ")\n");
    }
}
