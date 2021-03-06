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
    protected TableColumn<Product, Integer> quantityTableColumn;
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
    @FXML
    protected Label timeLabel;
    @FXML
    protected Label totalPriceLabel;

    private DBConnecter database = DBConnecter.getInstance();
    private Customer selectCustomer;
    private ObservableList<Product> products = FXCollections.observableArrayList();
    private String prID = "00000";
    private ArrayList<Stage> popUpStages = new ArrayList<>();
    private int totalPrice = 0;

    @FXML
    protected void initialize() {
        productIDTableColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        productNameTableColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        pricePerPieceTableColumn.setCellValueFactory(new PropertyValueFactory<>("pricePerEach"));
        pricePerPieceTableColumn.setStyle(" -fx-alignment: CENTER-RIGHT;");
        quantityTableColumn.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        quantityTableColumn.setStyle(" -fx-alignment: CENTER-RIGHT;");
        amountTableColumn.setCellValueFactory(new PropertyValueFactory<>("amount"));
        amountTableColumn.setStyle(" -fx-alignment: CENTER-RIGHT;");

        productTableView.setItems(products);

        PageManager.setClockInView(timeLabel);

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
        else if (date.isAfter(LocalDate.now()) || date.isBefore(LocalDate.now().minusDays(30))) {
            PageManager.newAlert("Create PR error", String.format("Date must be between %s and %s.",
                    LocalDate.now().minusDays(30).toString(), LocalDate.now().toString()), Alert.AlertType.ERROR);
        }
        else {
            if (totalPrice > selectCustomer.getLimitAsInt()) {
                PageManager.newAlert("Create PR warning", String.format("Total Price is over limit : %,d > %,d.\n" +
                                "NOTE : You can't create Quotation of this PR if quotation still has totalCost more than limit",
                        totalPrice, selectCustomer.getLimitAsInt()), Alert.AlertType.WARNING);
            }

            System.out.println(date);
            for (Product p : products) {
                database.insertPR(prID, p.getId(), date.toString(), selectCustomer.getId(), "Incomplete"
                        , totalPrice, p.getQuantityAsInt(), p.getPricePerEachAsInt());
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
            totalPrice += ((Product) arg).getAmountAsInt();
            totalPriceLabel.setText(String.format("%,d Baht.", totalPrice));
        }
    }

    public Customer getSelectCustomer() {
        return selectCustomer;
    }
}
