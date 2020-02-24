/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package solid.spintaxer;

import java.util.Scanner;

/**
 *
 * @author jacob
 */
public class SolidSpintaxer {
    //To do :
    // implement buffered reader so that parse and file are both one character
    // a time
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        boolean nFlag = false;
        boolean rFlag = false;
        boolean pFlag = false;
        boolean sFlag = false;
        boolean iFlag = false;
        boolean hFlag = false;
        boolean fFlag = false;
        boolean oFlag = false;
        boolean aFlag = false;
        int numOfFiles = 1;
        int permGiven;
        String fileInput = args[0];
        String fileOut;
        for(int i = 0; i < args.length; i++){
            switch(args[i]) {
                case "-n":
                case "--count":
                    nFlag = true;
                    numOfFiles = Integer.parseInt(args[i+1]);
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
                    permGiven = Integer.parseInt(args[i+1]);
                    break;
                case "-s":
                case "--sequential":
                    //sequential file names and tags.
                    //if combined with tag, include in file name
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
                case "-a":
                case "--a":
                    //generates all possible permutations as files
                    //give warning if < 10
                    //reject if < 100,000
                    aFlag = true;
                case "-u":
                case "--unique":
            }
        }
        if(hFlag){
            System.out.println("HELP TEXT PLACEHOLDER");
            return;
        }

        if(nFlag){
            
        }
            
            
        // TODO code application logic here
//        System.out.println("This program is a proof of concept");
//        System.out.println("Please type a text using the Solid "
//                + "Spintax format and press enter when done");
//        
//	System.out.println("You entered ");
//	System.out.println("Processing...");
//        String input = "Project @{hello | {foo|bar} | {100-200}} is ..."
//                + "Projecct @@{hello | {foo|bar} | {100-200}} is ...";
//        SolidStrSwitch foo = new SolidStrSwitch("foo");
//        SolidStrSwitch bar = new SolidStrSwitch("bar");
//        SolidSwitch foobar = new SolidSwitch();
//        foobar.addChild(foo);
//        foobar.addChild(bar);
//        SolidStrSwitch hello = new SolidStrSwitch("hello");
//        SolidIntSwitch intSwitch = new SolidIntSwitch(100,200);
//        SolidGlobalSwitch mainSwitch = new SolidGlobalSwitch();
//        mainSwitch.addChild(hello);
//        mainSwitch.addChild(foobar);
//        mainSwitch.addChild(intSwitch);
//        SolidGlobalSwitch switch2 = new SolidGlobalSwitch();
//        switch2.addChild(hello);
//        switch2.addChild(foobar);
//        switch2.addChild(intSwitch);
//        switch2.setMaster(mainSwitch);
//        SolidStrSwitch project = new SolidStrSwitch("Project ");
//        SolidStrSwitch is = new SolidStrSwitch(" is ... \n");
//        SolidText text = new SolidText();
//        text.addSwitch(project);
//        text.addSwitch(mainSwitch);
//        text.addSwitch(is);
//        text.addSwitch(project);
//        text.addSwitch(switch2);
//        text.addSwitch(is);
//        System.out.println(text.spin());
//        System.out.println("test2");
        SolidIntSwitch test = new SolidIntSwitch(1000,9000);
//        System.out.println(test.spin(8000));
        SolidSwitch s1 = new SolidSwitch();
        SolidStrSwitch x = new SolidStrSwitch("x");
        SolidStrSwitch y = new SolidStrSwitch("y");
        SolidStrSwitch z = new SolidStrSwitch("z");
        s1.addChild(x);
        s1.addChild(y);
        s1.addChild(z);
        SolidSwitch s2 = new SolidSwitch();
        SolidIntSwitch int5560 = new SolidIntSwitch(55,65);
        s2.addChild(int5560);
        SolidStrSwitch a = new SolidStrSwitch("a");
        SolidStrSwitch b = new SolidStrSwitch("b");
        SolidStrSwitch c = new SolidStrSwitch("c");
        SolidSwitch s3 = new SolidSwitch();
        s3.addChild(a);
        s3.addChild(b);
        s3.addChild(c);
        s2.addChild(s3);
        SolidIntSwitch int1020 = new SolidIntSwitch(10,20);
        s2.addChild(int1020);
        SolidSwitch parent = new SolidSwitch();
        parent.addChild(s1);
        parent.addChild(s2);
//        System.out.println(parent.toString());
//        System.out.println(parent.spin(3));
        //TEST SolidText
        SolidIntSwitch int010 = new SolidIntSwitch(0,10);
        SolidIntSwitch int06 = new SolidIntSwitch(0,6);
        SolidIntSwitch int0102 = new SolidIntSwitch(0,10);
        SolidText test2 = new SolidText();
        test2.addSwitch(int010);
        test2.addSwitch(int06);
        test2.addSwitch(int0102);
        System.out.println(test2.spin(100));
        SolidText text = parse("Introducing project "
                + "@{hello|{foo|bar}|{100-200}}. Project "
                + "@{hello|{foo|bar}|{100-200}} is a new security initiative "
                + "from Solid Security. Our goal with project "
                + "@{hello|{foo|bar}|{100-200}} is to...");
        // SolidText text = parse("Hello");
        System.out.println(text);
        System.out.println(text.spin(5));
//        System.out.println(text.spin(1));
        System.out.println("There are " + text.permutations() + " possible "
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
    
    
}
