package solid.spintax.spinner.SolidSpintax;

import java.math.BigInteger;
import java.util.ArrayList;

/**
 * <i>Solid Spintax Switch</i>
 * <p>
 * A standard switch that chooses from amongst its children when spun.
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
public class SolidSpintaxSwitch implements SolidSpintaxElement {
    private final ArrayList<SolidSpintaxElement> children;
    private final int switches = 0;

    public SolidSpintaxSwitch() {
        children = new ArrayList<>();
    }

    /**
     * Adds the provided {@code SolidSpintaxElement} to the switch.
     * @param child to be added to the switch
     * @since 2.0.0
     */
    public void addChild(SolidSpintaxElement child) {
        children.add(child);
    }

    @Override
    public String spin(BigInteger permutation) {
        for (int i = 0; i < children.size(); i++) {
            BigInteger curPermutations = children.get(i).countPermutations();
            if (permutation.compareTo(curPermutations) < 0) {
                return children.get(i).spin(permutation);
            } else {
                permutation = permutation.subtract(curPermutations);
            }
        }
        System.out.println("Error: permutation not reached");
        return "ERROR";
    }

    @Override
    public String toString() {
//        //handles the case of an unnecessary SolidSpintaxSwitch when you only have 1 int switch
//        if(children.size() == 1 && children.get(0) instanceof SolidSpintaxIntegerSwitch){
//            return children.get(0).toString();
//        }
        String out = "{";
        Boolean first = true;
        for (SolidSpintaxElement s : children) {
            if (!first) {
                out += "|";
            }
            first = false;
            out += s.toString();
        }
        out += "}";
        return out;
    }

    /**
     * Returns an {@code ArrayList} of this switch's children.
     * @return list of children
     * @since 2.0.0
     */
    public ArrayList<SolidSpintaxElement> getChildren() {
        return children;
    }

    @Override
    public BigInteger countPermutations() {
        BigInteger permutations = BigInteger.ZERO;
        for (SolidSpintaxElement s : children) {
            permutations = permutations.add(s.countPermutations());
        }
        return permutations;
    }

    @Override
    public int countSwitches() {
        int count = 1;
        count = children.stream().map((s) -> s.countSwitches()).reduce(count, Integer::sum);
        return count;
    }
}
