package me.rudraksha007.billgenerator.utilities;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.Vector;

public class BillData implements Serializable {

    private String party;
    private String address;
    private String state;
    private String gstIn;
    private String invoice;
    private Date date;
    private String transport;
    private String vehicle;
    private Vector<Vector> tableData;
    private String remark;
    private String supplier;

    public BillData(String party, String address, String state, String gstIn, String invoice, Date date,
                    String transport, String vehicle, Vector<Vector> tableData, String remark, String supplier) {
        this.party = party;
        this.address = address;
        this.state = state;
        this.gstIn = gstIn;
        this.invoice = invoice;
        this.date = date;
        this.transport = transport;
        this.vehicle = vehicle;
        this.tableData = tableData;
        this.remark = remark;
        this.supplier = supplier;
    }

    public String getParty() {
        return party;
    }

    public void setParty(String party) {
        this.party = party;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getGstIn() {
        return gstIn;
    }

    public void setGstIn(String gstIn) {
        this.gstIn = gstIn;
    }

    public String getInvoice() {
        return invoice;
    }

    public void setInvoice(String invoice) {
        this.invoice = invoice;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getTransport() {
        return transport;
    }

    public void setTransport(String transport) {
        this.transport = transport;
    }

    public String getVehicle() {
        return vehicle;
    }

    public void setVehicle(String vehicle) {
        this.vehicle = vehicle;
    }

    public Vector<Vector> getTableData() {
        return tableData;
    }

    public void setTableData(Vector<Vector> tableData) {
        this.tableData = tableData;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getSupplier() {
        return supplier;
    }

    public void setSupplier(String supplier) {
        this.supplier = supplier;
    }

    public String getAsString(){
        StringBuilder s = new StringBuilder(party).append("&&%&&");
        SimpleDateFormat format = new SimpleDateFormat("DD-MM-YYYY");

        s.append(address).append("&&%&&").append(state).append("&&%&&").append(gstIn).append("&&%&&").append(invoice).append("&&%&&")
                .append(format.format(date)).append("&&%&&").append(transport).append("&&%&&").append(vehicle).append("&&%&&").append(remark)
                .append("&&%&&").append(supplier).append("%&%&");
        //%&%& a %%&& b %%&& c    &&%&&   d %%&& e %%&& f   &&%&&   g %%&& h %%&& i   &&%&&j,k,l&&%&&m,n,o
        System.out.println(tableData);
        int i = 0;
        for (Vector<String> vector: tableData){
            int j = 0;
            for (Object str: vector){
                s.append(str);
                if (vector.size()-1!=j)s.append("%%&&");
                j++;
            }
            if (vector.size()-1!=i)s.append("&&%&&");
            i++;
        }
        return s.toString();
    }

    public BillData(String serialized){
        String[] stringData = serialized.split("%&%&")[0].split("&&%&&");
        String tableData = serialized.split("%&%&")[1];

        this.party = stringData[0];
        this.address = stringData[1];
        this.state = stringData[2];
        this.gstIn = stringData[3];
        this.invoice = stringData[4];
        SimpleDateFormat form = new SimpleDateFormat("DD-MM-YYYY");
        try {
            this.date = form.parse(stringData[5]);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
        this.transport = stringData[6];
        this.vehicle = stringData[7];
        this.remark = stringData[8];
        this.supplier = stringData[9];

        Vector<Vector>vector = new Vector<>();
        for (String rows: tableData.split("&&%&&")){
            Vector<String> vec = new Vector<>(Arrays.asList(rows.split("%%&&")));
            vector.add(vec);
        }
        this.tableData = vector;
    }
}
