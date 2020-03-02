/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package solid_investigator;
import java.util.HashMap;

/**
 *
 * @author jacob
 */
public class SolidGlobalSwitch extends SolidSwitch {
    private String result;
    public static HashMap<String, SolidGlobalSwitch> switches = new HashMap<String,SolidGlobalSwitch>();
    private SolidGlobalSwitch master;
    
    public SolidGlobalSwitch() {
        super();
    }
    
    public void setMaster(SolidGlobalSwitch master) {
        this.master = master;
    }
    
    public String spin(int tag) {
        if(master==null) {
            this.result = super.spin(tag);
            return result;
        } else {
            return master.result;
        }
    }
    
    public String toString() {
        return "@" + super.toString();
    }
    
    public int permutations(){
        if(master==null){
            return super.permutations();
        } else {
            return 1;
        }
    }
    
}
