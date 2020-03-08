package solid.spintax.spinner.SolidSpintax;

import java.math.BigInteger;

/**
 * <i>Solid Spintax Text</i>
 * <p>
 * A basic spintax element that returns a piece of text when spun.
 * 
 * @author Solid Security
 * @author Vivek Nair
 * @author Jacob Fuehne
 * @since 2.0.0
 */
public class SolidSpintaxText implements SolidSpintaxElement {
    private String body;

    public SolidSpintaxText(String body) {
        this.body = body;
    }

    @Override
    public String spin(BigInteger tag) {
        if (!tag.equals(BigInteger.ZERO)) {
            System.out.print("ERROR: Unable to generate spintax.");
            System.exit(1);
        }
        return body;
    }

    @Override
    public String toString() {
        String out = "";
        for (int i = 0; i < body.length(); i++) {
            char curr = body.charAt(i);
            if (curr == '@' || curr == '|' || curr == '{' || curr == '}' || curr == '\\') {
                out += '\\';
            }
            out += curr;
        }
        return out;
    }

    @Override
    public BigInteger countPermutations() {
        return BigInteger.ONE;
    }

    @Override
    public int countSwitches() {
        return 0;
    }
    
    @Override
    public BigInteger identify(String input){
        BigInteger bi = new BigInteger(input);
        System.out.println(bi);
        bi = BigInteger.valueOf(10);
        return bi;
    }
}
