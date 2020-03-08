/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package solid_investigator;


import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Dictionary;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;
import java.util.Scanner;
import solid.spintax.spinner.*;
import solid.spintax.spinner.SolidSpintax.*;


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
        String spintaxS = "Project {hello|foo|bar|goodbye}";
        String leak = "Project foo";
        Map<BigInteger, String> tagDatabase = new HashMap<BigInteger,String>();
        SolidSpintaxElement spintaxE = SolidSpintaxSpinner.parse(spintaxS);
        tagDatabase.put(BigInteger.valueOf(0), "Sally");
        tagDatabase.put(BigInteger.valueOf(1), "Mary");
        tagDatabase.put(BigInteger.valueOf(3), "Bob");
        Map<BigInteger, String> permutations = new HashMap<BigInteger,String>();
        BigInteger leakTag = BigInteger.valueOf(-1);
        for (Map.Entry<BigInteger,String> entry : tagDatabase.entrySet()){
            BigInteger currTag = entry.getKey();
            String docPerm = spintaxE.spin(currTag);
            permutations.put(currTag, docPerm);
        }
        for(Map.Entry<BigInteger,String> entry : permutations.entrySet()){
            boolean match = true;
            String docPerm = entry.getValue();
            for(int i = 0; i < leak.length();i++){
               char currCharP = docPerm.charAt(i);
               char currCharL = leak.charAt(i);
               if(currCharP != currCharL){
                   match = false;
               }
            }
            if(match){
                leakTag = entry.getKey();
            }
        }
        String leaker = tagDatabase.get(leakTag);
//        BigInteger permutations = text.countPermutations();
        System.out.println("this is the leaker");
        ArrayList<String> variants;
        System.out.println(leaker);
        BigInteger test = BigInteger.valueOf(100);
        BigInteger mod = BigInteger.valueOf(11);
        System.out.println(test.modInverse(mod));
//        System.out.println(findPermutation(leak,text));
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
    
    public static int findPermutation(String leak, SolidSpintaxSwitch spintax){
        int perm = 0;
        String spintaxS = spintax.toString();
        int openBracesCount = 0;
        int i = 0;
        int j = 0;
        int optionCounter = 0;
        String substringL = "";
        String substringS = "";
        boolean match = true;
//        ArrayList<SolidSpintaxSwitch> children = spintax.getChildren();
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
//                        System.out.println(children.get(optionCounter));
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
//                        System.out.println(children.get(optionCounter));
//                        perm = findPermutation(substringL, children.get(optionCounter));
                        System.out.println(optionCounter);
                        perm = optionCounter;
                        return perm;
                    }
                    optionCounter++;
//                    System.out.println(children.get(optionCounter));
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
