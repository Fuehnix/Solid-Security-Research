/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package solid.spintaxer;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Random;

/**
 *
 * @author jacob
 */
public class SolidSwitch {
    private ArrayList<SolidSwitch> children;
    private int switches = 0;
    
    
    public SolidSwitch(){
        children = new ArrayList<SolidSwitch>();
    }
    
    public void addChild(SolidSwitch child) {
        children.add(child);
    }
    
    public String spin(int tag){
        int length = children.size();
        //absolute int range
        for(int i = 0; i < children.size(); i++){
            int curPermutations = children.get(i).permutations();
            if(tag < curPermutations){
                return children.get(i).spin(tag);
            } else {
                tag = tag - (curPermutations);
            }
        }
        System.out.println("Error: tag not reached");
        return "ERROR";
    }
    
    public String toString() {
        String out = "{";
        Boolean first = true;
        for (SolidSwitch s : children) {
            if(!first) {
                out += "|";
            }
            first = false;
            out += s.toString();
        }
        out += "}";
        return out;
    }
    
    public ArrayList<SolidSwitch> getChildren(){
        return children;
    }
    
    public int permutations(){
        int permutations = 0;
        for (SolidSwitch s : children) {
            permutations = permutations + (s.permutations());
        }
        return permutations;
    }
//    void setMaster(SolidGlobalSwitch get) {
//        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
//    }
    public int switches(){
        int switches = 1;
        for(SolidSwitch s : children){
            switches += s.switches();
        }
        return switches;
    }
        
}
