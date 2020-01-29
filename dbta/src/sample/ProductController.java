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

import javax.swing.*;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class ProductController implements Initializable {

    @FXML
    private TextField id;
    @FXML
    private TextField name;
    @FXML
    private TextField quantity;
    @FXML
    private TextField price;
    @FXML
    private TableView<Product> tableProduct;
    @FXML
    private TableColumn<Product, Integer> product_id;
    @FXML
    public TableColumn<Product, String> product_name;
    @FXML
    public TableColumn<Product, Integer> product_quantity;
    @FXML
    public TableColumn<Product, Float> product_price;
    @FXML
    private Button btnSignOut;
    @FXML
    private Button btnBack;

    private ObservableList<Product> data;
    private PreparedStatement preparedStatement;
    private Connector db;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        db = new Connector();
    }

    @FXML
    public void insertData() throws SQLException {
        String product_name = name.getText();
        String product_quantity = quantity.getText();
        String product_price = price.getText();

        int product_quantity_int = Integer.parseInt(product_quantity);
        float product_price_int = Float.parseFloat(product_price);

        String query = "INSERT INTO Items (item_name, item_qty, item_price) VALUES (?, ?, ?)";

        Connection con = db.getConnection();

        preparedStatement = null;

        try {
            preparedStatement = con.prepareStatement(query);
            preparedStatement.setString(1, product_name);
            preparedStatement.setInt(2, product_quantity_int);
            preparedStatement.setFloat(3, product_price_int);
        } catch (SQLException e) {
            alertMessage("" + e);
            System.out.println(e);
        } finally {
            name.clear();
            quantity.clear();
            price.clear();
            preparedStatement.execute();
            preparedStatement.close();
            refresh();
        }
    }

    @FXML
    private void deleteData() throws SQLException {
        String retrieved_id = id.getText();
        Connection con = db.getConnection();

        String query = "DELETE FROM Items WHERE item_id=" + retrieved_id;

        try {
            preparedStatement = con.prepareStatement(query);
        } catch (SQLException e) {
            alertMessage("" + e);
            System.out.println(e);
        } finally {
            id.clear();
            preparedStatement.execute();
//            preparedStatement.close();1234
            refresh();
        }
    }

    public void refresh() {
        try {
            Connection con = db.getConnection();
            data = FXCollections.observableArrayList();
            ResultSet rs = con.createStatement().executeQuery("SELECT * FROM Items");
            while (rs.next()) {
                data.add(new Product(rs.getInt("item_id"), rs.getString("item_name"), rs.getInt("item_qty"), rs.getFloat("item_price")));
            }
            product_id.setCellValueFactory(new PropertyValueFactory<>("item_id"));
            product_name.setCellValueFactory(new PropertyValueFactory<>("item_name"));
            product_quantity.setCellValueFactory(new PropertyValueFactory<>("item_qty"));
            product_price.setCellValueFactory(new PropertyValueFactory<>("item_price"));

//        tableProduct.setItems(null);
            tableProduct.setItems(data);


        } catch (SQLException ex) {
            alertMessage("" + ex);
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
            String product_id_ = id.getText();
            String product_name_ = name.getText();
            String product_quantity_ = quantity.getText();
            String product_price_ = price.getText();

            int product_id_int;
            String product_name_string;
            int product_quantity_int;
            float product_price_int;

            if (!product_id_.equals("")) {
                Connection con = db.getConnection();
                if (product_name_.equals("")) {
                    String query = "SELECT item_name FROM Items WHERE item_id=" + product_id_;
                    preparedStatement = con.prepareStatement(query);
                    ResultSet rs = preparedStatement.executeQuery();
                    rs.next();
                    product_name_string = rs.getString("product_name");
                    rs.close();
                } else {
                    product_name_string = product_name_;
                }

                if (product_quantity_.equals("")) {
                    String query = "SELECT item_qty FROM Items WHERE item_id=" + product_id_;
                    preparedStatement = con.prepareStatement(query);
                    ResultSet rs = preparedStatement.executeQuery();
                    rs.next();
                    product_quantity_int = rs.getInt("product_quantity");
                    rs.close();
                } else {
                    product_quantity_int = Integer.parseInt(product_quantity_);
                }

                if (product_price_.equals("")) {
                    String query = "SELECT item_price FROM Items WHERE item_id=" + product_id_;
                    preparedStatement = con.prepareStatement(query);
                    ResultSet rs = preparedStatement.executeQuery();
                    rs.next();
                    product_price_int = rs.getFloat("price");
                    rs.close();
                } else {
                    product_price_int = Float.parseFloat(product_price_);
                }

                String query = "UPDATE Items SET item_name=?, item_qty=?, item_price=? WHERE item_id=?";
                product_id_int = Integer.parseInt(product_id_);

                preparedStatement = con.prepareStatement(query);
                preparedStatement.setString(1, product_name_string);
                preparedStatement.setInt(2, product_quantity_int);
                preparedStatement.setFloat(3, product_price_int);
                preparedStatement.setInt(4, product_id_int);


            } else {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alertMessage("Something is missing!");
            }

        } catch (SQLException e) {
            System.out.println(e);
        } finally {
            id.clear();
            name.clear();
            quantity.clear();
            price.clear();
            preparedStatement.execute();
            System.out.println("sas");
            preparedStatement.close();
            refresh();
        }
    }

    @FXML
    public void signOutButtonAction(MouseEvent event) {
        if (event.getSource() == btnSignOut) {
            try {
                //add you loading or delays - ;-)
                Node node = (Node) event.getSource();
                Stage stage = (Stage) node.getScene().getWindow();
                //stage.setMaximized(true);
                stage.close();
                Scene scene = new Scene(FXMLLoader.load(getClass().getResource("/sample/loginpage.fxml")));
                stage.setScene(scene);
                stage.show();

            } catch (IOException ex) {
                System.err.println(ex.getMessage());
            }

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

    private void alertMessage(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setContentText("Hi there, " + message);
        alert.showAndWait();
    }
}
