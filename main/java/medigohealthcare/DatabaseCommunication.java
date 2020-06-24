/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package medigohealthcare;

import com.google.api.client.util.Base64;
import com.google.api.core.ApiFuture;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.firestore.CollectionReference;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.Query;
import com.google.cloud.firestore.QueryDocumentSnapshot;
import com.google.cloud.firestore.QuerySnapshot;
import com.google.cloud.firestore.SetOptions;
import com.google.cloud.firestore.WriteResult;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.cloud.FirestoreClient;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.Message;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import pharmacistGUI.SendNotification;
import relations.Doctor;
import relations.Medicines;
import relations.Patients;
import relations.Pharmacist;
import com.google.api.core.ApiFuture;
import com.google.api.core.ApiFutures;
import com.google.cloud.firestore.CollectionReference;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.FieldValue;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.QueryDocumentSnapshot;
import com.google.cloud.firestore.QuerySnapshot;
import com.google.cloud.firestore.WriteResult;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import relations.Caretaker;
import relations.Deliveries;
import relations.Pharmacy;

/**
 *
 * @author 20175707
 */
public class DatabaseCommunication {
    
    // Connect to the database
    public static void connectToDatabase() throws FileNotFoundException, IOException{
        InputStream serviceAccount = DatabaseCommunication.class.getResourceAsStream("YOUR KEY");
        
        FirebaseOptions options = new FirebaseOptions.Builder()
            .setCredentials(GoogleCredentials.fromStream(serviceAccount))
            .setDatabaseUrl("YOUR DATABASE URL")
            .build();
        FirebaseApp.initializeApp(options);
    }
    
    // Push data patient when added to the system
    public static void writePatientData(String firstName, String lastName, 
            String birthday, String userName, 
            String password, ArrayList<String> medicineCurrent, String nextDelivery,
            String address, String pharmacy, String doctorEmail, String signature) {
        
        final Firestore db = FirestoreClient.getFirestore();
        Map<String, Object> docData = new HashMap<>();
        docData.put("firstName", firstName);
        docData.put("lastName", lastName);
        docData.put("birthday", birthday);
        docData.put("email", userName);
        docData.put("password", password);
        docData.put("medicineCurrent", medicineCurrent);
        docData.put("firstLog", true);
        docData.put("nextDelivery", nextDelivery);
        docData.put("address", address);
        docData.put("pharmacy", pharmacy);
        docData.put("doctorEmail", doctorEmail);
        docData.put("prescriptionSignature", signature);
        docData.put("caretaker", null);
        ApiFuture<WriteResult> future = db.collection("patients").document(userName).set(docData);
    }
    
    // Get data patient
    public static Patients readPatientData(String firstName, String lastName, 
            String birthday) throws InterruptedException, ExecutionException {
        
        final Firestore db = FirestoreClient.getFirestore();
        Patients patient = null;
        CollectionReference patients = db.collection("patients");
        
        Query query = patients.whereEqualTo("firstName", firstName)
                .whereEqualTo("lastName", lastName)
                .whereEqualTo("birthday", birthday);
        ApiFuture<QuerySnapshot> querySnapshot = query.get();
        for (DocumentSnapshot document : querySnapshot.get().getDocuments()) {
            patient = document.toObject(Patients.class);
        }
        return patient;
    }
    
    // Get data patient only email
    public static Patients readPatientData(String email) throws InterruptedException, ExecutionException {
        
        final Firestore db = FirestoreClient.getFirestore();
        Patients patient = null;
        CollectionReference patients = db.collection("patients");
        
        Query query = patients.whereEqualTo("email", email);
        ApiFuture<QuerySnapshot> querySnapshot = query.get();
        for (DocumentSnapshot document : querySnapshot.get().getDocuments()) {
            patient = document.toObject(Patients.class);
        }
        return patient;
    }
    
    // Loggin doctor
    public static Doctor logginDoctor(String email, String password) 
            throws InterruptedException, ExecutionException {
        
        final Firestore db = FirestoreClient.getFirestore();
        Doctor doctor = null;
        CollectionReference patients = db.collection("doctors");
        
        Query query = patients.whereEqualTo("email", email)
                .whereEqualTo("password", password);
        ApiFuture<QuerySnapshot> querySnapshot = query.get();
        for (DocumentSnapshot document : querySnapshot.get().getDocuments()) {
            doctor = document.toObject(Doctor.class);
        }
        return doctor;
    }
    
    // Loggin pharmacist
    public static Pharmacist logginPharm(String email, String password) 
            throws InterruptedException, ExecutionException {
        
        final Firestore db = FirestoreClient.getFirestore();
        Pharmacist pharmacist = null;
        CollectionReference patients = db.collection("pharmacists");
        
        Query query = patients.whereEqualTo("email", email)
                .whereEqualTo("password", password);
        ApiFuture<QuerySnapshot> querySnapshot = query.get();
        for (DocumentSnapshot document : querySnapshot.get().getDocuments()) {
            pharmacist = document.toObject(Pharmacist.class);
        }
        return pharmacist;
    }
    
    // Retrieve medicines
    public static HashMap<String, String> readMedicines() throws InterruptedException, ExecutionException {
        
        final Firestore db = FirestoreClient.getFirestore();
        HashMap<String, String> medicineList = new HashMap<String, String>();
        CollectionReference medicines = db.collection("medicines");
        Query query = medicines;
        ApiFuture<QuerySnapshot> querySnapshot = query.get();
        for (DocumentSnapshot document : querySnapshot.get().getDocuments()) {
            Medicines med = document.toObject(Medicines.class);
            medicineList.put(med.medicineName, med.description);
        }

        return medicineList;
        
    }
    
    // Update medicines patient doctor
    public static void updatePatientMedicines(String idPatient, ArrayList<String> medicineCurrent, ArrayList<String> medicinePast, String doctorEmail, String prescriptionSignature) {
        final Firestore db = FirestoreClient.getFirestore();
        
        Map<String, Object> update = new HashMap<>();
        update.put("medicineCurrent", medicineCurrent);
        update.put("medicinePast", medicinePast);
        update.put("doctorEmail", doctorEmail);
        update.put("prescriptionSignature", prescriptionSignature);

        ApiFuture<WriteResult> writeResult = db
            .collection("patients")
            .document(idPatient)
            .set(update, SetOptions.merge());
    }
    
    // Update medicines pharmacist
    public static void updatePatientMedicines(String idPatient, ArrayList<String> medicineCurrent, ArrayList<String> medicinePast) {
        final Firestore db = FirestoreClient.getFirestore();
        
        Map<String, Object> update = new HashMap<>();
        update.put("medicineCurrent", medicineCurrent);
        update.put("medicinePast", medicinePast);

        ApiFuture<WriteResult> writeResult = db
            .collection("patients")
            .document(idPatient)
            .set(update, SetOptions.merge());
    }
    
    // Retrieve patients for delivery 
    public static ArrayList<Patients> retrievePatientsForDelivery(String date, String pharmacy) 
            throws InterruptedException, ExecutionException {
        
        final Firestore db = FirestoreClient.getFirestore();
        ArrayList<Patients> patients = new ArrayList<Patients>();
        CollectionReference pat = db.collection("patients");
        
        Query query = pat
                .whereEqualTo("nextDelivery", date)
                .whereEqualTo("pharmacy", pharmacy);
        ApiFuture<QuerySnapshot> querySnapshot = query.get();
        for (DocumentSnapshot document : querySnapshot.get().getDocuments()) {
            patients.add(document.toObject(Patients.class));
        }
        
        String dateAndTime2 = date;
        String dateAndTime = dateAndTime2.replace("/", "-");
        String[] dt = dateAndTime.split("-");
        
        String month = null;
        switch(dt[1]){
            case "January":
                month = "1";
                break;
            case "February":
                month = "2";
                break;
            case "March":
                month = "3";
                break;
            case "April":
                month = "4";
                break;
            case "May":
                month = "5";
                break;
            case "June":
                month = "6";
                break;
            case "July":
                month = "7";
                break;
            case "August":
                month = "8";
                break;
            case "September":
                month = "9";
                break;
            case "October":
                month = "10";
                break;
            case "November":
                month = "11";
                break;
            case "December":
                month = "12";
                break;
        }
        
        String dateQ2 = dt[0] + "-" + month + "-" + dt[2] + "-" + dt[3];
        
        Query query2 = pat
                .whereEqualTo("nextDelivery", dateQ2)
                .whereEqualTo("pharmacy", pharmacy);
        ApiFuture<QuerySnapshot> querySnapshot2 = query2.get();
        for (DocumentSnapshot document : querySnapshot2.get().getDocuments()) {
            patients.add(document.toObject(Patients.class));
        }
        
        return patients;
    }
    
    // Send message to user app about the delivery start
    public static void notifyUser(String email, int time){
        //Get token
        final Firestore db = FirestoreClient.getFirestore();
        DocumentReference docRef = db.collection("patients").document(email);
        ApiFuture<DocumentSnapshot> future = docRef.get();
        DocumentSnapshot document = null;
        try {
            document = future.get();
        } catch (InterruptedException ex) {
            Logger.getLogger(SendNotification.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ExecutionException ex) {
            Logger.getLogger(SendNotification.class.getName()).log(Level.SEVERE, null, ex);
        }
        Patients patient = null;
        if (document.exists()) {
            patient = document.toObject(Patients.class);
        }
        String registrationToken = patient.token;
        
        // Create message
        Message message = Message.builder()
            .putData("Patient", patient.firstName + " " + patient.lastName)
            .putData("Arrival", String.valueOf(time))
            .setToken(registrationToken)
            .build();
        String response=null;
        try {
            // Send a message to the device corresponding to the provided
            // registration token.
            response = FirebaseMessaging.getInstance().send(message);
        } catch (FirebaseMessagingException ex) {
            Logger.getLogger(SendNotification.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        System.out.println("Successfully sent message: " + response);
    }
    
    
    // Add a delivery
    public static void addDelivery(String id, String emailPharmacist, String pharmacy, String deliveryDate, String robotID) {
        
        final Firestore db = FirestoreClient.getFirestore();
        Map<String, Object> docData = new HashMap<>();
        docData.put("id", id);
        docData.put("emailPharmacist", emailPharmacist);
        docData.put("pharmacy", pharmacy);
        docData.put("deliveryDate", deliveryDate);
        docData.put("robotID", robotID);
        docData.put("current", true);

        ApiFuture<WriteResult> future = db.collection("deliveries").document(id).set(docData);
    }
    
    // Add a delivery patients info
    public static void addDeliveryPatientsInfo(String id, ArrayList<String> emailPatients, HashMap<String, ArrayList<String>> medicines) {
        
        final Firestore db = FirestoreClient.getFirestore();
        
        Map<String, Object> update = new HashMap<>();
        update.put("emailPatients", emailPatients);
        update.put("medicines", medicines);
        
        ApiFuture<WriteResult> writeResult = db
            .collection("deliveries")
            .document(id)
            .set(update, SetOptions.merge());
    }
    
    // Add delivey to patient
    public static void addDeliveryPatient(String email, String deliveryID) throws InterruptedException, ExecutionException{
        final Firestore db = FirestoreClient.getFirestore();
        DocumentReference patient = db.collection("patients").document(email);

        // Atomically add a new delivery.
        ApiFuture<WriteResult> arrayUnion = patient.update("deliveries",
            FieldValue.arrayUnion(deliveryID));
        
        // Get patient
        ApiFuture<DocumentSnapshot> future = patient.get();
        // block on response
        DocumentSnapshot document = future.get();
        Patients p = null;
        if (document.exists()) {
          p = document.toObject(Patients.class);
        } 
        
        String pastD = putPastDelivery(p.nextDelivery);
        
        // Update deliveries fields. 
        ApiFuture<WriteResult> next = patient.update("nextDelivery", "");
            WriteResult r1 = next.get();
        ApiFuture<WriteResult> past = patient.update("pastDelivery", pastD);
            WriteResult r2 = past.get();
    }
    
    // Add delivey to pharmacist
    public static void addDeliveryPharmacist(String email, String deliveryID){
        final Firestore db = FirestoreClient.getFirestore();
        DocumentReference patient = db.collection("pharmacists").document(email);

        // Atomically add a new delivery.
        ApiFuture<WriteResult> arrayUnion = patient.update("deliveriesPerformed",
            FieldValue.arrayUnion(deliveryID));
    }

    // get current deliveries
    public static ArrayList<Deliveries> getCurrentDeliveries(Pharmacist pharmacist) throws InterruptedException, ExecutionException {
        final Firestore db = FirestoreClient.getFirestore();
        ArrayList<Deliveries> deliveries = new ArrayList<Deliveries>();
        CollectionReference d = db.collection("deliveries");
        
        Query query = d
                .whereEqualTo("pharmacy", pharmacist.pharmacy)
                .whereEqualTo("current", true);
        ApiFuture<QuerySnapshot> querySnapshot = query.get();
        for (DocumentSnapshot document : querySnapshot.get().getDocuments()) {
            deliveries.add(document.toObject(Deliveries.class));
        }
        return deliveries;
    }
    
    // get past deliveries 
    public static ArrayList<Deliveries> getPastDeliveries(Pharmacist pharmacist) throws InterruptedException, ExecutionException {
        final Firestore db = FirestoreClient.getFirestore();
        ArrayList<Deliveries> deliveries = new ArrayList<Deliveries>();
        CollectionReference d = db.collection("deliveries");
        
        Query query = d
                .whereEqualTo("pharmacy", pharmacist.pharmacy)
                .whereEqualTo("current", false);
        ApiFuture<QuerySnapshot> querySnapshot = query.get();
        for (DocumentSnapshot document : querySnapshot.get().getDocuments()) {
            deliveries.add(document.toObject(Deliveries.class));
        }
        return deliveries;
    }
    
    // Update delivery state
    public static void updateDeliveryState(String id) throws InterruptedException, ExecutionException {
        final Firestore db = FirestoreClient.getFirestore();
        
        Map<String, Object> update = new HashMap<>();
        update.put("current", false);

        ApiFuture<WriteResult> writeResult = db
            .collection("deliveries")
            .document(id)
            .set(update, SetOptions.merge());
        
        // Get delivery
        ApiFuture<DocumentSnapshot> future = db
            .collection("deliveries")
            .document(id)
            .get();
        DocumentSnapshot document = future.get();
        Deliveries d = null;
        if (document.exists()) {
          d = document.toObject(Deliveries.class);
        } 
        
        // Get patient emails
        ArrayList<String> patientEmails = new ArrayList<>();
        for (String email : d.emailPatients){
            patientEmails.add(email);
        }
        
        // Update otp
        Map<String, Object> updateOtp = new HashMap<>();
        updateOtp.put("otp", "");
        
        for (String pEmail : patientEmails) {
            ApiFuture<WriteResult> w = db
                .collection("patients")
                .document(pEmail)
                .set(updateOtp, SetOptions.merge());
        }
    }
    
    // Get delivery
    public static Deliveries getDelivery(String id) throws InterruptedException, ExecutionException {
        final Firestore db = FirestoreClient.getFirestore();
        DocumentReference d = db.collection("deliveries").document(id);
        
        // Get delivery
        ApiFuture<DocumentSnapshot> future = d.get();
        // block on response
        DocumentSnapshot document = future.get();
        Deliveries delivery = null;
        if (document.exists()) {
          delivery = document.toObject(Deliveries.class);
        } 
        
        return delivery;
    }

    // Get pharmacies addresses
    public static HashMap<String, String> retrievePharmaciesAddresses() throws InterruptedException, ExecutionException {
        final Firestore db = FirestoreClient.getFirestore();
        HashMap<String, String> pAddresses = new HashMap<>();
        
        ApiFuture<QuerySnapshot> future = db.collection("pharmacy").get();
        // future.get() blocks on response
        List<QueryDocumentSnapshot> documents = future.get().getDocuments();
        for (QueryDocumentSnapshot document : documents) {
          Pharmacy pharm = document.toObject(Pharmacy.class);
          pAddresses.put(pharm.name, pharm.address);
        }
        
        return pAddresses;
    }
    
    // Get pharmacy address for pharmacist
    public static String getPharmacyAddress(String pharmacistPharmacy) throws InterruptedException, ExecutionException{
        final Firestore db = FirestoreClient.getFirestore();
        Pharmacy pharmacy = null;
        CollectionReference pharm = db.collection("pharmacy");
        
        Query query = pharm.whereEqualTo("name", pharmacistPharmacy);
        ApiFuture<QuerySnapshot> querySnapshot = query.get();
        for (DocumentSnapshot document : querySnapshot.get().getDocuments()) {
            pharmacy = document.toObject(Pharmacy.class);
        }
        return pharmacy.address;
    }
    
    // get pharmacist
    public static ArrayList<Pharmacist> getPharmacists(String pharmacy) throws InterruptedException, ExecutionException {
        final Firestore db = FirestoreClient.getFirestore();
        ArrayList<Pharmacist> pharmacists = new ArrayList<Pharmacist>();
        CollectionReference d = db.collection("pharmacists");
        
        Query query = d
                .whereEqualTo("pharmacy", pharmacy);
              //  .whereEqualTo("responsible", false);
        ApiFuture<QuerySnapshot> querySnapshot = query.get();
        for (DocumentSnapshot document : querySnapshot.get().getDocuments()) {
            pharmacists.add(document.toObject(Pharmacist.class));
        }
        return pharmacists;
    }

    // Add pharmacist to the system
    public static void addPharmacist(String firstName, String lastName, String email, String password, boolean resposible, String pharmacy) {
        final Firestore db = FirestoreClient.getFirestore();
        Map<String, Object> docData = new HashMap<>();
        docData.put("firstName", firstName);
        docData.put("lastName", lastName);
        docData.put("email", email);
        docData.put("password", password);
        docData.put("pharmacy", pharmacy);
        docData.put("responsible", resposible);
        ApiFuture<WriteResult> future = db.collection("pharmacists").document(email).set(docData);
    }

    public static void removePharmacist(String pEmail) {
        final Firestore db = FirestoreClient.getFirestore();
        DocumentReference docRef = db.collection("pharmacists").document(pEmail);
        Map<String, Object> updates = new HashMap<>();
        updates.put("password", FieldValue.delete());
        // Update and delete the "capital" field in the document
        ApiFuture<WriteResult> writeResult = docRef.update(updates);
    }

    public static ArrayList<Caretaker> getCaretakers() throws InterruptedException, ExecutionException {
        final Firestore db = FirestoreClient.getFirestore();
        ArrayList<Caretaker> caretakers = new ArrayList<>();
        
        ApiFuture<QuerySnapshot> future = db.collection("caretakers").get();
        // future.get() blocks on response
        List<QueryDocumentSnapshot> documents = future.get().getDocuments();
        for (QueryDocumentSnapshot document : documents) {
          caretakers.add(document.toObject(Caretaker.class));
        }
        
        return caretakers;
    }

    // add caretaker
    public static void addCaretaker(String firstName, String lastName, String birthday, String email, String password, ArrayList<String> patients) {
        final Firestore db = FirestoreClient.getFirestore();
        Map<String, Object> docData = new HashMap<>();
        docData.put("firstName", firstName);
        docData.put("lastName", lastName);
        docData.put("birthday", birthday);
        docData.put("email", email);
        docData.put("password", password);
        docData.put("patients", patients);
        ApiFuture<WriteResult> future = db.collection("caretakers").document(email).set(docData);
    }

    // add patient with caretaker
    public static void writePatientDataCaretaker(String firstName, String lastName, String birthday, String patientToAdd, ArrayList<String> medicineCurrent, String nextDelivery, String address, String pharmacyClosest, String dEmail, String signature, String cEmail) {
        final Firestore db = FirestoreClient.getFirestore();
        Map<String, Object> docData = new HashMap<>();
        docData.put("firstName", firstName);
        docData.put("lastName", lastName);
        docData.put("birthday", birthday);
        docData.put("email", patientToAdd);
        docData.put("password", null);
        docData.put("medicineCurrent", medicineCurrent);
        docData.put("firstLog", true);
        docData.put("nextDelivery", nextDelivery);
        docData.put("address", address);
        docData.put("pharmacy", pharmacyClosest);
        docData.put("doctorEmail", dEmail);
        docData.put("prescriptionSignature", signature);
        docData.put("caretaker", cEmail);
        ApiFuture<WriteResult> future = db.collection("patients").document(patientToAdd).set(docData);
    }

    // add patient to caretaker
    public static void updateCaretakerPatients(String email, String patientToAdd) {
        final Firestore db = FirestoreClient.getFirestore();
        DocumentReference patient = db.collection("caretakers").document(email);

        // Atomically add a new delivery.
        ApiFuture<WriteResult> arrayUnion = patient.update("patients",
            FieldValue.arrayUnion(patientToAdd));
    }
    
    public static String putPastDelivery(String pastDelivery){
        String[] dateAndTime = pastDelivery.split(" ");
        
        String[] date = dateAndTime[0].split("/");
        String[] dateD = null;
        
        String day = null;
        if (date.length == 1) {
            dateD = date[0].split("-");
            switch(dateD[0]){
            case "1":
                day = "01";
                break;
            case "2":
                day = "02";
                break;
            case "3":
                day = "03";
                break;
            case "4":
                day = "04";
                break;
            case "5":
                day = "05";
                break;
            case "6":
                day = "06";
                break;
            case "7":
                day = "07";
                break;
            case "8":
                day = "08";
                break;
            case "9":
                day = "09";
                break;
            default:
                day = dateD[0];
        }
            
        } else {
        switch(date[0]){
            case "1":
                day = "01";
                break;
            case "2":
                day = "02";
                break;
            case "3":
                day = "03";
                break;
            case "4":
                day = "04";
                break;
            case "5":
                day = "05";
                break;
            case "6":
                day = "06";
                break;
            case "7":
                day = "07";
                break;
            case "8":
                day = "08";
                break;
            case "9":
                day = "09";
                break;
            default:
                day = date[0];
        }
        }
        String month = null;
        
        if (date.length == 1) {
            dateD = date[0].split("-");
            switch(dateD[1]){
            case "1":
                month = "01";
                break;
            case "2":
                month = "02";
                break;
            case "3":
                month = "03";
                break;
            case "4":
                month = "04";
                break;
            case "5":
                month = "05";
                break;
            case "6":
                month = "06";
                break;
            case "7":
                month = "07";
                break;
            case "8":
                month = "08";
                break;
            case "9":
                month = "09";
                break;
            default:
                month = dateD[1];
        }
            
        } else {
        switch(date[1]){
            case "January":
                month = "01";
                break;
            case "February":
                month = "02";
                break;
            case "March":
                month = "03";
                break;
            case "April":
                month = "04";
                break;
            case "May":
                month = "05";
                break;
            case "June":
                month = "06";
                break;
            case "July":
                month = "07";
                break;
            case "August":
                month = "08";
                break;
            case "September":
                month = "09";
                break;
            case "October":
                month = "10";
                break;
            case "November":
                month = "11";
                break;
            case "December":
                month = "12";
                break;
        }
        }
        String returnString = null;
        if (date.length == 1) {
            returnString = day + "-" + month + "-" + dateD[2];
        } else {
            returnString = day + "-" + month + "-" + date[2];
        }
        return returnString;
    }
}
    

