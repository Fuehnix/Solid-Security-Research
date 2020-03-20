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
public class ModelNotFoundException extends Exception{
    private String errorMessage = "";
    public ModelNotFoundException(Exception e){
        errorMessage = "Model data was unable to be written to file, errmsg: " + e;
    }
    
    public String toString(){
        return errorMessage; 
    }
}
