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

import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.ResourceBundle;

public class TransactionDetailController  implements Initializable {
    @FXML
    private TextField id_1;
    @FXML
    private TextField id;
    @FXML
    private TextField name;
    @FXML
    private TextField qty;
    @FXML
    private Button btnBack;
    @FXML
    private TableView<TransactionDetail> tableTransactionDetails;
    @FXML
    private TableColumn<TransactionDetail, Integer> transaction_details_id;
    @FXML
    private TableColumn<TransactionDetail, Integer> transaction_id;
    @FXML
    private TableColumn<TransactionDetail, String> product_trans_name;
    @FXML
    private TableColumn<TransactionDetail, Integer> quantity;
    @FXML
    private TableView<Product> tableProduct;
    @FXML
    private TableColumn<Product, Integer> product_id;
    @FXML
    private TableColumn<Product, String> product_name;
    @FXML
    private TableColumn<Product, Integer> product_quantity;
    @FXML
    private TableColumn<Product, Integer> product_price;


    private ObservableList<TransactionDetail> data;
    private ObservableList<Product> data_product;
    private PreparedStatement preparedStatement;
    private Connector db;
    private ResultSet rs;
//    private FXMLDocumentController product_info;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        db = new Connector();
    }

    public int getMaxId() {
        String query_3 = "SELECT MAX(trx_id) FROM TransactionDetails";

        Connection con = db.getConnection();

        preparedStatement = null;

        try {
            preparedStatement = con.prepareStatement(query_3);
            rs = preparedStatement.executeQuery();
            rs.next();
            int transaction_id_int = rs.getInt(1);
            rs.close();
            System.out.println(transaction_id_int);
            return transaction_id_int;
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return 0;
    }

    @FXML
    public void display() {
        Product pd = tableProduct.getSelectionModel().getSelectedItem();
        name.setText(pd.getItem_name());
    }

    @FXML
    public void insertData() throws SQLException {
        String transaction_id = id.getText();
        String product_name_string = name.getText();
        String quantity = qty.getText();

        int transaction_id_int = Integer.parseInt(transaction_id);
        int quantity_int = Integer.parseInt(quantity);


        String query = "INSERT INTO TransactionDetails (bill_id, item_id, quantity) VALUES (?, ?, ?)";

        Connection con = db.getConnection();

        preparedStatement = null;

        String query_2 = "SELECT item_id, item_qty FROM Items WHERE item_name=?";
        preparedStatement = con.prepareStatement(query_2);
        preparedStatement.setString(1, product_name_string);
        rs = preparedStatement.executeQuery();
        rs.next();
        int product_id_int = rs.getInt("item_id");
        int product_quantity_in_table_product = rs.getInt("item_qty");
        rs.close();

        if ((quantity_int - product_quantity_in_table_product) > 0) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Something is missing!");
            alert.setContentText("Sorry we don't have this number of items. Kindly insert a lesser number than stated before");

            alert.showAndWait();
        } else if (product_quantity_in_table_product == 0) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Something is missing!");
            alert.setContentText("Oops, the item is finished");

            alert.showAndWait();
        } else {
            try {
                preparedStatement = con.prepareStatement(query);
                preparedStatement.setInt(1, transaction_id_int);
                preparedStatement.setInt(2, product_id_int);
                preparedStatement.setInt(3, quantity_int);

                preparedStatement.execute();
                preparedStatement.close();
                refresh();
                refresh_product();

            } catch (SQLException e) {
                System.out.println(e);
            } finally {
                id_1.clear();
                id.clear();
                name.clear();
                qty.clear();
//                preparedStatement.execute();
//                preparedStatement.close();
//                refresh();
//                refresh_product();

                try {
//                    int transaction_details_id_int = getMaxId();
                    int quantityy = product_quantity_in_table_product - quantity_int;
                    String query_4 = "UPDATE Items SET item_qty=?";
//                    String query_4 = "UPDATE Items INNER JOIN TransactionDetails ON TransactionDetails.item_id = Items.item_id SET " +
//                            "item_qty=? WHERE trx_id=?";
                    preparedStatement = con.prepareStatement(query_4);
//                    System.out.println(quantity_int);
                    preparedStatement.setInt(1, quantityy);
//                    preparedStatement.setInt(2, transaction_details_id_int);
                    preparedStatement.execute();
                    preparedStatement.close();
                } catch (SQLException e) {
                    System.out.println(e);
                } finally {
                    preparedStatement.execute();
                    preparedStatement.close();
                }
            }
        }
    }

    @FXML
    private void deleteData() throws SQLException {
        String retrieved_id = id_1.getText();

        Connection con = db.getConnection();

        String query = "DELETE FROM TransactionDetails WHERE trx_id=" + retrieved_id;

        try {
            preparedStatement = con.prepareStatement(query);
        } catch (SQLException e) {
            System.out.println(e);
        } finally {
            id_1.clear();
            preparedStatement.execute();
            preparedStatement.close();
            refresh();
        }
    }


    public void refresh() {
        try {
            Connection con = db.getConnection();
            data = FXCollections.observableArrayList();
            ResultSet rs = con.createStatement().executeQuery("SELECT trx_id, bill_id, Items.item_name, quantity FROM TransactionDetails LEFT JOIN Items ON TransactionDetails.`item_id`= Items.item_id");
            transaction_details_id.setCellValueFactory(new PropertyValueFactory<>("transaction_details_id"));
            transaction_id.setCellValueFactory(new PropertyValueFactory<>("transaction_id"));
            product_trans_name.setCellValueFactory(new PropertyValueFactory<>("product_trans_name"));
            quantity.setCellValueFactory(new PropertyValueFactory<>("quantity"));
            while (rs.next()) {
                data.add(new TransactionDetail(rs.getInt("trx_id"), rs.getInt("bill_id")
                        , rs.getString("item_name"), rs.getInt("quantity")));
            }


//        tableProduct.setItems(null);
            tableTransactionDetails.setItems(data);
            refresh_product();

        } catch (SQLException ex) {
            System.out.println("I am not here G-Ladies");
            System.err.println("Error" + ex);
        }
    }

    public void refresh_product() {
        try {
            Connection con = db.getConnection();
            data_product = FXCollections.observableArrayList();
            ResultSet rs = con.createStatement().executeQuery("SELECT * FROM Items");
            while (rs.next()) {
                data_product.add(new Product(rs.getInt("item_id"), rs.getString("item_name"), rs.getInt("item_qty"), rs.getFloat("item_price")));
            }

            product_id.setCellValueFactory(new PropertyValueFactory<>("item_id"));
            product_name.setCellValueFactory(new PropertyValueFactory<>("item_name"));
            product_quantity.setCellValueFactory(new PropertyValueFactory<>("item_qty"));
            product_price.setCellValueFactory(new PropertyValueFactory<>("item_price"));

//        tableProduct.setItems(null);
            tableProduct.setItems(data_product);

        } catch (SQLException ex) {
            System.err.println("Error" + ex);
        }
    }


    @FXML
    private void handleButtonAction(ActionEvent event) {
        refresh();
    }

    @FXML
    public void displayNo() {
        TransactionDetail td = tableTransactionDetails.getSelectionModel().getSelectedItem();
        String transaction_details_string = String.valueOf(td.getTransaction_details_id());

        id_1.setText(transaction_details_string);
    }

    @FXML
    private void updateData() throws SQLException {
        try {
            String transaction_details_id_ = id_1.getText();
            String transaction_id_ = id.getText();
            String product_name_ = name.getText();
            String quantity_ = qty.getText();

            int transaction_id_int;
            int product_id_int;
            int quantity_int;


            if (!transaction_details_id_.equals("")) {
                Connection con = db.getConnection();
                if (transaction_id_.equals("")) {
                    String query = "SELECT bill_id FROM TransactionDetails WHERE trx_id=" + transaction_details_id_;
                    preparedStatement = con.prepareStatement(query);
                    ResultSet rs = preparedStatement.executeQuery();
                    rs.next();
                    transaction_id_int = rs.getInt("bill_id");
                    rs.close();
                } else {
                    transaction_id_int = Integer.parseInt(transaction_id_);
                }
                if (product_name_.equals("")) {
                    String query = "SELECT item_id FROM TransactionDetails WHERE trx_id=" + transaction_details_id_;
                    preparedStatement = con.prepareStatement(query);
                    ResultSet rs = preparedStatement.executeQuery();
                    rs.next();
                    product_id_int = rs.getInt("item_id");
                    rs.close();
                } else {
                    String query = "SELECT item_id FROM Items WHERE item_name=?";
                    preparedStatement = con.prepareStatement(query);
                    preparedStatement.setString(1, product_name_);
                    ResultSet rs = preparedStatement.executeQuery();
                    rs.next();
                    product_id_int = rs.getInt("item_id");
                    rs.close();

                }
                if (quantity_.equals("")) {
                    String query = "SELECT quantity FROM TransactionDetails WHERE trx_id=" + transaction_details_id_;
                    preparedStatement = con.prepareStatement(query);
                    ResultSet rs = preparedStatement.executeQuery();
                    rs.next();
                    quantity_int = rs.getInt("quantity");
                    rs.close();
                } else {
                    quantity_int = Integer.parseInt(quantity_);
                }
                String query = "UPDATE TransactionDetails SET bill_id=?, item_id=?, quantity=? WHERE trx_id=?";
                int transaction_details_id_int = Integer.parseInt(transaction_details_id_);

                preparedStatement = con.prepareStatement(query);
                preparedStatement.setInt(1, transaction_id_int);
                preparedStatement.setInt(2, product_id_int);
                preparedStatement.setInt(3, quantity_int);
                preparedStatement.setInt(4, transaction_details_id_int);
            } else {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Something is missing!");
                alert.setContentText("Please enter your ID!!!");

                alert.showAndWait();
            }

        } catch (SQLException e) {
            System.out.println(e);
        } finally {
            id_1.clear();
            id.clear();
            qty.clear();
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
    @FXML
    public void printBill() {
        String transaction_id = id.getText();
        String date = new SimpleDateFormat("dd-MM-yyyy").format(new Date());
        String Date = "Date: " + date;
        String Transaction_Id = "Transaction id: " + transaction_id;

        preparedStatement = null;
        Connection con = db.getConnection();

        if (!transaction_id.equals("")) {
            try {
                System.out.println("       OTW Restaurant       ");
                System.out.println("     Jl. Raya Klp. Kopyor Blok BA 2 No.1, Jkt Utara      ");
                System.out.println("       (021) 4531245       ");
                System.out.println("====================================");
                System.out.println(Date);
                System.out.println(Transaction_Id);


                String query_3 = "SELECT Bill.dine_type, Bill.table_no, Bill.guest_qty, Bill.payment_method FROM Bill INNER JOIN TransactionDetails ON TransactionDetails.bill_id = Bill.bill_id WHERE TransactionDetails.`bill_id` = " + transaction_id;
                preparedStatement = con.prepareStatement(query_3);
                rs = preparedStatement.executeQuery();
                rs.next();

                String dine = rs.getString("dine_type");
                int table = rs.getInt("table_no");
                int guest = rs.getInt("guest_qty");
                String pay = rs.getString("payment_method");
                System.out.println("Dine type: " +dine);
                System.out.println("Table No: " +table);
                System.out.println("Guest Qty: " +guest);
                System.out.println("Payment method: " +pay);

                System.out.println("------------------------------------");

                String query_1 = "SELECT Items.item_name, TransactionDetails.quantity, Items.item_price FROM TransactionDetails INNER JOIN Items ON TransactionDetails.item_id = Items.item_id WHERE TransactionDetails.`bill_id` = " + transaction_id;
                preparedStatement = con.prepareStatement(query_1);
                ResultSet rs = preparedStatement.executeQuery();

                System.out.println("Product Name     Quantity     Price");

                while (rs.next()) {
                    String product_name = rs.getString("item_name");
                    String quantity = rs.getString("quantity");
                    String price = rs.getString("item_price");

                    String Info = product_name + "        " + quantity + "        " + price;


                    System.out.println(Info);
                }

                String query_2 = "SELECT SUM(Items.item_price * TransactionDetails.quantity) FROM TransactionDetails INNER JOIN Items ON TransactionDetails.item_id = Items.item_id WHERE TransactionDetails.`bill_id` = " + transaction_id;
                preparedStatement = con.prepareStatement(query_2);
                rs = preparedStatement.executeQuery();
                rs.next();

                String total_price = rs.getString(1);
                String Total_Price = "Total Price: " + total_price;

                System.out.println("-----------------------------------");
                System.out.println(Total_Price);
                System.out.println("-----------------------------------");
                System.out.println("Thanks for Coming");

                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Success!");
                alert.setContentText("Receipt successfully printed!");

                alert.showAndWait();
            } catch (Exception e) {
                System.out.println(e);
            }


        } else {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Something is missing!");
            alert.setContentText("The ID is empty");

            alert.showAndWait();
        }
    }
}
