/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package solid.spintaxer;

import java.util.ArrayList;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 *
 * @author jacob
 */
public class SolidSwitchTest {
    private int repetitions = 100;
    /**
     * Test of addChild method, of class SolidSwitch.
     */
    @Test
    public void testAddChild() {
//        System.out.println("addChild");
        SolidSwitch child = new SolidStrSwitch("Hello");
        SolidSwitch instance = new SolidSwitch();
        instance.addChild(child);
        // TODO review the generated test code and remove the default call to fail.
        assert(instance.getChildren().contains(child));
    }

    /**
     * Test of spin method, of class SolidSwitch.
     */
    @Test
    public void testSpinStrBasic() {
        System.out.println("spin");
        SolidSwitch instance = new SolidSwitch();
        String option1S = "foo";
        String option2S = "bar";
        SolidStrSwitch option1 = new SolidStrSwitch(option1S);
        SolidStrSwitch option2 = new SolidStrSwitch(option2S);
        instance.addChild(option1);
        instance.addChild(option2);
        ArrayList<String> results = new ArrayList<String>();
        for(int i = 0; i < repetitions; i++){
            results.add(instance.spin());
        }
        assert(results.contains(option1S));
        assert(results.contains(option2S));
        // TODO review the generated test code and remove the default call to fail.
    }

    /**
     * Test of toString method, of class SolidSwitch.
     */
    @Test
    public void testToString() {
        System.out.println("toString");
        SolidSwitch instance = new SolidSwitch();
        String expResult = "";
        String result = instance.toString();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }
    
}
