/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package solid.spintaxer;

import java.util.ArrayList;

/**
 *
 * @author jacob
 */
public class SolidText extends SolidSwitch {
    private ArrayList<SolidSwitch> body;
    
    
    public SolidText(){
        body = new ArrayList<SolidSwitch>();
    }
    
    public void addSwitch(SolidSwitch sswitch){
        body.add(sswitch);
    }
    
    public String spin(){
        String out = "";
        for(SolidSwitch sswitch : body) {
            out += sswitch.spin();
        }
        return out;
    }
    
    public String toString() {
        String out = "";
        for (SolidSwitch s : body) {
            out += s.toString();
        }
        out += "";
        return out;
    }
    
    public int permutations(){
        int permutations = 1;
        for (SolidSwitch s : body) {
            permutations *= s.permutations();
        }
        return permutations;
    }
    
}
