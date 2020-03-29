/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package solid.spintaxer;

import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author User
 */
public class SolidSpintaxerRule {
    private String inputRule = "";
    private String outputRule = "";
    private Pattern inputPattern;

    public SolidSpintaxerRule(String input, String output) throws Exception{
        inputRule = input;
        outputRule = output;
        inputPattern = getPatternFromRegexString(inputRule);
    }
    
    public String getInputRule() {
        return inputRule;
    }

    public void setInputRule(String inputRule) {
        this.inputRule = inputRule;
    }

    public String getOutputRule() {
        return outputRule;
    }

    public void setOutputRule(String outputRule) {
        this.outputRule = outputRule;
    }

    public Pattern getInputPattern() {
        return inputPattern;
    }
    
    public static Pattern getPatternFromRegexString(String regex) throws Exception{
        Pattern pattern = Pattern.compile(regex);
        return pattern;
    }
    
    public static String applyOutputRule(String input, Pattern pattern, String outputRule){
        Matcher matcher = pattern.matcher(input);
        System.out.println(matcher.toString());
        matcher.replaceAll(outputRule);
        System.out.println(matcher.toString());
        return matcher.toString();
    }
    
    public String applyOutputRule(String input, String outputRule){
        Matcher matcher = inputPattern.matcher(input);
        System.out.println(matcher.toString());
        matcher.replaceAll(outputRule);
        System.out.println(matcher.toString());
        return matcher.toString();
    }
    
    public String applyOutputRule(String input){
        Matcher matcher = inputPattern.matcher(input);
        System.out.println(matcher.toString());
        matcher.replaceAll(outputRule);
        System.out.println(matcher.toString());
        return matcher.toString();
    }
}
