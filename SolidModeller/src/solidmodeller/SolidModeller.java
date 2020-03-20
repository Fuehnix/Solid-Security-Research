/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package solidmodeller;

///**

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Map;
import java.util.TreeMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.sourceforge.argparse4j.ArgumentParsers;
import net.sourceforge.argparse4j.impl.Arguments;
import net.sourceforge.argparse4j.inf.ArgumentGroup;
import net.sourceforge.argparse4j.inf.ArgumentParser;
import net.sourceforge.argparse4j.inf.ArgumentParserException;
import net.sourceforge.argparse4j.inf.Namespace;
import org.apache.commons.math.MathException;

// * <i> Solid Modeller</i>
// * <p>
// * Command-line utility designed to determine the most likely source of a leak
// * <p>
// * For command-line arguments, run with --help.
// * <p>
// * @author Solid Security
// * @author Jacob Fuehne
// * @author Vivek Nair
// * @version 1.0.0
// * @since 1.0.0
// */
public class SolidModeller {
    private static final String MODELLER_VERSION = "1.0.0";
    public static final String SPINTAX_VERSION = "1.0.0";
    public static final int MAX_TAG_DATABASE_SIZE = 100000;
    public static final int MAX_LOG_SIZE = 1000;
    public static final int CHARACTER_LIMIT = 50;
    
    /**
     * Main method, runs upon program execution.
     * @param args the command line arguments
     * @throws java.lang.Exception throws various exceptions, seen in the class.  
     * These exceptions print error messages, then end the program.
     */
    public static void main(String[] args){
        printHeader();
        //handles args last
        
        ArgumentParser parser = ArgumentParsers.newFor("modeller")
                .addHelp(false)
                .defaultFormatWidth(80)
                .build()
                .description("Command-line utility to match tag and leaked document to eachother.");
        parser.addArgument("leakedfile")
                .metavar("<FILE>")
                .nargs("?")
                .help("the text file of the leaked document");
        parser.addArgument("outputmodel")
                .metavar("<FILE>")
                .nargs("?")
                .help("the file created by the investigator with similarity distances against the leaked document");

        ArgumentGroup information = parser.addArgumentGroup("information")
                .description("information about this program");
        information.addArgument("-h", "--help")
                .action(Arguments.storeTrue())
                .help("shows this help message");
        information.addArgument("-v", "--version")
                .action(Arguments.storeTrue())
                .help("show version information");
        ArgumentGroup input = parser.addArgumentGroup("input")
                .description("specify where output files should be stored");
        input.addArgument("-i", "--input")
                .metavar("<input_model>")
                .help("input model to update ");
        ArgumentGroup values = parser.addArgumentGroup("values")
                .description("specify the value to affect calculations");
        values.addArgument("-t", "--threshold ")
                .metavar("<double>")
                .help("p-val for statistical significance");
        
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
                System.out.println("Solid Spintax Modeller Version: " + MODELLER_VERSION);
            }
            return;
        }
        String leakDataFileName = res.get("leakedfile");
        String outputModelFileName = res.get("outputModel");
        leakDataFileName = "basic-leaked.txt";
        outputModelFileName = "testModel.out";
        String leakString = "";
        String outputModelString = "";
        try {
            leakString = readFileAsString(leakDataFileName);
//            System.out.println(leakString);
        } catch (Exception ex) {
            System.out.println("Error: could not read " + leakDataFileName);
            return;
        }
        try {
            outputModelString = readFileAsString(outputModelFileName);
//            System.out.println(outputModelString);
        } catch (Exception ex) {
            System.out.println("Error: could not read " + outputModelFileName);
            return;
        }
        SolidModelRecord testRecord = new SolidModelRecord(outputModelString);
        System.out.println(testRecord.toString());
        SolidModel testModel = new SolidModel(testRecord);
        SolidModel testModel2 = new SolidModel(testRecord);
        testModel2.add(testModel);
        try {;
            System.out.println(testModel2.printStatistics());
        } catch (MathException ex) {
            return;
        }
        
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
