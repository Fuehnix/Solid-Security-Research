/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package solidmodeller;

import java.util.ArrayList;
import java.util.Map;
import java.util.TreeMap;
import org.apache.commons.math.MathException;
import org.apache.commons.math.distribution.NormalDistribution;
import org.apache.commons.math.distribution.NormalDistributionImpl;
import org.apache.commons.math.stat.descriptive.moment.StandardDeviation;


/**
 *
 * @author jacob
 */
public class SolidModel {
    private ArrayList<SolidModelRecord> records;
    private static double totalDistance;
    private int entries;
    private TreeMap<String,Double> tMap = new TreeMap<String,Double>();
    
    public SolidModel(){
        records = new ArrayList<>();
        tMap = new TreeMap<String,Double>();
    }
    
    public SolidModel(SolidModelRecord record){
        records = new ArrayList<>();
        tMap = record.getMap();
        records.add(record);
    }
    public SolidModel(ArrayList<SolidModelRecord> records){
        this.records = records;
        for(int i = 0; i < records.size(); i++){
            add(records.get(i));
        }
    }
    
    
    /*
        This method assumes and requires that the name entries are unique
    */
    public void add(SolidModelRecord record){
        totalDistance += record.getDistanceTotal();
        for (Map.Entry<String,Double> entry : tMap.entrySet()){
            String name = entry.getKey();
            double distance = entry.getValue();
            
        }
    }
    
    /*
        This method assumes and requires that the name entries are unique
    */
    public void add(SolidModel records){
        totalDistance += records.getTotalDistance();
        TreeMap<String,Double> otherMap = records.getMap();
        entries += otherMap.size();
        for (Map.Entry<String,Double> entry : tMap.entrySet()){
            String name = entry.getKey();
            double dist = entry.getValue();
            if(otherMap.containsKey(name)){
                double otherDist = otherMap.get(name);
                double newDist = dist + otherDist;
                tMap.put(name,newDist);
            }
        }
    }
    
    public static double calculatePValue(double mean, double variance) throws MathException{
        double pValue = 0;
        NormalDistribution d;
        d = new NormalDistributionImpl(mean, variance);
        System.out.println(d.inverseCumulativeProbability(0.9));
        return pValue;
    }
    
    public static double calculateMean(ArrayList<Double> data){
        double total = 0;
        for(int i = 0; i < data.size(); i++){
            total += data.get(i);
        }
        double size = data.size();
        double mean = total/size;
        return mean;
    }
    
    public static double calculateMean(double total, double size){
        double mean = total/size;
        return mean;
    }
    
    public static double calculateMeanOther(double total, double size, double entryDist){
        total = total - entryDist;
        size = size - 1;
        double mean = total/size;
        return mean;
    }
    
    public static double calculateSTD(ArrayList<Double> data){
        double std = 0;
        
        double dataArr[] = new double[data.size()];
        for(int i = 0; i < dataArr.length; i++){
             dataArr[i] = data.get(i);
        }
        return calculateSTD(data);
    }
    
    public static double calculateSTD(double[] data){
        double std = 0;
        StandardDeviation sd2 = new StandardDeviation();
        std = sd2.evaluate(data);      
        return std;
    }
    
    public double getTotalDistance(){
        return totalDistance;
    }
    
    public TreeMap<String,Double> getMap(){
        return tMap;
    }
}
