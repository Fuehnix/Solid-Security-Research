/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package solid.spintaxer;

import java.util.ArrayList;
import java.util.Random;

/**
 *
 * @author jacob
 */
public class SolidSwitch {
    private ArrayList<SolidSwitch> children;
    
    
    public SolidSwitch(){
        children = new ArrayList<SolidSwitch>();
    }
    
    public void addChild(SolidSwitch child) {
        children.add(child);
    }
    
    public String spin(){
        int length = children.size();
        Random rand = new Random();
        //absolute int range
        int randInt = rand.nextInt(length);
        SolidSwitch selected = children.get(randInt);
        return selected.spin();
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
        
}
