package me.rudraksha007.billgenerator;

import me.rudraksha007.billgenerator.utilities.DataManager;

import java.io.Serializable;

public class UserData implements Serializable {
    private static final long serialversionUID = 1L;

    private String companyName;
    private String address;
    private String state;
    private String phn;
    private String gstin;
    private String bankName;
    private String accNo;
    private String branch;
    private String ifsc;
    private float cgst;
    private float igst;
    private float sgst;

    public UserData(String companyName, String address, String state, String phn,
                    String gstin, String bankName, String accNo, String branch, String ifsc, float cgst, float sgst,
                    float igst) {
        this.companyName = companyName;
        this.address = address;
        this.state = state;
        this.phn = phn;
        this.gstin = gstin;
        this.bankName = bankName;
        this.accNo = accNo;
        this.branch = branch;
        this.ifsc = ifsc;
        this.cgst = cgst;
        this.sgst = sgst;
        this.igst = igst;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
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

    public String getPhn() {
        return phn;
    }

    public void setPhn(String phn) {
        this.phn = phn;
    }

    public String getGstin() {
        return gstin;
    }

    public void setGstin(String gstin) {
        this.gstin = gstin;
    }

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    public String getAccNo() {
        return accNo;
    }

    public void setAccNo(String accNo) {
        this.accNo = accNo;
    }

    public String getBranch() {
        return branch;
    }

    public void setBranch(String branch) {
        this.branch = branch;
    }

    public String getIfsc() {
        return ifsc;
    }

    public void setIfsc(String ifsc) {
        this.ifsc = ifsc;
    }

    public float getCgst() {
        return cgst;
    }

    public void setCgst(float cgst) {
        this.cgst = cgst;
    }

    public float getIgst() {
        return igst;
    }

    public void setIgst(float igst) {
        this.igst = igst;
    }

    public float getSgst() {
        return sgst;
    }

    public void setSgst(float sgst) {
        this.sgst = sgst;
    }
}
