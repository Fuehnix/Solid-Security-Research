/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package solid.spintaxer;

/**
 *
 * @author jacob
 */
public class SolidGlobalSwitch extends SolidSwitch {
    private String hash, result;
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
}
