package solid.spintax.spinner.SolidSpintax;
import java.math.BigInteger; 

/**
 * <i>Solid Spintax Integer Switch</i>
 * <p>
 * Returns an integer in the specified range when spun.
 * 
 * Uses the
 * <a href="https://github.com/SolidSecurity/Solid-Spintax-Specification">Solid Spintax</a>
 * standard from <i>Solid Security</i>.
 * 
 * @author Solid Security
 * @author Vivek Nair
 * @author Jacob Fuehne
 * @since 2.0.0
 */
public class SolidSpintaxIntegerSwitch implements SolidSpintaxElement {
    private final BigInteger min;
    private final BigInteger max;

    public SolidSpintaxIntegerSwitch(BigInteger min, BigInteger max) {
        this.min = min;
        this.max = max;
    }

    @Override
    public String spin(BigInteger tag) {
        //Random rand = new Random();
        //absolute int range
        BigInteger range = max.subtract(min).add(BigInteger.ONE);
        if (tag.add(min).compareTo(max) > 0) {
            System.out.println("SolidIntSwitch tag not in range");
            return "ERROR";
        }
        BigInteger num = tag.add(min);
        String out = num.toString();
        return out;
    }

    @Override
    public String toString() {
        String out = "{";
        out += min.toString() + "-" + max.toString();
        out += "}";
        return out;
    }

    @Override
    public BigInteger countPermutations() {
        BigInteger permutations = max.subtract(min).add(BigInteger.ONE);
        return permutations;
    }

    @Override
    public int countSwitches() {
        return 1;
    }
}
