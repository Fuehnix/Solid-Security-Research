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
import org.apache.commons.text.similarity.*;


/**
 *
 * @author jacob
 */
public class Solid_Investigator {
    private static StringBuilder output = new StringBuilder();

    /**
     * NOTES FOR WHEN YOU COME BACK TO THIS:
     * LOOK INTO CS 440 LECTURE, UNDER VECTOR SEMANTICS
     * ALSO, LOOK INTO WHAT IS THE BEST METHOD FOR IMPARTIAL DISTANCE
     * @param args the command line arguments
     */
    public static void main(String[] args) throws IOException{
        String spintax = "Project {hello|foo|bar|goodbye}";
        String leak = "Project foo";
        Map<String, String> tagDatabase = new HashMap<String,String>();
        tagDatabase.put("0", "Sally");
        tagDatabase.put("1", "Mary");
        tagDatabase.put("3", "Bob");
//        BigInteger permutations = text.countPermutations();
        System.out.println("this is the leaker");
        String leaker = identify(leak,spintax,tagDatabase);
        System.out.println(leaker);
    }
    
    public static String identify(String leak, String spintax, Map<String, String> tagDatabase){
        String leaker = "";
        SolidSpintaxElement spintaxE = SolidSpintaxSpinner.parse(spintax);
        Map<String, String> permutations = createPermutationMap(spintaxE, tagDatabase);
        Map<String,Integer> tagToDist = new HashMap<String,Integer>();
        String mostLikelyLeaker = "";
        for(Map.Entry<String,String> entry : permutations.entrySet()){
            String docPerm = entry.getValue();
            String currTag = entry.getKey();
            LevenshteinDistance lev = new LevenshteinDistance();
            int dist = lev.apply(leak,docPerm);
            tagToDist.put(currTag,dist);
            int mostLikelyLeakerDist;
            if(mostLikelyLeaker.equals("")){
                mostLikelyLeaker = currTag;
                mostLikelyLeakerDist = dist;
            } else{
                mostLikelyLeakerDist = tagToDist.get(mostLikelyLeaker);
                if(mostLikelyLeakerDist > dist){
                    mostLikelyLeaker = currTag;
                }
            }
            System.out.println(dist);
        }
        return mostLikelyLeaker;
    }
    
    public static Map<String, String> createPermutationMap(SolidSpintaxElement spintax, Map<String, String> tagDatabase){
        Map<String, String> permutations = new HashMap<String,String>();
        for (Map.Entry<String,String> entry : tagDatabase.entrySet()){
            String currTag = entry.getKey();
            BigInteger perm = new BigInteger(currTag,32);
            String docPerm = spintax.spin(perm);
            permutations.put(currTag, docPerm);
        }
        return permutations;
    }
    
    
}
