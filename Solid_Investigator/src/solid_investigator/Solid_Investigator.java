/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package solid_investigator;


import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;

/**
 *
 * @author jacob
 */
public class Solid_Investigator {
    private static StringBuilder output = new StringBuilder();

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws IOException{
        String spintax = "Project {hello|foo|bar}";
        String leak = "Project foo";
        SolidText text = SolidSpintaxer.parse(spintax);
        int permutations = text.permutations();
//        System.out.println(variants);
        System.out.println("this is the leak");
        ArrayList<String> variants;
//        System.out.println(variants.indexOf(leak));
        System.out.println(text.switches());
//        System.out.println(text.getBody());
//        for(SolidSwitch sswitch : text.getBody()){
//            if(sswitch.switches() > 0){
//                System.out.println(sswitch);
//                System.out.println(sswitch.switches() + " > " + 0);
//                variants = new ArrayList<String>();
//                int num = sswitch.permutations();
//                for(int k = 0; k < num; k++){
//                    variants.add(sswitch.spin(k));
//                }
//                System.out.println(variants);
//            }
//        }
        System.out.println(text.getBody());
        System.out.println(findPermutation(leak,text));
    }
    
    public static int getSwitchEndIndex(String input){
        boolean end = false;
        int openBracesCount = 1;
        int i = 0;
        while(!end){
            end = true;
        }
        return i;
    }
    
    public static int findPermutation(String leak, SolidSwitch spintax){
        int perm = 0;
        String spintaxS = spintax.toString();
        int openBracesCount = 0;
        int i = 0;
        int j = 0;
        int optionCounter = 0;
        String substringL = "";
        String substringS = "";
        boolean match = true;
        ArrayList<SolidSwitch> children = spintax.getChildren();
        int lastMatchIndex = 0;
        while(i < leak.length()){
            char currL = leak.charAt(i);
            char currS = spintaxS.charAt(j);
            if(currL == currS){
                i++;
                j++;
                substringS += currS;
                substringL += currL;
                System.out.println("substringL " + substringL);
                System.out.println("substringS " + substringS);
                continue;
            }
            if(currS == '@'){
                continue;
            } else if(currS == '{' && currL != '{'){
                openBracesCount += 1;
                if(openBracesCount == 1){
                    System.out.println(substringL.equals(substringS));
                    substringS = "";
                    substringL = "";
                    lastMatchIndex = i;
                    j++;
                    optionCounter++;
                    continue;
                }
            } else if(currS == '|' && currL != '|'){
                if(openBracesCount == 1){
                    if(match){
                        System.out.println(";aldskfjasdk;fj;adslkfj");
                        System.out.println(children.get(optionCounter));
//                        perm = findPermutation(substringL, children.get(optionCounter));
                        System.out.println(optionCounter);
                        perm = optionCounter;
                        return perm;
                    }
                    System.out.println(substringL);
//                    System.out.println(children.get(optionCounter));
                    j++;
                    substringS = "";
                    lastMatchIndex = i;
                    continue;
                }
            } else if(currS == '}' && currL != '}'){
                openBracesCount -= 1;
                if(openBracesCount == 0){
                    if(match){
                        System.out.println(";aldskfjasdk;fj;adslkfj");
                        System.out.println(children.get(optionCounter));
//                        perm = findPermutation(substringL, children.get(optionCounter));
                        System.out.println(optionCounter);
                        perm = optionCounter;
                        return perm;
                    }
                    optionCounter++;
                    System.out.println(children.get(optionCounter));
                    System.out.println(substringL);
                    j++;
                    substringS = "";
                    lastMatchIndex = i;
                    continue;
                }
            }  else {
                match = false;
                System.out.println("currL " + currL);
                System.out.println("currS " + currS);
                j++;
                i = lastMatchIndex;
                continue;
            }
        }
        System.out.println("RETURN PERM");
        return perm;
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
    
    public static void printAndLog(String in){
        System.out.println(in);
        output.append(in + "\n");
    }
}
