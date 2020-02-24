/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package solid.spintaxer;

import java.util.HashMap;

/**
 *
 * @author jacob
 */
public class SolidGlobalSwitch extends SolidSwitch {
    private String hash, result;
    public static HashMap<String,SolidGlobalSwitch> switches;
    private SolidGlobalSwitch master;
    
    public SolidGlobalSwitch() {
        super();
    }
    
    public void setMaster(SolidGlobalSwitch master) {
        this.master = master;
    }
    
    public String spin() {
        if(master==null) {
            this.result = super.spin();
            return result;
        } else {
            return master.result;
        }
    }
    
    public String toString() {
        return "@" + super.toString();
    }
}
