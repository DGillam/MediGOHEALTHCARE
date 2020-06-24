/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package relations;

import java.util.ArrayList;


/**
 *
 * @author 20175707
 */
public class Pharmacist {
    public String firstName;
    public String lastName;
    public String email;
    public String password;
    public ArrayList<String> deliveriesPerfomed;
    public boolean responsible;
    public String pharmacy;
    
    public Pharmacist(){
        
    }
    
    public Pharmacist(String firstName, String lastName, String email, String password,
            String pharmacy, boolean responsible, ArrayList<String> deliveriesPerfomed){
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
        this.deliveriesPerfomed = deliveriesPerfomed;
        this.responsible = responsible;
        this.pharmacy = pharmacy;
    }
}
