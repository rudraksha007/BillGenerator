package me.rudraksha007.billgenerator;

import me.rudraksha007.billgenerator.GUI.AddProduct;
import me.rudraksha007.billgenerator.GUI.BillData;
import me.rudraksha007.billgenerator.GUI.Home;
import me.rudraksha007.billgenerator.GUI.Summary;
import me.rudraksha007.billgenerator.utilities.DataManager;

import javax.swing.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Main {

    public static DataManager dataManager = new DataManager();
    public static Map<String, JFrame> frames = new HashMap<>();
    public static Map<String, Item>itemMap = dataManager.loadProductData();
    public static List<String>searchList = new ArrayList<>();
    public static List<String>Employees = (List<String>) dataManager.loadUserData("employeeData.data");
    public static UserData data;

    public static void main(String[] args) {
        data = (UserData) dataManager.loadUserData("userData.data");
        if (Employees==null)Employees = new ArrayList<>();
        dataManager.startServer();
        if (itemMap!=null){
            for (String s: itemMap.keySet()){
                searchList.add(s);
                for (String var: itemMap.get(s).getSizes().keySet()){
                    searchList.add(s+"/"+var);
                }
            }
        }else itemMap = new HashMap<>();
        SwingUtilities.invokeLater(() -> {
            frames.put("home", new Home(data!=null));
            frames.put("add-product", new AddProduct());
            frames.put("summary", new Summary());
            frames.get("home").setVisible(true);
        });
//        new Connections().initialize();
//        Connections.SheetId = "1suGFq_Gz7fHdOGSP-qY6M5Wzs-Wz7HWV4AMgFtF0VZQ";
//        new SheetsHelper().craftStructure();
    }
    public static void shutDown(){
        dataManager.shutDownServer();
        System.exit(0);
    }
}
