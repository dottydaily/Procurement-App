package controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import model.DBConnecter;
import model.PageManager;
import model.Product;

import java.sql.ResultSet;
import java.sql.SQLException;
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
    protected TableColumn<Product, Integer> pricePerPieceTableColumn;
    @FXML
    protected TableColumn<Product, Integer> amountTableColumn;

    @FXML
    protected Label amountPromptLabel;

    @FXML
    protected JFXTextField amountTextField;

    @FXML
    protected JFXButton addProductButton;
    @FXML
    protected JFXButton backButton;

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
            amountPromptLabel.setOpacity(0);
            amountTextField.setOpacity(0);
            amountTextField.setDisable(true);
            addProductButton.setOpacity(0);
            addProductButton.setDisable(true);
        } else if (previousController instanceof CreatePRController) {
            backButton.setText("Close");
        }

        try {
            resultSet = database.getResultSet("SELECT * FROM product_list");
            products = getProductList(resultSet);

            for (Product product : products) {
                System.out.println(product);
            }
        } catch (SQLException sqlE) {
            System.out.println("Cannot query from customer_list.");
            sqlE.printStackTrace();
        }

        productIDTableColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        productNameTableColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        pricePerPieceTableColumn.setCellValueFactory(new PropertyValueFactory<>("pricePerEach"));
        amountTableColumn.setCellValueFactory(new PropertyValueFactory<>("amount"));

        productTableView.setItems(products);
    }

    private ObservableList<Product> getProductList(ResultSet resultSet) {
        ObservableList<Product> list = FXCollections.observableArrayList();

        try {
            while (resultSet.next()) {
                Product product = new Product(resultSet.getString(2)
                        , resultSet.getString(3), resultSet.getString(4));
                product.setId(resultSet.getString(1));

                list.add(product);
            }
        } catch (SQLException sqlE) {
            sqlE.printStackTrace();
        }

        return list;
    }

    @FXML
    public void clickProduct(MouseEvent event) {
        selectedProduct = productTableView.getSelectionModel().getSelectedItem();
        addProductButton.setDisable(false);
        amountTextField.setDisable(false);
    }

    @FXML
    public void handleAddProductButton() {
        if (amountTextField.getText().isEmpty()) {
            PageManager.newAlert("Select product error."
                    , "Please fill all of information", Alert.AlertType.INFORMATION);
            return;
        }

        selectedProduct.setAmount(Integer.parseInt(amountTextField.getText()));
        notifyObservers(selectedProduct);
        addProductButton.setDisable(true);
        amountTextField.clear();
        amountTextField.setDisable(true);
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
