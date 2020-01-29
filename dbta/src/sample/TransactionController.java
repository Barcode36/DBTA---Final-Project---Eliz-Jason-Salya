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
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class TransactionController implements Initializable {
    @FXML
    private TextField trans_id;
    @FXML
    private TextField st_id;
    @FXML
    private TextField table_n;
    @FXML
    private TextField guest;
    @FXML
    private TableView<Transaction> tableTransaction;
    @FXML
    private TableColumn<Transaction, Integer> transaction_id;
    @FXML
    private TableColumn<Transaction, Integer> staff_id;
    @FXML
    private TableColumn<Transaction, String> dine_type;
    @FXML
    private TableColumn<Transaction, Integer> table_no;
    @FXML
    private TableColumn<Transaction, Integer> guest_quantity;
    @FXML
    private TableColumn<Transaction, String> payment_method;

    @FXML
    private Button btnBack;

    private ObservableList<Transaction> data;
    private PreparedStatement preparedStatement;
    private Connector db;

    public ComboBox<String> comboBox;
    ObservableList<String> list = FXCollections.observableArrayList("dine-in", "take-away");

    public ComboBox<String> payBox;
    ObservableList<String> payList = FXCollections.observableArrayList("cash", "credit card", "debit card", "ovo", "gopay", "dana");

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        db = new Connector();
        comboBox.setItems(list);
        payBox.setItems(payList);
    }

    @FXML
    public void insertData() throws Exception {
        String staff_id = st_id.getText();
        String d_type = comboBox.getValue();
        String tab_no = table_n.getText();
        String guest_tot = guest.getText();
        String pay_met = payBox.getValue();

        int staff_id_int = Integer.parseInt(staff_id);
        int tab_no_int = Integer.parseInt(tab_no);
        int guest_t_int = Integer.parseInt(guest_tot);

        String query = "INSERT INTO Bill (dine_type, table_no, guest_qty, staff_id, payment_method) VALUES (?, ?, ?, ?, ?)";

        Connection con = db.getConnection();

        preparedStatement = null;

        try {
            preparedStatement = con.prepareStatement(query);
            preparedStatement.setString(1, d_type);
            preparedStatement.setInt(2, tab_no_int);
            preparedStatement.setInt(3, guest_t_int);
            preparedStatement.setInt(4, staff_id_int);
            preparedStatement.setString(5, pay_met);

        } catch (SQLException invalid) {
            System.out.println("Error: " + invalid);

        } finally {
            st_id.clear();
            table_n.clear();
            guest.clear();
            preparedStatement.execute();
            preparedStatement.close();
            refresh();
        }
    }

    @FXML
    private void deleteData() throws SQLException {
        String retrieved_id = trans_id.getText();

        Connection con = db.getConnection();

        String query = "DELETE FROM Bill WHERE bill_id=" + retrieved_id;

        try {
            preparedStatement = con.prepareStatement(query);
        } catch (SQLException e) {
            System.out.println(e);
        } finally {
            trans_id.clear();
            preparedStatement.execute();
            preparedStatement.close();
            refresh();
        }
    }

    public void refresh() {
        try {
            Connection con = db.getConnection();
            data = FXCollections.observableArrayList();
            ResultSet rs = con.createStatement().executeQuery("SELECT * FROM Bill");
            while (rs.next()) {
                data.add(new Transaction(rs.getInt("bill_id"), rs.getInt("staff_id"), rs.getString("dine_type"), rs.getInt("table_no"), rs.getInt("guest_qty"), rs.getString("payment_method")));
            }

            transaction_id.setCellValueFactory(new PropertyValueFactory<>("transaction_id"));
            staff_id.setCellValueFactory(new PropertyValueFactory<>("staff_id"));
            dine_type.setCellValueFactory(new PropertyValueFactory<>("dine_type"));
            table_no.setCellValueFactory(new PropertyValueFactory<>("table_no"));
            guest_quantity.setCellValueFactory(new PropertyValueFactory<>("guest_qty"));
            payment_method.setCellValueFactory(new PropertyValueFactory<>("pay_method"));

            tableTransaction.setItems(data);
        } catch (SQLException ex) {
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
            String transaction_id_ = trans_id.getText();
            String staff_id_ = st_id.getText();
            String dine_t_ = comboBox.getValue();
            String table_no_ = table_n.getText();
            String guest_qty_ = guest.getText();
            String pay_met_ = payBox.getValue();

            int transaction_id_int;
            int staff_id_int;
            String dine_t_string;
            int table_no_int;
            int guest_qty_int;
            String pay_met_string;

            if (!transaction_id_.equals("")) {
                Connection con = db.getConnection();

                if (staff_id_.equals("")) {
                    String query = "SELECT staff_id FROM Bill WHERE bill_id =" + transaction_id_;

                    preparedStatement = con.prepareStatement(query);
                    ResultSet rs = preparedStatement.executeQuery();
                    rs.next();
                    staff_id_int = rs.getInt("staff_id");
                    rs.close();
                } else {
                    staff_id_int = Integer.parseInt(staff_id_);
                }
                if (dine_t_.equals("")) {
                    String query = "SELECT dine_type FROM Bill WHERE bill_id =" + transaction_id_;

                    preparedStatement = con.prepareStatement(query);
                    ResultSet rs = preparedStatement.executeQuery();
                    rs.next();
                    dine_t_string = rs.getString("dine_type");
                    rs.close();
                } else {
                    dine_t_string = dine_t_;
                }
                if (table_no_.equals("")) {
                    String query = "SELECT table_no FROM Bill WHERE bill_id =" + transaction_id_;

                    preparedStatement = con.prepareStatement(query);
                    ResultSet rs = preparedStatement.executeQuery();
                    rs.next();
                    table_no_int = rs.getInt("table_no");
                    rs.close();
                } else {
                    table_no_int = Integer.parseInt(table_no_);
                }
                if (guest_qty_.equals("")) {
                    String query = "SELECT guest_qty FROM Bill WHERE bill_id =" + transaction_id_;

                    preparedStatement = con.prepareStatement(query);
                    ResultSet rs = preparedStatement.executeQuery();
                    rs.next();
                    guest_qty_int = rs.getInt("guest_qty");
                    rs.close();
                } else {
                    guest_qty_int = Integer.parseInt(guest_qty_);
                }
                if (pay_met_.equals("")) {
                    String query = "SELECT payment_method FROM Bill WHERE bill_id =" + transaction_id_;

                    preparedStatement = con.prepareStatement(query);
                    ResultSet rs = preparedStatement.executeQuery();
                    rs.next();
                    pay_met_string = rs.getString("payment_method");
                    rs.close();
                } else {
                    pay_met_string = pay_met_;
                }
                String query = "UPDATE Bill SET staff_id=?, dine_type=?, table_no=?, guest_qty=?, payment_method=? WHERE bill_id =?";
                transaction_id_int = Integer.parseInt(transaction_id_);

                preparedStatement = con.prepareStatement(query);
                preparedStatement.setInt(1, staff_id_int);
                preparedStatement.setString(2, dine_t_string);
                preparedStatement.setInt(3, table_no_int);
                preparedStatement.setInt(4, guest_qty_int);
                preparedStatement.setString(5, pay_met_string);
                preparedStatement.setInt(6, transaction_id_int);

            } else {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Something is missing!");
                alert.setContentText("I have a great message for you!");

                alert.showAndWait();
            }
        } catch (SQLException e) {
            System.out.println(e);
        } finally {
            trans_id.clear();
            st_id.clear();
            table_n.clear();
            guest.clear();
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
