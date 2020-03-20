/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package solidmodeller;

import java.util.ArrayList;
import java.util.Collection;
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
    private static double totalDistance = 0;
    private int entries = 0;
    private TreeMap<String,Double> tMap = new TreeMap<String,Double>();
    
    public SolidModel(){
        records = new ArrayList<>();
        tMap = new TreeMap<String,Double>();
    }
    
    public SolidModel(SolidModelRecord record){
        records = new ArrayList<>();
        tMap = record.getMap();
        records.add(record);
        totalDistance += record.getDistanceTotal();
    }
    
    public SolidModel(ArrayList<SolidModelRecord> records){
        this.records = records;
        for(int i = 0; i < records.size(); i++){
            add(records.get(i));
        }
        entries = tMap.size();
    }
    
    /*
        This method assumes and requires that the name entries are unique
    */
    public void add(SolidModelRecord record){
        totalDistance += record.getDistanceTotal();
        Map<String, Double> otherMap = record.getMap();
        otherMap.forEach((k, v) -> tMap.merge(k, v, (a, b) -> a + b));
        entries = tMap.size();
    }
    
    /*
        This method assumes and requires that the name entries are unique
    */
    public void add(SolidModel records){
        Map<String, Double> otherMap = records.getMap();
        totalDistance += records.getTotalDistance();
        otherMap.forEach((k, v) -> tMap.merge(k, v, (a, b) -> a + b));
        entries = tMap.size();
        System.out.println("entries: " + entries);
    }
    
    public static double calculatePValue(double mean, double variance, double value) throws MathException{
        double pValue = 0;
        NormalDistribution d;
        System.out.println(mean + " and var "   + variance);
        d = new NormalDistributionImpl(mean, variance);
        System.out.println("P value is     " + d.inverseCumulativeProbability(value));
        return pValue;
    }
    
//    public static double calculateMean(ArrayList<Double> data){
//        double total = 0;
//        for(int i = 0; i < data.size(); i++){
//            total += data.get(i);
//        }
//        double size = data.size();
//        double mean = total/size;
//        return mean;
//    }
//    
//    public static double calculateMean(double total, double size){
//        double mean = total/size;
//        return mean;
//    }
    
    public static double calculateMeanOther(double total, double size, double entryDist){
        System.out.println(total);
        total = total - entryDist;
        System.out.println(total);
        System.out.println(entryDist);
        System.out.println(size);
        size = size - 1;
        System.out.println(size);
        double mean = total/size;
        return mean;
    }
    
    public static double calculateSTD(ArrayList<Double> data, double mean){
        double std = 0;
        for(int i = 0; i< data.size(); i++){
            std += Math.pow((data.get(i) - mean/data.size()),2);
        }
        return std;
    }
    
    public double getTotalDistance(){
        return totalDistance;
    }
    
    public TreeMap<String,Double> getMap(){
        return tMap;
    }
    
    public String printStatistics() throws MathException{
        String out = "";
        ArrayList<Double> values = new ArrayList<>();
        values.addAll(tMap.values());
        TreeMap<String,Double> pMap = new TreeMap<>();
        System.out.println("size   " +tMap.size());
        for (Map.Entry<String,Double> entry : tMap.entrySet()){
            System.out.println(entry.toString());
            String name = entry.getKey();
            double dist = entry.getValue();
            double mean = 0;
            double std = 1;
            double pvalue = 0;
            mean = calculateMeanOther(totalDistance,entries,dist);
            System.out.println(totalDistance);
            ArrayList<Double> excluding = values;
            excluding.remove(dist);
            std = calculateSTD(excluding,mean);
            pvalue = calculatePValue(mean,std,dist);
            String pvalueS = String.valueOf(pvalue);
            System.out.println(dist + "    " +mean+ "    " + std +"   " + pvalueS);
            pMap.put(name, pvalue);
        }
        return pMap.toString();
    }
}
