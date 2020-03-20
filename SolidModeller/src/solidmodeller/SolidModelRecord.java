/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package solidmodeller;

import java.util.ArrayList;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.ConcurrentHashMap;

/**
 *
 * @author jacob
 */
public class SolidModelRecord {
    private String spintaxFile;
    private String leakFile;
    private String switches;
    private String permutations;
    private String characters;
    private String algorithm;
    private String date;
    private ArrayList<String> array;
    private TreeMap<String,Double> tMap = new TreeMap<>();
    private ArrayList<Double> distances;
    private double totalDistance = 0;
    
    public SolidModelRecord(String spintaxFile, String leakFile, 
            String switches, String permutations, String characters,
            String algorithm, String date, TreeMap<String,Double> tMap){
        this.spintaxFile = spintaxFile;
        this.leakFile = leakFile;
        this.switches = switches;
        this.permutations = permutations;
        this.characters = characters;
        this.algorithm = algorithm;
        this.date = date;
        this.tMap = tMap;
    }
    
    public SolidModelRecord(String outputModel){
        String[] lines = outputModel.split("\n");
        spintaxFile = lines[0];
        leakFile = lines[1];
        switches = lines[2];
        permutations = lines[3];
        characters = lines[4];
        algorithm = lines[5];
        date = lines[6];
        for(int i = 8; i < lines.length; i++){
//            System.out.println(lines[i]);
            String[] values = lines[i].split(":");
            double dist = Double.parseDouble(values[1]);
            String name = values[0];
//            System.out.println(name);
//            System.out.println(dist);
            tMap.put(name,dist);
        }
        distances = new ArrayList<>();
        for (Map.Entry<String,Double> entry : tMap.entrySet()){
            double value = entry.getValue();
            distances.add(value);
            totalDistance += value;
        }
    }

    public String getSpintaxFile() {
        return spintaxFile;
    }

    public String getLeakFile() {
        return leakFile;
    }

    public String getSwitches() {
        return switches;
    }

    public String getPermutations() {
        return permutations;
    }

    public String getCharacters() {
        return characters;
    }

    public String getAlgorithms() {
        return algorithm;
    }

    public String getDate() {
        return date;
    }
    
    public TreeMap<String,Double> getMap(){
        return tMap;
    }
    
    public ArrayList<Double> getDistances(){
        return distances;
    }
    
    public double getDistanceTotal(){
        return totalDistance;
    }
    
    public String toString(){
        String out = "";
        out += spintaxFile + "\n";
        out += leakFile + "\n";
        out += switches + "\n";
        out += permutations + "\n";
        out += characters + "\n";
        out += algorithm + "\n";
        out += date + "\n";
        for (Map.Entry<String,Double> entry : tMap.entrySet()){
            out += entry.toString() + "\n";
        }
        return out;
    }
}
