/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package me.rudraksha007.billgenerator.GUI;

import me.rudraksha007.billgenerator.Main;
import me.rudraksha007.billgenerator.utilities.DataManager;
import me.rudraksha007.billgenerator.utilities.Utils;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.AWTEventListener;
import java.awt.event.ItemEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 *
 * @author rudra
 */
public class AddParty extends javax.swing.JFrame {

    private int editingIndex;
    DataManager manager = new DataManager();
    AWTEventListener listener = new AWTEventListener() {
        @Override
        public void eventDispatched(AWTEvent event) {
            if (event.getID()!= MouseEvent.MOUSE_CLICKED)return;
            if (isEditing)return;
            Component c = (Component) event.getSource();
            while (c!=null&&c!=table&&c!=btnDelete&&c!=btnEdit)c = c.getParent();
            if (c==table||c==btnDelete||c==btnEdit)return;
            table.clearSelection();
        }
    };
    /**
     * Creates new form AddParty
     */
    public AddParty() {
        initComponents();
        configureElements();
        this.setLocationRelativeTo(null);
    }

    @Override
    public void setVisible(boolean visible){
        if (visible){
            this.clear();
            Toolkit.getDefaultToolkit().addAWTEventListener(listener, AWTEvent.MOUSE_EVENT_MASK);
            ResultSet set = manager.get("SELECT * FROM Parties");
            try {
                DefaultTableModel model = (DefaultTableModel) table.getModel();
                model.setRowCount(0);
                int i =1;
                while (set.next()){
                    model.addRow(new String[]{String.valueOf(i), set.getString(1),
                            set.getString(2), set.getString(3), set.getString(4),
                            set.getString(5), String.valueOf(set.getFloat(6))});
                    i++;
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
        else Toolkit.getDefaultToolkit().removeAWTEventListener(listener);
        super.setVisible(visible);
    }

    public void configureElements(){
        table.getColumnModel().getColumn(0).setPreferredWidth((int) Math.round(table.getWidth()*0.0714));
        table.getColumnModel().getColumn(1).setPreferredWidth((int) Math.round(table.getWidth()*0.15));
        table.getColumnModel().getColumn(3).setPreferredWidth((int) Math.round(table.getWidth()*0.15));
        table.getColumnModel().getColumn(4).setPreferredWidth((int) Math.round(table.getWidth()*0.15));
        table.getColumnModel().getColumn(5).setPreferredWidth((int) Math.round(table.getWidth()*0.15));
        table.getColumnModel().getColumn(6).setPreferredWidth((int) Math.round(table.getWidth()*0.1));

        table.getColumnModel().getColumn(0).setMaxWidth((int) Math.round(table.getWidth()*0.0714));
        table.getColumnModel().getColumn(1).setMaxWidth((int) Math.round(table.getWidth()*0.15));
        table.getColumnModel().getColumn(3).setMaxWidth((int) Math.round(table.getWidth()*0.15));
        table.getColumnModel().getColumn(4).setMaxWidth((int) Math.round(table.getWidth()*0.1));
        table.getColumnModel().getColumn(5).setMaxWidth((int) Math.round(table.getWidth()*0.1));
        table.getColumnModel().getColumn(6).setMaxWidth((int) Math.round(table.getWidth()*0.1));

        table.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                if (e.getValueIsAdjusting())return;
                if (isEditing)return;
                btnDelete.setEnabled(table.getSelectedRow()>=0);
                btnEdit.setEnabled(table.getSelectedRow()>=0);
            }
        });
        ((DefaultComboBoxModel<String>)cmbSupplier.getModel()).addAll(Main.Employees);
        new Utils().setNumberOnly(txtBalance);
        ResultSet set = manager.get("SELECT * FROM Parties");
        try {
            DefaultTableModel model = (DefaultTableModel) table.getModel();
            int i =1;
            while (set.next()){
                model.addRow(new String[]{String.valueOf(i), set.getString(1),
                set.getString(2), set.getString(3), set.getString(4),
                set.getString(5), String.valueOf(set.getFloat(6))});
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
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
        pnlInputs = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        txtParty = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        txtAddress = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        cmbState = new javax.swing.JComboBox<>();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        txtGst = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        cmbSupplier = new javax.swing.JComboBox<>();
        txtBalance = new javax.swing.JTextField();
        pnlData = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        table = new javax.swing.JTable();
        pnlButtons = new javax.swing.JPanel();
        btnDelete = new javax.swing.JButton();
        btnAdd = new javax.swing.JButton();
        btnEdit = new javax.swing.JButton();
        jLabel7 = new javax.swing.JLabel();
        btnBack = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
        setMinimumSize(new java.awt.Dimension(640, 480));
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                formWindowClosing(evt);
            }
        });

        jLabel1.setText("Party");

        txtParty.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtPartyKeyPressed(evt);
            }
        });

        jLabel2.setText("Address");

        txtAddress.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtAddressKeyPressed(evt);
            }
        });

        jLabel3.setText("State");

        cmbState.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "1. JAMMU AND KASHMIR", "2. HIMACHAL PRADESH", "3. PUNJAB", "4. CHANDIGARH", "5. UTTARAKHAND", "6. HARYANA", "7. DELHI", "8. RAJASTHAN", "9. UTTAR PRADESH", "10. BIHAR", "11. SIKKIM", "12. ARUNACHAL PRADESH", "13. NAGALAND", "14. MANIPUR", "15. MIZORAM", "16. TRIPURA", "17. MEGHALAYA", "18. ASSAM", "19. WEST BENGAL", "20. JHARKHAND", "21. ORISSA", "22. CHHATTISGARH", "23. MADHYA PRADESH", "24. GUJARAT", "25. DAMAN AND DIU", "26. DADAR AND NAGAR HAVELI", "27. MAHARASTRA", "29. KARNATAKA", "30. GOA", "31. LAKSHADWEEP", "32. KERALA", "33. TAMIL NADU", "34. PUDUCHERRY", "35. ANDAMAN AND NICOBAR", "36. TELANGANA", "37. ANDHRA PRADESH", "97. OTHER TERRITORY", "96. OTHER COUNTRY" }));
        cmbState.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                cmbStateItemStateChanged(evt);
            }
        });
        cmbState.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                cmbStateFocusGained(evt);
            }
        });

        jLabel4.setText("Supplier");

        jLabel5.setText("GSTIN");

        txtGst.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtGstKeyPressed(evt);
            }
        });

        jLabel6.setText("Opening Bal.");

        cmbSupplier.setEditable(true);
        cmbSupplier.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                cmbSupplierItemStateChanged(evt);
            }
        });
        cmbSupplier.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                cmbSupplierKeyPressed(evt);
            }
        });

        txtBalance.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtBalanceKeyPressed(evt);
            }
        });

        javax.swing.GroupLayout pnlInputsLayout = new javax.swing.GroupLayout(pnlInputs);
        pnlInputs.setLayout(pnlInputsLayout);
        pnlInputsLayout.setHorizontalGroup(
            pnlInputsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlInputsLayout.createSequentialGroup()
                .addGroup(pnlInputsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pnlInputsLayout.createSequentialGroup()
                        .addComponent(jLabel2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(txtAddress, javax.swing.GroupLayout.DEFAULT_SIZE, 265, Short.MAX_VALUE))
                    .addGroup(pnlInputsLayout.createSequentialGroup()
                        .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 85, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(txtParty)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pnlInputsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel3)
                    .addComponent(jLabel4))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(pnlInputsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(cmbSupplier, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(cmbState, 0, 405, Short.MAX_VALUE))
                .addGap(22, 22, 22)
                .addGroup(pnlInputsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 48, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel5))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pnlInputsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(txtBalance, javax.swing.GroupLayout.DEFAULT_SIZE, 267, Short.MAX_VALUE)
                    .addComponent(txtGst))
                .addContainerGap())
        );

        pnlInputsLayout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {jLabel1, jLabel2, jLabel3, jLabel4, jLabel5, jLabel6});

        pnlInputsLayout.setVerticalGroup(
            pnlInputsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlInputsLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pnlInputsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(txtParty, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel3)
                    .addComponent(cmbState, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel5)
                    .addComponent(txtGst, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(pnlInputsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel2, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(pnlInputsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(txtAddress, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel4)
                        .addComponent(jLabel6)
                        .addComponent(cmbSupplier, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(txtBalance, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(16, Short.MAX_VALUE))
        );

        pnlInputsLayout.linkSize(javax.swing.SwingConstants.VERTICAL, new java.awt.Component[] {cmbState, cmbSupplier, jLabel1, jLabel3, jLabel4, jLabel5, jLabel6, txtAddress, txtBalance, txtGst, txtParty});

        table.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "S. No.", "Party", "Address", "State", "Supplier", "GSTIN", "Opening Bal"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        table.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        table.getTableHeader().setReorderingAllowed(false);
        jScrollPane1.setViewportView(table);
        table.getColumnModel().getSelectionModel().setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        if (table.getColumnModel().getColumnCount() > 0) {
            table.getColumnModel().getColumn(0).setResizable(false);
            table.getColumnModel().getColumn(1).setResizable(false);
            table.getColumnModel().getColumn(2).setResizable(false);
            table.getColumnModel().getColumn(3).setResizable(false);
            table.getColumnModel().getColumn(4).setResizable(false);
            table.getColumnModel().getColumn(5).setResizable(false);
            table.getColumnModel().getColumn(6).setResizable(false);
        }

        javax.swing.GroupLayout pnlDataLayout = new javax.swing.GroupLayout(pnlData);
        pnlData.setLayout(pnlDataLayout);
        pnlDataLayout.setHorizontalGroup(
            pnlDataLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlDataLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1)
                .addContainerGap())
        );
        pnlDataLayout.setVerticalGroup(
            pnlDataLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlDataLayout.createSequentialGroup()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 433, Short.MAX_VALUE)
                .addContainerGap())
        );

        btnDelete.setText("Delete");
        btnDelete.setEnabled(false);
        btnDelete.setFocusPainted(false);
        btnDelete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDeleteActionPerformed(evt);
            }
        });

        btnAdd.setText("Add New");
        btnAdd.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAddActionPerformed(evt);
            }
        });

        btnEdit.setText("Edit");
        btnEdit.setEnabled(false);
        btnEdit.setFocusPainted(false);
        btnEdit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEditActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout pnlButtonsLayout = new javax.swing.GroupLayout(pnlButtons);
        pnlButtons.setLayout(pnlButtonsLayout);
        pnlButtonsLayout.setHorizontalGroup(
            pnlButtonsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlButtonsLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(btnDelete)
                .addGap(115, 115, 115)
                .addComponent(btnAdd)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(btnEdit)
                .addContainerGap(886, Short.MAX_VALUE))
        );

        pnlButtonsLayout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {btnAdd, btnDelete, btnEdit});

        pnlButtonsLayout.setVerticalGroup(
            pnlButtonsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlButtonsLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pnlButtonsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnDelete)
                    .addComponent(btnAdd)
                    .addComponent(btnEdit))
                .addContainerGap(56, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(pnlButtons, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(pnlData, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(pnlInputs, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(pnlInputs, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(pnlData, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(pnlButtons, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        jPanel1Layout.linkSize(javax.swing.SwingConstants.VERTICAL, new java.awt.Component[] {pnlButtons, pnlInputs});

        jLabel7.setFont(new java.awt.Font("Impact", 0, 36)); // NOI18N
        jLabel7.setText("Customize Parties");

        btnBack.setText("Back");
        btnBack.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBackActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(btnBack)
                        .addGap(18, 18, 18)
                        .addComponent(jLabel7)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jLabel7, javax.swing.GroupLayout.DEFAULT_SIZE, 65, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(22, 22, 22)
                        .addComponent(btnBack)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void txtPartyKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtPartyKeyPressed
        if (evt.getKeyCode()!= KeyEvent.VK_ENTER)return;
        txtAddress.grabFocus();
    }//GEN-LAST:event_txtPartyKeyPressed

    private void txtAddressKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtAddressKeyPressed
        if (evt.getKeyCode()!= KeyEvent.VK_ENTER)return;
        cmbState.grabFocus();
    }//GEN-LAST:event_txtAddressKeyPressed

    private void cmbStateItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cmbStateItemStateChanged
        if (evt.getStateChange()!= ItemEvent.SELECTED)return;
        if (isEditing)return;
        cmbSupplier.grabFocus();
    }//GEN-LAST:event_cmbStateItemStateChanged

    private void cmbSupplierKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_cmbSupplierKeyPressed
        if (evt.getKeyCode()!= KeyEvent.VK_ENTER)return;
        if (isEditing)return;
        txtGst.grabFocus();
    }//GEN-LAST:event_cmbSupplierKeyPressed

    private void cmbSupplierItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cmbSupplierItemStateChanged
        if (evt.getStateChange()!= ItemEvent.SELECTED)return;
        txtGst.grabFocus();
    }//GEN-LAST:event_cmbSupplierItemStateChanged

    private void txtGstKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtGstKeyPressed
        if (evt.getKeyCode()!= KeyEvent.VK_ENTER)return;
        txtBalance.grabFocus();
    }//GEN-LAST:event_txtGstKeyPressed

    private void txtBalanceKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtBalanceKeyPressed
        if (evt.getKeyCode()!= KeyEvent.VK_ENTER)return;
        if (isEditing)btnEditActionPerformed(null);
        else btnAddActionPerformed(null);
        txtParty.grabFocus();
    }//GEN-LAST:event_txtBalanceKeyPressed

    private void btnDeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDeleteActionPerformed
        isEditing = true;
        int resp = JOptionPane.showConfirmDialog(this, "Delete?", "Confirm", JOptionPane.YES_NO_OPTION);
        isEditing = false;
        if(resp!=JOptionPane.YES_OPTION)return;
        manager.run("DELETE FROM Parties WHERE Name = '"+table.getValueAt(table.getSelectedRow(), 1)+"'");
        ((DefaultTableModel)table.getModel()).removeRow(table.getSelectedRow());
        table.clearSelection();
        for (int i = 0; i < table.getRowCount(); i++) {
            table.setValueAt(i+1, i, 0);
        }
    }//GEN-LAST:event_btnDeleteActionPerformed

    private void btnBackActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBackActionPerformed
        Main.frames.get("home").setVisible(true);
        if (isEditing){
            isEditing = false;
            toggleAccessibility();
        }
        this.setVisible(false);
        this.clear();

    }//GEN-LAST:event_btnBackActionPerformed

    private void btnAddActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAddActionPerformed
        if (txtParty.getText().isEmpty()||txtAddress.getText().isEmpty()||txtBalance.getText().isEmpty()||txtGst.getText().isEmpty()
        ||cmbSupplier.getSelectedItem()==null){
            JOptionPane.showMessageDialog(this, "All Fields are necessary", "Error", JOptionPane.ERROR_MESSAGE);
            txtParty.grabFocus();
            return;
        }
        if (!Main.Employees.contains(cmbSupplier.getSelectedItem().toString()))Main.Employees.add(String.valueOf(cmbSupplier.getSelectedItem()));
        for (int i = 0; i < table.getRowCount(); i++) {
            if (!String.valueOf(table.getValueAt(i, 1)).equalsIgnoreCase(txtParty.getText()))continue;
            table.setValueAt(txtAddress.getText(), i, 2);
            table.setValueAt(String.valueOf(cmbState.getSelectedItem()), i, 3);
            table.setValueAt(String.valueOf(cmbSupplier.getSelectedItem()), i, 4);
            table.setValueAt(txtGst.getText(), i, 5);
            table.setValueAt(txtBalance.getText(), i, 6);
            manager.run("UPDATE Parties SET Address = '"+txtAddress.getText()+"', State = '"+cmbState.getSelectedItem()+"'," +
                    "Supplier = '"+ cmbSupplier.getSelectedItem()+"', GSTIN = '"+txtGst.getText()+"', OpeningBal = "+txtBalance.getText()+
                    " WHERE Name = '"+txtParty.getText()+"'");
            clear();
            return;
        }
        try {
            PreparedStatement statement = DataManager.con.prepareStatement("INSERT INTO Parties" +
                    "(Name,Address,State,Supplier,GSTIN,OpeningBal) VALUES (?,?,?,?,?,?)");
            statement.setString(1, txtParty.getText());
            statement.setString(2, txtAddress.getText());
            statement.setString(3, String.valueOf(cmbState.getSelectedItem()));
            statement.setString(4, String.valueOf(cmbSupplier.getSelectedItem()));
            statement.setString(5, txtGst.getText());
            statement.setFloat(6, Float.parseFloat(txtBalance.getText()));
            manager.run(statement);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        ((DefaultTableModel)table.getModel()).addRow(new String[]{String.valueOf(table.getRowCount()+1),txtParty.getText(),
                txtAddress.getText(),String.valueOf(cmbState.getSelectedItem()),String.valueOf(cmbSupplier.getSelectedItem()),
                txtGst.getText(),txtBalance.getText()});
        clear();
    }//GEN-LAST:event_btnAddActionPerformed

    private boolean isEditing = false;
    private void btnEditActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEditActionPerformed
        if (isEditing){
            if (txtParty.getText().isEmpty()||txtAddress.getText().isEmpty()||txtBalance.getText().isEmpty()||txtGst.getText().isEmpty()
                    ||cmbSupplier.getSelectedItem()==null){
                JOptionPane.showMessageDialog(this, "All Fields are necessary", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            btnEdit.setText("Edit");
            manager.run("UPDATE Parties SET Address = '"+txtAddress.getText()+"', State = '"+cmbState.getSelectedItem()+"', Supplier = '" +
                    cmbSupplier.getSelectedItem()+"', GSTIN = '"+txtGst.getText()+"', OpeningBal = "+Float.parseFloat(txtBalance.getText())+" WHERE" +
                    " Name = '"+txtParty.getText()+"'");
            table.setValueAt(txtParty.getText(),table.getSelectedRow(), 1);
            table.setValueAt(txtAddress.getText(), table.getSelectedRow(), 2);
            table.setValueAt(String.valueOf(cmbState.getSelectedItem()), table.getSelectedRow(), 3);
            table.setValueAt(String.valueOf(cmbSupplier.getSelectedItem()), table.getSelectedRow(), 4);
            table.setValueAt(txtGst.getText(), table.getSelectedRow(), 5);
            table.setValueAt(txtBalance.getText(), table.getSelectedRow(), 6);
            clear();
            isEditing = false;
            toggleAccessibility();
            txtParty.grabFocus();
            return;
        }
        btnEdit.setText("Save Edit");
        isEditing = true;
        txtParty.setText(String.valueOf(table.getValueAt(table.getSelectedRow(), 1)));
        txtAddress.setText(String.valueOf(table.getValueAt(table.getSelectedRow(), 2)));
        cmbState.setSelectedItem(String.valueOf(table.getValueAt(table.getSelectedRow(), 3)));
        cmbSupplier.setSelectedItem(String.valueOf(table.getValueAt(table.getSelectedRow(), 4)));
        txtGst.setText(String.valueOf(table.getValueAt(table.getSelectedRow(), 5)));
        txtBalance.setText(String.valueOf(table.getValueAt(table.getSelectedRow(), 6)));
        toggleAccessibility();
    }//GEN-LAST:event_btnEditActionPerformed

    private void formWindowClosing(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosing
        Main.frames.get("home").setVisible(true);
        clear();
        this.setVisible(false);
    }//GEN-LAST:event_formWindowClosing

    private void cmbStateFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_cmbStateFocusGained
        cmbState.setPopupVisible(true);
    }//GEN-LAST:event_cmbStateFocusGained

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
                System.out.println(info.getName());
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(AddParty.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(AddParty.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(AddParty.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(AddParty.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new AddParty().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnAdd;
    private javax.swing.JButton btnBack;
    private javax.swing.JButton btnDelete;
    private javax.swing.JButton btnEdit;
    private javax.swing.JComboBox<String> cmbState;
    private javax.swing.JComboBox<String> cmbSupplier;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JPanel pnlButtons;
    private javax.swing.JPanel pnlData;
    private javax.swing.JPanel pnlInputs;
    private javax.swing.JTable table;
    private javax.swing.JTextField txtAddress;
    private javax.swing.JTextField txtBalance;
    private javax.swing.JTextField txtGst;
    private javax.swing.JTextField txtParty;
    // End of variables declaration//GEN-END:variables

    public void clear(){
        txtParty.setText("");
        txtAddress.setText("");
        txtGst.setText("");
        txtBalance.setText("");
        if (!isEditing)table.clearSelection();
        cmbState.setSelectedIndex(0);
        cmbSupplier.setSelectedItem(null);
    }

    public void toggleAccessibility(){
        btnAdd.setEnabled(!isEditing);
        btnDelete.setEnabled(!isEditing);
        txtParty.setEditable(!isEditing);
    }
}
