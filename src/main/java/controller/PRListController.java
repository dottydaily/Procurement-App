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

public class PRListController extends Observable {

    @FXML
    protected TableView<PRDetail> prDetailTableView;
    @FXML
    protected TableColumn<PRDetail, String> pRIdTableColumn;
    @FXML
    protected TableColumn<PRDetail, String> customerIdTableColumn;
    @FXML
    protected TableColumn<PRDetail, String> firstNameTableColumn;
    @FXML
    protected TableColumn<PRDetail, String> lastNameTableColumn;
    @FXML
    protected TableColumn<PRDetail, String> emailTableColumn;
    @FXML
    protected TableColumn<PRDetail, String> addressTableColumn;
    @FXML
    protected TableColumn<PRDetail, String> telTableColumn;
    @FXML
    protected TableColumn<PRDetail, String> statusTableColumn;
    @FXML
    protected Label descriptionLabel;

    @FXML
    protected JFXButton backButton;

    private DBConnecter database = DBConnecter.getInstance();
    private Object previousController;
    private ArrayList<Observer> observers = new ArrayList<>();
    private ObservableList<PRDetail> prDetails;

    public PRListController(Object previousController) {
        this.previousController = previousController;

        if (this.previousController instanceof CreateQuotationController) {
            addObserver((CreateQuotationController) this.previousController);
        }
    }

    @FXML
    protected void initialize() {
        if (previousController instanceof CreateQuotationController) {
            backButton.setText("Close");
            statusTableColumn.setCellValueFactory(new PropertyValueFactory<>("customerStatus"));
        } else if (previousController instanceof SelectMenuController) {
            statusTableColumn.setCellValueFactory(new PropertyValueFactory<>("prStatus"));
            statusTableColumn.setText("PR Status");
            descriptionLabel.setOpacity(0);
        }

        prDetails = getPRDetailList();

        pRIdTableColumn.setCellValueFactory(new PropertyValueFactory<>("purchaseRequestId"));
        customerIdTableColumn.setCellValueFactory(new PropertyValueFactory<>("customerID"));
        firstNameTableColumn.setCellValueFactory(new PropertyValueFactory<>("firstName"));
        lastNameTableColumn.setCellValueFactory(new PropertyValueFactory<>("lastName"));
        emailTableColumn.setCellValueFactory(new PropertyValueFactory<>("email"));
        addressTableColumn.setCellValueFactory(new PropertyValueFactory<>("address"));
        telTableColumn.setCellValueFactory(new PropertyValueFactory<>("phoneNumber"));

        prDetailTableView.setItems(prDetails);
    }

    private ObservableList<PRDetail> getPRDetailList() {
        ObservableList<PRDetail> list = FXCollections.observableArrayList();

        // we need only record unique by pr_id
        try {
            String query = "SELECT\n" +
                    "    pr.pr_id,\n" +
                    "    pr.product_id,\n" +
                    "    pr.date,\n" +
                    "    pr.pr_status,\n" +
                    "    customer_list.customer_id,\n" +
                    "    customer_list.customer_firstname,\n" +
                    "    customer_list.customer_lastname,\n" +
                    "    customer_list.customer_email,\n" +
                    "    customer_list.customer_address,\n" +
                    "    customer_list.customer_tel,\n" +
                    "    customer_list.customer_status\n" +
                    "FROM pr\n" +
                    "INNER JOIN customer_list ON pr.customer_id = customer_list.customer_id \n";
            if (previousController instanceof CreateQuotationController) {
                query = query +
                        "AND pr.pr_status = \"Incomplete\"\n" +
                        "GROUP BY pr.pr_id";
            } else if (previousController instanceof SelectMenuController) {
                query = query +
                        "GROUP BY pr.pr_id";
            }

            ResultSet prResultSet = database.getResultSet(query);

            while (prResultSet.next()) {
                PR pr = new PR(prResultSet.getString(1), prResultSet.getString(2)
                        , prResultSet.getString(3), prResultSet.getString(5)
                        , prResultSet.getString(4));
                Customer customer = customer = new Customer(prResultSet.getString(6),
                        prResultSet.getString(7), prResultSet.getString(8),
                        prResultSet.getString(9), prResultSet.getString(11),
                        prResultSet.getString(10), 5000);
                customer.setId(prResultSet.getString(5));

                PRDetail prDetail = new PRDetail(pr, customer);
                list.add(prDetail);
                System.out.println(prDetail);
            }
        } catch (SQLException sqlE) {
            sqlE.printStackTrace();
        }

        return list;
    }

    @FXML
    public void clickPRDetail(MouseEvent event) {
        if (event.getClickCount() == 2 && previousController instanceof CreateQuotationController) {

            if (database.hasValueInTable("quotation_list", "pr_id"
                    , prDetailTableView.getSelectionModel().getSelectedItem().getPurchaseRequestId())) {
                PageManager.newAlert("Found an exist quotation with this PR!"
                        , "Already have Quotation with this PR!\n" +
                                "Please be aware that this will replace your exist quotation."
                        , Alert.AlertType.WARNING);
            }

            notifyObservers(prDetailTableView.getSelectionModel().getSelectedItem());
            Stage stage = (Stage) prDetailTableView.getScene().getWindow();
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
