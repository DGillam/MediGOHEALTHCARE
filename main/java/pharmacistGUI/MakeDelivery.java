/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pharmacistGUI;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.Timestamp;
import java.security.spec.InvalidKeySpecException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.microedition.io.StreamConnection;
import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import javax.swing.ListModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import medigohealthcare.DatabaseCommunication;
import medigohealthcare.DistanceCalculator;
import medigohealthcare.Security;
import relations.Patients;
import relations.Pharmacist;

/**
 *
 * @author 20175707
 */
public class MakeDelivery extends javax.swing.JFrame {

    DefaultListModel availableListModel;
    DefaultListModel allocatedListModel;
    Pharmacist pharmacist;
    String date;
    ImageIcon img;
    /**
     * Creates new form MakeDelivery
     */
    public MakeDelivery(Pharmacist pharmacist) {
        img = new ImageIcon(getClass().getResource("/MediGO round symbol.png"));
        availableListModel = new DefaultListModel();
        allocatedListModel = new DefaultListModel();
        this.pharmacist = pharmacist;
        
        // Bluetooth part 1
        availableListModel.addElement("Compartment 1");
        availableListModel.addElement("Compartment 2");
        initComponents();
        
        // Add listeners for buttons
        deallocate.setEnabled(false);
        
        alloc.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                alloc.setEnabled(false);
            }
        });
        jList2.addListSelectionListener(new ListSelectionListener() {
            public void valueChanged(ListSelectionEvent e) {
                alloc.setEnabled(true);
            }
        });
        
        deallocate.setEnabled(false);
        
        deallocate.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                deallocate.setEnabled(false);
            }
        });
        
        jList3.addListSelectionListener(new ListSelectionListener() {
            public void valueChanged(ListSelectionEvent e) {
                deallocate.setEnabled(true);
            }
        });
    }

    private MakeDelivery() {
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

        jPanel3 = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        symbol = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        jList2 = new javax.swing.JList<>();
        jScrollPane3 = new javax.swing.JScrollPane();
        jList3 = new javax.swing.JList<>();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        deallocate = new javax.swing.JButton();
        alloc = new javax.swing.JButton();
        startDelivery = new javax.swing.JButton();
        cancel = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setIconImage(img.getImage());

        jPanel3.setBackground(new java.awt.Color(255, 255, 255));

        jPanel2.setBackground(new java.awt.Color(42, 81, 197));

        symbol.setIcon(new javax.swing.ImageIcon(getClass().getResource("/logoBar.jpg"))); // NOI18N

        jLabel6.setFont(new java.awt.Font("Tahoma", 0, 36)); // NOI18N
        jLabel6.setForeground(new java.awt.Color(255, 255, 255));
        jLabel6.setText("Make a Delivery");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addComponent(symbol)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel6)
                .addGap(33, 33, 33))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(jLabel6, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(symbol, javax.swing.GroupLayout.PREFERRED_SIZE, 76, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(0, 0, Short.MAX_VALUE))
        );

        jList2.setModel(availableListModel);
        jList2.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        jScrollPane2.setViewportView(jList2);

        jList3.setModel(allocatedListModel);
        jList3.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        jScrollPane3.setViewportView(jList3);

        jLabel2.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel2.setText("Available Compartments:");

        jLabel3.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel3.setText("Allocated Compartments:");

        jLabel4.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel4.setText("Delivery Robot AB123");

        deallocate.setBackground(new java.awt.Color(42, 81, 197));
        deallocate.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        deallocate.setForeground(new java.awt.Color(255, 255, 255));
        deallocate.setText("Deallocate ");
        deallocate.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                deallocateActionPerformed(evt);
            }
        });

        alloc.setBackground(new java.awt.Color(42, 81, 197));
        alloc.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        alloc.setForeground(new java.awt.Color(255, 255, 255));
        alloc.setText("Allocate");
        alloc.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                allocActionPerformed(evt);
            }
        });

        startDelivery.setBackground(new java.awt.Color(42, 81, 197));
        startDelivery.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        startDelivery.setForeground(new java.awt.Color(255, 255, 255));
        startDelivery.setText("Start Delivery");
        startDelivery.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                startDeliveryActionPerformed(evt);
            }
        });

        cancel.setBackground(new java.awt.Color(42, 81, 197));
        cancel.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        cancel.setForeground(new java.awt.Color(255, 255, 255));
        cancel.setText("Cancel");
        cancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cancelActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel2)
                            .addComponent(jLabel4)
                            .addComponent(jLabel3))
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 305, Short.MAX_VALUE)
                            .addComponent(jScrollPane3))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(deallocate, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(alloc, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(cancel, javax.swing.GroupLayout.PREFERRED_SIZE, 117, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(startDelivery)))
                .addContainerGap())
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel4)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(alloc, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 134, Short.MAX_VALUE))
                .addGap(18, 18, 18)
                .addComponent(jLabel3)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(deallocate, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 134, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(30, 30, 30)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(startDelivery)
                    .addComponent(cancel))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void deallocateActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_deallocateActionPerformed
        // TODO add your handling code here:
        int size = allocatedListModel.getSize();

        if (size == 0 || jList3.getSelectedValue() == null) { //Nobody's left, disable firing.
            deallocate.setEnabled(false);
        } else {
            String allocated = jList3.getSelectedValue();
            String[] allocatedArray = allocated.split(" - ");
            availableListModel.addElement(allocatedArray[0]);
            allocatedListModel.removeElement(allocated);
        }
    }//GEN-LAST:event_deallocateActionPerformed

    private void allocActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_allocActionPerformed
        // TODO add your handling code here:
        int size = availableListModel.getSize();

        if (size == 0 || jList2.getSelectedValue() == null) { //Nobody's left, disable firing.
            alloc.setEnabled(false);
        } else {
            if (allocatedListModel.getSize()!=0){
                int selectedIndex = jList2.getSelectedIndex();
                try {
                    new PatientList(availableListModel, allocatedListModel, selectedIndex, date, pharmacist).setVisible(true);
                } catch (InterruptedException | ExecutionException ex) {
                    Logger.getLogger(MakeDelivery.class.getName()).log(Level.SEVERE, null, ex);
                } catch (NullPointerException ex) {
                    JOptionPane.showMessageDialog(null, "No patients selected this delivery date and time.", "Error", JOptionPane.PLAIN_MESSAGE); 
                }
            } else {
                String selectedMed = jList2.getSelectedValue();
                int selectedIndex = jList2.getSelectedIndex();
                new SelectDate(availableListModel, allocatedListModel, selectedIndex, date, pharmacist, this).setVisible(true);
            }
        }
    }//GEN-LAST:event_allocActionPerformed

    private void startDeliveryActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_startDeliveryActionPerformed
        // TODO add your handling code here:
        // Id delivery
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date time = new Date();
        String id = dateFormat.format(time) + " " + pharmacist.email;
        // Id Robot
        String robotID = "AB123";
        // Save delivery details
        DatabaseCommunication.addDelivery(id, pharmacist.email, pharmacist.pharmacy, date, robotID);
        HashMap<String, String> patientEmailAddress = new HashMap<>();
        ArrayList<String> patientsEmail = new ArrayList<>();
        HashMap<String, ArrayList<String>> medicines = new HashMap<>();
        
        if (allocatedListModel.getSize()==0){
            JOptionPane.showMessageDialog(null, "No compartment has been allocated.", "Error", JOptionPane.PLAIN_MESSAGE);                 
        } else {
            Object[] allocatedPatients = allocatedListModel.toArray();
            for (Object allocated : allocatedPatients) {
                       String[] allocatedArray = allocated.toString().split(" - ");

               // Encrypt password
               try {
                   Security.saveEncryptedCode(allocatedArray[1], allocatedArray[2]);
               } catch (InterruptedException | ExecutionException | NoSuchAlgorithmException | InvalidKeySpecException | NoSuchPaddingException | InvalidKeyException | IllegalBlockSizeException | BadPaddingException ex) {
                   Logger.getLogger(MakeDelivery.class.getName()).log(Level.SEVERE, null, ex);
               }
               
               // Add Delivery
               Patients patient = null;
                try {
                    patient = DatabaseCommunication.readPatientData(allocatedArray[1]);
                } catch (InterruptedException | ExecutionException ex) {
                    Logger.getLogger(MakeDelivery.class.getName()).log(Level.SEVERE, null, ex);
                }
               
               patientsEmail.add(patient.email);
               medicines.put(patient.email, patient.medicineCurrent);
               patientEmailAddress.put(patient.email, patient.address); 
               
                try {
                    // Add deliveries to patient
                    DatabaseCommunication.addDeliveryPatient(patient.email, id);
                } catch (InterruptedException | ExecutionException ex) {
                    Logger.getLogger(MakeDelivery.class.getName()).log(Level.SEVERE, null, ex);
                }
               
               // Update user medicine hystory 
               ArrayList<String> medCurrent = new ArrayList<>();
               
               String[] oDate = date.split(" ");
               for (String med : patient.medicineCurrent) {
                   if (!med.contains(" -- Started on: ")) {
                        medCurrent.add(med + " -- Started on: " + oDate[0]);
                   } else {
                       medCurrent.add(med);
                   }
               }
               DatabaseCommunication.updatePatientMedicines(patient.email, medCurrent, patient.medicinePast);
            }
            // Update deliveries pharmacist
            DatabaseCommunication.addDeliveryPharmacist(pharmacist.email, id);
            
            //Put patients info current delivery
            DatabaseCommunication.addDeliveryPatientsInfo(id, patientsEmail, medicines);
            
            //Claculate distances
            String pharmAddress = null;
            try {
                pharmAddress = DatabaseCommunication.getPharmacyAddress(pharmacist.pharmacy);
            } catch (InterruptedException | ExecutionException ex) {
                Logger.getLogger(MakeDelivery.class.getName()).log(Level.SEVERE, null, ex);
            }
            HashMap<String, Integer> deliveryDistances = new HashMap<>();
            DistanceCalculator dc = new DistanceCalculator();
            try {
                dc.deliveryTimes(pharmAddress, patientEmailAddress);
            } catch (IOException | InterruptedException ex) {
                Logger.getLogger(MakeDelivery.class.getName()).log(Level.SEVERE, null, ex);
            }
            
            deliveryDistances = dc.deliveryDistances;
            
            int timeD = 0;
            Iterator itD = deliveryDistances.entrySet().iterator();
            while (itD.hasNext()) {
                Map.Entry pair = (Map.Entry)itD.next();
                timeD = timeD + ((int) pair.getValue() / 166);
                // Notify User 
                DatabaseCommunication.notifyUser((String) pair.getKey(), timeD);
                timeD = timeD + 5;
        }
            this.dispose();
        }
    }//GEN-LAST:event_startDeliveryActionPerformed

    private void cancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cancelActionPerformed
        // TODO add your handling code here:
        this.dispose();
    }//GEN-LAST:event_cancelActionPerformed

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
            java.util.logging.Logger.getLogger(MakeDelivery.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(MakeDelivery.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(MakeDelivery.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(MakeDelivery.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new MakeDelivery().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton alloc;
    private javax.swing.JButton cancel;
    private javax.swing.JButton deallocate;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JList<String> jList2;
    private javax.swing.JList<String> jList3;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JButton startDelivery;
    private javax.swing.JLabel symbol;
    // End of variables declaration//GEN-END:variables
}
