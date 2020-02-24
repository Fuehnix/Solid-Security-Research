/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package solid.spintaxer;

import java.util.Random;

/**
 *
 * @author jacob
 */
public class SolidIntSwitch extends SolidSwitch {
    private int min, max;
    
    public SolidIntSwitch(int min, int max){
        this.min = min;
        this.max = max;
    }
    
    public String spin(int tag){
        //Random rand = new Random();
        //absolute int range
        int range = max - min + 1;
        if(tag + min > max){
            System.out.println("SolidIntSwitch tag not in range");
//            throw new Exception(); 
        }
        int num = tag + min;
        String out = Integer.toString(num);
        return out;
    }
    
    public String toString() {
        String out = "{";
        out += Integer.toString(min) + "-" + Integer.toString(max);
        out += "}";
        return out;
    }
    
    public int permutations(){
        int permutations = max - min + 1;
        return permutations;
    }
}
