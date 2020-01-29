package sample;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.fxml.FXML;


public class Staff {
    @FXML
    private final IntegerProperty staff_id;
    private final StringProperty staff_name;

    public Staff(Integer staff_id, String staffName) {
        this.staff_id = new SimpleIntegerProperty(staff_id);
        this.staff_name = new SimpleStringProperty(staffName);
    }

    public int getStaff_id() {
        return staff_id.get();
    }

    public void setStaff_id(Integer value) {
        staff_id.set(value);
    }


    public String getStaff_name() {
        return staff_name.get();
    }

    public void setStaff_name(String value) {
        staff_name.set(value);
    }

}
