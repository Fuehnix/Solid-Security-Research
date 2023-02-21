package solid.spintax.spinner.SolidSpintax;

import java.math.BigInteger;
import java.util.HashMap;

/**
 * <i>Solid Spintax Global Switch</i>
 * <p>
 * An extension of {@code SolidSpintaxSwitch} that always returns the same output
 * as identical switches.
 * 
 * @author Solid Security
 * @author Vivek Nair
 * @author Jacob Fuehne
 * @since 2.0.0
 */
public class SolidSpintaxGlobalSwitch extends SolidSpintaxSwitch implements SolidSpintaxElement {

    private String result;
    public static HashMap<String, SolidSpintaxGlobalSwitch> switches = new HashMap<String, SolidSpintaxGlobalSwitch>();
    private SolidSpintaxGlobalSwitch master;

    public SolidSpintaxGlobalSwitch() {
        super();
    }

    /**
     * Specify the master switch that this switch should copy.
     * @param master
     */
    public void setMaster(SolidSpintaxGlobalSwitch master) {
        this.master = master;
    }

    @Override
    public String spin(BigInteger tag) {
        if (master == null) {
            this.result = super.spin(tag);
            return result;
        } else {
            return master.result;
        }
    }

    @Override
    public String toString() {
        return "@" + super.toString();
    }

    @Override
    public BigInteger countPermutations() {
        if (master == null) {
            return super.countPermutations();
        } else {
            return BigInteger.ONE;
        }
    }
}
