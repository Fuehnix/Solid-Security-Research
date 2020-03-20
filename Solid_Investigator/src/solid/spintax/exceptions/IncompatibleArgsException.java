/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package solid.spintax.exceptions;

/**
 *
 * @author jacob
 */
public class IncompatibleArgsException extends Exception{
    private String errorMessage = "";
    public IncompatibleArgsException(String paramA, String paramB){
        errorMessage = "\nERROR: --" + paramA + " cannot be used with --" + paramB + ".\n\n";
    }
    
    public String toString(){
        return errorMessage; 
    }
}
