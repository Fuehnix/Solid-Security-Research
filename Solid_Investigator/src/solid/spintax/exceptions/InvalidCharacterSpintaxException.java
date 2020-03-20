/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package solid.spintax.exceptions;

/**
 * <i>InvalidCharacterSpintaxException</i>
 * <p>
 * @author Solid Security
 * @author Jacob Fuehne
 * @since 2.1.0
 */
public class InvalidCharacterSpintaxException {
    private String errorMessage = "";
    public InvalidCharacterSpintaxException(char a){
        errorMessage = "\nERROR: " + a + " is not a valid character + \n";
    }
    
    public String toString(){
        return errorMessage; 
    }
}
