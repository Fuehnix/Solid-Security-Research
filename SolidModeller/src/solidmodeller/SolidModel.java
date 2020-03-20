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
        totalDistance += record.getDistanceTotal();
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
        Map<String, Double> otherMap = record.getMap();
        otherMap.forEach((k, v) -> tMap.merge(k, v, (a, b) -> a + b));
    }
    
    /*
        This method assumes and requires that the name entries are unique
    */
    public void add(SolidModel records){
        Map<String, Double> otherMap = records.getMap();
        totalDistance += records.getTotalDistance();
        entries += otherMap.size();
        otherMap.forEach((k, v) -> tMap.merge(k, v, (a, b) -> a + b));
//        System.out.println(tMap);
//        for (Map.Entry<String,Double> entry : tMap.entrySet()){
//            System.out.println(entry.toString());
//        }
    }
    
    public static double calculatePValue(double mean, double variance) throws MathException{
        double pValue = 0;
        NormalDistribution d;
        System.out.println(mean + " and var "   + variance);
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
        return calculateSTD(dataArr);
    }
    
    public static double calculateSTD(double[] data){
        double std = 0;
        StandardDeviation sd2 = new StandardDeviation();
        std = sd2.evaluate(data);
        System.out.println("STD IS: " + std + "data is" + data.toString());
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
        for (Map.Entry<String,Double> entry : tMap.entrySet()){
            System.out.println(entry.toString());
            String name = entry.getKey();
            double dist = entry.getValue();
            double mean = 0;
            double std = 1;
            double pvalue = 0;
            mean = calculateMeanOther(totalDistance,entries,dist);
            ArrayList<Double> excluding = values;
            excluding.remove(dist);
            std = calculateSTD(excluding);
            pvalue = calculatePValue(mean,std);
            String pvalueS = Double.toString(pvalue);
            System.out.println(dist + "    " +mean+ "    " + std +"   " + pvalue);
            pMap.put(name, pvalue);
        }
        return pMap.toString();
    }
}
