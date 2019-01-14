package controller;

import com.jfoenix.controls.JFXButton;
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
import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;

public class AcceptPOController extends Observable {

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
    protected Label customerNameLabel;
    @FXML
    protected Label prIdLabel;
    @FXML
    protected Label quotationIdLabel;
    @FXML
    protected Label poIdLabel;
    @FXML
    protected Label sendDateLabel;
    @FXML
    protected JFXButton moreDetailButton;
    @FXML
    protected JFXButton closeButton;
    @FXML
    protected JFXButton orderCompleteButton;

    private DBConnecter database = DBConnecter.getInstance();
    private ArrayList<Observer> observers = new ArrayList<>();
    private ObservableList<Product> products = FXCollections.observableArrayList();
    private ResultSet resultSet;
    private PODetail selectedPoDetail;
    private ArrayList<Stage> popUpStages = new ArrayList<>();

    public AcceptPOController(POListController previousController) {
        addObserver(previousController);
        selectedPoDetail = previousController.getSelectedPoDetail();
    }

    @FXML
    protected void initialize() {
        try {
            resultSet = database.getResultSet("" +
                    "SELECT \n" +
                    "\tproduct_list.product_id,\n" +
                    "    product_list.product_name,\n" +
                    "    product_list.price_per_each,\n" +
                    "    product_list.product_amount\n" +
                    "FROM\n" +
                    "\tpo\n" +
                    "INNER JOIN quotation_list ON po.quotation_id = quotation_list.quotation_id \n" +
                    "AND po.po_id = " + selectedPoDetail.getPoId() + "\n" +
                    "INNER JOIN pr ON quotation_list.pr_id = pr.pr_id\n" +
                    "INNER JOIN product_list ON pr.product_id = product_list.product_id\n" +
                    "GROUP BY product_list.product_id");

            while (resultSet.next()) {
                Product product = new Product(resultSet.getString(2)
                        , resultSet.getString(3), resultSet.getString(4));
                product.setId(resultSet.getString(1));
                products.add(product);
            }
        } catch (SQLException sqlEx) {
            sqlEx.printStackTrace();
        }

        productIDTableColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        productNameTableColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        pricePerPieceTableColumn.setCellValueFactory(new PropertyValueFactory<>("pricePerEach"));
        amountTableColumn.setCellValueFactory(new PropertyValueFactory<>("amount"));

        productTableView.setItems(products);

        customerNameLabel.setText(selectedPoDetail.getCustomer().getFirstName() + " " +
                selectedPoDetail.getCustomer().getLastName());
        prIdLabel.setText(selectedPoDetail.getPrId());
        quotationIdLabel.setText(selectedPoDetail.getQuotationId());
        poIdLabel.setText(selectedPoDetail.getPoId());
        sendDateLabel.setText(selectedPoDetail.getSendDate());

        if (selectedPoDetail.getStatus().equals("Complete")) {
            orderCompleteButton.setDisable(true);
        }
    }

    public Customer getCustomerFromPoDetail() {
        return selectedPoDetail.getCustomer();
    }

    @FXML
    protected void handleMoreDetailButton(ActionEvent e) {
        CustomerDetailController controller = new CustomerDetailController(this);
        popUpStages.add(PageManager.newWindow("CustomerDetailView.fxml", "Customer detail", false, controller));
    }

    @FXML
    protected void handleOrderCompleteButton(ActionEvent e) {
        database.updatePoStatus(selectedPoDetail.getPoId()
                , selectedPoDetail.getPrId(), selectedPoDetail.getQuotationId(), "Complete");
        notifyObservers();
        PageManager.newAlert("Confirm PO Success", "This Purchase Order has confirmed. Order Complete."
                , Alert.AlertType.INFORMATION);
        handleCloseButton(e);
    }

    @FXML
    protected void handleCloseButton(ActionEvent e) {
        for (Stage stage : popUpStages) {
            stage.close();
        }

        Stage stage = (Stage) closeButton.getScene().getWindow();
        stage.close();
    }

    @Override
    public synchronized void addObserver(Observer o) {
        observers.add(o);
    }

    @Override
    public void notifyObservers() {
        for (Observer o : observers) {
            o.update(this, null);
        }
    }
}
