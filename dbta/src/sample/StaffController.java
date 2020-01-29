package sample;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import javax.swing.*;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class StaffController implements Initializable {

    @FXML
    public TextField staffId;
    @FXML
    private TextField staffName;
    @FXML
    private Button btnBack;
    @FXML
    private TableView<Staff> tableStaff;
    @FXML
    private TableColumn<Staff, Integer> staff_id;
    @FXML
    private TableColumn<Staff, String> staff_name;

    private ObservableList<Staff> data;
    private PreparedStatement preparedStatement;
    private Connector db;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        db = new Connector();
    }

    @FXML
    public void insertData() throws SQLException {
        String staff_Name = staffName.getText();

        String query = "INSERT INTO Staff (staff_name) VALUES (?)";
        Connection con = db.getConnection();

        preparedStatement = null;

        try {
            preparedStatement = con.prepareStatement(query);
            preparedStatement.setString(1, staff_Name);

//            if (preparedStatement.executeUpdate() > 0) {
//                JOptionPane.showMessageDialog(null, "New Staff Added");
//            }

        } catch (SQLException e) {
            System.out.println(e);

        } finally {
            staffName.clear();
            preparedStatement.executeUpdate();
            preparedStatement.close();
            refresh();
        }
    }

    @FXML
    private void deleteData() throws SQLException {
        String delete_id = staffId.getText();

        Connection con = db.getConnection();

        String query = "DELETE FROM Staff WHERE staff_id=" + delete_id;

        try {
            preparedStatement = con.prepareStatement(query);

            if (preparedStatement.executeUpdate() > 0) {
                JOptionPane.showMessageDialog(null, "Staff Data Deleted");
            }

        } catch (SQLException e) {
            System.out.println(e);
        } finally {
            staffId.clear();
            preparedStatement.execute();
            preparedStatement.close();
            refresh();
        }
    }

    public void refresh() {
        try {
            Connection con = db.getConnection();
            data = FXCollections.observableArrayList();
            ResultSet rs = con.createStatement().executeQuery("SELECT * FROM Staff");
            while (rs.next()) {
                data.add(new Staff(rs.getInt("staff_id"), rs.getString("staff_name")));
            }
            staff_id.setCellValueFactory(new PropertyValueFactory<>("staff_id"));
            staff_name.setCellValueFactory(new PropertyValueFactory<>("staff_name"));

            tableStaff.setItems(data);
        } catch (SQLException ex) {
            System.out.println("Invalid");
            System.err.println("Error" + ex);
        }
    }

    @FXML
    private void handleButtonAction(ActionEvent event) {
        refresh();
    }

    @FXML
    private void updateData() throws SQLException {
        try {
            String staff_id_ = staffId.getText();
            String staff_name_ = staffName.getText();

            int staff_id_int;
            String staff_name_string;

            if (!staff_id_.equals("")) {
                Connection con = db.getConnection();
                if (staff_name_.equals("")) {
                    String query = "SELECT staff_name FROM Staff WHERE staff_id=" + staff_id_;
                    preparedStatement = con.prepareStatement(query);
                    ResultSet rs = preparedStatement.executeQuery();
                    rs.next();
                    staff_name_string = rs.getString("staff_name");
                    rs.close();
                } else {
                    staff_name_string = staff_name_;
                }
                String query = "UPDATE Staff SET staff_name=? " + "WHERE staff_id=?";
                staff_id_int = Integer.parseInt(staff_id_);

                preparedStatement = con.prepareStatement(query);
                preparedStatement.setString(1, staff_name_string);
                preparedStatement.setInt(2, staff_id_int);

//                if (preparedStatement.executeUpdate() > 0) {
//                    JOptionPane.showMessageDialog(null, "Staff Data Updated");
//                }
//                preparedStatement.execute();
//                preparedStatement.close();
//                refresh();

            } else {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Something is missing!");
                alert.setContentText("Check your syntax!");

                alert.showAndWait();
            }
            } catch (SQLException e) {
                System.out.println(e);
            }
           finally {
                staffId.clear();
                staffName.clear();
                preparedStatement.execute();
                preparedStatement.close();
                refresh();
            }
    }

    @FXML
    public void backButtonAction(MouseEvent event) throws SQLException {
        if (event.getSource() == btnBack) {
            try {
                //add you loading or delays - ;-)
                Node node = (Node) event.getSource();
                Stage stage = (Stage) node.getScene().getWindow();
                //stage.setMaximized(true);
                stage.close();
                Scene scene = new Scene(FXMLLoader.load(getClass().getResource("/sample/Menu.fxml")));
                stage.setScene(scene);
                stage.show();

            } catch (IOException ex) {
                System.err.println(ex.getMessage());
                System.out.println("WTFFFF");

            }
        }
    }
}