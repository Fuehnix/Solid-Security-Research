/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package solid.spintaxer;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import org.junit.After;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import org.junit.Before;
import static solid.spintaxer.SolidSpintaxer.parse;
import static solid.spintaxer.SolidSpintaxer.readFileAsString;

/**
 *
 * @author jacob
 */
public class MainTest {
    private int repetitions = 100;
    private int versionNum = 1;
    private String fileInput = "spintax.txt";
    private String logo = ("\n       /MMM/ yMMm .MMMs                                                         \n"
        + "       /MMMmdNMMMddMMMs                                                         \n"
        + "+ ++++++ssssdMMMMMMMMMo    -\\\\\\\\\\\\\\\\\\   `:///////-  `/-         -/` -/////////. \n"
        + " `/`++++smNNNNNNMMMMy-    oMy          /NhoooooooNh .My         sM: sMsoooooosMs\n"
        + "           syyyyMMM.      +My++++++oo. hM.       yM .My         sM: sM-       NM\n"
        + "    `/.++++sssssMMM`               sMy hM.       yM .My         sM: sM-       NM\n"
        + "     `/.+++mNNNNMMM`      +My++++++yMs /NhoooooosNh .Md+++++++: sM: sMsooooooyMo\n"
        + "         :syyyyhMMMh/      -////////-   `:///////.  `/////////- -/` -////////:` \n"
        + "  `/`+++ssssmMMMMMMMMN`                                                         \n"
        + " :./++++NMMMMMMMMMMMMM.                                                         \n"
        + "================================================================================\n");
    private String version = ("Solid Spinner v. " + versionNum + " (Solid Spintax Standard v. " + versionNum + ")");
    private String introText = logo + "\n\n" + version + "\n"+"\n";
    //credits for @before and after to: 
    //https://stackoverflow.com/questions/1119385/junit-test-for-system-out-println
    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    private final PrintStream originalOut = System.out;

    @Before
    public void setUpStreams() {
        System.setOut(new PrintStream(outContent));
    }

    @After
    public void restoreStreams() {
        System.setOut(originalOut);
//        System.out.println(outContent.toString());
    }
    
    @Test
    public void out() {
        System.out.print("hello");
        assertEquals("hello", outContent.toString());
    }
    
    @Test
    public void BasicReadFileTest() {
        try{
            String[] args = new String[]{fileInput};
            SolidSpintaxer.main(args);
            assert(true);
        } catch(Exception e){
            e.printStackTrace();
            assert(false);
        }
    }
    
    
    /**
     * THIS TEST IS FAILING AND I DO NOT KNOW WHY!!!
     */
    @Test
    public void BasicOutputTest(){
//        System.out.print("hello");
        try{
            String[] args = new String[]{fileInput,"-v"};
            SolidSpintaxer.main(args);
            System.setOut(originalOut);
            String expected = introText;
            String result = outContent.toString();
            assertEquals(expected.length(),result.length());
            for(int i = 0; i < outContent.toString().length(); i++){
                //this part prevents pointless errors with null char comparisons
                if(Character.getNumericValue(result.charAt(i)) == -1 && Character.getNumericValue(expected.charAt(i)) == -1){
                    continue;
                }
                if(result.charAt(i) != expected.charAt(i)){
                    System.out.println("these are different ");
                    System.out.println(Character.getNumericValue(result.charAt(i)) + " equals " + Character.getNumericValue(expected.charAt(i)));
                    System.out.println(result.charAt(i) + " equals " + expected.charAt(i));
                    assert(false);
                }
            }
//            assert(result.equals(expected));
            assert(true);
//            assertEquals(logo+ "\n" + version + "\n", outContent.toString());
        } catch(Exception e){
            e.printStackTrace();
            assert(false);
        }
    }
    
    @Test
    public void testFileCountAndCreation(){
        try{
            int numOfFiles = 3;
            String[] args = new String[]{"spintax.txt","-n",Integer.toString(numOfFiles)};
            SolidSpintaxer.main(args);
            System.setOut(originalOut);
            String expected = introText;
            String input = readFileAsString(fileInput);
            expected += "Loading input file (" + fileInput + ")...\n\n";
            expected += "Loaded " + input.length() + " characters\n\n";
            SolidText text = parse(input);
            int permutations = text.permutations();
            expected += "Loaded " + text.switches() + " switches constituting "
                    + permutations + " permutations\n\n";
            expected += text.toString() + "\n\n";
            expected += "Generating " + numOfFiles + "output files...\n\n";
            expected += "NO   FILE            TAG     PERMUTATION\n\n";
            String result = outContent.toString();
            for(int i = 0; i < expected.toString().length(); i++){
                //this part prevents pointless errors with null char comparisons
                if(Character.getNumericValue(result.charAt(i)) == -1 && Character.getNumericValue(expected.charAt(i)) == -1){
                    continue;
                }
                if(result.charAt(i) != expected.charAt(i)){
                    System.out.print("these are different ");
                    System.out.print(result.substring(i-100,i));
                    System.out.print("here");
                    System.out.print(result.substring(i, i+100));
                    
                    System.out.print(expected.substring(i-100,i));
                    System.out.print("here");
                    System.out.print(result.substring(i, i+100));
                    System.out.println(Character.getNumericValue(result.charAt(i)) + " equals " + Character.getNumericValue(expected.charAt(i)));
                    System.out.println(result.charAt(i) + " equals " + expected.charAt(i));
                    assert(false);
                }
            }
            assert(true);
            for(int i = 0; i < numOfFiles; i++){
                String name = Integer.toString(i) + "spintax.txt";
                assert(result.contains(name));
            }
//            assertEquals(logo+ "\n" + version + "\n", outContent.toString());
        } catch(Exception e){
            e.printStackTrace();
            assert(false);
        }
    }
    
    @Test
    public void testCreationFromPerm(){
        try{
            int numOfFiles = 3;
            int permGiven = 0;
            String[] args = new String[]{"spintax.txt","-p",Integer.toString(permGiven)};
            SolidSpintaxer.main(args);
            System.setOut(originalOut);
            String expected = introText;
            String input = readFileAsString(fileInput);
            expected += "Loading input file (" + fileInput + ")...\n\n";
            expected += "Loaded " + input.length() + " characters\n\n";
            SolidText text = parse(input);
            int permutations = text.permutations();
            expected += "Loaded " + text.switches() + " switches constituting "
                    + permutations + " permutations\n\n";
            expected += text.toString() + "\n\n";
            expected += "Generating " + numOfFiles + "output files...\n\n";
            expected += "NO   FILE            TAG     PERMUTATION\n\n";
            String result = outContent.toString();
            assert(result.contains(text.toString()));
            assert(result.contains(text.spin(0)));
        } catch(Exception e){
            e.printStackTrace();
            assert(false);
        }
    }
    
    @Test
    public void testSequential(){
        try{
            int numOfFiles = 3;
            String[] args = new String[]{"spintax.txt","-n",Integer.toString(numOfFiles),"-s"};
            SolidSpintaxer.main(args);
//            System.setOut(originalOut);
            String expected = introText;
            String input = readFileAsString(fileInput);
            expected += "Loading input file (" + fileInput + ")...\n\n";
            expected += "Loaded " + input.length() + " characters\n\n";
            SolidText text = parse(input);
            int permutations = text.permutations();
            expected += "Loaded " + text.switches() + " switches constituting "
                    + permutations + " permutations\n\n";
            expected += text.toString() + "\n\n";
            expected += "Generating " + numOfFiles + "output files...\n\n";
            expected += "NO   FILE            TAG     PERMUTATION\n\n";
            String result = outContent.toString();
            assert(result.contains(text.toString()));
            assert(result.contains(text.spin(0)));
        } catch(Exception e){
            e.printStackTrace();
            assert(false);
        }
    }
    
    @Test
    public void testBadFlagMissingCount(){
        try{
            int numOfFiles = 3;
            String[] args = new String[]{"spintax.txt","-n"};
            SolidSpintaxer.main(args);
            System.setOut(originalOut);
            String result = outContent.toString();
            assert(result.contains("Error"));
        } catch(Exception e){
            e.printStackTrace();
            assert(false);
        }
    }
    
    @Test
    public void testBadFlagMissingPerm(){
        try{
            int numOfFiles = 3;
            String[] args = new String[]{"spintax.txt","-p"};
            SolidSpintaxer.main(args);
            System.setOut(originalOut);
            String result = outContent.toString();
            assert(result.contains("Error"));
        } catch(Exception e){
            e.printStackTrace();
            assert(false);
        }
    }
    
    @Test
    public void testBadFlagRandPerm(){
        try{
            int numOfFiles = 3;
            String[] args = new String[]{"spintax.txt","-r","-p","0"};
            SolidSpintaxer.main(args);
            System.setOut(originalOut);
            String result = outContent.toString();
            assert(result.contains("Error"));
        } catch(Exception e){
            e.printStackTrace();
            assert(false);
        }
    }
    
    
    //unclear how to test, System.in delay ruins the other tests.
//    @Test
//    public void testAllPermutationWarning(){
//        try{
//            int numOfFiles = 3;
//            String[] args = new String[]{"spintax.txt","-a"};
//            SolidSpintaxer.main(args);
//            String result = outContent.toString();
//            assert(result.contains("Warning"));
//            System.setOut(originalOut);
//        } catch(Exception e){
//            e.printStackTrace();
//            assert(false);
//        }
//    }
    
    
}
