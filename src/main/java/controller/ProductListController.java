package controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXTextField;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import model.DBConnecter;
import model.PageManager;
import model.Product;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;

public class ProductListController extends Observable {

    @FXML
    protected TableView<Product> productTableView;
    @FXML
    protected TableColumn<Product, String> productIDTableColumn;
    @FXML
    protected TableColumn<Product, String> productNameTableColumn;
    @FXML
    protected TableColumn<Product, String> pricePerPieceTableColumn;
    @FXML
    protected TableColumn<Product, String> quantityTableColumn;
    @FXML
    protected TableColumn<Product, String> amountTableColumn;

    @FXML
    protected Label quantityPromptLabel;
    @FXML
    protected Label descriptionLabel;
    @FXML
    protected Label timeLabel;

    @FXML
    protected JFXTextField amountTextField;

    @FXML
    protected JFXButton addProductButton;
    @FXML
    protected JFXButton backButton;

    @FXML
    protected JFXTextField productNameTextField;
    @FXML
    protected JFXTextField priceStartTextField;
    @FXML
    protected JFXTextField priceEndTextField;
    @FXML
    protected JFXButton searchButton;

    private DBConnecter database = DBConnecter.getInstance();
    private ResultSet resultSet;
    private Object previousController;
    private ArrayList<Observer> observers = new ArrayList<>();
    private ObservableList<Product> products;
    private Product selectedProduct;

    public ProductListController(Object previousController) {
        this.previousController = previousController;

        if (this.previousController instanceof CreatePRController) {
            addObserver((CreatePRController) this.previousController);
        }
    }

    @FXML
    protected void initialize() {
        if (this.previousController instanceof SelectMenuController) {
            quantityPromptLabel.setOpacity(0);
            amountTextField.setOpacity(0);
            amountTextField.setDisable(true);
            addProductButton.setOpacity(0);
            addProductButton.setDisable(true);
            descriptionLabel.setOpacity(0);
        } else if (previousController instanceof CreatePRController) {
            searchButton.setDefaultButton(true);
            backButton.setText("Close");
        }

        PageManager.setClockInView(timeLabel);

        productIDTableColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        productNameTableColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        pricePerPieceTableColumn.setCellValueFactory(new PropertyValueFactory<>("pricePerEach"));
        pricePerPieceTableColumn.setStyle(" -fx-alignment: CENTER-RIGHT;");
        quantityTableColumn.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        quantityTableColumn.setStyle(" -fx-alignment: CENTER-RIGHT;");
        amountTableColumn.setCellValueFactory(new PropertyValueFactory<>("amount"));
        amountTableColumn.setStyle(" -fx-alignment: CENTER-RIGHT;");

        productTableView.setItems(products);
    }

    private ObservableList<Product> getProductList(String additionalQuery) {
        ObservableList<Product> list = FXCollections.observableArrayList();

        try {
            resultSet = database.getResultSet("SELECT * FROM product_list\n" + additionalQuery);

            while (resultSet.next()) {
                Product product = new Product(resultSet.getString(2)
                        , resultSet.getString(3), resultSet.getString(4));
                product.setId(resultSet.getString(1));

                list.add(product);
            }

            for (Product product : list) {
                System.out.println(product);
            }
        } catch (SQLException sqlE) {
            System.out.println("Cannot query from product_list.");
            sqlE.printStackTrace();
        }

        return list;
    }

    @FXML
    public void clickProduct(MouseEvent event) {
        if (previousController instanceof CreatePRController) {
            selectedProduct = productTableView.getSelectionModel().getSelectedItem();

            searchButton.setDefaultButton(false);
            searchButton.setDisable(true);
            productNameTextField.setDisable(true);
            priceStartTextField.setDisable(true);
            priceEndTextField.setDisable(true);

            addProductButton.setDisable(false);
            addProductButton.setDefaultButton(true);
            amountTextField.setDisable(false);
        }
    }

    @FXML
    public void handleAddProductButton() {
        if (amountTextField.getText().isEmpty()) {
            PageManager.newAlert("Select product error."
                    , "Please fill all of information.", Alert.AlertType.INFORMATION);
            return;
        } else if (amountTextField.getText().matches(".*\\D+.*")) {
            PageManager.newAlert("Select product error."
                    , "Amount must be positive number.", Alert.AlertType.INFORMATION);
            return;
        }

        selectedProduct.setQuantity(Integer.parseInt(amountTextField.getText()));
        notifyObservers(selectedProduct);
        addProductButton.setDisable(true);
        addProductButton.setDefaultButton(false);
        searchButton.setDefaultButton(true);
        amountTextField.clear();
        amountTextField.setDisable(true);

        searchButton.setDefaultButton(true);
        searchButton.setDisable(false);
        productNameTextField.setDisable(false);
        priceStartTextField.setDisable(false);
        priceEndTextField.setDisable(false);
    }

    @FXML
    public void handleBackButton(ActionEvent e) {
        if (previousController instanceof SelectMenuController) {
            PageManager.swapPage(e, "SelectMenuView.fxml");
        } else {
            Stage stage = (Stage) backButton.getScene().getWindow();
            stage.close();
        }
    }

    @FXML
    protected void handleSearchButton(ActionEvent e) {
        String additionQuery = "WHERE product_list.product_name LIKE '%'\n";

        if (!productNameTextField.getText().isEmpty()) {
            String name = productNameTextField.getText();
            additionQuery = String.format("WHERE product_list.product_name LIKE '%%%s%%'\n", name);
        }

        int startPrice = 0;
        int endPrice = 50000;
        if (!priceStartTextField.getText().isEmpty()) {
            if (priceStartTextField.getText().matches("\\d+")) {
                startPrice = Integer.parseInt(priceStartTextField.getText());
            }

            if (startPrice > endPrice || startPrice < 0) {
                startPrice = 0;
                priceStartTextField.clear();
            }
        }
        if (!priceEndTextField.getText().isEmpty()) {
            if (priceEndTextField.getText().matches("\\d+")) {
                endPrice = Integer.parseInt(priceEndTextField.getText());
            }

            if (endPrice < startPrice || endPrice > 50000) {
                endPrice = 50000;
                priceEndTextField.clear();
            }
        }
        additionQuery += String.format("AND product_list.price_per_each BETWEEN %d AND %d\n", startPrice, endPrice);

        System.out.println("Additional Query : " + additionQuery);

        products = getProductList(additionQuery);
        productTableView.setItems(products);
    }

    @Override
    public synchronized void addObserver(Observer o) {
        observers.add(o);
    }

    @Override
    public void notifyObservers(Object object) {
        for (Observer o : observers) {
            o.update(this, object);
        }
    }
}
