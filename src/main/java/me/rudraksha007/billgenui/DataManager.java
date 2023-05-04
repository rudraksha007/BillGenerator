package me.rudraksha007.billgenui;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DataManager {
    private String path;

    public Map<String,List<Item>> loadProductData(){
        File f = new File(System.getProperty("user.home")+File.separator+"AppData"+
                File.separator+"Local"+File.separator+"BillGenerator"+File.separator+"productList.data");
        if (!f.exists())return null;
        try {
            FileInputStream fos = new FileInputStream(f);
            ObjectInputStream in = new ObjectInputStream(fos);
            Map<String,List<String>>map = (Map<String, List<String>>) in.readObject();
            Map<String,List<Item>>items = new HashMap<>();
            for (Map.Entry<String,List<String>>entry: map.entrySet()){
                List<Item>list = new ArrayList<>();
                for (String s:entry.getValue()){
                    list.add(new Item(s));
                }
                items.put(entry.getKey(), list);
            }
            return items;
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void saveData(){
        File f = new File(System.getProperty("user.home")+File.separator+"AppData"+
                File.separator+"Local"+File.separator+"Bill Generator"+File.separator+"productList.data");
        Map<String, List<String>>map = new HashMap<>();
        for (Map.Entry<String,List<Item>> entry: Main.items.entrySet()){
            List<String>list = new ArrayList<>();
            for (Item i: entry.getValue()){
                list.add(i.toString());
            }
            map.put(entry.getKey(), list);
        }
        if (!f.exists()) {
            try {
                f = new File(System.getProperty("user.home")+File.separator+"AppData"+
                        File.separator+"Local"+File.separator+"BillGenerator");
                if (!f.exists())f.mkdir();
                f = new File(f.getAbsolutePath()+File.separator+"productList.data");
                if (!f.exists())f.createNewFile();
                System.out.println(f.getAbsolutePath());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        try {
            FileOutputStream fos = new FileOutputStream(f,false);
            ObjectOutputStream out = new ObjectOutputStream(fos);
            out.writeObject(map);
            out.flush();
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
