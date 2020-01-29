package sample;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.property.SimpleStringProperty;

public class TransactionDetail {
    private final IntegerProperty transaction_details_id;
    private final IntegerProperty transaction_id;
    private final StringProperty product_trans_name;
    private final IntegerProperty quantity;

    public TransactionDetail(Integer transaction_details_id, Integer transaction_id, String product_trans_name, Integer quantity) {
        this.transaction_details_id = new SimpleIntegerProperty(transaction_details_id);
        this.transaction_id = new SimpleIntegerProperty(transaction_id);
        this.product_trans_name = new SimpleStringProperty(product_trans_name);
        this.quantity = new SimpleIntegerProperty(quantity);
    }

    public int getTransaction_details_id() {
        return transaction_details_id.get();
    }

    public void setTransaction_details_id(Integer value) {
        transaction_details_id.set(value);
    }

    public int getTransaction_id() {
        return transaction_id.get();
    }

    public void setTransaction_id(Integer value) {
        transaction_id.set(value);
    }

    public String getProduct_trans_name() {
        return product_trans_name.get();
    }

    public void setProduct_trans_name(String value) {
        product_trans_name.set(value);

    }

    public int getQuantity() {
        return quantity.get();
    }

    public void setQuantity(Integer value) {
        quantity.set(value);
    }
}
