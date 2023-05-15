/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package me.rudraksha007.billgenerator.GUI;

import me.rudraksha007.billgenerator.Item;
import me.rudraksha007.billgenerator.Main;
import me.rudraksha007.billgenerator.utilities.DataManager;

import javax.swing.*;
import javax.swing.event.CellEditorListener;
import javax.swing.event.ChangeEvent;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.sql.Date;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;
import java.util.*;

/**
 *
 * @author rudra
 */
public class BillData extends javax.swing.JFrame {
    JList<String> menu = new JList<>();
    JScrollPane menuPane = new JScrollPane();
    JTextField txtAutofill = new JTextField();
    JTextField txtQuantity = new JTextField();
    JTextField field;
    List<Integer>codes = Arrays.asList(KeyEvent.VK_UP,KeyEvent.VK_KP_UP, KeyEvent.VK_LEFT, KeyEvent.VK_KP_LEFT,
            KeyEvent.VK_RIGHT, KeyEvent.VK_KP_RIGHT);
    DataManager manager = new DataManager();
    /**
     * Creates new form BillData
     */
    public BillData() {
        initComponents();
        this.setLocationRelativeTo(null);
        txtQuantity = (JTextField) table.getColumnModel().getColumn(3)
                .getCellEditor().getTableCellEditorComponent(table,"", true, table.getSelectedRow(), 3);
        field = (JTextField) table.getColumnModel().getColumn(1)
                .getCellEditor().getTableCellEditorComponent(table,"", true, table.getSelectedRow(), 1);
        configureElements();
        txtName.grabFocus();
        DefaultComboBoxModel<String>model = new DefaultComboBoxModel<>();
        model.addAll(Main.Employees);
        cmbEmployee.setModel(model);
    }

    public void configureElements(){
        table.getColumnModel().getColumn(0).setMaxWidth((int) Math.round(table.getWidth()*0.05));
        table.getColumnModel().getColumn(3).setMaxWidth((int) Math.round(table.getWidth()*0.05));
        table.getColumnModel().getColumn(2).setMaxWidth((int) Math.round(table.getWidth()*0.08));
        table.getColumnModel().getColumn(4).setMaxWidth((int) Math.round(table.getWidth()*0.1));
        table.getColumnModel().getColumn(5).setMaxWidth((int) Math.round(table.getWidth()*0.1));

        table.getColumnModel().getColumn(0).setPreferredWidth((int) Math.round(table.getWidth()*0.05));
        table.getColumnModel().getColumn(2).setPreferredWidth((int) Math.round(table.getWidth()*0.08));
        table.getColumnModel().getColumn(3).setPreferredWidth((int) Math.round(table.getWidth()*0.1));
        table.getColumnModel().getColumn(4).setPreferredWidth((int) Math.round(table.getWidth()*0.1));
        table.getColumnModel().getColumn(5).setPreferredWidth((int) Math.round(table.getWidth()*0.1));

        table.getSelectionModel().addListSelectionListener(e -> {
            if (e.getValueIsAdjusting())return;
            btnRemove.setEnabled(table.getSelectedRow()>=0);
        });
        field.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {
                String typed = ((JTextField)e.getSource()).getText();
                getAutoComplete(typed,e);
            }

            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode()==KeyEvent.VK_BACK_SPACE) {
                    getAutoComplete(((JTextField)e.getSource()).getText(),e);
                    return;
                }
                if (e.getKeyCode()==KeyEvent.VK_DOWN||e.getKeyCode()==KeyEvent.VK_KP_DOWN){
                    e.consume();
                    table.getCellEditor(table.getSelectedRow(), 1).stopCellEditing();
                    menu.grabFocus();

                }
                else if (e.getKeyCode()==KeyEvent.VK_ENTER){
                    if (field.getText().split("/").length>=3)
                        table.getCellEditor(table.getSelectedRow(), 1).stopCellEditing();
                }
                else if (codes.contains(e.getKeyCode()))menuPane.setVisible(false);

            }

            @Override
            public void keyReleased(KeyEvent e) {

            }
        });
        field.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {
                if (table.getSelectedRow()<0)return;
                table.setValueAt("", table.getSelectedRow(), 2);
                table.setValueAt("", table.getSelectedRow(), 4);
                table.setValueAt("", table.getSelectedRow(), 5);
            }

            @Override
            public void focusLost(FocusEvent e) {
                if (e.getOppositeComponent()==null){menuPane.setVisible(false);return;}
                if (e.getOppositeComponent().equals(menu))return;
                if (e.getCause().toString().equals("TRAVERSAL_FORWARD"))return;
                menuPane.setVisible(false);
            }
        });
        table.getColumnModel().getColumn(3).getCellEditor().getTableCellEditorComponent(
                table,"", true, table.getSelectedRow(),3).addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {
                if (!Character.isDigit(e.getKeyChar())){
                    e.setKeyChar('\0');
                }
            }

            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode()==KeyEvent.VK_BACK_SPACE){
                    try {
                        int num = Integer.parseInt(txtQuantity.getText().substring(0,txtQuantity.getText().length()-1));
                        float rate = Float.parseFloat(table.getValueAt(table.getSelectedRow(), 4).toString().split("/")[0]);
                        table.setValueAt(num*rate, table.getSelectedRow(), 5);
                    }catch (NullPointerException|NumberFormatException ignored){}
                    return;
                }
                if (e.getKeyCode()==KeyEvent.VK_ENTER){
                    String desc = String.valueOf(table.getValueAt(table.getSelectedRow(), 1));
                    String[] data = desc.split("/");
                    int num = Integer.parseInt(txtQuantity.getText());
                    float rate = 0;
                    Item i = Main.itemMap.get(data[0]+"/"+data[1]);
                    for (Map.Entry<String, Float>entry: i.getSizes().entrySet()){
                        if (!entry.getKey().equalsIgnoreCase(desc.replace(data[0]+"/"+data[1]+"/","")))continue;
                        rate = entry.getValue();
                        break;
                    }
                    table.setValueAt(num*rate, table.getSelectedRow(), 5);
                    btnAddActionPerformed(null);
                    return;
                }
                if (!Character.isDigit(e.getKeyChar())){
                    e.consume();
                    Toolkit.getDefaultToolkit().beep();
                }
                try {
                    int num = Integer.parseInt(txtQuantity.getText()+e.getKeyChar());
                    float rate = Float.parseFloat(table.getValueAt(table.getSelectedRow(), 4).toString().split("/")[0]);
                    table.setValueAt(num*rate, table.getSelectedRow(), 5);
                }catch (NullPointerException|NumberFormatException ignored){}
            }

            @Override
            public void keyReleased(KeyEvent e) {

            }
        });

        table.getColumnModel().getColumn(1).getCellEditor().addCellEditorListener(new CellEditorListener() {
            @Override
            public void editingStopped(ChangeEvent e) {
                if (field.getText().split("/").length>=3)
                    autoFill();
            }

            @Override
            public void editingCanceled(ChangeEvent e) {

            }
        });
        table.getColumnModel().getColumn(3).getCellEditor().addCellEditorListener(new CellEditorListener() {
            @Override
            public void editingStopped(ChangeEvent e) {
                String desc = String.valueOf(table.getValueAt(table.getSelectedRow(), 1));
                String[] data = desc.split("/");
                int num = Integer.parseInt(txtQuantity.getText());
                float rate = 0;
                try{
                    Item i = Main.itemMap.get(data[0]+"/"+data[1]);
                    for (Map.Entry<String, Float>entry: i.getSizes().entrySet()){
                        if (!entry.getKey().equalsIgnoreCase(desc.replace(data[0]+"/"+data[1]+"/","")))continue;
                        rate = entry.getValue();
                        break;
                    }
                }catch (ArrayIndexOutOfBoundsException ignored){}
                table.setValueAt(num*rate, table.getSelectedRow(), 5);
                float t = calculateTotal();
                if (t==-1)return;
                lblTotal.setText("Total + Tax: ₹"+t);
            }

            @Override
            public void editingCanceled(ChangeEvent e) {
                editingStopped(e);
            }
        });

        menu.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {

            }

            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode()!=KeyEvent.VK_ENTER)return;
                autoFill();
            }

            @Override
            public void keyReleased(KeyEvent e) {

            }
        });
        menu.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                table.getColumnModel().getColumn(1).getCellEditor().stopCellEditing();
                if (e.getClickCount()<2)return;
                autoFill();
            }

            @Override
            public void mousePressed(MouseEvent e) {

            }

            @Override
            public void mouseReleased(MouseEvent e) {

            }

            @Override
            public void mouseEntered(MouseEvent e) {

            }

            @Override
            public void mouseExited(MouseEvent e) {

            }
        });
        menu.setModel(new DefaultListModel<>());
        menuPane.setViewportView(menu);
        menuPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        menuPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);

        btnAdd.setFocusPainted(false);
        btnCancel.setFocusPainted(false);
        btnRemove.setFocusPainted(false);
        btnSave.setFocusPainted(false);

        invoiceData.add(menuPane, JLayeredPane.POPUP_LAYER);
        field.setBackground(new Color(184,207,229,255));
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        invoiceDetails = new javax.swing.JPanel();
        billToParty = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        txtName = new javax.swing.JTextField();
        txtAddress = new javax.swing.JTextField();
        cmbState = new javax.swing.JComboBox<>();
        txtGstin = new javax.swing.JTextField();
        GST = new javax.swing.JPanel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        txtInvoice = new javax.swing.JTextField();
        date = new com.toedter.calendar.JDateChooser();
        jLabel9 = new javax.swing.JLabel();
        cmbTransport = new javax.swing.JComboBox<>();
        cmbEmployee = new javax.swing.JComboBox<>();
        invoiceData = new javax.swing.JLayeredPane();
        jScrollPane1 = new javax.swing.JScrollPane();
        table = new javax.swing.JTable();
        btnSave = new javax.swing.JButton();
        btnCancel = new javax.swing.JButton();
        btnRemove = new javax.swing.JButton();
        btnAdd = new javax.swing.JButton();
        lblTotal = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        txtRemarks = new javax.swing.JTextField();

        setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
        setTitle("Creating Invoice");
        setMinimumSize(new java.awt.Dimension(640, 480));
        setPreferredSize(new java.awt.Dimension(1280, 720));
        addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                formMouseClicked(evt);
            }
        });
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                formWindowClosing(evt);
            }
        });

        invoiceDetails.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                invoiceDetailsMouseClicked(evt);
            }
        });

        jLabel1.setText("Name");

        jLabel2.setText("Adress");

        jLabel4.setText("State");

        jLabel3.setText("GSTIN");

        txtName.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtNameKeyPressed(evt);
            }
        });

        txtAddress.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtAddressKeyPressed(evt);
            }
        });

        cmbState.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "1. JAMMU AND KASHMIR", "2. HIMACHAL PRADESH", "3. PUNJAB", "4. CHANDIGARH", "5. UTTARAKHAND", "6. HARYANA", "7. DELHI", "8. RAJASTHAN", "9. UTTAR PRADESH", "10. BIHAR", "11. SIKKIM", "12. ARUNACHAL PRADESH", "13. NAGALAND", "14. MANIPUR", "15. MIZORAM", "16. TRIPURA", "17. MEGHALAYA", "18. ASSAM", "19. WEST BENGAL", "20. JHARKHAND", "21. ORISSA", "22. CHHATTISGARH", "23. MADHYA PRADESH", "24. GUJARAT", "25. DAMAN AND DIU", "26. DADAR AND NAGAR HAVELI", "27. MAHARASTRA", "29. KARNATAKA", "30. GOA", "31. LAKSHADWEEP", "32. KERALA", "33. TAMIL NADU", "34. PUDUCHERRY", "35. ANDAMAN AND NICOBAR", "36. TELANGANA", "37. ANDHRA PRADESH", "97. OTHER TERRITORY", "96. OTHER COUNTRY" }));
        cmbState.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                cmbStateItemStateChanged(evt);
            }
        });

        txtGstin.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtGstinKeyPressed(evt);
            }
        });

        javax.swing.GroupLayout billToPartyLayout = new javax.swing.GroupLayout(billToParty);
        billToParty.setLayout(billToPartyLayout);
        billToPartyLayout.setHorizontalGroup(
            billToPartyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(billToPartyLayout.createSequentialGroup()
                .addGroup(billToPartyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(billToPartyLayout.createSequentialGroup()
                        .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(txtName))
                    .addGroup(billToPartyLayout.createSequentialGroup()
                        .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(txtAddress))
                    .addGroup(billToPartyLayout.createSequentialGroup()
                        .addGroup(billToPartyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(billToPartyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txtGstin)
                            .addComponent(cmbState, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                .addContainerGap())
        );
        billToPartyLayout.setVerticalGroup(
            billToPartyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(billToPartyLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(billToPartyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(txtName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(billToPartyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(txtAddress)
                    .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(billToPartyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cmbState, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(billToPartyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(txtGstin)
                    .addComponent(jLabel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        GST.setPreferredSize(new java.awt.Dimension(300, 142));

        jLabel5.setText("Invoice Number");

        jLabel6.setText("Invoice Date");

        jLabel7.setText("Transport Mode");

        txtInvoice.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtInvoiceKeyPressed(evt);
            }
        });

        date.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                dateKeyPressed(evt);
            }
        });

        jLabel9.setText("Assigned to");

        cmbTransport.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "By Road" }));

        cmbEmployee.setEditable(true);
        cmbEmployee.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                cmbEmployeeItemStateChanged(evt);
            }
        });
        cmbEmployee.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                cmbEmployeeKeyPressed(evt);
            }
        });

        javax.swing.GroupLayout GSTLayout = new javax.swing.GroupLayout(GST);
        GST.setLayout(GSTLayout);
        GSTLayout.setHorizontalGroup(
            GSTLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(GSTLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(GSTLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 114, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(GSTLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                        .addComponent(jLabel9, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 111, Short.MAX_VALUE)
                        .addComponent(jLabel7, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel6, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(GSTLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(cmbTransport, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(date, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(txtInvoice)
                    .addComponent(cmbEmployee, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
        );
        GSTLayout.setVerticalGroup(
            GSTLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(GSTLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(GSTLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtInvoice))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(GSTLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(date, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(GSTLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cmbTransport, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(GSTLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel9, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cmbEmployee, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        javax.swing.GroupLayout invoiceDetailsLayout = new javax.swing.GroupLayout(invoiceDetails);
        invoiceDetails.setLayout(invoiceDetailsLayout);
        invoiceDetailsLayout.setHorizontalGroup(
            invoiceDetailsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(invoiceDetailsLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(billToParty, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(GST, javax.swing.GroupLayout.DEFAULT_SIZE, 351, Short.MAX_VALUE)
                .addContainerGap())
        );
        invoiceDetailsLayout.setVerticalGroup(
            invoiceDetailsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, invoiceDetailsLayout.createSequentialGroup()
                .addGroup(invoiceDetailsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(billToParty, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(GST, javax.swing.GroupLayout.DEFAULT_SIZE, 138, Short.MAX_VALUE))
                .addGap(40, 40, 40))
        );

        table.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null}
            },
            new String [] {
                "S.No.", "Product Description", "HSN Code", "Quantity", "Rate", "Total"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Object.class, java.lang.String.class, java.lang.Object.class, java.lang.Integer.class, java.lang.Object.class, java.lang.Object.class
            };
            boolean[] canEdit = new boolean [] {
                false, true, false, true, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        table.getTableHeader().setReorderingAllowed(false);
        jScrollPane1.setViewportView(table);
        table.getColumnModel().getSelectionModel().setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        if (table.getColumnModel().getColumnCount() > 0) {
            table.getColumnModel().getColumn(0).setResizable(false);
            table.getColumnModel().getColumn(1).setCellEditor(new DefaultCellEditor(txtAutofill));
            table.getColumnModel().getColumn(2).setResizable(false);
            table.getColumnModel().getColumn(3).setResizable(false);
            table.getColumnModel().getColumn(3).setCellEditor(new DefaultCellEditor(txtQuantity));
            table.getColumnModel().getColumn(5).setResizable(false);
        }

        btnSave.setText("Save & Print");
        btnSave.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSaveActionPerformed(evt);
            }
        });

        btnCancel.setText("Cancel");
        btnCancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCancelActionPerformed(evt);
            }
        });

        btnRemove.setText("Remove Item");
        btnRemove.setEnabled(false);
        btnRemove.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRemoveActionPerformed(evt);
            }
        });

        btnAdd.setText("Add Item");
        btnAdd.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAddActionPerformed(evt);
            }
        });

        jLabel8.setText("Remark:");

        invoiceData.setLayer(jScrollPane1, javax.swing.JLayeredPane.DEFAULT_LAYER);
        invoiceData.setLayer(btnSave, javax.swing.JLayeredPane.DEFAULT_LAYER);
        invoiceData.setLayer(btnCancel, javax.swing.JLayeredPane.DEFAULT_LAYER);
        invoiceData.setLayer(btnRemove, javax.swing.JLayeredPane.DEFAULT_LAYER);
        invoiceData.setLayer(btnAdd, javax.swing.JLayeredPane.DEFAULT_LAYER);
        invoiceData.setLayer(lblTotal, javax.swing.JLayeredPane.DEFAULT_LAYER);
        invoiceData.setLayer(jLabel8, javax.swing.JLayeredPane.DEFAULT_LAYER);
        invoiceData.setLayer(txtRemarks, javax.swing.JLayeredPane.DEFAULT_LAYER);

        javax.swing.GroupLayout invoiceDataLayout = new javax.swing.GroupLayout(invoiceData);
        invoiceData.setLayout(invoiceDataLayout);
        invoiceDataLayout.setHorizontalGroup(
            invoiceDataLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(invoiceDataLayout.createSequentialGroup()
                .addGroup(invoiceDataLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1)
                    .addGroup(invoiceDataLayout.createSequentialGroup()
                        .addComponent(btnAdd)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnRemove)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel8)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtRemarks, javax.swing.GroupLayout.PREFERRED_SIZE, 92, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnCancel, javax.swing.GroupLayout.PREFERRED_SIZE, 73, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnSave)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(lblTotal, javax.swing.GroupLayout.PREFERRED_SIZE, 101, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );

        invoiceDataLayout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {lblTotal, txtRemarks});

        invoiceDataLayout.setVerticalGroup(
            invoiceDataLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(invoiceDataLayout.createSequentialGroup()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 254, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(invoiceDataLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(invoiceDataLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(btnRemove)
                        .addComponent(btnAdd)
                        .addComponent(btnCancel)
                        .addComponent(btnSave)
                        .addComponent(jLabel8)
                        .addComponent(txtRemarks, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(lblTotal, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGap(14, 14, 14)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(invoiceData)
                    .addComponent(invoiceDetails, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(14, 14, 14))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(invoiceDetails, javax.swing.GroupLayout.PREFERRED_SIZE, 149, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(invoiceData)
                .addGap(25, 25, 25))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void formWindowClosing(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosing
        this.setVisible(false);
        Main.frames.get("home").setVisible(true);
    }//GEN-LAST:event_formWindowClosing

    private void btnAddActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAddActionPerformed
        menuPane.setVisible(false);
        float total = calculateTotal();
        if (total ==-1){
            JOptionPane.showMessageDialog(this, "Complete all row first!", "Error!",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }
        if (table.getValueAt(table.getRowCount()-1,0)!=null){
            DefaultTableModel model = (DefaultTableModel) table.getModel();
            model.addRow(new String[]{null,null,null,null,null,null});
        }
        table.setRowSelectionInterval(table.getRowCount()-1, table.getRowCount()-1);
        changeSelection(table.getRowCount()-1, 1, field);
        System.out.println(total);
        lblTotal.setText("Total + Tax: ₹"+ total);
    }//GEN-LAST:event_btnAddActionPerformed

    private void btnCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancelActionPerformed
        int response = JOptionPane.showConfirmDialog(this,"Do you really want to cancel?",
                "Confirm?", JOptionPane.YES_NO_CANCEL_OPTION);
        if (response!=JOptionPane.YES_OPTION)return;
        clear();
        this.dispose();
        Main.frames.get("home").setVisible(true);
    }//GEN-LAST:event_btnCancelActionPerformed

    private void btnRemoveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRemoveActionPerformed
        if (table.getRowCount()<2)return;
        SwingUtilities.invokeLater(() -> {
            table.getCellEditor(table.getSelectedRow(), table.getSelectedColumn()).stopCellEditing();
            ((DefaultTableModel)table.getModel()).removeRow(table.getSelectedRow());
            table.clearSelection();
            menuPane.setVisible(false);
        });
    }//GEN-LAST:event_btnRemoveActionPerformed

    private void btnSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSaveActionPerformed
        if (txtName.getText()==null||txtAddress.getText()==null||txtGstin.getText()==null ||txtInvoice.getText()==null
        ||date.getDate()==null||String.valueOf(cmbEmployee.getSelectedItem()).isEmpty()){
            JOptionPane.showMessageDialog(this, "All Fields Are Required!",
                    "Fill all Fields!", JOptionPane.ERROR_MESSAGE);
            return;
        }
        int r = JOptionPane.showConfirmDialog(this, "Do you Really want to save?","Confirm?"
        , JOptionPane.YES_NO_CANCEL_OPTION);
        if (r!=JOptionPane.YES_OPTION)return;
        Date selectedDate = Date.valueOf(LocalDate.ofInstant(date.getDate().toInstant(), ZoneId.systemDefault()));
        manager.addTransactionEntry(selectedDate, txtName.getText(), txtAddress.getText(),
                String.valueOf(cmbEmployee.getSelectedItem()), DataManager.EntryType.SALE,
                Float.parseFloat(lblTotal.getText().split("₹")[1]), txtRemarks.getText());
        Main.Employees.add(String.valueOf(cmbEmployee.getSelectedItem()));
        manager.saveUserData(Main.Employees, "employeeData.data");
        new DataManager().addTransactionEntry(Date.valueOf(LocalDate.ofInstant(date.getDate().toInstant(), ZoneId.systemDefault())),
                txtName.getText(), txtAddress.getText(), cmbEmployee.getSelectedItem().toString(), DataManager.EntryType.SALE,
                Float.parseFloat(lblTotal.getText().split("₹")[1]), txtRemarks.getText());
        Main.frames.get("home").setVisible(true);
        this.dispose();
    }//GEN-LAST:event_btnSaveActionPerformed

    private void formMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_formMouseClicked
        menuPane.setVisible(false);
        table.clearSelection();
    }//GEN-LAST:event_formMouseClicked

    private void txtNameKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtNameKeyPressed
        if (evt.getKeyCode()!=KeyEvent.VK_ENTER)return;
        txtAddress.grabFocus();
    }//GEN-LAST:event_txtNameKeyPressed

    private void txtAddressKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtAddressKeyPressed
        if (evt.getKeyCode()!=KeyEvent.VK_ENTER)return;
        cmbState.grabFocus();
        cmbState.showPopup();
    }//GEN-LAST:event_txtAddressKeyPressed

    private void txtInvoiceKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtInvoiceKeyPressed
        if (evt.getKeyCode()!=KeyEvent.VK_ENTER)return;
        date.grabFocus();
    }//GEN-LAST:event_txtInvoiceKeyPressed

    private void dateKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_dateKeyPressed
        if (evt.getKeyCode()!=KeyEvent.VK_ENTER)return;
        cmbEmployee.grabFocus();
    }//GEN-LAST:event_dateKeyPressed

    private void cmbStateItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cmbStateItemStateChanged
        if (evt.getStateChange()!= ItemEvent.SELECTED)return;
        txtGstin.grabFocus();
        float t = calculateTotal();
        if (t==-1)return;
        lblTotal.setText("Total + Tax: ₹"+t);

    }//GEN-LAST:event_cmbStateItemStateChanged

    private void txtGstinKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtGstinKeyPressed
        if (evt.getKeyCode()!=KeyEvent.VK_ENTER)return;
        txtInvoice.grabFocus();
    }//GEN-LAST:event_txtGstinKeyPressed

    private void invoiceDetailsMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_invoiceDetailsMouseClicked
        menuPane.setVisible(false);
        table.clearSelection();
    }//GEN-LAST:event_invoiceDetailsMouseClicked

    private void cmbEmployeeItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cmbEmployeeItemStateChanged
        if (evt.getStateChange()!=ItemEvent.SELECTED)return;
        changeSelection(0,1, field);
    }//GEN-LAST:event_cmbEmployeeItemStateChanged

    private void cmbEmployeeKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_cmbEmployeeKeyPressed
        if (evt.getKeyCode()!=KeyEvent.VK_ENTER)return;
        changeSelection(0, 1, field);
    }//GEN-LAST:event_cmbEmployeeKeyPressed

    public void changeSelection(int row, int col, JTextField field){
        if (table.editCellAt(row, col)){
            table.changeSelection(row, col, false, false);
        }
        field.grabFocus();
    }
    public List<String> getAutoComplete(String start, KeyEvent e){
        DefaultListModel<String>model = (DefaultListModel<String>) menu.getModel();
        model.removeAllElements();
        List<String>list = new ArrayList<>();
        if (Character.isLetterOrDigit(e.getKeyChar())||e.getKeyCode()==KeyEvent.VK_UNDEFINED){
            for (String s: Main.searchList){
                if (s.toLowerCase().contains((start + e.getKeyChar()).toLowerCase().trim())) list.add(s);
            }
            if (list.size()<1){
                menuPane.setVisible(false);
                menu.removeAll();
                e.consume();
                Toolkit.getDefaultToolkit().beep();
                return null;
            }
        }
        else if (e.getKeyCode()==KeyEvent.VK_BACK_SPACE){
            if (start.isEmpty()){
                list.addAll(Main.searchList);
            }
            else{
                String s1 = start.substring(0, start.length()-1).toLowerCase();
                e.setKeyCode(KeyEvent.VK_UNDEFINED);
                return getAutoComplete(s1, e);
            }
            if (list.size()<1)return null;
        }
        model.addAll(list);
        menu.setModel(model);
        menu.setFixedCellWidth(table.getColumnModel().getColumn(1).getWidth());
        menu.setFixedCellHeight(table.getRowHeight(table.getSelectedRow()));
        menuPane.setSize(table.getColumnModel().getColumn(1).getWidth(),
                table.getRowHeight(table.getSelectedRow())*Math.min(list.size(),8));
        menu.setSize(table.getColumnModel().getColumn(1).getWidth(),
                table.getRowHeight(table.getSelectedRow())*Math.min(list.size(),8));

        int x = ((JComponent)e.getSource()).getLocation().x;
        int y = table.getTableHeader().getHeight()+
                table.getCellRect(table.getSelectedRow(), 1, true).y+
                table.getRowHeight(table.getSelectedRow());
        menuPane.setLocation(x, y);
        menuPane.setVisible(true);
        field.grabFocus();
        return list;
    }

    public void autoFill(){
        if (menu.getSelectedIndex() == -1) {
            menu.setSelectedIndex(0);
        }
        if (menu.getSelectedValue().split("/").length<3){
            if (table.editCellAt(table.getSelectedRow(),1)){
                table.changeSelection(table.getSelectedRow(),1, false, false);
            }
            field.grabFocus();
        }
        else {
            for (int i = 0; i < table.getRowCount()-1; i++) {
                if (i==table.getSelectedRow())continue;
                if (String.valueOf(table.getValueAt(i, 1)).equalsIgnoreCase(menu.getSelectedValue()))return;
            }
            String[] data = menu.getSelectedValue().split("/");
            String hsn = ""; float rate = 0;
            Item item = Main.itemMap.get(data[0]+"/"+data[1]);
            for (Map.Entry<String, Float> size: item.getSizes().entrySet()){
                if (!size.getKey().equalsIgnoreCase(
                        menu.getSelectedValue().toLowerCase().replace((data[0]+"/"+data[1]+"/")
                                .toLowerCase(),"")))continue;
                hsn = item.getHsn();
                rate = size.getValue();
                break;
            }

            table.setValueAt(menu.getSelectedValue(), table.getSelectedRow(),1);
            table.setValueAt(table.getSelectedRow()+1, table.getSelectedRow(), 0);
            table.setValueAt(hsn, table.getSelectedRow(), 2);
            table.setValueAt(rate+"/unit", table.getSelectedRow(),4);
            if (table.editCellAt(table.getSelectedRow(),3)){
                table.changeSelection(table.getSelectedRow(),3, false, false);
            }
            txtQuantity.grabFocus();
            menuPane.setVisible(false);
        }
    }

    public void clear(){
        menuPane.setVisible(false);
        table.clearSelection();
        txtInvoice.setText(null);
        txtGstin.setText(null);
        txtAddress.setText(null);
        txtName.setText(null);
        cmbState.setSelectedIndex(0);
        date.cleanup();
        DefaultTableModel model = (DefaultTableModel) table.getModel();
        model.setRowCount(0);
        ((DefaultTableModel) table.getModel()).addRow(new String[]{null,null,null,null,null,null});
        table.setModel(model);
    }

    public float calculateTotal(){
        try {
            float total = 0;
            for (int i = 0; i < table.getRowCount() ; i++) {
                String s= table.getValueAt(i,5).toString();
                if (s.isEmpty()){

                    return -1;
                }
                total = total+Float.parseFloat(s);
            }
            if (Main.data.getState().equalsIgnoreCase(String.valueOf(cmbState.getSelectedItem()))){
                total = total+total*Main.data.getCgst()/100;
            }else {
                total = total+(total*Main.data.getSgst()/100)+(total*Main.data.getIgst()/100);
            }
            return total;
        }catch (NullPointerException ignored){}
        return 0;
    }
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
            java.util.logging.Logger.getLogger(BillData.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(BillData.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(BillData.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(BillData.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new BillData().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel GST;
    private javax.swing.JPanel billToParty;
    private javax.swing.JButton btnAdd;
    private javax.swing.JButton btnCancel;
    private javax.swing.JButton btnRemove;
    private javax.swing.JButton btnSave;
    private javax.swing.JComboBox<String> cmbEmployee;
    private javax.swing.JComboBox<String> cmbState;
    private javax.swing.JComboBox<String> cmbTransport;
    private com.toedter.calendar.JDateChooser date;
    private javax.swing.JLayeredPane invoiceData;
    private javax.swing.JPanel invoiceDetails;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel lblTotal;
    private javax.swing.JTable table;
    private javax.swing.JTextField txtAddress;
    private javax.swing.JTextField txtGstin;
    private javax.swing.JTextField txtInvoice;
    private javax.swing.JTextField txtName;
    private javax.swing.JTextField txtRemarks;
    // End of variables declaration//GEN-END:variables
}
