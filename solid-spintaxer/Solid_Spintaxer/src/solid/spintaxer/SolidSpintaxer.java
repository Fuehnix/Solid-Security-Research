/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package solid.spintaxer;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;
import java.util.Scanner;

/**
 *
 * @author jacob
 */
public class SolidSpintaxer {
    private static final int fileWarningNum = 10;
    private static final int fileRejectNum = 100000;

    
    //To do :
    // implement buffered reader so that parse and file are both one character
    // a time
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args){
        boolean nFlag = false;
        boolean rFlag = false;
        boolean pFlag = false;
        boolean sFlag = false;
        boolean iFlag = false;
        boolean hFlag = false;
        boolean fFlag = false;
        boolean oFlag = false;
        boolean aFlag = false;
        boolean uFlag = false;
        int numOfFiles = 1;
        int permGiven = 0;
        int fileStartNum = 0;
        int version = 1;
        ArrayList<Integer> list = new ArrayList<Integer>();
        String fileInput;
        System.out.println("       /MMM/ yMMm .MMMs                                                         \n"
        + "       /MMMmdNMMMddMMMs                                                         \n"
        + "+ ++++++ssssdMMMMMMMMMo    -\\\\\\\\\\\\\\\\\\   `:///////-  `/-         -/` -/////////. \n"
        + " `/`++++smNNNNNNMMMMy-    oMy          /NhoooooooNh .My         sM: sMsoooooosMs\n"
        + "           syyyyMMM.      +My++++++oo. hM.       yM .My         sM: sM-       NM\n"
        + "    `/.++++sssssMMM`               sMy hM.       yM .My         sM: sM-       NM\n"
        + "     `/.+++mNNNNMMM`      +My++++++yMs /NhoooooosNh .Md+++++++: sM: sMsooooooyMo\n"
        + "         :syyyyhMMMh/      -////////-   `:///////.  `/////////- -/` -////////:` \n"
        + "  `/`+++ssssmMMMMMMMMN`                                                         \n"
        + " :./++++NMMMMMMMMMMMMM.                                                         \n"
        + "================================================================================\n\n");
        System.out.println("Solid Spinner v. " + version + " (Solid Spintax Standard v. " + version + ")");
        try{
            fileInput = args[0];
        } catch(Exception e){
            fileInput = "spintax.txt";
        }
        String fileOut = "";
        for(int i = 0; i < args.length; i++){
            switch(args[i]) {
                case "-n":
                case "--count":
                    nFlag = true;
                    try{
                        numOfFiles = Integer.parseInt(args[i+1]);
                    } catch(Exception e){
                        e.printStackTrace();
                        System.out.println("Error: invalid arg format. Please"
                                + " run with -help or -h to see proper format");
                    }
                    break;
                case "-r":
                case "-rand":
                    //throw a flag and don't do anything if sFlag is true as well
                    rFlag = true;
                    break;
                case "-p":
                case "--perm":
                    //basically flag, this refers to what is being passed as tag
                    pFlag = true;
                    try{
                        permGiven = Integer.parseInt(args[i+1]);
                        fileStartNum = permGiven;
                    } catch(Exception e){
                        e.printStackTrace();
                        System.out.println("Error: invalid arg format. Please"
                                + " run with -help or -h to see proper format");
                    }
                    break;
                case "-s":
                case "--sequential":
                    //sequential file names and perms.
                    //if combined with perm, include in file name
                    sFlag = true;
                    break;
                case "-i":
                case "-info":
                    //prints the number of possible permutations
                    iFlag = true;
                case "-h":
                case "-help":
                    //ignores all other flags, gives warning, and displays help
                    //information about tags and their purpose
                    hFlag = true;
                case "-f":
                case "--logfile":
                    //shows the file output names and the system commandline outputs
                    fFlag = true;
                case "-o":
                case "--out":
                    //allows you to change the filename default
                    oFlag = true;
                    fileOut = args[i+1];
                    try{
                        fileOut = args[i+1];
                    } catch(Exception e){
                        e.printStackTrace();
                        System.out.println("Error: invalid arg format. Please"
                                + " run with -help or -h to see proper format");
                    }
                case "-a":
                case "--a":
                    //generates all possible permutations as files
                    //give warning if < 10
                    //reject if < 100,000
                    aFlag = true;
                case "-u":
                case "--unique":
                    uFlag = true;
            }
        }
        if(hFlag){
            System.out.println("-n,   -count <#>          specifies a number of files to be generated\n" +
        "-h,   -help               print this help\n" +
        "-r,   -rand               randomizes the permutation generation, enabled by default, but conflicts with sequential"+
        "-p,   -perm <#>           specifies the permutation to be generated"+
        "-s,   -sequential         generates permutations sequentially by their perm #"+
        "-i,   -info               prints the number of permutations possible"+
        "-f,   -logfile            creates a logfile of system outputs"+
        "-o,   -out                specifies the outputs file, by default, it is the same as input");
            return;
        }
        if(sFlag && rFlag){
            System.out.println("ERROR: permutations cannot "
                    + "be both random and sequential");
            return;
        }
        
        SolidText text;
        try{
            System.out.println("Loading input file (" + fileInput + ")...");
            String input = readFileAsString(fileInput);
            System.out.println("Loaded " + input.length() + " characters");
            text = parse(input);
            int permutations = text.permutations();
            System.out.println("Loaded " + text.switches() + " switches constituting "
                    + permutations + " permutations");
            System.out.println(text);
            
            if(aFlag && permutations > fileRejectNum){
                System.out.println("Warning, this will print " + fileWarningNum + 
                        " cases");
            }
            
            if(aFlag && permutations > fileRejectNum){
                System.out.println("Warning, this will print " + fileRejectNum + 
                        " cases");
            }
            
            if(fileOut == "") {
                fileOut = fileInput;
            }
            
            if(uFlag){
                list = getUniquePerms(permutations,numOfFiles);
            }
            
            System.out.println("Generating " + numOfFiles + "output files...");
            System.out.println("");
            Random rand = new Random();
            for(int i = 0; i < numOfFiles; i++) {
                FileWriter fw = new FileWriter(new File(Integer.toString(i) 
                        + fileInput));
                if(rFlag || !sFlag){
                    permGiven = rand.nextInt(permutations);
                }
                if(uFlag){
                    permGiven = (list.get(i));
                }
                fw.write(text.spin(permGiven).toString());
                permGiven++;
            }
        }catch(Exception e){
            e.printStackTrace();
        }

            
            
        // TODO code application logic here

        SolidText ex1 = parse("Introducing project "
                + "@{hello|{foo|bar}|{100-200}}. Project "
                + "@{hello|{foo|bar}|{100-200}} is a new security initiative "
                + "from Solid Security. Our goal with project "
                + "@{hello|{foo|bar}|{100-200}} is to...");
        // SolidText text = parse("Hello");
        System.out.println(ex1);
        System.out.println(ex1.spin(5));
//        System.out.println(text.spin(1));
        System.out.println("There are " + ex1.permutations() + " possible "
                + "permutations");

    }
    
    public static SolidText parse(String input){
//        System.out.print("parse(" + input + ") = ");
        if(input.matches("\\{[0-9]+-[0-9]+\\}")){
            SolidText text = new SolidText();
            int pos = input.indexOf("-");
            int min = Integer.parseInt(input.substring(1, pos));
            int max = Integer.parseInt(input.substring(pos + 1, input.length()-1));
            SolidIntSwitch temp = new SolidIntSwitch(min,max);
            text.addSwitch(temp);
            return text;
        }
        
        SolidText text = new SolidText();
        SolidSwitch currSwitch;
        currSwitch = new SolidSwitch();
        String substring = "";
        int openBracesCount = 0;
        for(int i = 0; i < input.length(); i++) {
            char curr = input.charAt(i);
            if(curr == '@'){
                currSwitch = new SolidGlobalSwitch();
                continue;
            }
            if(curr == '{') {
                openBracesCount += 1;
                if(openBracesCount == 1) {
                    SolidStrSwitch temp = new SolidStrSwitch(substring);
                    text.addSwitch(temp);
                    //System.out.println(substring);
                    substring = "";
                    continue;
                }
            } else if (curr == '|') {
                if(openBracesCount == 1){
                    SolidSwitch option = parse(substring);
                    currSwitch.addChild(option);
                    substring = "";
                    //System.out.println(substring);
                    continue;
                }
            } else if (curr == '}'){
                openBracesCount -= 1;
                if(openBracesCount == 0){
                    SolidSwitch option = parse(substring);
                    currSwitch.addChild(option);
                    text.addSwitch(currSwitch);
                    
                    if(currSwitch instanceof SolidGlobalSwitch) {
                        SolidGlobalSwitch globalSwitch = (SolidGlobalSwitch) currSwitch;
                        String contents = currSwitch.toString();
                        if(SolidGlobalSwitch.switches.containsKey(contents)) {
                            globalSwitch.setMaster((SolidGlobalSwitch) SolidGlobalSwitch.switches.get(contents));
                        } else {
                            SolidGlobalSwitch.switches.put(contents, globalSwitch);
                        }
                    }
                    
                    currSwitch = new SolidSwitch();
                    substring = "";
                    //System.out.println(substring);
                    continue;
                }
            }
            substring += input.charAt(i);
        }
        
        SolidStrSwitch temp = new SolidStrSwitch(substring);
        text.addSwitch(temp);
        return text;
    }
    
    public static String readFileAsString(String fileName) throws Exception { 
        String data = ""; 
        data = new String(Files.readAllBytes(Paths.get(fileName))); 
        return data; 
    }
    
    public static ArrayList<Integer> getUniquePerms(int permutations, int numOfPerms){
        ArrayList<Integer> list = new ArrayList<Integer>();
        ArrayList<Integer> out = new ArrayList<Integer>();
        for (int i=1; i<11; i++) {
            list.add(i);
        }
        Collections.shuffle(list);
        for (int i=0; i<numOfPerms; i++) {
            out.add(list.get(i));
        }
        return out;
    }
    
}