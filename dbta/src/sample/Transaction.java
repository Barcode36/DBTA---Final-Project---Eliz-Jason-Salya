package sample;

import javafx.beans.property.*;

public class Transaction {
    private final IntegerProperty transaction_id;
    private final IntegerProperty staff_id;
    private final StringProperty dine_type;
    private final IntegerProperty table_no;
    private final IntegerProperty guest_qty;
    private final StringProperty pay_method;

    public Transaction(Integer transaction_id, Integer staff_id, String dine_type, Integer table_no, Integer guest_qty, String payment) {
        this.transaction_id = new SimpleIntegerProperty(transaction_id);
        this.staff_id = new SimpleIntegerProperty(staff_id);
        this.dine_type = new SimpleStringProperty(dine_type);
        this.table_no = new SimpleIntegerProperty(table_no);
        this.guest_qty = new SimpleIntegerProperty(guest_qty);
        this.pay_method = new SimpleStringProperty(payment);
    }

    public int getTransaction_id() {
        return transaction_id.get();
    }

    public void setTransaction_id(Integer value) {
        transaction_id.set(value);
    }

    public int getStaff_id() {
        return staff_id.get();
    }

    public void setStaff_id(Integer value) {
        staff_id.set(value);
    }

    public String getDine_type() {
        return dine_type.get();
    }

    public void setDine_type(String value) {
        dine_type.set(value);
    }

    public int getTable_no() {
        return table_no.get();
    }

    public void setTable_no(Integer value) {
        table_no.set(value);
    }

    public int getGuest_qty() {
        return guest_qty.get();
    }

    public void setGuest_qty(Integer value) {
        guest_qty.set(value);
    }

    public String getPay_method() {
        return pay_method.get();
    }

    public void setPay_method(String value) {
        pay_method.set(value);
    }
}