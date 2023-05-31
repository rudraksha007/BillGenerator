/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package me.rudraksha007.billgenerator.GUI2;

import me.rudraksha007.billgenerator.GUI.AddTransaction;
import me.rudraksha007.billgenerator.Item;
import me.rudraksha007.billgenerator.Main;
import me.rudraksha007.billgenerator.utilities.DataManager;
import me.rudraksha007.billgenerator.utilities.Utils;

import javax.swing.*;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreePath;
import java.awt.*;
import java.awt.event.*;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 *
 * @author rudra
 */
public class Home extends javax.swing.JFrame {

    DataManager manager = new DataManager();
    Utils utils = new Utils();
    Color selected = new Color(6, 20, 58);
    Color focused = new Color(11, 42, 123);
    Color panelColor = new Color(18, 73, 214);
    boolean isBillEditing;
    /**
     * Creates new form Home
     */
    public Home() {
        initComponents();
        this.setLocationRelativeTo(null);
        addMouseHoverEffect(lblPortfolio);addMouseHoverEffect(lblBill);addMouseHoverEffect(lblAddProduct);
        addMouseHoverEffect(lblAddParty);addMouseHoverEffect(lblEdit);addMouseHoverEffect(lblSummary);
        changePanel(pnlPortfolio);
        configureBillPanel();
    }

    public void configureBillPanel(){
        isBillEditing = false;
        billTable.putClientProperty("terminateEditOnFocusLost", Boolean.TRUE);

        DefaultComboBoxModel<String> parties = new DefaultComboBoxModel<>();
        parties.addElement("");
        ResultSet set = manager.get("SELECT * FROM Parties");
        try {while (set.next()){parties.addElement(set.getString(1));}
        } catch (SQLException e) {throw new RuntimeException(e);}
        cmbBillParty.setModel(parties);


    }

    public void configureAddProductPanel(){
        utils.setNumberOnly(txtAddProductBrand);
        productTree.addTreeSelectionListener(e -> {
            try {
                String[] path = (String[]) e.getPath().getPath();
                txtAddProductBrand.setText(path[1]);
                txtAddProductName.setText(path[2]);
                txtAddProductHsn.setText(Main.items.get(path[1]).get(path[2]).getHsn());
                txtAddProductVariant.setText(path[3]);
                DefaultTableModel model = new DefaultTableModel();
                for (Map.Entry<String, Float>entry: Main.items.get(path[1]).get(path[2]).getSizes().entrySet()){
                    model.addRow(new Object[]{model.getRowCount()+1, entry.getKey(), entry.getValue()+"/item"});
                }
            }catch (ArrayIndexOutOfBoundsException ignored){}
        });

        txtAddProductBrand.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {

            }

            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode()!=KeyEvent.VK_ENTER)return;
                if (productTree.getModel().getIndexOfChild(productTree.getModel().getRoot(), txtAddProductBrand.getText())>=0){
                    productTree.setSelectionPath(new TreePath(new Object[]{productTree.getModel().getRoot(),txtAddProductBrand.getText()}));
                }
                txtAddProductName.setText("");
                txtAddProductName.grabFocus();
            }

            @Override
            public void keyReleased(KeyEvent e) {

            }
        });

        txtAddProductName.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {

            }

            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode()!=KeyEvent.VK_ENTER)return;
                if (productTree.getModel().getRoot())
            }

            @Override
            public void keyReleased(KeyEvent e) {

            }
        });
        if (Main.items==null||Main.items.isEmpty())return;
        for (String s: Main.items.keySet()){
            DefaultMutableTreeNode brandNode = new DefaultMutableTreeNode(s);
            DefaultMutableTreeNode productNode;
            for (Item i: Main.items.get(s).values()){
                productNode = new DefaultMutableTreeNode(i.getName());
                for (String var: i.getSizes().keySet()){
                    productNode.add(new DefaultMutableTreeNode(var));
                }
                brandNode.add(productNode);
            }
        }
    }

    private void changePanel(JPanel panel) {
        pnlPortfolio.setVisible(pnlPortfolio ==panel);
        pnlAddParty.setVisible(pnlAddParty==panel);
        pnlAddProduct.setVisible(pnlAddProduct==panel);
        pnlBill.setVisible(pnlBill==panel);
        pnlEdit.setVisible(pnlEdit==panel);
        pnlSummary.setVisible(pnlSummary==panel);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        MainPane = new javax.swing.JPanel();
        pnlMenu = new JPanel(){
            public void paint(Graphics g){
                super.paint(g);
                Graphics2D g2d = (Graphics2D)g;
                GradientPaint p = new GradientPaint(0f,0f, panelColor, 0f, getHeight(), Color.WHITE);
                g2d.setPaint(p);
                g2d.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        lblPortfolio = new javax.swing.JLabel();
        lblBill = new javax.swing.JLabel();
        lblAddProduct = new javax.swing.JLabel();
        lblAddParty = new javax.swing.JLabel();
        lblEdit = new javax.swing.JLabel();
        lblSummary = new javax.swing.JLabel();
        ContentPane = new javax.swing.JLayeredPane();
        pnlBill = new javax.swing.JPanel();
        pnlInputs = new javax.swing.JPanel();
        InputLine1 = new javax.swing.JPanel();
        lblParty = new javax.swing.JLabel();
        cmbBillParty = new javax.swing.JComboBox<>();
        lblAddress = new javax.swing.JLabel();
        txtAddress = new javax.swing.JTextField();
        lblState = new javax.swing.JLabel();
        txtState = new javax.swing.JTextField();
        lblGst = new javax.swing.JLabel();
        txtGst = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        txtSupplier = new javax.swing.JTextField();
        InputLine2 = new javax.swing.JPanel();
        lblInvoice = new javax.swing.JLabel();
        txtInvoice = new javax.swing.JTextField();
        lblInvoiceDate = new javax.swing.JLabel();
        dataChooser = new com.toedter.calendar.JDateChooser();
        lblVehicleNo = new javax.swing.JLabel();
        txtVehicle = new javax.swing.JTextField();
        lblRemarks = new javax.swing.JLabel();
        txtRemarks = new javax.swing.JTextField();
        BillData = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        billTable = new javax.swing.JTable();
        pnlButtons = new javax.swing.JPanel();
        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        lblTotal = new javax.swing.JLabel();
        txtTotal = new javax.swing.JTextField();
        lblTax = new javax.swing.JLabel();
        txtTax = new javax.swing.JTextField();
        lblGTotal = new javax.swing.JLabel();
        txtGTotal = new javax.swing.JTextField();
        pnlAddParty = new javax.swing.JPanel();
        jPanel21 = new javax.swing.JPanel();
        jLabel5 = new javax.swing.JLabel();
        jPanel6 = new javax.swing.JPanel();
        btnDelete = new javax.swing.JButton();
        btnEdit = new javax.swing.JButton();
        jPanel7 = new javax.swing.JPanel();
        jPanel8 = new javax.swing.JPanel();
        jPanel10 = new javax.swing.JPanel();
        jLabel12 = new javax.swing.JLabel();
        jTextField3 = new javax.swing.JTextField();
        jLabel13 = new javax.swing.JLabel();
        jComboBox2 = new javax.swing.JComboBox<>();
        jLabel14 = new javax.swing.JLabel();
        jTextField4 = new javax.swing.JTextField();
        btnAdd = new javax.swing.JButton();
        jPanel11 = new javax.swing.JPanel();
        jLabel9 = new javax.swing.JLabel();
        jTextField1 = new javax.swing.JTextField();
        jLabel11 = new javax.swing.JLabel();
        jComboBox1 = new javax.swing.JComboBox<>();
        jLabel10 = new javax.swing.JLabel();
        jTextField2 = new javax.swing.JTextField();
        jPanel9 = new javax.swing.JPanel();
        jScrollPane3 = new javax.swing.JScrollPane();
        table1 = new javax.swing.JTable();
        pnlPortfolio = new javax.swing.JPanel();
        pnlAddProduct = new javax.swing.JPanel();
        pnlAddProductsContents2 = new javax.swing.JPanel();
        jPanel23 = new javax.swing.JPanel();
        jLabel16 = new javax.swing.JLabel();
        jPanel3 = new javax.swing.JPanel();
        jScrollPane6 = new javax.swing.JScrollPane();
        productTree = new javax.swing.JTree();
        jButton2 = new javax.swing.JButton();
        pnlAddProductsFields = new javax.swing.JPanel();
        jLabel8 = new javax.swing.JLabel();
        jPanel19 = new javax.swing.JPanel();
        lblBrand16 = new javax.swing.JLabel();
        txtAddProductBrand = new javax.swing.JTextField();
        lblName16 = new javax.swing.JLabel();
        txtAddProductName = new javax.swing.JTextField();
        lblHsn16 = new javax.swing.JLabel();
        txtAddProductHsn = new javax.swing.JTextField();
        lblVariant16 = new javax.swing.JLabel();
        txtAddProductVariant = new javax.swing.JTextField();
        lblRate16 = new javax.swing.JLabel();
        txtAddProductRate = new javax.swing.JTextField();
        btnAddProductDone = new javax.swing.JButton();
        btnAddProductCancel = new javax.swing.JButton();
        jButton1 = new javax.swing.JButton();
        jScrollPane4 = new javax.swing.JScrollPane();
        tblAddProducts = new javax.swing.JTable();
        pnlEdit = new javax.swing.JPanel();
        pnlSummary = new javax.swing.JPanel();
        jPanel12 = new javax.swing.JPanel();
        pnlFilters = new javax.swing.JPanel();
        cmbParty1 = new javax.swing.JComboBox<>();
        cmbSupplier = new javax.swing.JComboBox<>();
        cmbLocation = new javax.swing.JComboBox<>();
        cmbRemark = new javax.swing.JComboBox<>();
        dtFrom = new com.toedter.calendar.JDateChooser();
        jLabel17 = new javax.swing.JLabel();
        jLabel18 = new javax.swing.JLabel();
        dtTo = new com.toedter.calendar.JDateChooser();
        cmbTransaction = new javax.swing.JComboBox<>();
        pnlData = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        table2 = new javax.swing.JTable();
        pnlSum = new javax.swing.JPanel();
        btnDelete1 = new javax.swing.JButton();
        btnEdit1 = new javax.swing.JButton();
        btnBack = new javax.swing.JButton();
        btnAddTransaction = new javax.swing.JButton();
        jPanel13 = new javax.swing.JPanel();
        jLabel19 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setMaximumSize(new java.awt.Dimension(1280, 720));
        setPreferredSize(new java.awt.Dimension(1280, 720));

        MainPane.setLayout(new java.awt.BorderLayout());

        lblPortfolio.setBackground(new java.awt.Color(255, 255, 255));
        lblPortfolio.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblPortfolio.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/profile.png"))); // NOI18N
        lblPortfolio.setText("Portfolio");
        lblPortfolio.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        lblPortfolio.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        lblPortfolio.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                lblPortfolioMouseClicked(evt);
            }
        });
        pnlMenu.add(lblPortfolio);

        lblBill.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblBill.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/BillGen.png"))); // NOI18N
        lblBill.setText("New Bill");
        lblBill.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        lblBill.setMaximumSize(new java.awt.Dimension(80, 80));
        lblBill.setMinimumSize(new java.awt.Dimension(80, 80));
        lblBill.setPreferredSize(new java.awt.Dimension(80, 80));
        lblBill.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        lblBill.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                lblBillMouseClicked(evt);
            }
        });
        pnlMenu.add(lblBill);

        lblAddProduct.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblAddProduct.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/AddProduct.png"))); // NOI18N
        lblAddProduct.setText("Add Product");
        lblAddProduct.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        lblAddProduct.setMaximumSize(new java.awt.Dimension(40, 40));
        lblAddProduct.setMinimumSize(new java.awt.Dimension(40, 40));
        lblAddProduct.setPreferredSize(new java.awt.Dimension(80, 80));
        lblAddProduct.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        lblAddProduct.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                lblAddProductMouseClicked(evt);
            }
        });
        pnlMenu.add(lblAddProduct);

        lblAddParty.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblAddParty.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/AddParty.png"))); // NOI18N
        lblAddParty.setText("Add Party");
        lblAddParty.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        lblAddParty.setMaximumSize(new java.awt.Dimension(40, 40));
        lblAddParty.setMinimumSize(new java.awt.Dimension(40, 40));
        lblAddParty.setPreferredSize(new java.awt.Dimension(80, 80));
        lblAddParty.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        lblAddParty.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                lblAddPartyMouseClicked(evt);
            }
        });
        pnlMenu.add(lblAddParty);

        lblEdit.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblEdit.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/Edit.png"))); // NOI18N
        lblEdit.setText("Edit Your Data");
        lblEdit.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        lblEdit.setMaximumSize(new java.awt.Dimension(40, 40));
        lblEdit.setMinimumSize(new java.awt.Dimension(40, 40));
        lblEdit.setPreferredSize(new java.awt.Dimension(80, 80));
        lblEdit.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        lblEdit.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                lblEditMouseClicked(evt);
            }
        });
        pnlMenu.add(lblEdit);

        lblSummary.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblSummary.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/summary.png"))); // NOI18N
        lblSummary.setText("Summary");
        lblSummary.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        lblSummary.setMaximumSize(new java.awt.Dimension(40, 40));
        lblSummary.setMinimumSize(new java.awt.Dimension(40, 40));
        lblSummary.setPreferredSize(new java.awt.Dimension(80, 80));
        lblSummary.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        lblSummary.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                lblSummaryMouseClicked(evt);
            }
        });
        pnlMenu.add(lblSummary);

        MainPane.add(pnlMenu, java.awt.BorderLayout.PAGE_START);

        ContentPane.setLayout(new javax.swing.OverlayLayout(ContentPane));

        pnlBill.setLayout(new java.awt.BorderLayout());

        pnlInputs.setLayout(new java.awt.BorderLayout());

        lblParty.setText("Party");
        InputLine1.add(lblParty);

        cmbBillParty.setPreferredSize(new java.awt.Dimension(200, 22));
        InputLine1.add(cmbBillParty);

        lblAddress.setText("Address");
        InputLine1.add(lblAddress);

        txtAddress.setEditable(false);
        txtAddress.setPreferredSize(new java.awt.Dimension(200, 22));
        InputLine1.add(txtAddress);

        lblState.setText("State");
        InputLine1.add(lblState);

        txtState.setEditable(false);
        txtState.setPreferredSize(new java.awt.Dimension(200, 22));
        InputLine1.add(txtState);

        lblGst.setText("GSTIN");
        InputLine1.add(lblGst);

        txtGst.setEditable(false);
        txtGst.setPreferredSize(new java.awt.Dimension(200, 22));
        InputLine1.add(txtGst);

        jLabel6.setText("Supplier");
        jLabel6.setPreferredSize(new java.awt.Dimension(50, 16));
        InputLine1.add(jLabel6);

        txtSupplier.setPreferredSize(new java.awt.Dimension(200, 22));
        InputLine1.add(txtSupplier);

        pnlInputs.add(InputLine1, java.awt.BorderLayout.PAGE_START);

        lblInvoice.setText("Invoice No.");
        InputLine2.add(lblInvoice);

        txtInvoice.setPreferredSize(new java.awt.Dimension(200, 22));
        InputLine2.add(txtInvoice);

        lblInvoiceDate.setText("Invoice Date");
        lblInvoiceDate.setPreferredSize(new java.awt.Dimension(70, 16));
        InputLine2.add(lblInvoiceDate);

        dataChooser.setPreferredSize(new java.awt.Dimension(200, 22));
        InputLine2.add(dataChooser);

        lblVehicleNo.setText("Vehicle No.");
        InputLine2.add(lblVehicleNo);

        txtVehicle.setPreferredSize(new java.awt.Dimension(200, 22));
        InputLine2.add(txtVehicle);

        lblRemarks.setText("Remarks");
        InputLine2.add(lblRemarks);

        txtRemarks.setPreferredSize(new java.awt.Dimension(200, 22));
        InputLine2.add(txtRemarks);

        pnlInputs.add(InputLine2, java.awt.BorderLayout.CENTER);

        pnlBill.add(pnlInputs, java.awt.BorderLayout.PAGE_START);

        billTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "S.No.", "Product Description", "HSN Code", "Quantity", "Rate", "Total"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, true, false, true, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        billTable.getTableHeader().setReorderingAllowed(false);
        jScrollPane1.setViewportView(billTable);
        if (billTable.getColumnModel().getColumnCount() > 0) {
            billTable.getColumnModel().getColumn(0).setMinWidth(63);
            billTable.getColumnModel().getColumn(0).setPreferredWidth(63);
            billTable.getColumnModel().getColumn(0).setMaxWidth(63);
            billTable.getColumnModel().getColumn(2).setMinWidth(150);
            billTable.getColumnModel().getColumn(2).setPreferredWidth(150);
            billTable.getColumnModel().getColumn(2).setMaxWidth(150);
            billTable.getColumnModel().getColumn(3).setMinWidth(80);
            billTable.getColumnModel().getColumn(3).setPreferredWidth(80);
            billTable.getColumnModel().getColumn(3).setMaxWidth(80);
            billTable.getColumnModel().getColumn(4).setMinWidth(126);
            billTable.getColumnModel().getColumn(4).setPreferredWidth(126);
            billTable.getColumnModel().getColumn(4).setMaxWidth(126);
            billTable.getColumnModel().getColumn(5).setMinWidth(150);
            billTable.getColumnModel().getColumn(5).setPreferredWidth(150);
            billTable.getColumnModel().getColumn(5).setMaxWidth(150);
        }

        javax.swing.GroupLayout BillDataLayout = new javax.swing.GroupLayout(BillData);
        BillData.setLayout(BillDataLayout);
        BillDataLayout.setHorizontalGroup(
            BillDataLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(BillDataLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1)
                .addContainerGap())
        );
        BillDataLayout.setVerticalGroup(
            BillDataLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1)
        );

        pnlBill.add(BillData, java.awt.BorderLayout.CENTER);

        pnlButtons.setLayout(new java.awt.BorderLayout());

        jPanel1.setLayout(new java.awt.GridLayout(1, 0));

        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/AddItem.png"))); // NOI18N
        jLabel1.setText("Add Item");
        jLabel1.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jLabel1.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jPanel1.add(jLabel1);

        jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/RemoveItem.png"))); // NOI18N
        jLabel2.setText("Remove Item");
        jLabel2.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jLabel2.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jPanel1.add(jLabel2);

        jLabel3.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/Cancel.png"))); // NOI18N
        jLabel3.setText("Cancel");
        jLabel3.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jLabel3.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jPanel1.add(jLabel3);

        jLabel4.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/Save.png"))); // NOI18N
        jLabel4.setText("Save");
        jLabel4.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jLabel4.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jPanel1.add(jLabel4);

        pnlButtons.add(jPanel1, java.awt.BorderLayout.CENTER);

        lblTotal.setText("Total: ");
        lblTotal.setPreferredSize(new java.awt.Dimension(32, 22));
        jPanel2.add(lblTotal);

        txtTotal.setEditable(false);
        txtTotal.setText("0.0");
        txtTotal.setPreferredSize(new java.awt.Dimension(200, 22));
        jPanel2.add(txtTotal);

        lblTax.setText("Tax:");
        jPanel2.add(lblTax);

        txtTax.setEditable(false);
        txtTax.setText("0.0");
        txtTax.setPreferredSize(new java.awt.Dimension(200, 22));
        jPanel2.add(txtTax);

        lblGTotal.setText("Total + Tax: ");
        jPanel2.add(lblGTotal);

        txtGTotal.setEditable(false);
        txtGTotal.setText("0.0");
        txtGTotal.setPreferredSize(new java.awt.Dimension(200, 22));
        jPanel2.add(txtGTotal);

        pnlButtons.add(jPanel2, java.awt.BorderLayout.PAGE_START);

        pnlBill.add(pnlButtons, java.awt.BorderLayout.PAGE_END);

        ContentPane.add(pnlBill);

        pnlAddParty.setPreferredSize(new java.awt.Dimension(1232, 548));
        pnlAddParty.setLayout(new java.awt.BorderLayout());

        jLabel5.setFont(new java.awt.Font("Impact", 0, 36)); // NOI18N
        jLabel5.setText("Customize Parties");
        jLabel5.setPreferredSize(new java.awt.Dimension(265, 70));
        jPanel21.add(jLabel5);

        pnlAddParty.add(jPanel21, java.awt.BorderLayout.PAGE_START);

        btnDelete.setText("Delete");
        btnDelete.setEnabled(false);
        btnDelete.setFocusPainted(false);

        btnEdit.setText("Edit");
        btnEdit.setEnabled(false);
        btnEdit.setFocusPainted(false);

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(btnDelete)
                .addGap(206, 206, 206)
                .addComponent(btnEdit)
                .addContainerGap(907, Short.MAX_VALUE))
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnDelete)
                    .addComponent(btnEdit))
                .addContainerGap(56, Short.MAX_VALUE))
        );

        pnlAddParty.add(jPanel6, java.awt.BorderLayout.PAGE_END);

        jPanel7.setLayout(new java.awt.BorderLayout());

        jPanel8.setPreferredSize(new java.awt.Dimension(1268, 80));
        jPanel8.setLayout(new java.awt.BorderLayout());

        jPanel10.setPreferredSize(new java.awt.Dimension(1268, 48));

        jLabel12.setText("Address");
        jLabel12.setPreferredSize(new java.awt.Dimension(90, 22));
        jPanel10.add(jLabel12);

        jTextField3.setPreferredSize(new java.awt.Dimension(265, 22));
        jPanel10.add(jTextField3);

        jLabel13.setText("Supplier");
        jLabel13.setPreferredSize(new java.awt.Dimension(90, 22));
        jPanel10.add(jLabel13);

        jComboBox2.setEditable(true);
        jComboBox2.setPreferredSize(new java.awt.Dimension(265, 22));
        jPanel10.add(jComboBox2);

        jLabel14.setText("Opening Balance");
        jLabel14.setPreferredSize(new java.awt.Dimension(90, 22));
        jPanel10.add(jLabel14);

        jTextField4.setPreferredSize(new java.awt.Dimension(265, 22));
        jPanel10.add(jTextField4);

        btnAdd.setText("Add");
        jPanel10.add(btnAdd);

        jPanel8.add(jPanel10, java.awt.BorderLayout.CENTER);

        jLabel9.setText("Party");
        jLabel9.setPreferredSize(new java.awt.Dimension(90, 22));
        jPanel11.add(jLabel9);

        jTextField1.setPreferredSize(new java.awt.Dimension(265, 22));
        jPanel11.add(jTextField1);

        jLabel11.setText("State");
        jLabel11.setPreferredSize(new java.awt.Dimension(90, 22));
        jPanel11.add(jLabel11);

        jComboBox1.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "1. JAMMU AND KASHMIR", "2. HIMACHAL PRADESH", "3. PUNJAB", "4. CHANDIGARH", "5. UTTARAKHAND", "6. HARYANA", "7. DELHI", "8. RAJASTHAN", "9. UTTAR PRADESH", "10. BIHAR", "11. SIKKIM", "12. ARUNACHAL PRADESH", "13. NAGALAND", "14. MANIPUR", "15. MIZORAM", "16. TRIPURA", "17. MEGHALAYA", "18. ASSAM", "19. WEST BENGAL", "20. JHARKHAND", "21. ORISSA", "22. CHHATTISGARH", "23. MADHYA PRADESH", "24. GUJARAT", "25. DAMAN AND DIU", "26. DADAR AND NAGAR HAVELI", "27. MAHARASTRA", "29. KARNATAKA", "30. GOA", "31. LAKSHADWEEP", "32. KERALA", "33. TAMIL NADU", "34. PUDUCHERRY", "35. ANDAMAN AND NICOBAR", "36. TELANGANA", "37. ANDHRA PRADESH", "97. OTHER TERRITORY", "96. OTHER COUNTRY" }));
        jComboBox1.setPreferredSize(new java.awt.Dimension(265, 22));
        jPanel11.add(jComboBox1);

        jLabel10.setText("GSTIN");
        jLabel10.setPreferredSize(new java.awt.Dimension(90, 22));
        jPanel11.add(jLabel10);

        jTextField2.setPreferredSize(new java.awt.Dimension(265, 22));
        jPanel11.add(jTextField2);

        jPanel8.add(jPanel11, java.awt.BorderLayout.PAGE_START);

        jPanel7.add(jPanel8, java.awt.BorderLayout.PAGE_START);

        table1.setModel(new javax.swing.table.DefaultTableModel(
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
        table1.getTableHeader().setReorderingAllowed(false);
        jScrollPane3.setViewportView(table1);
        if (table1.getColumnModel().getColumnCount() > 0) {
            table1.getColumnModel().getColumn(0).setMinWidth(60);
            table1.getColumnModel().getColumn(0).setPreferredWidth(60);
            table1.getColumnModel().getColumn(0).setMaxWidth(60);
            table1.getColumnModel().getColumn(1).setMinWidth(200);
            table1.getColumnModel().getColumn(1).setPreferredWidth(200);
            table1.getColumnModel().getColumn(1).setMaxWidth(200);
            table1.getColumnModel().getColumn(3).setMinWidth(180);
            table1.getColumnModel().getColumn(3).setPreferredWidth(180);
            table1.getColumnModel().getColumn(3).setMaxWidth(180);
            table1.getColumnModel().getColumn(4).setMinWidth(150);
            table1.getColumnModel().getColumn(4).setPreferredWidth(150);
            table1.getColumnModel().getColumn(4).setMaxWidth(150);
            table1.getColumnModel().getColumn(5).setMinWidth(180);
            table1.getColumnModel().getColumn(5).setPreferredWidth(180);
            table1.getColumnModel().getColumn(5).setMaxWidth(180);
            table1.getColumnModel().getColumn(6).setMinWidth(150);
            table1.getColumnModel().getColumn(6).setPreferredWidth(150);
            table1.getColumnModel().getColumn(6).setMaxWidth(150);
        }

        javax.swing.GroupLayout jPanel9Layout = new javax.swing.GroupLayout(jPanel9);
        jPanel9.setLayout(jPanel9Layout);
        jPanel9Layout.setHorizontalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel9Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane3)
                .addContainerGap())
        );
        jPanel9Layout.setVerticalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel9Layout.createSequentialGroup()
                .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 298, Short.MAX_VALUE)
                .addContainerGap())
        );

        jPanel7.add(jPanel9, java.awt.BorderLayout.CENTER);

        pnlAddParty.add(jPanel7, java.awt.BorderLayout.CENTER);

        ContentPane.setLayer(pnlAddParty, 1);
        ContentPane.add(pnlAddParty);

        pnlPortfolio.setPreferredSize(new java.awt.Dimension(1232, 548));

        javax.swing.GroupLayout pnlPortfolioLayout = new javax.swing.GroupLayout(pnlPortfolio);
        pnlPortfolio.setLayout(pnlPortfolioLayout);
        pnlPortfolioLayout.setHorizontalGroup(
            pnlPortfolioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );
        pnlPortfolioLayout.setVerticalGroup(
            pnlPortfolioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );

        ContentPane.setLayer(pnlPortfolio, 2);
        ContentPane.add(pnlPortfolio);

        pnlAddProduct.setPreferredSize(new java.awt.Dimension(1232, 548));
        pnlAddProduct.setLayout(new java.awt.BorderLayout());

        pnlAddProductsContents2.setLayout(new java.awt.GridLayout(1, 0));

        jPanel23.setLayout(new java.awt.BorderLayout());

        jLabel16.setFont(new java.awt.Font("Impact", 0, 24)); // NOI18N
        jLabel16.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel16.setText("All Products");
        jLabel16.setPreferredSize(new java.awt.Dimension(116, 50));
        jPanel23.add(jLabel16, java.awt.BorderLayout.PAGE_START);

        javax.swing.tree.DefaultMutableTreeNode treeNode1 = new javax.swing.tree.DefaultMutableTreeNode("root");
        productTree.setModel(new javax.swing.tree.DefaultTreeModel(treeNode1));
        productTree.setRootVisible(false);
        jScrollPane6.setViewportView(productTree);

        jButton2.setText("Delete");

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane6)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                        .addGap(0, 541, Short.MAX_VALUE)
                        .addComponent(jButton2)))
                .addContainerGap())
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane6, javax.swing.GroupLayout.DEFAULT_SIZE, 477, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton2)
                .addGap(12, 12, 12))
        );

        jPanel23.add(jPanel3, java.awt.BorderLayout.CENTER);

        pnlAddProductsContents2.add(jPanel23);

        pnlAddProductsFields.setLayout(new java.awt.BorderLayout());

        jLabel8.setFont(new java.awt.Font("Impact", 0, 24)); // NOI18N
        jLabel8.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel8.setText("Add New Product");
        jLabel8.setPreferredSize(new java.awt.Dimension(161, 50));
        pnlAddProductsFields.add(jLabel8, java.awt.BorderLayout.PAGE_START);

        lblBrand16.setFont(new java.awt.Font("Courier New", 1, 14)); // NOI18N
        lblBrand16.setText("Brand");
        lblBrand16.setPreferredSize(new java.awt.Dimension(150, 22));

        txtAddProductBrand.setPreferredSize(new java.awt.Dimension(400, 22));

        lblName16.setFont(new java.awt.Font("Courier New", 1, 14)); // NOI18N
        lblName16.setText("Name");
        lblName16.setPreferredSize(new java.awt.Dimension(150, 22));

        txtAddProductName.setPreferredSize(new java.awt.Dimension(400, 22));

        lblHsn16.setFont(new java.awt.Font("Courier New", 1, 14)); // NOI18N
        lblHsn16.setText("HSN Code");
        lblHsn16.setPreferredSize(new java.awt.Dimension(150, 22));

        txtAddProductHsn.setPreferredSize(new java.awt.Dimension(400, 22));

        lblVariant16.setFont(new java.awt.Font("Courier New", 1, 14)); // NOI18N
        lblVariant16.setText("Variant");
        lblVariant16.setPreferredSize(new java.awt.Dimension(150, 22));

        txtAddProductVariant.setPreferredSize(new java.awt.Dimension(400, 22));

        lblRate16.setFont(new java.awt.Font("Courier New", 1, 14)); // NOI18N
        lblRate16.setText("Rate");
        lblRate16.setPreferredSize(new java.awt.Dimension(150, 22));

        txtAddProductRate.setPreferredSize(new java.awt.Dimension(400, 22));

        btnAddProductDone.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/tick.png"))); // NOI18N
        btnAddProductDone.setText("Done");
        btnAddProductDone.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnAddProductDone.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);

        btnAddProductCancel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/Cancel.png"))); // NOI18N
        btnAddProductCancel.setText("Cancel");
        btnAddProductCancel.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnAddProductCancel.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);

        jButton1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/plus.png"))); // NOI18N
        jButton1.setText("Variant");
        jButton1.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButton1.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);

        tblAddProducts.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "S. No.", "Variant", "Rate"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane4.setViewportView(tblAddProducts);
        if (tblAddProducts.getColumnModel().getColumnCount() > 0) {
            tblAddProducts.getColumnModel().getColumn(0).setMinWidth(40);
            tblAddProducts.getColumnModel().getColumn(0).setPreferredWidth(40);
            tblAddProducts.getColumnModel().getColumn(0).setMaxWidth(40);
            tblAddProducts.getColumnModel().getColumn(2).setMinWidth(180);
            tblAddProducts.getColumnModel().getColumn(2).setPreferredWidth(180);
            tblAddProducts.getColumnModel().getColumn(2).setMaxWidth(180);
        }

        javax.swing.GroupLayout jPanel19Layout = new javax.swing.GroupLayout(jPanel19);
        jPanel19.setLayout(jPanel19Layout);
        jPanel19Layout.setHorizontalGroup(
            jPanel19Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel19Layout.createSequentialGroup()
                .addGap(43, 43, 43)
                .addGroup(jPanel19Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jScrollPane4)
                    .addGroup(jPanel19Layout.createSequentialGroup()
                        .addComponent(btnAddProductCancel)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 331, Short.MAX_VALUE)
                        .addComponent(jButton1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnAddProductDone))
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel19Layout.createSequentialGroup()
                        .addComponent(lblBrand16, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(5, 5, 5)
                        .addComponent(txtAddProductBrand, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel19Layout.createSequentialGroup()
                        .addComponent(lblName16, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(5, 5, 5)
                        .addComponent(txtAddProductName, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel19Layout.createSequentialGroup()
                        .addComponent(lblHsn16, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(5, 5, 5)
                        .addComponent(txtAddProductHsn, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel19Layout.createSequentialGroup()
                        .addComponent(lblVariant16, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(5, 5, 5)
                        .addComponent(txtAddProductVariant, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel19Layout.createSequentialGroup()
                        .addComponent(lblRate16, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(5, 5, 5)
                        .addComponent(txtAddProductRate, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)))
                .addGap(43, 43, 43))
        );
        jPanel19Layout.setVerticalGroup(
            jPanel19Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel19Layout.createSequentialGroup()
                .addGap(5, 5, 5)
                .addGroup(jPanel19Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblBrand16, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtAddProductBrand, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel19Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblName16, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtAddProductName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel19Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblHsn16, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtAddProductHsn, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel19Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblVariant16, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtAddProductVariant, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel19Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblRate16, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtAddProductRate, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel19Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btnAddProductDone)
                    .addComponent(btnAddProductCancel)
                    .addComponent(jButton1))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane4, javax.swing.GroupLayout.DEFAULT_SIZE, 250, Short.MAX_VALUE)
                .addContainerGap())
        );

        pnlAddProductsFields.add(jPanel19, java.awt.BorderLayout.CENTER);

        pnlAddProductsContents2.add(pnlAddProductsFields);

        pnlAddProduct.add(pnlAddProductsContents2, java.awt.BorderLayout.CENTER);

        ContentPane.setLayer(pnlAddProduct, 3);
        ContentPane.add(pnlAddProduct);

        pnlEdit.setPreferredSize(new java.awt.Dimension(1232, 548));

        javax.swing.GroupLayout pnlEditLayout = new javax.swing.GroupLayout(pnlEdit);
        pnlEdit.setLayout(pnlEditLayout);
        pnlEditLayout.setHorizontalGroup(
            pnlEditLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );
        pnlEditLayout.setVerticalGroup(
            pnlEditLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );

        ContentPane.setLayer(pnlEdit, 4);
        ContentPane.add(pnlEdit);

        pnlSummary.setPreferredSize(new java.awt.Dimension(1232, 548));

        cmbParty1.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Party" }));
        cmbParty1.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                cmbParty1ItemStateChanged(evt);
            }
        });

        cmbSupplier.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Supplier" }));
        cmbSupplier.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                cmbSupplierItemStateChanged(evt);
            }
        });

        cmbLocation.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Location" }));
        cmbLocation.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                cmbLocationItemStateChanged(evt);
            }
        });

        cmbRemark.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Remarks" }));
        cmbRemark.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                cmbRemarkItemStateChanged(evt);
            }
        });

        jLabel17.setText("From");

        jLabel18.setText("To");

        cmbTransaction.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "All Transactions", "Purchased", "Paid_to_DBC", "Sale", "Received" }));
        cmbTransaction.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                cmbTransactionItemStateChanged(evt);
            }
        });

        javax.swing.GroupLayout pnlFiltersLayout = new javax.swing.GroupLayout(pnlFilters);
        pnlFilters.setLayout(pnlFiltersLayout);
        pnlFiltersLayout.setHorizontalGroup(
            pnlFiltersLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlFiltersLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pnlFiltersLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(pnlFiltersLayout.createSequentialGroup()
                        .addComponent(jLabel17)
                        .addGap(16, 16, 16)
                        .addComponent(dtFrom, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addComponent(cmbParty1, 0, 296, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pnlFiltersLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pnlFiltersLayout.createSequentialGroup()
                        .addComponent(jLabel18)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(dtTo, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addComponent(cmbLocation, 0, 296, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pnlFiltersLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(cmbTransaction, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(cmbSupplier, 0, 293, Short.MAX_VALUE))
                .addGap(8, 8, 8)
                .addComponent(cmbRemark, 0, 291, Short.MAX_VALUE)
                .addContainerGap())
        );
        pnlFiltersLayout.setVerticalGroup(
            pnlFiltersLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlFiltersLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pnlFiltersLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(cmbParty1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cmbSupplier, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cmbLocation, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cmbRemark, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 8, Short.MAX_VALUE)
                .addGroup(pnlFiltersLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(dtFrom, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel17, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel18, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(dtTo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cmbTransaction, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
        );

        table2.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "S.No.", "Invoice No.", "Date", "Party", "Address", "Supplier", "Purchased", "Paid_to_DBC", "Sale", "Received", "Remark"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        table2.getTableHeader().setReorderingAllowed(false);
        jScrollPane2.setViewportView(table2);

        javax.swing.GroupLayout pnlDataLayout = new javax.swing.GroupLayout(pnlData);
        pnlData.setLayout(pnlDataLayout);
        pnlDataLayout.setHorizontalGroup(
            pnlDataLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane2, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 1208, Short.MAX_VALUE)
        );
        pnlDataLayout.setVerticalGroup(
            pnlDataLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 317, Short.MAX_VALUE)
        );

        btnDelete1.setText("Delete");
        btnDelete1.setEnabled(false);
        btnDelete1.setFocusPainted(false);
        btnDelete1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDelete1ActionPerformed(evt);
            }
        });

        btnEdit1.setText("Edit");
        btnEdit1.setEnabled(false);
        btnEdit1.setFocusPainted(false);
        btnEdit1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEdit1ActionPerformed(evt);
            }
        });

        btnBack.setText("Back");
        btnBack.setFocusPainted(false);
        btnBack.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBackActionPerformed(evt);
            }
        });

        btnAddTransaction.setText("Add");
        btnAddTransaction.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAddTransactionActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout pnlSumLayout = new javax.swing.GroupLayout(pnlSum);
        pnlSum.setLayout(pnlSumLayout);
        pnlSumLayout.setHorizontalGroup(
            pnlSumLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlSumLayout.createSequentialGroup()
                .addComponent(btnBack)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(btnAddTransaction)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnEdit1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnDelete1)
                .addContainerGap())
        );
        pnlSumLayout.setVerticalGroup(
            pnlSumLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlSumLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pnlSumLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnDelete1)
                    .addComponent(btnBack)
                    .addComponent(btnEdit1)
                    .addComponent(btnAddTransaction))
                .addContainerGap(54, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanel12Layout = new javax.swing.GroupLayout(jPanel12);
        jPanel12.setLayout(jPanel12Layout);
        jPanel12Layout.setHorizontalGroup(
            jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel12Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(pnlFilters, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(pnlData, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(pnlSum, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel12Layout.setVerticalGroup(
            jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel12Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(pnlFilters, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(pnlData, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(pnlSum, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        jLabel19.setFont(new java.awt.Font("Impact", 0, 36)); // NOI18N
        jLabel19.setText("Summary");
        jPanel13.add(jLabel19);

        javax.swing.GroupLayout pnlSummaryLayout = new javax.swing.GroupLayout(pnlSummary);
        pnlSummary.setLayout(pnlSummaryLayout);
        pnlSummaryLayout.setHorizontalGroup(
            pnlSummaryLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlSummaryLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pnlSummaryLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel12, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel13, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        pnlSummaryLayout.setVerticalGroup(
            pnlSummaryLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnlSummaryLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel13, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel12, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        ContentPane.setLayer(pnlSummary, 5);
        ContentPane.add(pnlSummary);

        MainPane.add(ContentPane, java.awt.BorderLayout.CENTER);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(MainPane, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(MainPane, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void lblPortfolioMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblPortfolioMouseClicked
        changePanel(pnlPortfolio);
        lblPortfolio.setBackground(selected);
    }//GEN-LAST:event_lblPortfolioMouseClicked

    private void lblBillMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblBillMouseClicked
        changePanel(pnlBill);
        lblBill.setBackground(selected);
    }//GEN-LAST:event_lblBillMouseClicked

    private void lblAddProductMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblAddProductMouseClicked
        changePanel(pnlAddProduct);
        lblAddProduct.setBackground(selected);
    }//GEN-LAST:event_lblAddProductMouseClicked

    private void lblAddPartyMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblAddPartyMouseClicked
        changePanel(pnlAddParty);
        lblAddParty.setBackground(selected);
    }//GEN-LAST:event_lblAddPartyMouseClicked

    private void lblEditMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblEditMouseClicked
        changePanel(pnlEdit);
        lblEdit.setBackground(selected);
    }//GEN-LAST:event_lblEditMouseClicked

    private void lblSummaryMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblSummaryMouseClicked
        changePanel(pnlSummary);
        lblSummary.setBackground(selected);
    }//GEN-LAST:event_lblSummaryMouseClicked

    private void cmbParty1ItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cmbParty1ItemStateChanged
        if (evt.getStateChange()!= ItemEvent.SELECTED)return;
        setFilteredTableData();
    }//GEN-LAST:event_cmbParty1ItemStateChanged

    private void cmbSupplierItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cmbSupplierItemStateChanged
        cmbPartyItemStateChanged(evt);
    }//GEN-LAST:event_cmbSupplierItemStateChanged

    private void cmbLocationItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cmbLocationItemStateChanged
        cmbPartyItemStateChanged(evt);
    }//GEN-LAST:event_cmbLocationItemStateChanged

    private void cmbRemarkItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cmbRemarkItemStateChanged
        cmbPartyItemStateChanged(evt);
    }//GEN-LAST:event_cmbRemarkItemStateChanged

    private void cmbTransactionItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cmbTransactionItemStateChanged
        if (evt.getStateChange()!=ItemEvent.SELECTED)return;
        setFilteredTableData();
    }//GEN-LAST:event_cmbTransactionItemStateChanged

    private void btnDelete1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDelete1ActionPerformed
        manager.run("DELETE FROM Transactions WHERE InvoiceNo = '"+billTable.getValueAt(billTable.getSelectedRow(),1)+"'AND Date = '"
            +Date.valueOf(String.valueOf(billTable.getValueAt(billTable.getSelectedRow(), 2)))+"' AND Purchased = "+
            Float.parseFloat(String.valueOf(billTable.getValueAt(billTable.getSelectedRow(), 6)))+" AND Paid_to_DBC = "+
            Float.parseFloat(String.valueOf(billTable.getValueAt(billTable.getSelectedRow(), 7)))+" AND Sale = "+
            Float.parseFloat(String.valueOf(billTable.getValueAt(billTable.getSelectedRow(), 8)))+" AND Received = "+
            Float.parseFloat(String.valueOf(billTable.getValueAt(billTable.getSelectedRow(), 9))));
        if (billTable.getValueAt(billTable.getSelectedRow(), 1)!=" "){
            manager.run("DELETE FROM Bills WHERE InvoiceNo = '"+billTable.getValueAt(billTable.getSelectedRow(), 1)+"'");
        }
        ((DefaultTableModel)billTable.getModel()).removeRow(billTable.getSelectedRow());
    }//GEN-LAST:event_btnDelete1ActionPerformed

    private void btnEdit1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEdit1ActionPerformed
        ResultSet set = manager.get("SELECT * FROM Bills WHERE InvoiceNo = '"+billTable.getValueAt(billTable.getSelectedRow(), 1)+"'");
        try {
            if (!set.next()){
                JOptionPane.showMessageDialog(this,"This bill doesn't exist, or was generated by other party"
                    , "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            String serialized = set.getString(2);
            Bill b = new Bill();
            b.setVisible(true);
            b.setBill(new BillData(serialized));
            formWindowClosing(null);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }//GEN-LAST:event_btnEdit1ActionPerformed

    private void btnBackActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBackActionPerformed
        Main.frames.get("home").setVisible(true);
        Toolkit.getDefaultToolkit().removeAWTEventListener(clickListener);
        this.dispose();
    }//GEN-LAST:event_btnBackActionPerformed

    private void btnAddTransactionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAddTransactionActionPerformed
        AddTransaction dialogue = new AddTransaction(this, true);
        dialogue.addWindowListener(new WindowListener() {
            @Override
            public void windowOpened(WindowEvent e) {

            }

            @Override
            public void windowClosing(WindowEvent e) {
            }

            @Override
            public void windowClosed(WindowEvent e) {
                if (dialogue.party==null)return;
                String address,supplier;
                ResultSet set = manager.get("SELECT * FROM Parties WHERE Name = '"+dialogue.party+"'");
                try {
                    if (!set.next())return;
                    address = set.getString(2);
                    supplier = set.getString(4);
                } catch (SQLException ex) {
                    throw new RuntimeException(ex);
                }
                manager.addTransactionEntry(dialogue.date, dialogue.party,address,supplier,
                    DataManager.EntryType.valueOf(dialogue.transaction.toUpperCase()),dialogue.value,dialogue.remark,"-");
                setFilteredTableData();
            }

            @Override
            public void windowIconified(WindowEvent e) {

            }

            @Override
            public void windowDeiconified(WindowEvent e) {

            }

            @Override
            public void windowActivated(WindowEvent e) {

            }

            @Override
            public void windowDeactivated(WindowEvent e) {

            }
        });
        dialogue.setVisible(true);
    }//GEN-LAST:event_btnAddTransactionActionPerformed

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
            java.util.logging.Logger.getLogger(Home.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Home.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Home.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Home.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Home().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel BillData;
    private javax.swing.JLayeredPane ContentPane;
    private javax.swing.JPanel InputLine1;
    private javax.swing.JPanel InputLine2;
    private javax.swing.JPanel MainPane;
    private javax.swing.JTable billTable;
    private javax.swing.JButton btnAdd;
    private javax.swing.JButton btnAddProductCancel;
    private javax.swing.JButton btnAddProductDone;
    private javax.swing.JButton btnAddTransaction;
    private javax.swing.JButton btnBack;
    private javax.swing.JButton btnDelete;
    private javax.swing.JButton btnDelete1;
    private javax.swing.JButton btnEdit;
    private javax.swing.JButton btnEdit1;
    private javax.swing.JComboBox<String> cmbBillParty;
    private javax.swing.JComboBox<String> cmbLocation;
    private javax.swing.JComboBox<String> cmbParty1;
    private javax.swing.JComboBox<String> cmbRemark;
    private javax.swing.JComboBox<String> cmbSupplier;
    private javax.swing.JComboBox<String> cmbTransaction;
    private com.toedter.calendar.JDateChooser dataChooser;
    private com.toedter.calendar.JDateChooser dtFrom;
    private com.toedter.calendar.JDateChooser dtTo;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JComboBox<String> jComboBox1;
    private javax.swing.JComboBox<String> jComboBox2;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel10;
    private javax.swing.JPanel jPanel11;
    private javax.swing.JPanel jPanel12;
    private javax.swing.JPanel jPanel13;
    private javax.swing.JPanel jPanel19;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel21;
    private javax.swing.JPanel jPanel23;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JScrollPane jScrollPane6;
    private javax.swing.JTextField jTextField1;
    private javax.swing.JTextField jTextField2;
    private javax.swing.JTextField jTextField3;
    private javax.swing.JTextField jTextField4;
    private javax.swing.JLabel lblAddParty;
    private javax.swing.JLabel lblAddProduct;
    private javax.swing.JLabel lblAddress;
    private javax.swing.JLabel lblBill;
    private javax.swing.JLabel lblBrand16;
    private javax.swing.JLabel lblEdit;
    private javax.swing.JLabel lblGTotal;
    private javax.swing.JLabel lblGst;
    private javax.swing.JLabel lblHsn16;
    private javax.swing.JLabel lblInvoice;
    private javax.swing.JLabel lblInvoiceDate;
    private javax.swing.JLabel lblName16;
    private javax.swing.JLabel lblParty;
    private javax.swing.JLabel lblPortfolio;
    private javax.swing.JLabel lblRate16;
    private javax.swing.JLabel lblRemarks;
    private javax.swing.JLabel lblState;
    private javax.swing.JLabel lblSummary;
    private javax.swing.JLabel lblTax;
    private javax.swing.JLabel lblTotal;
    private javax.swing.JLabel lblVariant16;
    private javax.swing.JLabel lblVehicleNo;
    private javax.swing.JPanel pnlAddParty;
    private javax.swing.JPanel pnlAddProduct;
    private javax.swing.JPanel pnlAddProductsContents2;
    private javax.swing.JPanel pnlAddProductsFields;
    private javax.swing.JPanel pnlBill;
    private javax.swing.JPanel pnlButtons;
    private javax.swing.JPanel pnlData;
    private javax.swing.JPanel pnlEdit;
    private javax.swing.JPanel pnlFilters;
    private javax.swing.JPanel pnlInputs;
    private javax.swing.JPanel pnlMenu;
    private javax.swing.JPanel pnlPortfolio;
    private javax.swing.JPanel pnlSum;
    private javax.swing.JPanel pnlSummary;
    private javax.swing.JTree productTree;
    private javax.swing.JTable table1;
    private javax.swing.JTable table2;
    private javax.swing.JTable tblAddProducts;
    private javax.swing.JTextField txtAddProductBrand;
    private javax.swing.JTextField txtAddProductHsn;
    private javax.swing.JTextField txtAddProductName;
    private javax.swing.JTextField txtAddProductRate;
    private javax.swing.JTextField txtAddProductVariant;
    private javax.swing.JTextField txtAddress;
    private javax.swing.JTextField txtGTotal;
    private javax.swing.JTextField txtGst;
    private javax.swing.JTextField txtInvoice;
    private javax.swing.JTextField txtRemarks;
    private javax.swing.JTextField txtState;
    private javax.swing.JTextField txtSupplier;
    private javax.swing.JTextField txtTax;
    private javax.swing.JTextField txtTotal;
    private javax.swing.JTextField txtVehicle;
    // End of variables declaration//GEN-END:variables

    public void addMouseHoverEffect(JComponent component){
        component.setBackground(new Color(255, 255, 255, 0));
        component.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {

            }

            @Override
            public void mousePressed(MouseEvent e) {

            }

            @Override
            public void mouseReleased(MouseEvent e) {

            }

            @Override
            public void mouseEntered(MouseEvent e) {
                if (component.getBackground().equals(selected))return;
                component.setBackground(focused);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                if (component.getBackground().equals(selected))return;
                component.setBackground(new Color(255, 255, 255, 0));
            }
        });
    }

    public void setFilteredTableData(){
        StringBuilder command = new StringBuilder("SELECT * FROM Transactions");
        if (cmbBillParty.getSelectedIndex()!=0){
            command.append(" WHERE Party = '").append(cmbBillParty.getSelectedItem()).append("'");
        }
        if (cmbLocation.getSelectedIndex()!=0){
            addConditionSeparator(command);
            command.append(" Address = '").append(cmbLocation.getSelectedItem()).append("'");
        }
        if (cmbSupplier.getSelectedIndex()!=0){
            addConditionSeparator(command);
            command.append(" Supplier = '").append(cmbSupplier.getSelectedItem()).append("'");
        }
        if (cmbRemark.getSelectedIndex()!=0){
            addConditionSeparator(command);
            command.append(" Remark = '").append(cmbRemark.getSelectedItem()).append("'");
        }
        if (dtFrom.getDate()!=null){
            addConditionSeparator(command);
            command.append(" Date > '").append(Date.valueOf(LocalDate.ofInstant(dtFrom.getDate().toInstant(), ZoneId.systemDefault())))
                    .append("'");
        }
        if (dtTo.getDate()!=null){
            addConditionSeparator(command);
            command.append(" Date < '").append(Date.valueOf(LocalDate.ofInstant(dtTo.getDate().toInstant(), ZoneId.systemDefault())))
                    .append("'");
        }
        if (cmbTransaction.getSelectedIndex()!=0){
            addConditionSeparator(command);
            command.append(" ").append(cmbTransaction.getSelectedItem()).append(" > 0");
        }
        command.append(" ORDER BY Date ASC");
        setTableData(manager.get(command.toString()));
    }

    public void setTableData(ResultSet set){
        if (set==null)return;
        ((DefaultTableModel)billTable.getModel()).setRowCount(0);
        DefaultTableModel model = (DefaultTableModel) billTable.getModel();
        java.util.List<String> partyModel = new ArrayList<>();
        java.util.List<String> locModel = new ArrayList<>();
        java.util.List<String> supplierModel = new ArrayList<>();
        List<String> remarkModel = new ArrayList<>();
        String loc,supplier,remark;
        try {
            int i=1;
            while (set.next()){
                loc = set.getString(4);
                supplier = set.getString(5);
                remark = set.getString(10);
                if (starting){
                    if (!locModel.contains(loc))locModel.add(loc);
                    if (!supplierModel.contains(supplier))supplierModel.add(supplier);
                    if (!remarkModel.contains(remark))remarkModel.add(remark);
                }
                model.addRow(new Object[]{
                        i, set.getString(1),set.getDate(2),set.getString(3), loc, supplier,set.getFloat(6), set.getFloat(7),
                        set.getFloat(8), set.getFloat(9),remark
                });
                i++;
            }
            if (starting){
                set = manager.get("SELECT * FROM Parties");
                DefaultComboBoxModel<String> mod = (DefaultComboBoxModel<String>) cmbBillParty.getModel();
                while (set.next()){
                    mod.addElement(set.getString(1));
                }
                cmbBillParty.setModel(mod);
                set.getStatement().close();
                ((DefaultComboBoxModel<String>)cmbLocation.getModel()).addAll(locModel);
                ((DefaultComboBoxModel<String>)cmbSupplier.getModel()).addAll(supplierModel);
                ((DefaultComboBoxModel<String>)cmbRemark.getModel()).addAll(remarkModel);
            }
        }catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void addConditionSeparator(StringBuilder s){
        if (s.toString().contains("WHERE"))s.append(" AND");
        else s.append(" WHERE");
    }
}
