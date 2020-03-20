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
    private static final String INVESTIGATOR_VERSION = "1.0.1";
    public static final String SPINTAX_VERSION = "1.0.1";
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
        SolidModeller model = new SolidModeller("basic-leaked.txt","testModel.out");
        String leakDataFileName = "basic-leaked.txt";
        String outputModelFileName = "testModel.out";
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
        try {
            System.out.println(testModel2.printStatistics());
        } catch (MathException ex) {
            Logger.getLogger(SolidModeller.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public SolidModeller(String leakDataFileName, String outputModelFileName){

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
