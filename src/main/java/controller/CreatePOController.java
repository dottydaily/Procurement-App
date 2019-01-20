package controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDatePicker;
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
import javafx.stage.Stage;
import model.*;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;

public class CreatePOController implements Observer {

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
    protected Label quotationDetailLabel;
    @FXML
    protected Label totalPriceLabel;

    @FXML
    protected JFXTextField sendDateTextField;
    @FXML
    protected JFXDatePicker sendDateDatePicker;

    @FXML
    protected JFXButton findQuotationButton;
    @FXML
    protected JFXButton customerMoreDetailButton;
    @FXML
    protected JFXButton backButton;
    @FXML
    protected JFXButton createButton;

    private DBConnecter database = DBConnecter.getInstance();
    private Customer selectedCustomer;
    private QuotationDetail selectedQuationDetail;
    private ObservableList<Product> products = FXCollections.observableArrayList();
    private String pOID = "00000";
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

        try {
            ResultSet resultSet = database.getResultSet("SELECT MAX(po_id) FROM po");

            if (resultSet.next()) {
                if (pOID == null) {
                    pOID = "00000";
                }
                else if (resultSet.getString(1) != null){
                    pOID = String.format("%05d", Integer.parseInt(resultSet.getString(1))+1);
                } else {
                    pOID = "00000";
                }
            }

        } catch (SQLException sqlE) {
            sqlE.printStackTrace();
        }

        System.out.println(pOID);
    }

    public Customer getSelectedCustomer() {
        return selectedCustomer;
    }

    @FXML
    protected void handleFindQuotationButton(ActionEvent e) {
        QuotationListController controller = new QuotationListController(this);
        PageManager.newWindow("QuotationListView.fxml", "Choose Quotation from this list", true, controller);
    }

    @FXML
    protected void handleCustomerMoreDetailButton(ActionEvent e) {
        CustomerDetailController controller = new CustomerDetailController(this);
        popUpStages.add(PageManager.newWindow("CustomerDetailView.fxml", "Customer detail", false, controller));
    }

    @FXML
    protected void handleCreateButton(ActionEvent e) {
        LocalDate date = sendDateDatePicker.getValue();
        if (date == null) {
            PageManager.newAlert("Create PO error", "Please choose send date of PO", Alert.AlertType.ERROR);
        }
        else if (date.isBefore(LocalDate.now()) || date.isAfter(LocalDate.now().plusDays(30))) {
            PageManager.newAlert("Create PO error", String.format("Date must be between %s and %s.",
                    LocalDate.now().toString(), LocalDate.now().plusDays(30).toString()), Alert.AlertType.ERROR);
        }
        else {
            String sendDate = date.toString();
            database.insertPO(selectedQuationDetail.getPurchaseRequestId()
                    , selectedQuationDetail.getQuotationId(), sendDate);
            PageManager.newAlert("Create PO success", "Complete register PO", Alert.AlertType.INFORMATION);
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
        if (o instanceof QuotationListController) {
            customerMoreDetailButton.setDisable(false);
            createButton.setDisable(false);

            selectedQuationDetail = (QuotationDetail) arg;
            selectedCustomer = selectedQuationDetail.getCustomer();

            customerNameLabel.setText(selectedCustomer.getFirstName() + " " + selectedCustomer.getLastName());
            quotationDetailLabel.setText("ID: "+selectedQuationDetail.getQuotationId()+", Date: "+selectedQuationDetail.getDate());

            updateTableView();
        }
    }

    private void updateTableView() {
        System.out.println("Update TableView");
        products = selectedQuationDetail.getProducts();
        productTableView.setItems(products);

        int totalCost = 0;

        for (Product p : products) {
            totalCost += p.getPricePerEachAsInt()*p.getQuantityAsInt();
        }

        totalPriceLabel.setText(Integer.toString(totalCost)+" Baht.");
    }
}