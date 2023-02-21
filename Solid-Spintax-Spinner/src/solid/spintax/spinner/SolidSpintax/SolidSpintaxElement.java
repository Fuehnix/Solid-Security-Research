package solid.spintax.spinner.SolidSpintax;

import java.math.BigInteger;

/**
 * <i>Solid Spintax Element</i>
 * <p>
 * An (abstract) element in a Solid Spintax standard. Guaranteed to implement
 * several basic methods corresponding to universal functionality.
 * 
 * @author Solid Security
 * @author Vivek Nair
 * @author Jacob Fuehne
 * @since 2.0.0
 */
public interface SolidSpintaxElement {

    /**
     * Generates the document permutation corresponding to {@code permutation}.
     * 
     * @param permutation the number corresponding to the document permutation to be generated
     * @return text value of generated document
     * @since 2.0.0
     */
    public String spin(BigInteger permutation);

    /**
     * Returns the actual spintax corresponding to this object.
     * 
     * @return text value of actual spintax
     * @since 2.0.0
     */
    @Override
    public String toString();

    /**
     * Counts the number of possible permutations in this object.
     * 
     * @return number of permutations
     * @since 2.0.0
     */
    public BigInteger countPermutations();
    
    /**
     * Returns the number of switches in this object.
     * 
     * @return number of switches
     * @since 2.0.0
     */
    public int countSwitches();
}