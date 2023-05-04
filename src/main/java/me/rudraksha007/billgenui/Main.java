/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package me.rudraksha007.billgenui;

import me.rudraksha007.billgenui.GUI.*;
import me.rudraksha007.billgenui.Google.Connections;

import javax.swing.*;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author rudra
 */
public class Main {

    public static Map<String, JFrame>frames = new HashMap<>();
    public static Map<String, List<Item>>items = new HashMap<>();
    public static List<Item>allItems = new ArrayList<>();

    public static void main(String[] args) {
        Map<String, List<Item>>items = new DataManager().loadProductData();
        if (items!=null){
            Main.items = items;
            for (Map.Entry<String, List<Item>>entry: items.entrySet()){
                allItems.addAll(entry.getValue());
            }
        }
        frames.put("home", new Home());
        frames.put("new-bill", new NewBill());
        frames.put("bill-data", new BillData());
        frames.put("bill-item", new BillItem());
        frames.put("add-product", new AddProduct());
        frames.get("home").setVisible(true);

//        new Connections().initialize();
//        Connections.SheetId = "1suGFq_Gz7fHdOGSP-qY6M5Wzs-Wz7HWV4AMgFtF0VZQ";
//        new SheetsHelper().craftStructure();
    }
}
