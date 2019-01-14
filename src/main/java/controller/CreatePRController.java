package controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDatePicker;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import model.*;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;

public class CreatePRController implements Observer {

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
    protected JFXButton findCustomerButton;
    @FXML
    protected JFXButton createCustomerButton;
    @FXML
    protected JFXButton moreDetailButton;
    @FXML
    protected JFXButton addFromProductHistoryButton;
    @FXML
    protected JFXButton addANewProductManually;
    @FXML
    protected JFXButton createButton;
    @FXML
    protected JFXButton backButton;

    @FXML
    protected JFXDatePicker dateOfRequestDatePicker;

    @FXML
    protected Label customerNameLabel;

    private DBConnecter database = DBConnecter.getInstance();
    private Customer selectCustomer;
    private ObservableList<Product> products = FXCollections.observableArrayList();
    private String prID = "00000";
    private ArrayList<Stage> popUpStages = new ArrayList<>();

    @FXML
    protected void initialize() {
        productIDTableColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        productNameTableColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        pricePerPieceTableColumn.setCellValueFactory(new PropertyValueFactory<>("pricePerEach"));
        amountTableColumn.setCellValueFactory(new PropertyValueFactory<>("amount"));

        productTableView.setItems(products);


        try {
            ResultSet resultSet = database.getResultSet("SELECT MAX(pr_id) FROM pr");

            if (resultSet.next()) {
                prID = String.format("%05d", Integer.parseInt(resultSet.getString(1))+1);

                if (prID == null) {
                    prID = "00000";
                }
            }

        } catch (SQLException sqlE) {
            sqlE.printStackTrace();
        }

        System.out.println(prID);
    }

    @FXML
    protected void handleFindCustomerButton(ActionEvent e) {
        CustomerListController controller = new CustomerListController(this);
        PageManager.newWindow("CustomerListView.fxml", "Choose customer from this list", true, controller);
    }

    @FXML
    protected void handleCreateCustomerButton(ActionEvent e) {
        CreateCustomerController controller = new CreateCustomerController(this);
        PageManager.newWindow("CreateCustomerView.fxml", "Create customer", true, controller);
    }

    @FXML
    protected void handleMoreDetailButton(ActionEvent e) {
        CustomerDetailController controller = new CustomerDetailController(this);
        popUpStages.add(PageManager.newWindow("CustomerDetailView.fxml", "Customer detail", false, controller));
    }

    @FXML
    protected void handleAddFromProductHistoryButton() {
        ProductListController controller = new ProductListController(this);
        popUpStages.add(PageManager.newWindow("ProductListView.fxml", "Product list", false, controller));
    }

    @FXML
    protected void handleAddANewProductManually() {
        CreateProductController controller = new CreateProductController(this);
        popUpStages.add(PageManager.newWindow("CreateProductView.fxml", "Create product", false, controller));
    }

    @FXML
    protected void handleCreateButton(ActionEvent e) {
        LocalDate date = dateOfRequestDatePicker.getValue();
        if (date == null) {
            PageManager.newAlert("Create PR error", "Please choose date of request.", Alert.AlertType.ERROR);
        }
        else if (date.isAfter(LocalDate.now())) {
            PageManager.newAlert("Create PR error", "Date mustn't after today. "
                    + "[" + LocalDate.now().toString() + "]", Alert.AlertType.ERROR);
        }
        else {
            System.out.println(date);
            for (Product p : products) {
                database.insertPR(prID, p.getId(), date.toString(), selectCustomer.getId(), "Incomplete");
            }

            PageManager.newAlert("Create PR success", "Complete register PR", Alert.AlertType.INFORMATION);
            handleBackButton(e);
        }
    }

    @FXML
    protected void handleBackButton(ActionEvent e) {
        for (Stage s : popUpStages) {
            s.close();
        }
        PageManager.swapPage(e, "SelectMenuView.fxml");
    }

    @Override
    public void update(Observable o, Object arg) {
        if (o instanceof CreateCustomerController || o instanceof CustomerListController) {
            selectCustomer = (Customer) arg;
            customerNameLabel.setText(selectCustomer.getFirstName() + " " + selectCustomer.getLastName());
            moreDetailButton.setDisable(false);
            addFromProductHistoryButton.setDisable(false);
            addANewProductManually.setDisable(false);
        }
        else if (o instanceof CreateProductController || o instanceof ProductListController) {
            createButton.setDisable(false);
            products.add((Product) arg);
        }
    }

    public Customer getSelectCustomer() {
        return selectCustomer;
    }
}
