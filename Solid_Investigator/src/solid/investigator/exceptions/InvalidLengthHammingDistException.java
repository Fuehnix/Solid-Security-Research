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
public class InvalidLengthHammingDistException extends Exception{
    private String errorMessage = "";
    public InvalidLengthHammingDistException(int leakLength, int docPermLength){
        errorMessage = "\nERROR: Hamming distance cannot have texts of differing length.\n" +
        "Leak Length: " + leakLength + ", Permutation Length: " + docPermLength + "\n\n";
    }
    
    public String toString(){
        return errorMessage; 
    }
}
