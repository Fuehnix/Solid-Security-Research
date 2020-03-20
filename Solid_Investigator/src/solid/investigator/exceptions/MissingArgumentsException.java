/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package solid.investigator.exceptions;

/**
 * <i>InvalidLengthHammingDistException</i>
 * <p>
 * @author Solid Security
 * @author Jacob Fuehne
 * @since 2.1.0
 */
public class MissingArgumentsException extends Exception{
    private String errorMessage = "";
    public MissingArgumentsException(){
        errorMessage = "\nERROR: Investigator requires at minimum [Spintax File]"
                + " [Leaked File] [Tag Database] along with -d [OutputModel File]\n\n";
    }
    
    public String toString(){
        return errorMessage; 
    }
}
