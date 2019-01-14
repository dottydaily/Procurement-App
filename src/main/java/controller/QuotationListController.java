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
    protected Label descriptionLabel;

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
        } else if (previousController instanceof SelectMenuController) {
            descriptionLabel.setOpacity(0);
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

        try {
            String query = "SELECT\n" +
                    "    quotation_list.quotation_id,\n" +
                    "    quotation_list.pr_id,\n" +
                    "    quotation_list.date,\n" +
                    "    quotation_list.customer_id,\n" +
                    "    customer_list.customer_firstname,\n" +
                    "    customer_list.customer_lastname,\n" +
                    "    quotation_list.total_cost\n" +
                    "FROM\n" +
                    "    quotation_list\n" +
                    "INNER JOIN customer_list ON quotation_list.customer_id = customer_list.customer_id\n";

            if (previousController instanceof CreatePOController) {
                query = query +
                        "AND quotation_list.quotation_status = \"Incomplete\"\n" +
                        "GROUP BY quotation_list.quotation_id";
            } else if (previousController instanceof SelectMenuController) {
                query = query +
                        "GROUP BY quotation_list.quotation_id";
            }

            ResultSet quotationResultSet = database.getResultSet(query);
            while (quotationResultSet.next()) {

                QuotationDetail quotationDetail = new QuotationDetail(quotationResultSet.getInt(1)
                        , quotationResultSet.getInt(2), quotationResultSet.getString(3)
                        , quotationResultSet.getInt(4), quotationResultSet.getString(5)
                        , quotationResultSet.getString(6), quotationResultSet.getInt(7));
                list.add(quotationDetail);
            }
        } catch (SQLException sqlE) {
            sqlE.printStackTrace();
        }

        return list;
    }

    @FXML
    public void clickQuotationDetail(MouseEvent event) {
        if (event.getClickCount() == 2 && previousController instanceof CreatePOController) {

            if (database.hasValueInTable("po", "quotation_id"
                    , quotationTableView.getSelectionModel().getSelectedItem().getQuotationId())) {
                PageManager.newAlert("Found an exist PO with this Quotation!"
                        , "Already have Purchase Order with this Quotation!\n" +
                                "Please select another quotation"
                        , Alert.AlertType.ERROR);
            } else {
                notifyObservers(quotationTableView.getSelectionModel().getSelectedItem());
                Stage stage = (Stage) quotationTableView.getScene().getWindow();
                stage.close();
            }
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
