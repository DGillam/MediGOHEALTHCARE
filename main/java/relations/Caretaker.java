/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package relations;

import java.util.ArrayList;
import java.util.HashMap;

/**
 *
 * @author 20175707
 */
public class Caretaker {
    public String firstName;
    public String lastName;
    public String birthday;
    public String email;
    public String password;
    public ArrayList<String> patients;
    
    public Caretaker(){
        
    }
    
    public Caretaker(String firstName, String lastName, String email, String password,
            ArrayList<String> patients, String birthday){
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
        this.patients = patients;
        this.birthday = birthday;
    }
}
