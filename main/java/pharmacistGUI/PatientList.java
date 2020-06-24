/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pharmacistGUI;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SignatureException;
import java.security.spec.InvalidKeySpecException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.concurrent.ExecutionException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import medigohealthcare.DatabaseCommunication;
import medigohealthcare.Security;
import relations.Patients;
import relations.Pharmacist;

/**
 *
 * @author 20175707
 */
public class PatientList extends javax.swing.JFrame {
    
    DefaultListModel patientsListModel;
    DefaultListModel availableListModel;
    DefaultListModel allocatedListModel;
    String selectedMed;
    int selectedIndex;
    String date;
    ArrayList<Patients> patients;
    Pharmacist pharmacist;
    ImageIcon img;
    /**
     * Creates new form PatientList
     */
    public PatientList(DefaultListModel availableListModel, DefaultListModel allocatedListModel, int selectedIndex, String date, Pharmacist pharmacist) throws NullPointerException, InterruptedException, ExecutionException {
        img = new ImageIcon(getClass().getResource("/MediGO round symbol.png"));
        patientsListModel = new DefaultListModel();
        this.availableListModel = availableListModel;
        this.allocatedListModel = allocatedListModel;
        this.selectedIndex = selectedIndex;
        this.date = date;
        this.pharmacist = pharmacist;
        
        initComponents();
        String[] dateDis = date.split(" ");
        dateInfo.setText("Patients indicating " + dateDis[0] + " between " + dateDis[1]);
        
        // Retrieve patients for delivery
        this.patients = DatabaseCommunication.retrievePatientsForDelivery(date, pharmacist.pharmacy);
        ArrayList<Patients> loopPatients = new ArrayList<Patients>();
        loopPatients = DatabaseCommunication.retrievePatientsForDelivery(date, pharmacist.pharmacy);
        Object[] allocatedPatients = allocatedListModel.toArray();
        for(int i = 0; i < loopPatients.size(); i++){
            // Check if patient is of the pharmacy 
            if (!loopPatients.get(i).pharmacy.equals(pharmacist.pharmacy)){
                patients.remove(i);
            }
            // Check if patient never logged in
            if (loopPatients.get(i).publicKey == null){
                patients.remove(i);
            }
            // Check if already out for delivery
            if (!(loopPatients.get(i).otp == null || loopPatients.get(i).otp.equals(""))) {
                patients.remove(i);
            } else {
                for (Object allocated : allocatedPatients) {
                    String[] check = allocated.toString().split(" - ");
                    // Check if already allocated
                    if (loopPatients.get(i).email.equals(check[1])){
                        patients.remove(i);
                    }
                }
            }
        }
        
        // Check if patients is empty
        if (patients.size()==0){
            throw new NullPointerException();
        }
        // Display patients
        for (Patients patient : patients) {
            String info = patient.firstName + " " + patient.lastName + " - " + patient.email; 
            patientsListModel.addElement(info);
        }
        
        // Add button listener
        
        allocate.setEnabled(false);
        
        allocate.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                allocate.setEnabled(false);
            }
        });
        jList1.addListSelectionListener(new ListSelectionListener() {
            public void valueChanged(ListSelectionEvent e) {
                allocate.setEnabled(true);
            }
        });
        
    }

    private PatientList() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jList1 = new javax.swing.JList<>();
        jLabel5 = new javax.swing.JLabel();
        dateInfo = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        symbol = new javax.swing.JLabel();
        allocate = new javax.swing.JButton();
        can = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setIconImage(img.getImage());

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));

        jList1.setModel(patientsListModel);
        jList1.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        jScrollPane1.setViewportView(jList1);

        jLabel5.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel5.setText("as delivery date and time:");

        dateInfo.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        dateInfo.setText("Patients indicating 10/06/2020 between 10:00-14:00 ");

        jPanel2.setBackground(new java.awt.Color(42, 81, 197));

        symbol.setIcon(new javax.swing.ImageIcon(getClass().getResource("/logoBar.jpg"))); // NOI18N

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addComponent(symbol)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addComponent(symbol, javax.swing.GroupLayout.PREFERRED_SIZE, 76, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );

        allocate.setBackground(new java.awt.Color(42, 81, 197));
        allocate.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        allocate.setForeground(new java.awt.Color(255, 255, 255));
        allocate.setText("Allocate Patient");
        allocate.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                allocateActionPerformed(evt);
            }
        });

        can.setBackground(new java.awt.Color(42, 81, 197));
        can.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        can.setForeground(new java.awt.Color(255, 255, 255));
        can.setText("Cancel");
        can.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                canActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(can, javax.swing.GroupLayout.PREFERRED_SIZE, 129, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(allocate))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(dateInfo, javax.swing.GroupLayout.PREFERRED_SIZE, 347, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel1Layout.createSequentialGroup()
                    .addContainerGap()
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                        .addComponent(jLabel5, javax.swing.GroupLayout.DEFAULT_SIZE, 347, Short.MAX_VALUE)
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.LEADING))
                    .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(dateInfo)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 421, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(allocate)
                    .addComponent(can))
                .addGap(19, 19, 19))
            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel1Layout.createSequentialGroup()
                    .addGap(118, 118, 118)
                    .addComponent(jLabel5)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 373, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap(58, Short.MAX_VALUE)))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void allocateActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_allocateActionPerformed
        // TODO add your handling code here:
        
        if (jList1.getSelectedValue() == null) {
            allocate.setEnabled(false);
        }
        String patientInfo = jList1.getSelectedValue();
        String[] patientInfoL = patientInfo.split(" - ");
        String patientEmail = patientInfoL[1];
        try {
            boolean validPrescription;
            validPrescription = verifySignature(patientEmail);
            if (validPrescription) {
                JOptionPane.showMessageDialog(null, "Valid prescription.", "Successfull operation", JOptionPane.PLAIN_MESSAGE);  
                new MedicineAllocation(availableListModel, allocatedListModel, selectedIndex, date, pharmacist, patientEmail, this).setVisible(true);
            } else {
            JOptionPane.showMessageDialog(null, "Prescription not valid.", "Error", JOptionPane.PLAIN_MESSAGE);    
            }    
        } catch (InterruptedException | ExecutionException | NoSuchAlgorithmException | InvalidKeySpecException | InvalidKeyException | SignatureException ex) {
            Logger.getLogger(PatientList.class.getName()).log(Level.SEVERE, null, ex);
        } catch (NullPointerException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, "The selected patient does not have a current prescription.", "Error", JOptionPane.PLAIN_MESSAGE); 
        }
    }//GEN-LAST:event_allocateActionPerformed

    private void canActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_canActionPerformed
        // TODO add your handling code here:
        this.dispose();
    }//GEN-LAST:event_canActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(PatientList.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(PatientList.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(PatientList.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(PatientList.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new PatientList().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton allocate;
    private javax.swing.JButton can;
    private javax.swing.JLabel dateInfo;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JList<String> jList1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel symbol;
    // End of variables declaration//GEN-END:variables
    
    private boolean verifySignature(String patientEmail) throws InterruptedException, ExecutionException, NoSuchAlgorithmException, InvalidKeySpecException, InvalidKeyException, SignatureException {
        Patients p = DatabaseCommunication.readPatientData(patientEmail);
        String doctorEmail = p.doctorEmail;
        String signature = p.prescriptionSignature;
        ArrayList<String> medicines = p.medicineCurrent;
        String prescription = null;
        for (String med : medicines){
            if (prescription == null) {
                if (!med.contains(" -- Started on: ")) {
                        prescription = med;
                   } else {
                        String[] pre = med.split(" -- Started on: ");
                       prescription = pre[0];
                   }
            } else {
                if (!med.contains(" -- Started on: ")) {
                        prescription = prescription + med;
                   } else {
                        String[] pre = med.split(" -- Started on: ");
                       prescription = prescription + pre[0];
                   }
                
        }
        }
        return Security.verifySignature(doctorEmail, signature, prescription);
    
    }

}