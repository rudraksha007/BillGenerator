package me.rudraksha007.billgenui;

public enum AppFrame {
    HOME("home"), NEW_BILL("new-bill"),BILL_DATA("bill-data"),ITEM("bill-item"),
    ADD_ITEM("add-product");
    String value;
    AppFrame(String value){
        this.value = value;
    }

    public String toString(){
        return value;
    }
}
