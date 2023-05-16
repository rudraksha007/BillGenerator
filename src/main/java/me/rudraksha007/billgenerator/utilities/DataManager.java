package me.rudraksha007.billgenerator.utilities;

import me.rudraksha007.billgenerator.Item;
import me.rudraksha007.billgenerator.Main;

import java.io.*;
import java.sql.*;
import java.util.HashMap;
import java.util.Map;

public class DataManager {

    public static Connection con;
    private final String dbURL = "jdbc:derby:"+getDataFolder()+"Database";

    public enum EntryType{
        PURCHASED(5), PAID_TO_DBC(6),SALE(7),RECEIVED (8);
        final int index;
        EntryType(int i) {
            index = i;
        }
        public int getIndex(){
            return index;
        }
    }
    public void startServer(){
        try {
            Class.forName("org.apache.derby.jdbc.EmbeddedDriver").newInstance();
            con = DriverManager.getConnection(dbURL+";create=true;");
            System.out.println("Derby Database Started Successfully");
        } catch (SQLException | ClassNotFoundException | InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    public void shutDownServer(){
        try {
            if (con != null) {
                DriverManager.getConnection(dbURL + ";shutdown=true");
                con.close();
            }
        }
        catch (SQLException e) {
            if (e.getSQLState().equals("08006")) {
                System.out.println("Derby database shut down successfully.");
                return;
            }
            e.printStackTrace();
        }
    }
    public Map<String, Item> loadProductData(){
        File f = new File(getDataFolder()+"productList.data");
        if (!f.exists())return null;
        try {
            FileInputStream fos = new FileInputStream(f);
            ObjectInputStream in = new ObjectInputStream(fos);
            Map<String,String>map = (Map<String, String>) in.readObject();
            Map<String,Item>items = new HashMap<>();
            for (Map.Entry<String, String>entry: map.entrySet()){
                items.put(entry.getKey(), new Item(entry.getValue()));
            }
            return items;
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }
    public Object loadUserData(String path){
        File f = new File(getDataFolder()+path);
        if (!f.exists())return null;
        try {
            ObjectInputStream stream = new ObjectInputStream(new FileInputStream(f));
            return stream.readObject();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void saveData(){
        File f = new File(getDataFolder()+"productList.data");
        Map<String, String>map = new HashMap<>();
        for (Map.Entry<String,Item> entry: Main.itemMap.entrySet()){
            map.put(entry.getKey(), entry.getValue().toString());
        }
        if (!f.exists()) {
            try {
                f = new File(System.getProperty("user.home")+File.separator+"AppData"+
                        File.separator+"Local"+File.separator+"BillGenerator");
                if (!f.exists())if (!f.mkdir()) {
                    System.out.println("Error: Can't create new Folder for saving Data");
                    return;
                }
                f = new File(f.getAbsolutePath()+File.separator+"productList.data");
                if (!f.exists())if (!f.createNewFile()) {
                    System.out.println("Error: Can't create new File for saving Data");
                    return;
                }
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

    public void saveUserData(Object o, String path){
        try {
            File f = new File(getDataFolder()+path);
            if (!f.exists())if (!f.createNewFile()) {
                System.out.println("Error: Can't create new File for saving Data");
                return;
            }
            ObjectOutputStream stream = new ObjectOutputStream(new FileOutputStream(getDataFolder()+path,
                    false));
            stream.writeObject(o);
            stream.flush();
            stream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String getDataFolder(){

        return System.getProperty("user.home")+File.separator+"AppData"+
                File.separator+"Local"+File.separator+"BillGenerator"+File.separator;
    }

    public void addTransactionEntry(Date date, String party, String address, String supplier, EntryType type, float value,
                                    String remark){
        if (remark==null)remark=" ";
        try {
            Statement statement = con.createStatement();
            String sql = "INSERT INTO Transactions(Date, Party, Address, Supplier, Purchased, Paid_to_DBC, " +
                    "Sale, Received, Remark)" +
                    " VALUES (?,?,?,?,?,?,?,?,?)";
            PreparedStatement s = con.prepareStatement(sql);
            s.setDate(1, date);
            s.setString(2, party);
            s.setString(3, address);
            s.setString(4, supplier);
            for (int i = 5; i < 9; i++) {
                if (i == type.getIndex())s.setFloat(i, value);
                else s.setFloat(i,0);
            }
            s.setString(9, remark);
            s.executeUpdate();
            s.close();
        } catch (SQLException e) {
            if (e.getSQLState().equalsIgnoreCase("42X05")){
                createTable("Transactions", "Date DATE","Party VARCHAR(50)","Address VARCHAR(50)","Supplier VARCHAR(50)",
                        "Purchased DECIMAL(11,2)", "Paid_to_DBC DECIMAL(11,2)","Sale DECIMAL(11,2)","Received DECIMAL(11,2)",
                        "Remark VARCHAR(50)");
                return;
            }
            e.printStackTrace();
        }
    }
    public void createTable(String tableName, String... columns){
        try {
            Statement statement = con.createStatement();
            StringBuilder sql = new StringBuilder("CREATE TABLE " + tableName + " (");
            int i = 0;
            for (String s: columns) {
                sql.append(s);
                if (columns.length>i+1)sql.append(",");
                i++;
            }
            sql.append(")");
            statement.executeUpdate(sql.toString());
            statement.close();
        } catch (SQLException e) {
            if (e.getSQLState().equalsIgnoreCase("X0Y32"))return;
            e.printStackTrace();
        }
    }

    public ResultSet get(String command){
        try {
            Statement state = con.createStatement();
            ResultSet set = state.executeQuery(command);
            return set;
        } catch (SQLException e) {
            if (e.getSQLState().equalsIgnoreCase("42X05"))return null;
            e.printStackTrace();
        }
        return null;
    }

    public void run(String cmd){
        try {
            Statement state = con.createStatement();
            state.execute(cmd);
            state.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
