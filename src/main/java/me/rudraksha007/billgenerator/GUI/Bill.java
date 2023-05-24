/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package me.rudraksha007.billgenerator.GUI;

import me.rudraksha007.billgenerator.Item;
import me.rudraksha007.billgenerator.Main;
import me.rudraksha007.billgenerator.POI.SpreadsheetTools;
import me.rudraksha007.billgenerator.utilities.BillData;
import me.rudraksha007.billgenerator.utilities.DataManager;
import me.rudraksha007.billgenerator.utilities.Utils;

import javax.swing.*;
import javax.swing.event.CellEditorListener;
import javax.swing.event.ChangeEvent;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellEditor;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.sql.*;
import java.sql.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.util.List;
import java.util.*;

/**
 *
 * @author rudra
 */
public class Bill extends javax.swing.JFrame {
    JList<String> menu = new JList<>();
    JScrollPane menuPane = new JScrollPane();
    JTextField txtAutofill = new JTextField();
    JTextField txtQuantity = new JTextField();
    JTextField field;
    List<Integer>codes = Arrays.asList(KeyEvent.VK_UP,KeyEvent.VK_KP_UP, KeyEvent.VK_LEFT, KeyEvent.VK_KP_LEFT,
            KeyEvent.VK_RIGHT, KeyEvent.VK_KP_RIGHT);
    DataManager manager = new DataManager();

    AWTEventListener listener = new AWTEventListener() {
        @Override
        public void eventDispatched(AWTEvent event) {
            if (event.getID()!=MouseEvent.MOUSE_CLICKED)return;
            Component c = (Component) event.getSource();
            while (c!=null&&!c.equals(table)&&!c.equals(menuPane)&&!c.equals(btnRemove))c = c.getParent();
            if (c==table||c==menuPane||c==btnRemove)return;
            if (table.isEditing())table.getColumnModel().getColumn(table.getSelectedColumn()).getCellEditor().stopCellEditing();
            table.clearSelection();
            calculateTotal();
        }
    };

    private boolean isEditing;
    /**
     * Creates new form BillData
     */
    public Bill() {
        isEditing = false;
        initComponents();
        this.setLocationRelativeTo(null);
        table.putClientProperty("terminateEditOnFocusLost", Boolean.TRUE);
        txtQuantity = (JTextField) table.getColumnModel().getColumn(3)
                .getCellEditor().getTableCellEditorComponent(table,"", true, table.getSelectedRow(), 3);
        field = (JTextField) table.getColumnModel().getColumn(1)
                .getCellEditor().getTableCellEditorComponent(table,"", true, table.getSelectedRow(), 1);
        configureElements();

    }

    public void setBill(BillData data){
        isEditing = true;
        txtInvoice.setEditable(false);
        date.setEnabled(false);
        cmbParty.setSelectedItem(data.getParty());
        txtAddress.setText(data.getAddress());
        txtState.setText(data.getState());
        txtGstin.setText(data.getGstIn());
        txtInvoice.setText(data.getInvoice());
        date.setDate(data.getDate());
        txtVehicleNo.setText(data.getVehicle());
        for (Vector<String> row: data.getTableData()){
            ((DefaultTableModel)table.getModel()).addRow(row);
        }
        calculateTotal();
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

        date.setDateFormatString("dd-MM-YYYY");
        table.getSelectionModel().addListSelectionListener(e -> {
            if (e.getValueIsAdjusting())return;
            btnRemove.setEnabled(table.getSelectedRow()>=0);
            if (txtQuantity.hasFocus()) {
                return;
            }
            if (table.getSelectedRow()>=0){
                if (table.editCellAt(table.getSelectedRow(), 1)){
                    table.changeSelection(table.getSelectedRow(), 1, false, false);
                }
                field.grabFocus();
            }
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
                    calculateTotal();
                }
                else if (e.getKeyCode()==KeyEvent.VK_ENTER){
                    if (field.getText().split("/").length>=3) {
                        table.getCellEditor(table.getSelectedRow(), 1).stopCellEditing();
                        calculateTotal();
                    }
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
                calculateTotal();
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
                    int num;
                    try {
                        num = Integer.parseInt(txtQuantity.getText());
                    }catch (NumberFormatException ex){
                        e.consume();
                        return;}
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
                if (field.getText().split("/").length>=3) autoFill();
//                menuPane.setVisible(false);
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
                int num ;
                try {
                    num = Integer.parseInt(txtQuantity.getText());
                }catch (NumberFormatException ex){return;}
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
                calculateTotal();
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

        invoiceData.add(menuPane, JLayeredPane.POPUP_LAYER);

        Toolkit.getDefaultToolkit().addAWTEventListener(listener, AWTEvent.MOUSE_EVENT_MASK);

        ResultSet set = manager.get("SELECT * FROM Parties");
        DefaultComboBoxModel<String> mod = (DefaultComboBoxModel<String>) cmbParty.getModel();
        try {
            while (set.next()){
                mod.addElement(set.getString(1));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        cmbParty.setModel(mod);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel2 = new javax.swing.JPanel();
        invoiceDetails = new javax.swing.JPanel();
        billToParty = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        txtAddress = new javax.swing.JTextField();
        txtGstin = new javax.swing.JTextField();
        cmbParty = new javax.swing.JComboBox<>();
        txtState = new javax.swing.JTextField();
        GST = new javax.swing.JPanel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        txtInvoice = new javax.swing.JTextField();
        date = new com.toedter.calendar.JDateChooser();
        cmbTransport = new javax.swing.JComboBox<>();
        jLabel10 = new javax.swing.JLabel();
        txtVehicleNo = new javax.swing.JTextField();
        jPanel1 = new javax.swing.JPanel();
        btnSave = new javax.swing.JButton();
        btnCancel = new javax.swing.JButton();
        btnRemove = new javax.swing.JButton();
        btnAdd = new javax.swing.JButton();
        invoiceData = new javax.swing.JLayeredPane();
        jScrollPane1 = new javax.swing.JScrollPane();
        table = new javax.swing.JTable();
        lblTotal = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        txtRemarks = new javax.swing.JTextField();
        jLabel9 = new javax.swing.JLabel();
        txtSupplier = new javax.swing.JTextField();
        jPanel3 = new javax.swing.JPanel();
        jLabel11 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
        setTitle("Creating Invoice");
        setMinimumSize(new java.awt.Dimension(1280, 720));
        setPreferredSize(new java.awt.Dimension(1280, 720));
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

        txtAddress.setEditable(false);

        txtGstin.setEditable(false);
        txtGstin.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtGstinKeyPressed(evt);
            }
        });

        cmbParty.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                cmbPartyItemStateChanged(evt);
            }
        });

        txtState.setEditable(false);

        javax.swing.GroupLayout billToPartyLayout = new javax.swing.GroupLayout(billToParty);
        billToParty.setLayout(billToPartyLayout);
        billToPartyLayout.setHorizontalGroup(
            billToPartyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(billToPartyLayout.createSequentialGroup()
                .addGroup(billToPartyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(billToPartyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(txtAddress)
                    .addComponent(cmbParty, 0, 559, Short.MAX_VALUE)))
            .addGroup(billToPartyLayout.createSequentialGroup()
                .addGroup(billToPartyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(billToPartyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(txtGstin)
                    .addComponent(txtState)))
        );
        billToPartyLayout.setVerticalGroup(
            billToPartyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(billToPartyLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(billToPartyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cmbParty, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(billToPartyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(txtAddress)
                    .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(billToPartyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtState, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(billToPartyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtGstin, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
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

        cmbTransport.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "By Road" }));

        jLabel10.setText("Vehicle No.");

        txtVehicleNo.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtVehicleNoKeyPressed(evt);
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
                        .addComponent(jLabel10, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel7, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 111, Short.MAX_VALUE)
                        .addComponent(jLabel6, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(GSTLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(cmbTransport, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(date, javax.swing.GroupLayout.DEFAULT_SIZE, 492, Short.MAX_VALUE)
                    .addComponent(txtVehicleNo)
                    .addComponent(txtInvoice)))
        );
        GSTLayout.setVerticalGroup(
            GSTLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(GSTLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(GSTLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtInvoice, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(GSTLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(date, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(GSTLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cmbTransport, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(GSTLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(txtVehicleNo)
                    .addComponent(jLabel10, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(17, 17, 17))
        );

        javax.swing.GroupLayout invoiceDetailsLayout = new javax.swing.GroupLayout(invoiceDetails);
        invoiceDetails.setLayout(invoiceDetailsLayout);
        invoiceDetailsLayout.setHorizontalGroup(
            invoiceDetailsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(invoiceDetailsLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(billToParty, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(GST, javax.swing.GroupLayout.DEFAULT_SIZE, 618, Short.MAX_VALUE)
                .addContainerGap())
        );
        invoiceDetailsLayout.setVerticalGroup(
            invoiceDetailsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, invoiceDetailsLayout.createSequentialGroup()
                .addGroup(invoiceDetailsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(billToParty, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(GST, javax.swing.GroupLayout.DEFAULT_SIZE, 141, Short.MAX_VALUE))
                .addGap(40, 40, 40))
        );

        btnSave.setText("Save");
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

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(btnAdd)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnRemove)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(btnCancel, javax.swing.GroupLayout.PREFERRED_SIZE, 73, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(btnSave)
                .addGap(12, 12, 12))
        );

        jPanel1Layout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {btnAdd, btnCancel, btnRemove, btnSave});

        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnSave)
                    .addComponent(btnCancel)
                    .addComponent(btnAdd)
                    .addComponent(btnRemove))
                .addGap(16, 16, 16))
        );

        table.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

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

        lblTotal.setFont(new java.awt.Font("Impact", 0, 18)); // NOI18N
        lblTotal.setText("Total + Tax :");

        jLabel8.setText("Remark:");

        jLabel9.setText("Assigned to");

        txtSupplier.setEditable(false);

        invoiceData.setLayer(jScrollPane1, javax.swing.JLayeredPane.DEFAULT_LAYER);
        invoiceData.setLayer(lblTotal, javax.swing.JLayeredPane.DEFAULT_LAYER);
        invoiceData.setLayer(jLabel8, javax.swing.JLayeredPane.DEFAULT_LAYER);
        invoiceData.setLayer(txtRemarks, javax.swing.JLayeredPane.DEFAULT_LAYER);
        invoiceData.setLayer(jLabel9, javax.swing.JLayeredPane.DEFAULT_LAYER);
        invoiceData.setLayer(txtSupplier, javax.swing.JLayeredPane.DEFAULT_LAYER);

        javax.swing.GroupLayout invoiceDataLayout = new javax.swing.GroupLayout(invoiceData);
        invoiceData.setLayout(invoiceDataLayout);
        invoiceDataLayout.setHorizontalGroup(
            invoiceDataLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(invoiceDataLayout.createSequentialGroup()
                .addGroup(invoiceDataLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1)
                    .addGroup(invoiceDataLayout.createSequentialGroup()
                        .addComponent(jLabel8)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtRemarks)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel9)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtSupplier, javax.swing.GroupLayout.PREFERRED_SIZE, 369, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lblTotal, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addContainerGap())
        );
        invoiceDataLayout.setVerticalGroup(
            invoiceDataLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(invoiceDataLayout.createSequentialGroup()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 380, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(invoiceDataLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel8)
                    .addComponent(txtRemarks, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel9, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblTotal, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(txtSupplier, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(8, 8, 8)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(invoiceData)
                            .addComponent(invoiceDetails, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGap(8, 8, 8))
                    .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(invoiceDetails, javax.swing.GroupLayout.PREFERRED_SIZE, 149, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(invoiceData)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        jLabel11.setFont(new java.awt.Font("Impact", 0, 36)); // NOI18N
        jLabel11.setText("Generating Bill");

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel11)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel11)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void formWindowClosing(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosing
        Toolkit.getDefaultToolkit().removeAWTEventListener(listener);
        this.dispose();
        if (!isEditing) Main.frames.get("home").setVisible(true);
        else new Summary().setVisible(true);
    }//GEN-LAST:event_formWindowClosing

    private void btnAddActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAddActionPerformed
        menuPane.setVisible(false);
        calculateTotal();
        if (table.getRowCount()==0){
            ((DefaultTableModel)table.getModel()).addRow(new Object[]{1, null, null, null, null, null});
            table.editCellAt(0, 1);
            table.changeSelection(0, 1, false, false);
            field.grabFocus();
            return;
        }
        if (table.getCellEditor(table.getRowCount()-1, 3).getCellEditorValue()!= null
                &&!table.getCellEditor(table.getRowCount()-1, 3).getCellEditorValue().toString().isEmpty()){
            DefaultTableModel model = (DefaultTableModel) table.getModel();
            model.addRow(new String[]{null,null,null,null,null,null});
        }
        table.editCellAt(table.getRowCount()-1, 1);
        table.changeSelection(table.getRowCount()-1, 1, false, false);
        field.grabFocus();

    }//GEN-LAST:event_btnAddActionPerformed

    private void btnCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancelActionPerformed
        int response = JOptionPane.showConfirmDialog(this,"Do you really want to cancel?",
                "Confirm?", JOptionPane.YES_NO_CANCEL_OPTION);
        if (response!=JOptionPane.YES_OPTION)return;
        this.dispose();
        Main.frames.get("home").setVisible(true);
    }//GEN-LAST:event_btnCancelActionPerformed

    private void btnRemoveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRemoveActionPerformed
        if (table.getRowCount()<1)return;
        SwingUtilities.invokeLater(() -> {
            ((DefaultTableModel)table.getModel()).removeRow(table.getSelectedRow());
            menuPane.setVisible(false);
            calculateTotal();
        });
    }//GEN-LAST:event_btnRemoveActionPerformed

    private void btnSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSaveActionPerformed
        if (txtInvoice.getText()==null ||date.getDate()==null||txtVehicleNo.getText().isEmpty()){
            JOptionPane.showMessageDialog(this, "All Fields Are Required!",
                    "Fill all Fields!", JOptionPane.ERROR_MESSAGE);
            return;
        }
        for (int i = 0; i < table.getRowCount(); i++) {
            if (String.valueOf(table.getValueAt(i, 5)).isEmpty()){
                JOptionPane.showMessageDialog(this, "Complete all rows first!",
                        "Fill all Fields!", JOptionPane.ERROR_MESSAGE);
                return;
            }
        }
        int r = JOptionPane.showConfirmDialog(this, "Do you Really want to save?","Confirm?"
        , JOptionPane.YES_NO_CANCEL_OPTION);
        if (r!=JOptionPane.YES_OPTION)return;
        JDialog dialog = new LoadingScreen(this,true);
        Bill home = this;
        new Utils().runAfter(new TimerTask() {
            @Override
            public void run() {
                Date selectedDate = Date.valueOf(LocalDate.ofInstant(date.getDate().toInstant(), ZoneId.systemDefault()));
                ResultSet set = manager.get("SELECT * FROM Transactions WHERE InvoiceNo = '"+txtInvoice.getText()+"'");
                try {
                    if(set!=null&&set.next()&&!isEditing){
                        dialog.dispose();
                        JOptionPane.showMessageDialog(home, "Invoice Number Already exists", "Error"
                                ,JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                    else if (isEditing){
                        manager.run("UPDATE Transactions SET DATE = '"+selectedDate+"', Party = '"+cmbParty.getSelectedItem()+
                                "', Address = '"+txtAddress.getText()+"', Supplier ='"+
                                txtSupplier.getText() +"', Sale = "+
                                Float.parseFloat(lblTotal.getText().split("")[1])+",Remark ='"+ txtRemarks.getText()+"'");
                    }
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
                if (!isEditing){
                    String cmd = "INSERT INTO Bills(InvoiceNo, Data) VALUES(?,?)";
                    try {
                        PreparedStatement state = DataManager.con.prepareStatement(cmd);
                        state.setString(1, txtInvoice.getText());
                        BillData data = new BillData(String.valueOf(cmbParty.getSelectedItem()), txtAddress.getText(), txtState.getText()
                                ,txtGstin.getText(), txtInvoice.getText(), Date.valueOf(LocalDate.ofInstant(date.getDate().toInstant(), ZoneId.systemDefault()))
                                , String.valueOf(cmbTransport.getSelectedItem()), txtVehicleNo.getText(), ((DefaultTableModel)table.getModel()).getDataVector()
                                , txtRemarks.getText(), txtSupplier.getText());
                        state.setString(2, data.getAsString());
                        state.execute();
                    } catch (SQLException e) {
                        throw new RuntimeException(e);
                    }
                    manager.addTransactionEntry(selectedDate, String.valueOf(cmbParty.getSelectedItem()), txtAddress.getText(),
                            txtSupplier.getText(), DataManager.EntryType.SALE,
                            Float.parseFloat(lblTotal.getText()), txtRemarks.getText(), txtInvoice.getText());
                }

                SimpleDateFormat format = new SimpleDateFormat("dd-MM-YYYY");
                System.out.println(date.getDate());
                System.out.println(format.format(date.getDate()));
                new SpreadsheetTools().saveXLSX(txtInvoice.getText(), format.format(date.getDate()),
                        String.valueOf(cmbTransport.getSelectedItem()),txtVehicleNo.getText(),
                        String.valueOf(cmbParty.getSelectedItem()), txtAddress.getText(),txtGstin.getText(),
                        txtState.getText(), Math.round(Float.parseFloat(lblTotal.getText().split("")[1])),
                        ((DefaultTableModel)table.getModel()).getDataVector());
                dialog.dispose();
                Main.frames.get("home").setVisible(true);
                home.dispose();
//                new Summary().setVisible(true);
//                dialog.dispose();
//                home.setVisible(false);
            }
        }, 1);
        dialog.setVisible(true);
    }//GEN-LAST:event_btnSaveActionPerformed

    private void txtInvoiceKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtInvoiceKeyPressed
        if (evt.getKeyCode()!=KeyEvent.VK_ENTER)return;
        date.grabFocus();
    }//GEN-LAST:event_txtInvoiceKeyPressed

    private void dateKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_dateKeyPressed
        if (evt.getKeyCode()!=KeyEvent.VK_ENTER)return;
    }//GEN-LAST:event_dateKeyPressed

    private void txtGstinKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtGstinKeyPressed
        if (evt.getKeyCode()!=KeyEvent.VK_ENTER)return;
        txtInvoice.grabFocus();
    }//GEN-LAST:event_txtGstinKeyPressed

    private void invoiceDetailsMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_invoiceDetailsMouseClicked
        menuPane.setVisible(false);
    }//GEN-LAST:event_invoiceDetailsMouseClicked

    private void cmbPartyItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cmbPartyItemStateChanged
        if (evt.getStateChange()!=ItemEvent.SELECTED)return;
        if (evt.getItem().toString().isEmpty())return;
        ResultSet set = manager.get("SELECT * FROM Parties WHERE Name = '"+cmbParty.getSelectedItem()+"'");
        try {
            if (!set.next())return;
            txtAddress.setText(set.getString(2));
            txtState.setText(set.getString(3));
            txtSupplier.setText(set.getString(4));
            txtGstin.setText(set.getString(5));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }//GEN-LAST:event_cmbPartyItemStateChanged

    private void txtVehicleNoKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtVehicleNoKeyPressed
        if (evt.getKeyCode()!=KeyEvent.VK_ENTER)return;
        if (txtVehicleNo.getText().isEmpty())return;
        btnAddActionPerformed(null);
    }//GEN-LAST:event_txtVehicleNoKeyPressed

    public void changeSelection(int row, int col, JTextField field){
        if (table.editCellAt(row, col)){
            table.changeSelection(row, col, false, false);
        }
//        table.setRowSelectionInterval(row,row);
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

    public List<String> getAutoComplete(String start){
        DefaultListModel<String>model = (DefaultListModel<String>) menu.getModel();
        model.removeAllElements();
        List<String>list = new ArrayList<>();
        for (String s: Main.searchList){
            if (s.toLowerCase().contains(start.toLowerCase().trim())) list.add(s);
        }
        if (list.size()<1){
            menuPane.setVisible(false);
            menu.removeAll();
            Toolkit.getDefaultToolkit().beep();
            return null;
        }
        model.addAll(list);
        menu.setModel(model);
        menu.setFixedCellWidth(table.getColumnModel().getColumn(1).getWidth());
        menu.setFixedCellHeight(table.getRowHeight(table.getSelectedRow()));
        menuPane.setSize(table.getColumnModel().getColumn(1).getWidth(),
                table.getRowHeight(table.getSelectedRow())*Math.min(list.size(),8));
        menu.setSize(table.getColumnModel().getColumn(1).getWidth(),
                table.getRowHeight(table.getSelectedRow())*Math.min(list.size(),8));

        int x = field.getLocation().x;
        int y = table.getTableHeader().getHeight()+
                table.getCellRect(table.getSelectedRow(), 1, true).y+
                table.getRowHeight(table.getSelectedRow());
        menuPane.setLocation(x, y);
        return list;
    }

    public void autoFill(){
        if (menu.getSelectedValue()==null){
            return;
        }
        if (menu.getSelectedIndex() == -1) {
            menu.setSelectedValue(table.getCellEditor(table.getSelectedRow(), 1).getCellEditorValue().toString(), true);
        }
        if (!menuPane.isVisible()){
            for (String s: getAutoComplete(field.getText())){
                if (s.split("/").length>2)menu.setSelectedValue(s, true);
            }
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

    public boolean calculateTotal(){
        try {
            float total = 0;
            for (int i = 0; i < table.getRowCount() ; i++) {
                String s= table.getValueAt(i,5).toString();
                if (s.isEmpty())continue;
                total = total+Float.parseFloat(s);
            }
            float offset;
            if (Main.data.getState().equalsIgnoreCase(txtState.getText())){
                offset = (total*Main.data.getCgst()/100)+(total*Main.data.getSgst()/100);
            }else {
                offset = (total*Main.data.getIgst()/100);
            }
            lblTotal.setText(String.valueOf(total+offset));
        }catch (NullPointerException ignored){}
        return true;
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
            java.util.logging.Logger.getLogger(Bill.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Bill.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Bill.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Bill.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Bill().setVisible(true);
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
    private javax.swing.JComboBox<String> cmbParty;
    private javax.swing.JComboBox<String> cmbTransport;
    private com.toedter.calendar.JDateChooser date;
    private javax.swing.JLayeredPane invoiceData;
    private javax.swing.JPanel invoiceDetails;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel lblTotal;
    private javax.swing.JTable table;
    private javax.swing.JTextField txtAddress;
    private javax.swing.JTextField txtGstin;
    private javax.swing.JTextField txtInvoice;
    private javax.swing.JTextField txtRemarks;
    private javax.swing.JTextField txtState;
    private javax.swing.JTextField txtSupplier;
    private javax.swing.JTextField txtVehicleNo;
    // End of variables declaration//GEN-END:variables
}
