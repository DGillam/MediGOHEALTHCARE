/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package medigohealthcare;

import com.google.api.client.util.Base64;
import static com.google.api.client.util.Charsets.UTF_8;
import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.SetOptions;
import com.google.cloud.firestore.WriteResult;
import com.google.common.io.Files;
import com.google.firebase.cloud.FirestoreClient;
import java.awt.RenderingHints.Key;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import static java.lang.System.out;
import java.nio.charset.Charset;
import java.nio.file.Paths;
import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.KeyStore;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.Signature;
import java.security.SignatureException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.concurrent.ExecutionException;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import relations.Doctor;
import relations.Patients;

/**
 *
 * @author 20175707
 */
public class Security {
    
    public void Security(){
        
    }
    
    // Generate the password and pass it as a String
    public static String generatePassword(){
        int password = (int) (Math.random()*1000000);
        int length = String.valueOf(password).length();
        length = 6 - length;
        String padded = Integer.toString(password);
        switch (length){
            case 1:
                padded = "0" + padded;
                break;
            case 2:
                padded = "00" + padded;
                break;
            case 3:
                padded = "000" + padded;
                break;
            case 4:
                padded = "0000" + padded;
                break;
            case 5:
                padded = "00000" + padded;
                break;
            case 6:
                padded = "000000";
                break;
            default:
        }
        return padded;
    }
    
    // Save encrypted compartment password 
    public static void saveEncryptedCode(String idPatient, String codeString) throws InterruptedException, ExecutionException, NoSuchAlgorithmException, InvalidKeySpecException, NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
        final Firestore db = FirestoreClient.getFirestore();
        
        // Get public key 
        DocumentReference docRef = db.collection("patients").document(idPatient);
        ApiFuture<DocumentSnapshot> future = docRef.get();
        DocumentSnapshot document = future.get();
        Patients patient = null;
        if (document.exists()) {
            patient = document.toObject(Patients.class);
        }
        
        String pubKey = patient.publicKey;
        byte[] publicBytes = Base64.decodeBase64(pubKey.getBytes());
        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(publicBytes);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        PublicKey puk = keyFactory.generatePublic(keySpec);

        // Encrypt code 
        Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
        cipher.init(Cipher.ENCRYPT_MODE, puk);
        byte[] codeEncBytes = cipher.doFinal(codeString.getBytes());
        String codeEnc = Base64.encodeBase64String(codeEncBytes);
        
        // Save enc code on the database
        Map<String, Object> update = new HashMap<>();
        update.put("otp", codeEnc);

        ApiFuture<WriteResult> save = db
            .collection("patients")
            .document(idPatient)
            .set(update, SetOptions.merge());
    }
    
    // Generate doctor key 
    public void generateKeyPair(String email) throws NoSuchAlgorithmException, FileNotFoundException, IOException{
        KeyPairGenerator generator = KeyPairGenerator.getInstance("RSA");
    generator.initialize(2048, new SecureRandom());
    KeyPair pair = generator.generateKeyPair();
        byte[] publicKey = pair.getPublic().getEncoded();
        String pubString = Base64.encodeBase64String(publicKey);
        byte[] privateKey = pair.getPrivate().getEncoded();
        String prvString = Base64.encodeBase64String(privateKey);
        
        // Save public key
        final Firestore db = FirestoreClient.getFirestore();

        Map<String, Object> update = new HashMap<>();
        update.put("publicKey", pubString);

        ApiFuture<WriteResult> save = db
            .collection("doctors")
            .document(email)
            .set(update, SetOptions.merge());
        
        // Save private key
        File prvFile = new File("YOUR PATH//prv.text");
        prvFile.createNewFile();
        Files.write(prvString.getBytes(), prvFile);
    }
        
    // Sign prescription 
    public static String signPrescription(String prescription) throws FileNotFoundException, IOException, NoSuchAlgorithmException, InvalidKeySpecException, InvalidKeyException, SignatureException{
        // Retrive prv
        File prvFile = new File("YOUR PATH//prv.text");
        Scanner myReader = new Scanner(prvFile);
        String prv = null;
        while (myReader.hasNextLine()) {
          if (prv!=null){
        prv = prv + myReader.nextLine();
          } else {
              prv = myReader.nextLine();
          }
        }
        myReader.close();
        
        // Get prv in rigth format
        byte[] privateBytes = Base64.decodeBase64(prv.getBytes());
        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(privateBytes);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        PrivateKey privateKey = keyFactory.generatePrivate(keySpec);
        
        // Sign the prescription
        Signature privateSignature = Signature.getInstance("SHA256withRSA");
        privateSignature.initSign(privateKey);
        privateSignature.update(prescription.getBytes(UTF_8));

        byte[] signature = privateSignature.sign();

        return Base64.encodeBase64String(signature);
    }
    
    // Verify signature
    public static boolean verifySignature(String doctorEmail, String signature, String prescription) throws InterruptedException, NoSuchAlgorithmException, ExecutionException, InvalidKeySpecException, InvalidKeyException, SignatureException{
        final Firestore db = FirestoreClient.getFirestore();
        
        // Get public key 
        DocumentReference docRef = db.collection("doctors").document(doctorEmail);
        ApiFuture<DocumentSnapshot> future = docRef.get();
        DocumentSnapshot document = future.get();
        Doctor doctor = null;
        if (document.exists()) {
            doctor = document.toObject(Doctor.class);
        }
        
        String pubKey = doctor.publicKey;
        byte[] publicBytes = Base64.decodeBase64(pubKey.getBytes());
        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(publicBytes);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        PublicKey publicKey = keyFactory.generatePublic(keySpec);
        
        // Verify
        Signature publicSignature = Signature.getInstance("SHA256withRSA");
        publicSignature.initVerify(publicKey);
        publicSignature.update(prescription.getBytes(UTF_8));

        byte[] signatureBytes = Base64.decodeBase64(signature);

        return publicSignature.verify(signatureBytes);
    }

}

