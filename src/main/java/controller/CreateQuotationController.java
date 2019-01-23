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
import model.*;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;

public class CreateQuotationController implements Observer {

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
    protected Label customerNameLabel;
    @FXML
    protected Label prDetailLabel;
    @FXML
    protected Label totalPriceLabel;

    @FXML
    protected JFXTextField bestPriceTextField;

    @FXML
    protected JFXButton findPRButton;
    @FXML
    protected JFXButton customerMoreDetailButton;
    @FXML
    protected JFXButton enterPriceButton;
    @FXML
    protected JFXButton backButton;
    @FXML
    protected JFXButton createButton;

    @FXML
    protected Label timeLabel;

    private DBConnecter database = DBConnecter.getInstance();
    private Customer selectedCustomer;
    private PRDetail selectedPRDetail;
    private ObservableList<Product> products = FXCollections.observableArrayList();
    private String quotationID = "00000";
    private Product selectedProduct;
    private ArrayList<Stage> popUpStages = new ArrayList<>();

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

        PageManager.setClockInView(timeLabel);

        try {
            ResultSet resultSet = database.getResultSet("SELECT MAX(quotation_id) FROM quotation_list");

            if (resultSet.next()) {
                if (quotationID == null) {
                    quotationID = "00000";
                }
                else if (resultSet.getString(1) != null){
                    quotationID = String.format("%05d", Integer.parseInt(resultSet.getString(1))+1);
                } else {
                    quotationID = "00000";
                }
            }

        } catch (SQLException sqlE) {
            sqlE.printStackTrace();
        }

        System.out.println(quotationID);
    }

    public Customer getSelectedCustomer() {
        return selectedCustomer;
    }

    @FXML
    protected void handleFindPRButton(ActionEvent e) {
        PRListController controller = new PRListController(this);
        PageManager.newWindow("PRListView.fxml", "Choose PR from this list", true, controller);
    }

    @FXML
    protected void handleCustomerMoreDetailButton(ActionEvent e) {
        CustomerDetailController controller = new CustomerDetailController(this);
        popUpStages.add(PageManager.newWindow("CustomerDetailView.fxml", "Customer detail", false, controller));
    }

    @FXML
    public void clickProduct(MouseEvent event) {
        selectedProduct = productTableView.getSelectionModel().getSelectedItem();
        enterPriceButton.setDisable(false);
        bestPriceTextField.setDisable(false);

        // focus on enter best price button instead of create button
        createButton.setDefaultButton(false);
        enterPriceButton.setDefaultButton(true);
    }

    @FXML
    protected void handleEnterPriceButton(ActionEvent e) {
        if (bestPriceTextField.getText().isEmpty()) {
            PageManager.newAlert("Enter best product price error."
                    , "Please fill your best price of product.", Alert.AlertType.INFORMATION);
            return;
        } else if (bestPriceTextField.getText().matches(".*\\D.*") ||
                Double.parseDouble(bestPriceTextField.getText()) <= 0){
            PageManager.newAlert("Enter best product price error."
                    , "Product Price must be positive number.", Alert.AlertType.INFORMATION);
            return;
        }

        for (int i = 0 ; i < products.size() ; i++) {
            if (products.get(i).getId().equals(selectedProduct.getId())) {
                products.get(i).setPricePerEach(Integer.parseInt(bestPriceTextField.getText()));
                System.out.println(products.get(i));
            }
        }

        System.out.println("Update price");

        int totalCost = 0;

        for (Product p : products) {
            database.updateProductData(p.getId(), p.getPricePerEachAsInt());
            totalCost += p.getPricePerEachAsInt()*p.getQuantityAsInt();
        }

        totalPriceLabel.setText(Integer.toString(totalCost)+" Baht.");

        productTableView.refresh();
        enterPriceButton.setDisable(true);
        bestPriceTextField.clear();
        bestPriceTextField.setDisable(true);

        // focus back to create button instead of enter price button
        createButton.setDefaultButton(true);
        enterPriceButton.setDefaultButton(false);
    }

    @FXML
    protected void handleCreateButton(ActionEvent e) {
        int totalCost = 0;

        for (Product p : products) {
            database.updateProductData(p.getId(), p.getPricePerEachAsInt());
            totalCost += p.getPricePerEachAsInt()*p.getQuantityAsInt();
        }

        if (database.hasValueInTable("quotation_list", "pr_id"
                , selectedPRDetail.getPurchaseRequestId())) {
            for (Product p : products) {
                database.updateQuotation(selectedPRDetail.getPurchaseRequestId(), p.getId()
                        , Integer.toString(totalCost), p.getPricePerEachAsInt());
            }

            PageManager.newAlert("Update Quotation success", "Complete update an existing Quotation."
                    , Alert.AlertType.INFORMATION);

            handleBackButton(e);
        } else if (totalCost > selectedCustomer.getLimitAsInt()) {
            PageManager.newAlert("Create Quotation error", String.format("Total Price is over limit : %,d > %,d.",
                    totalCost, selectedCustomer.getLimitAsInt()), Alert.AlertType.ERROR);
        }
        else {
            for (Product p : products) {
                System.out.println(p);
                database.insertQuotation(quotationID, selectedPRDetail.getPurchaseRequestId(), p.getId()
                        , selectedPRDetail.getDate(), selectedPRDetail.getCustomerID(), Integer.toString(totalCost)
                        , p.getPricePerEachAsInt());
            }

            int currentLimit = selectedCustomer.getLimitAsInt() - totalCost;
            database.updateCustomerLimit(selectedCustomer.getId(), currentLimit);

            PageManager.newAlert("Create Quotation success", "Complete register a new Quotation."
                    , Alert.AlertType.INFORMATION);

            handleBackButton(e);
        }
    }

    @FXML
    protected void handleBackButton(ActionEvent e) {
        for (Stage stage : popUpStages) {
            stage.close();
        }
        PageManager.swapPage(e, "SelectMenuView.fxml");
    }

    @Override
    public void update(Observable o, Object arg) {
        if (o instanceof PRListController) {

            // enable buttons that need to do with customerDetail
            customerMoreDetailButton.setDisable(false);
            createButton.setDisable(false);

            selectedPRDetail = (PRDetail) arg;
            selectedCustomer = new Customer(selectedPRDetail.getFirstName(), selectedPRDetail.getLastName(), selectedPRDetail.getEmail()
                    , selectedPRDetail.getAddress(), selectedPRDetail.getCustomerStatus(), selectedPRDetail.getPhoneNumber(), selectedPRDetail.getLimit());
            selectedCustomer.setId(selectedPRDetail.getCustomerID());

            System.out.println(selectedPRDetail.getLimit());
            System.out.println(selectedCustomer.getLimitAsInt());

            customerNameLabel.setText(selectedCustomer.getFirstName() + " " + selectedCustomer.getLastName());
            prDetailLabel.setText("ID: "+selectedPRDetail.getPurchaseRequestId()+", Date: "+selectedPRDetail.getDate());

            updateTableView();
        }
    }

    private void updateTableView() {
        System.out.println("Update TableView");
        products = selectedPRDetail.getProducts();
        productTableView.setItems(products);

        // total cost of all product in this table
        int totalCost = 0;
        for (Product p : products) {
            totalCost += p.getPricePerEachAsInt()*p.getQuantityAsInt();
        }

        totalPriceLabel.setText(Integer.toString(totalCost)+" Baht.");
    }
}
