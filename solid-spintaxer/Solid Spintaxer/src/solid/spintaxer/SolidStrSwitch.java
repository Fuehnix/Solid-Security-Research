/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package solid.spintaxer;

/**
 *
 * @author jacob
 */
public class SolidStrSwitch extends SolidSwitch {
    private String body;

    public SolidStrSwitch(String body){
        this.body = body;
    }
    
    public String spin(int tag){
        if(tag != 0){
            System.out.print("SolidStrSwitch tag is not 0");
        }
        return body;
    }
    
    public String toString() {
        String out =  "";
        for(int i = 0; i < body.length(); i++) {
            char curr = body.charAt(i);
            if(curr == '@' || curr == '|' || curr == '{' || curr == '}' || curr == '\\') {
                out += '\\';
            }
            out += curr;
        }
        return out;
    }
    
    public int permutations(){
        return 1;
    }
}
