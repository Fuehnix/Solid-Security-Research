/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package solid.investigator.exceptions;

/**
 * <i>InvalidTagDatabaseNameException</i>
 * <p>
 * @author Solid Security
 * @author Jacob Fuehne
 * @since 2.1.0
 */
public class InvalidTagDatabaseNameException extends Exception{
    private String errorMessage = "";
    public InvalidTagDatabaseNameException(){
        errorMessage = "\nERROR: TagDatabase names must not be longer than 50 characters\n\n"; 
    }
    
    public String toString(){
        return errorMessage; 
    }
}
