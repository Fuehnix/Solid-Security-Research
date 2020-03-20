/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package solid.investigator.exceptions;

/**
 *
 * @author jacob
 */
public class TagDatabaseSizeLimitException extends Exception{
    private String errorMessage = "";
    public TagDatabaseSizeLimitException(int num){
        errorMessage = "\nERROR: This tag database contains " + num + " entries,"
                + " but this implementation only supports 100,000 entries at this time.\n\n";
    }
    
    public String toString(){
        return errorMessage; 
    }
}
