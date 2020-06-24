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
public class Patients {
    public String firstName;
    public String lastName;
    public String birthday;
    public String email;
    public String password;
    public ArrayList<String> medicinePast;
    public ArrayList<String> medicineCurrent;
    public String publicKey;
    public String otp;
    public boolean firstLog;
    public String token;
    public String nextDelivery;
    public String pharmacy;
    public String address;
    public String prescriptionSignature;
    public String doctorEmail;
    public String caretaker;
    
    public Patients(){
        
    }
    
    public Patients(String firstName, String lastName, String birthday, 
            String email, String password, ArrayList<String> medicinePast, 
            ArrayList<String> medicineCurrent, String publicKey, 
            boolean firstLog, String otp, String token, String nextDelivery, 
            String pharmacy, String address, String prescriptionSignature,
            String doctorEmail, String caretaker){
        this.firstName = firstName;
        this.lastName = lastName;
        this.birthday = birthday;
        this.email = email;
        this.password = password;
        this.medicinePast = medicinePast;
        this.medicineCurrent = medicineCurrent;
        this.publicKey = publicKey;
        this.otp = otp;
        this.firstLog = firstLog;
        this.token = token;
        this.nextDelivery = nextDelivery;
        this.pharmacy = pharmacy;
        this.address = address;
        this.prescriptionSignature = prescriptionSignature;
        this.doctorEmail = doctorEmail;
        this.caretaker = caretaker;
    }
    
}
