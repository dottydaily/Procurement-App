package controller;

import com.jfoenix.controls.JFXButton;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
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

public class QuotationListController extends Observable {

    @FXML
    protected TableView<QuotationDetail> quotationTableView;
    @FXML
    protected TableColumn<Quotation, String> quotationIdTableColumn;
    @FXML
    protected TableColumn<Quotation, String> pRIdTableColumn;
    @FXML
    protected TableColumn<Quotation, String> dateTableColumn;
    @FXML
    protected TableColumn<Quotation, String> customerIdTableColumn;
    @FXML
    protected TableColumn<Quotation, String> firstNameTableColumn;
    @FXML
    protected TableColumn<Quotation, String> lastNameTableColumn;
    @FXML
    protected TableColumn<Quotation, Integer> totalCostTableColumn;

    @FXML
    protected JFXButton backButton;

    private DBConnecter database = DBConnecter.getInstance();
    private Object previousController;
    private ArrayList<Observer> observers = new ArrayList<>();
    private ObservableList<QuotationDetail> quotationDetails;

    public QuotationListController(Object previousController) {
        this.previousController = previousController;

        if (this.previousController instanceof CreatePOController) {
            addObserver((CreatePOController) this.previousController);
        }
    }

    @FXML
    protected void initialize() {
        if (previousController instanceof CreatePOController) {
            backButton.setText("Close");
        }

        quotationDetails = getQuotationDetailList();

        quotationIdTableColumn.setCellValueFactory(new PropertyValueFactory<>("quotationId"));
        pRIdTableColumn.setCellValueFactory(new PropertyValueFactory<>("purchaseRequestId"));
        dateTableColumn.setCellValueFactory(new PropertyValueFactory<>("date"));
        customerIdTableColumn.setCellValueFactory(new PropertyValueFactory<>("customerID"));
        firstNameTableColumn.setCellValueFactory(new PropertyValueFactory<>("firstName"));
        lastNameTableColumn.setCellValueFactory(new PropertyValueFactory<>("lastName"));
        totalCostTableColumn.setCellValueFactory(new PropertyValueFactory<>("totalCost"));

        quotationTableView.setItems(quotationDetails);
    }

    private ObservableList<QuotationDetail> getQuotationDetailList() {
        ObservableList<QuotationDetail> list = FXCollections.observableArrayList();

        System.out.println("Hello");
        try {
            ResultSet quotationResultSet = database.getResultSet("SELECT quotation_id, pr_id, product_id, date, customer_id, total_cost FROM quotation_list");
            String lastQuotationID = null;
            while (quotationResultSet.next()) {
                if (lastQuotationID != null) {
                    String currentQuotationID = String.format("%05d", Integer.parseInt(quotationResultSet.getString(1)));
                    if (currentQuotationID.equals(lastQuotationID)) {
                        System.out.println("Duplicate : " + currentQuotationID);
                        continue;
                    }
                }
                Quotation quotation = new Quotation(quotationResultSet.getString(1)
                        , quotationResultSet.getString(2), quotationResultSet.getString(3)
                        , quotationResultSet.getString(4), quotationResultSet.getString(5)
                        , Integer.parseInt(quotationResultSet.getString(6)));
                System.out.println(quotation);

                ResultSet customerResultSet = database.getResultSet(
                        "SELECT * FROM customer_list WHERE `customer_id` = " + quotation.getCustomer_id());
                Customer customer = null;
                if (customerResultSet.next()) {
                    customer = new Customer(customerResultSet.getString(2)
                            , customerResultSet.getString(3), customerResultSet.getString(4)
                            , customerResultSet.getString(5), customerResultSet.getString(6)
                            , customerResultSet.getString(7)
                            , Integer.parseInt(customerResultSet.getString(8)));
                    customer.setId(customerResultSet.getString(1));
                    System.out.println(customer);
                }

                QuotationDetail quotationDetail = new QuotationDetail(quotation, customer);
                list.add(quotationDetail);
                System.out.println(quotationDetail);

                lastQuotationID = quotation.getQuotationId();
                System.out.println("LastQuotationID: " + lastQuotationID);
            }
        } catch (SQLException sqlE) {
            sqlE.printStackTrace();
        }

        return list;
    }

    @FXML
    public void clickQuotationDetail(MouseEvent event) {
        if (event.getClickCount() == 2 && previousController instanceof CreatePOController) {
            notifyObservers(quotationTableView.getSelectionModel().getSelectedItem());
            Stage stage = (Stage) quotationTableView.getScene().getWindow();
            stage.close();
        }
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
