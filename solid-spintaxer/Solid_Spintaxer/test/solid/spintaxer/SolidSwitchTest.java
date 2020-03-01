/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package solid.spintaxer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 *
 * @author jacob
 */
public class SolidSwitchTest {
    private int repetitions = 100;
    /**
     * Test of addChild method, of class SolidSwitch.
     */
    
    //TO DO: FIX THESE AND TURN THEM INTO TEST CASES
    /**
     *  
     *  
        SolidIntSwitch test = new SolidIntSwitch(1000,9000);
        System.out.println(test.spin(8000));
        SolidSwitch s1 = new SolidSwitch();
        SolidStrSwitch x = new SolidStrSwitch("x");
        SolidStrSwitch y = new SolidStrSwitch("y");
        SolidStrSwitch z = new SolidStrSwitch("z");
        s1.addChild(x);
        s1.addChild(y);
        s1.addChild(z);
        SolidSwitch s2 = new SolidSwitch();
        SolidIntSwitch int5560 = new SolidIntSwitch(55,65);
        s2.addChild(int5560);
        SolidStrSwitch a = new SolidStrSwitch("a");
        SolidStrSwitch b = new SolidStrSwitch("b");
        SolidStrSwitch c = new SolidStrSwitch("c");
        SolidSwitch s3 = new SolidSwitch();
        s3.addChild(a);
        s3.addChild(b);
        s3.addChild(c);
        s2.addChild(s3);
        SolidIntSwitch int1020 = new SolidIntSwitch(10,20);
        s2.addChild(int1020);
        SolidSwitch parent = new SolidSwitch();
        parent.addChild(s1);
        parent.addChild(s2);
//        System.out.println(parent.toString());
//        System.out.println(parent.spin(3));
        //TEST SolidText
        SolidIntSwitch int010 = new SolidIntSwitch(0,10);
        SolidIntSwitch int06 = new SolidIntSwitch(0,6);
        SolidIntSwitch int0102 = new SolidIntSwitch(0,10);
        SolidText test2 = new SolidText();
        test2.addSwitch(int010);
        test2.addSwitch(int06);
        test2.addSwitch(int0102);
        System.out.println(test2.spin(100));
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
        SolidSwitch instance = new SolidSwitch();
        String option1S = "foo";
        String option2S = "bar";
        SolidStrSwitch option1 = new SolidStrSwitch(option1S);
        SolidStrSwitch option2 = new SolidStrSwitch(option2S);
        instance.addChild(option1);
        instance.addChild(option2);
        ArrayList<String> results = new ArrayList<String>();
        Random rand = new Random();
        for(int i = 0; i < repetitions; i++){
            int perm = rand.nextInt(2);
            results.add(instance.spin(perm));
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
        SolidSwitch instance = new SolidSwitch();
        SolidStrSwitch sSwitch = new SolidStrSwitch("test");
        instance.addChild(sSwitch);
        String expResult = "{test}";
        String result = instance.toString();
//        System.out.println(expResult + "  " + result);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
    }
    
    @Test
    public void testIntSwitchBasic(){
        SolidSwitch instance = new SolidSwitch();
        SolidIntSwitch iSwitch = new SolidIntSwitch(0,6);
        instance.addChild(iSwitch);
        String result = instance.toString();
        ArrayList<String> results = new ArrayList<String>();
        Random rand = new Random();
        for(int i = 0; i < repetitions; i++){
            int perm = rand.nextInt(7);
            results.add(instance.spin(perm));
        }
        Integer array[] = {0,1,2,3,4,5,6};
        for(String entry : results){
            int num = Integer.parseInt(entry);
            assert(Arrays.asList(array).contains(num));
        }
    }
    
    @Test
    public void testGlobalSwitchBasic(){
        String input = "Project @{hello | {foo|bar} | {100-200}} is ..."
                    + "Projecct @{hello | {foo|bar} | {100-200}} is ...";
        SolidStrSwitch foo = new SolidStrSwitch("foo");
        SolidStrSwitch bar = new SolidStrSwitch("bar");
        SolidSwitch foobar = new SolidSwitch();
        foobar.addChild(foo);
        foobar.addChild(bar);
        SolidStrSwitch hello = new SolidStrSwitch("hello");
        SolidIntSwitch intSwitch = new SolidIntSwitch(100,200);
        SolidGlobalSwitch mainSwitch = new SolidGlobalSwitch();
        mainSwitch.addChild(hello);
        mainSwitch.addChild(foobar);
        mainSwitch.addChild(intSwitch);
        SolidGlobalSwitch switch2 = new SolidGlobalSwitch();
        switch2.addChild(hello);
        switch2.addChild(foobar);
        switch2.addChild(intSwitch);
        switch2.setMaster(mainSwitch);
        SolidStrSwitch project = new SolidStrSwitch("Project ");
        SolidStrSwitch is = new SolidStrSwitch(" is ... \n");
        SolidText text = new SolidText();
        text.addSwitch(project);
        text.addSwitch(mainSwitch);
        text.addSwitch(is);
        text.addSwitch(project);
        text.addSwitch(switch2);
        text.addSwitch(is);
        int perm = text.permutations();
        for(int i = 0; i < perm; i++){
            String out = text.spin(i);
            String[] arrOut = out.split("\n");
//            System.out.println(arrOut[0]);
//            System.out.println(arrOut[1]);
            assert(arrOut[0].equals(arrOut[1]));
        }
    }
    
    @Test
    public void testSolidText(){
        SolidIntSwitch int010 = new SolidIntSwitch(0,10);
        SolidIntSwitch int0102 = new SolidIntSwitch(0,10);
        SolidIntSwitch int0103 = new SolidIntSwitch(0,10);
        SolidText test2 = new SolidText();
        test2.addSwitch(int010);
        test2.addSwitch(int0102);
        test2.addSwitch(int0103);
        int perm = test2.permutations();
        for(int i = 0; i < perm; i++){
            int tag = i;
            String out = test2.spin(i);
            int s1 = tag % 11;
            tag = (tag-s1)/11;
            int s2 = tag % 11;
            tag = (tag-s2)/11;
            int s3 = tag % 11;
            tag = tag-s3/11;
            String expected = Integer.toString(s1);
            expected += Integer.toString(s2);
            expected += Integer.toString(s3);
            assert(Integer.parseInt(out) == Integer.parseInt(expected));
        }
    }
    
    
    
    
    
}
