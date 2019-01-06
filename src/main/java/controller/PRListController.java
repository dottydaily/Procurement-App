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
        }

        prDetails = getPRDetailList();

        pRIdTableColumn.setCellValueFactory(new PropertyValueFactory<>("purchaseRequestId"));
        customerIdTableColumn.setCellValueFactory(new PropertyValueFactory<>("customerID"));
        firstNameTableColumn.setCellValueFactory(new PropertyValueFactory<>("firstName"));
        lastNameTableColumn.setCellValueFactory(new PropertyValueFactory<>("lastName"));
        emailTableColumn.setCellValueFactory(new PropertyValueFactory<>("email"));
        addressTableColumn.setCellValueFactory(new PropertyValueFactory<>("address"));
        telTableColumn.setCellValueFactory(new PropertyValueFactory<>("phoneNumber"));
        statusTableColumn.setCellValueFactory(new PropertyValueFactory<>("status"));

        prDetailTableView.setItems(prDetails);
    }

    private ObservableList<PRDetail> getPRDetailList() {
        ObservableList<PRDetail> list = FXCollections.observableArrayList();

        // we need only record unique by pr_id
        try {
            ResultSet prResultSet = database.getResultSet(
                    "SELECT\n" +
                            "   pr.pr_id,\n" +
                            "   pr.product_id,\n" +
                            "   pr.date,\n" +
                            "   customer_list.customer_id,\n" +
                            "   customer_list.customer_firstname,\n" +
                            "   customer_list.customer_lastname,\n" +
                            "   customer_list.customer_email,\n" +
                            "   customer_list.customer_address,\n" +
                            "   customer_list.customer_tel,\n" +
                            "   customer_list.customer_status\n" +
                            "FROM pr\n" +
                            "INNER JOIN customer_list ON pr.customer_id = customer_list.customer_id\n" +
                            "GROUP BY pr.pr_id ");

            while (prResultSet.next()) {
                PR pr = new PR(prResultSet.getString(1), prResultSet.getString(2),
                        prResultSet.getString(3), prResultSet.getString(4));
                Customer customer = customer = new Customer(prResultSet.getString(5),
                        prResultSet.getString(6), prResultSet.getString(7),
                        prResultSet.getString(8), prResultSet.getString(10),
                        prResultSet.getString(9), 5000);
                customer.setId(prResultSet.getString(4));

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
