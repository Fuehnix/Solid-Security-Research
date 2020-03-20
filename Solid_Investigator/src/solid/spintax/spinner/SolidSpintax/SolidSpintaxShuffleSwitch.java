package solid.spintax.spinner.SolidSpintax;

import java.math.BigInteger;
import java.util.ArrayList;

/**
 * <i>Solid Spintax Shuffle Switch</i>
 * <p>
 * A standard switch that chooses from amongst its children when spun.
 *
 * Uses the
 * <a href="https://github.com/SolidSecurity/Solid-Spintax-Specification">Solid Spintax</a>
 * standard from <i>Solid Security</i>.
 *
 * @author Solid Security
 * @author Thomas Quig
 * @since 2.0.0
 */
public class SolidSpintaxShuffleSwitch extends SolidSpintaxSwitch implements SolidSpintaxElement {
    private final ArrayList<SolidSpintaxElement> options;
    private final int switches = 0;

    public SolidSpintaxShuffleSwitch() {
        options = new ArrayList<>();
    }

    /**
     * Adds the provided {@code SolidSpintaxElement} to the switch.
     * @param child to be added to the switch
     * @since 2.0.0
     */
    public void addChild(SolidSpintaxElement child) {
        options.add(child);
    }

    @Override
    public String spin(BigInteger tag) {
        int length = options.size(),idxToAdd  = 0;
        ArrayList<String> spunOptions = new ArrayList<String>();
        ArrayList<Integer> indices = new ArrayList<Integer>();
        for (SolidSpintaxElement option : options) {
            indices.add(idxToAdd);
            idxToAdd++;
            BigInteger currPermutations = option.countPermutations();
            if (currPermutations.equals(BigInteger.ONE)) {
                spunOptions.add(option.spin(BigInteger.ZERO));
                continue;
            }
            BigInteger childTag = tag.mod(currPermutations);
            spunOptions.add(option.spin(childTag));
            tag = (tag.subtract(childTag)).divide(currPermutations);
        }
        
        String output = "";
        
        /*
        *   This loop requires explaination. First, you get the idxIdx, which is
        *   the index of the indicies ArrayList. That value at indices[idxIdx]
        *   then removed from the list and stored into optIdx.
        * 
        *   Given optIdx, spunOptions[optIdx] is then appended onto our resultant
        *   String (output). The tag is then recalculated using the algorithm
        *   featured in lines 50-52.
        */
        for(int i = 0; i < length; i++)
        {
            int idxIdx = tag.mod(BigInteger.valueOf(indices.size())).intValue();
            int optIdx = indices.remove(idxIdx);
            output += spunOptions.get(optIdx);
            if(i < (length - 1))
                tag = tag.subtract(tag.mod(BigInteger.valueOf(indices.size())))
                        .divide(BigInteger.valueOf(indices.size()));
        }
        return output;
    }

    @Override
    public String toString() {
        String out = "!" + super.toString();
        return out;
    }

    /**
     * Returns an {@code ArrayList} of this switch's options.
     * @return list of options
     * @since 2.0.0
     */
    public ArrayList<SolidSpintaxElement> getOptions() {
        return options;
    }

    @Override
    public BigInteger countPermutations() {
        BigInteger permutations = BigInteger.ONE;
        for (SolidSpintaxElement s : options) {
            permutations = permutations.multiply(s.countPermutations());
        }
        for(int i = 2; i <= options.size(); i++){
            permutations = permutations.multiply(BigInteger.valueOf(i));
        }
        return permutations;
    }
    @Override
    public int countSwitches() {
        int count = 1;
        count = options.stream().map((s) -> s.countSwitches()).reduce(count, Integer::sum);
        return count;
    }
}