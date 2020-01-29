package sample;

import javafx.beans.property.*;
import javafx.fxml.FXML;

public class Product {
    @FXML
    private final IntegerProperty item_id;
    private final StringProperty item_name;
    private final IntegerProperty item_qty;
    private final FloatProperty item_price;

    public Product(Integer item_id, String item_name, Integer item_qty, Float item_price) {
        this.item_id = new SimpleIntegerProperty(item_id);
        this.item_name = new SimpleStringProperty(item_name);
        this.item_qty = new SimpleIntegerProperty(item_qty);
        this.item_price = new SimpleFloatProperty(item_price);
    }


    public int getItem_id() {
        return item_id.get();
    }

    public void setItem_id(Integer value) {
        item_id.set(value);
    }

    public String getItem_name() {
        return item_name.get();
    }

    public void setItem_name(String value) {
        item_name.set(value);
    }

    public int getItem_qty() {
        return item_qty.get();
    }

    public void setItem_qty(Integer value) {
        item_qty.set(value);
    }

    public float getItem_price() {
        return item_price.get();
    }

    public void setItem_price(Float value) {
        item_price.set(value);
    }
}
