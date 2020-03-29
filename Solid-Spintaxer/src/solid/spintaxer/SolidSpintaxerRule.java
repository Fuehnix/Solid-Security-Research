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
        inputPattern = getPatternFromRegexString(input);
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
        String out = "";
//        System.out.println("Output rule: " + outputRule);
        Matcher matcher = pattern.matcher(input);
//        System.out.println(matcher.toString());
        out = matcher.replaceAll(outputRule);
//        System.out.println("Out is " + out);
        return out;
    }
    
    public String applyOutputRule(String input, String outputRule){
        String out = "";
//        System.out.println("Output rule: " + outputRule);
        Matcher matcher = inputPattern.matcher(input);
//        System.out.println(matcher.toString());
        out = matcher.replaceAll(outputRule);
//        System.out.println("Out is " + out);
        return out;
    }
    
    public String applyOutputRule(String input){
        String out = "";
//        System.out.println("Output rule: " + outputRule);
        Matcher matcher = inputPattern.matcher(input);
//        System.out.println(matcher.toString());
        out = matcher.replaceAll(outputRule);
//        System.out.println("Out is " + out);
//        System.out.println(matcher.replace("(Happy)\\.", "\\{$&\\|Excited\\.\\|Cheerful\\.\\}"));
        return out;
    }
    
    public String toString(){
        String out = "";
        out += "Replace: \n";
        out += inputRule + "\n";
        out += "with: \n";
        out += outputRule + "\n";
        out += "Input pattern is: \n";
        out += inputPattern;
        return out;
    }
}
