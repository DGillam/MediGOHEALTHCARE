/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package relations;


/**
 *
 * @author 20175707
 */
public class Doctor {
    public String firstName;
    public String lastName;
    public String email;
    public String password;
    public String publicKey;
    
    public Doctor(){
        
    }
    
    public Doctor(String firstName, String lastName, String email, String password, String publicKey){
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
        this.publicKey = publicKey;
    }
}
