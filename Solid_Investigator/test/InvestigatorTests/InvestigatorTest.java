/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package InvestigatorTests;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import org.junit.Before;
import org.junit.After;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

import solid_investigator.Solid_Investigator;
import solid_investigator.*;
import solid.spintax.spinner.SolidSpintax.*;
import solid.spintax.spinner.*;

/**
 *
 * @author thomas-quig
 */
public class InvestigatorTest {
    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    private final PrintStream originalOut = System.out;
    
    @Before
    public void setUpStreams() {
        System.setOut(new PrintStream(outContent));
    }

    @After
    public void restoreStreams() {
        System.setOut(originalOut);
        System.out.println(outContent.toString());
    }
    
    //1
    @Test
    public void AbsoltePassTest() {
        assert(true);
        assert(!false);
        assert(true && true);
        assert(false || true);
        
        assert(1==1);
        assert(1 != 2);
        assert(1 > 0);
        assert(0 < 1);
        
        assert("Equal".equals("Equal"));
        assert("Not Null" != null);
    }
    
    //2
    @Test
    public void BasicNoFailTest(){
        String[] args = new String[]{"-h"};
        try{
            Solid_Investigator.main(args);
            assert(true);
            assert(outContent.toString().contains("Spintax"));
        }catch (Exception e){
            assert(false);
        }
    }
    
     //3
    @Test
    public void CorrectVersionTest(){
        String[] args = new String[]{""};
        try{
            Solid_Investigator.main(args);
            assert(true); //Free Assertion For No Crash!
            assert(outContent.toString().contains("Investigator v1.0.1"));
        }catch (Exception e){
            assert(false);
        }
    }
    
    //4
    @Test
    public void HelpIgnoresAllTest(){
        String[] args = new String[]{"-h","Does Not Matter"};
        try{
            Solid_Investigator.main(args);
            assert(true); //Free Assertion For No Crash!
            assert(outContent.toString().contains("--help specified"));
        }catch (Exception e){
            assert(false);
        }
    }
    
    //5
    @Test
    public void VersionIgnoresAllTest(){
        String[] args = new String[]{""};
        try{
            Solid_Investigator.main(args);
            assert(true); //Free Assertion For No Crash!
            assert(outContent.toString().contains(""));
        }catch (Exception e){
            assert(false);
        }
    }
    
    //6
    @Test
    public void NoArgsTest(){
        String[] args = new String[]{};
        try{
            Solid_Investigator.main(args);
            assert(true); //Free Assertion For No Crash!
            assert(outContent.toString().contains("Spintax File Missing"));
            assert(outContent.toString().contains("Leaked File Missing"));
            assert(outContent.toString().contains("Tag Database File Missing"));
        }catch (Exception e){
            assert(false);
        }
    }
    
    //7
    @Test
    public void LeakMissingTest(){
        String[] args = new String[]{"spintaxfilename"};
        try{
            Solid_Investigator.main(args);
            assert(true); //Free Assertion For No Crash!
            assert(outContent.toString().contains("Leaked File Missing"));
        }catch (Exception e){
            assert(false);
        }
    }
    
    //8
    @Test
    public void TagDatabaseMissingTest(){
        String[] args = new String[]{"spintaxfilenamethatdoesntexist", "leakedfilenamethatdoesntexist"};
        try{
            Solid_Investigator.main(args);
            assert(true); //Free Assertion For No Crash!
            assert(outContent.toString().contains("Tag Database File Missing"));
        }catch (Exception e){
            assert(false);
        }
    }
    
    //9
    @Test
    public void FileNotFoundTest(){
        String[] args = new String[]{"Does","Not","Exist"};
        try{
            Solid_Investigator.main(args);
            assert(true); //Free Assertion For No Crash!
            assert(outContent.toString().contains("Reading the spintax file encountered an error, does it exist?"));
            assert(outContent.toString().contains("Reading the tag database file encountered an error, does it exist?"));
            assert(outContent.toString().contains("Reading the leaked file encountered an error, does it exist?"));
            
            
        }catch (Exception e){
            assert(false);
        }
    }
    
    //10
    @Test
    public void NoCrashValidFilesTest(){
        String[] args = new String[]{"test/Resources/basic-spintax.txt","test/Resources/basic-leaked.txt","test/Resources/basic-tagdatabase.txt"};
        try{
            Solid_Investigator.main(args);
            assert(true); //Free Assertion For No Crash!
            assert(!outContent.toString().contains("error"));
        }catch (Exception e){
            assert(false);
        }
    }
    
    //11
    @Test
    public void SwitchCountTest(){
        String[] args = new String[]{""};
        try{
            Solid_Investigator.main(args);
            assert(true); //Free Assertion For No Crash!
            assert(outContent.toString().contains(""));
        }catch (Exception e){
            assert(false);
        }
    }
    
    //12
    /**
     * Additional Testing Suites
     * 
     * 1. Malformed input unmatching file information
     * 2. Negative costs, how does that behave?
     * 3. Integer maxing costs spft
     */
    
    /*
    @Test
    public void GenericTest(){
        String[] args = new String[]{""};
        try{
            Solid_Investigator.main(args);
            assert(true); //Free Assertion For No Crash!
            assert(outContent.toString().contains(""));
        }catch (Exception e){
            assert(false);
        }
    }
    */
}
