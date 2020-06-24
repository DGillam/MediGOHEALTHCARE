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
public class Deliveries {
    public String id;
    public boolean current;
    public String deliveryDate;
    public ArrayList<String> emailPatients;
    public String emailPharmacist;
    public HashMap<String, ArrayList<String>> medicines;
    public String robotID;
    public String pharmacy;
    
    public Deliveries() {   
    }
    
    public Deliveries(String id, boolean current, String deliveryDate, ArrayList<String> emailPatients, String emailPharmacist, HashMap<String, ArrayList<String>> medicines, String robotID, String pharmacy) {
        this.id = id;
        this.current = current;
        this.deliveryDate = deliveryDate;
        this.emailPatients = emailPatients;
        this.emailPharmacist = emailPharmacist;
        this.medicines = medicines;
        this.robotID = robotID;
        this.pharmacy = pharmacy;
    }
}
