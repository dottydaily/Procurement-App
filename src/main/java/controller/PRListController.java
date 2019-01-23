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
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import model.*;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
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
    protected TableColumn<PRDetail, String> totalCostTableColumn;
    @FXML
    protected TableColumn<PRDetail, String> dateTableColumn;
    @FXML
    protected TableColumn<PRDetail, String> statusTableColumn;

    @FXML
    protected JFXTextField customerNameTextField;
    @FXML
    protected JFXTextField priceStartTextField;
    @FXML
    protected JFXTextField priceEndTextField;
    @FXML
    protected JFXDatePicker startDatePicker;
    @FXML
    protected JFXDatePicker endDatePicker;
    @FXML
    protected JFXButton searchButton;

    @FXML
    protected Label descriptionLabel;
    @FXML
    protected Label timeLabel;

    @FXML
    protected JFXButton backButton;

    private DBConnecter database = DBConnecter.getInstance();
    private Object previousController;
    private ResultSet resultSet;
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

        PageManager.setClockInView(timeLabel);

        pRIdTableColumn.setCellValueFactory(new PropertyValueFactory<>("purchaseRequestId"));
        customerIdTableColumn.setCellValueFactory(new PropertyValueFactory<>("customerID"));
        firstNameTableColumn.setCellValueFactory(new PropertyValueFactory<>("firstName"));
        lastNameTableColumn.setCellValueFactory(new PropertyValueFactory<>("lastName"));
        emailTableColumn.setCellValueFactory(new PropertyValueFactory<>("email"));
        totalCostTableColumn.setCellValueFactory(new PropertyValueFactory<>("totalCost"));
        totalCostTableColumn.setStyle(" -fx-alignment: CENTER-RIGHT;");
        dateTableColumn.setCellValueFactory(new PropertyValueFactory<>("date"));
    }

    private ObservableList<PRDetail> getPRDetailList(String additionalQuery) {
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
                    "    customer_list.customer_status,\n" +
                    "    pr.total_cost,\n" +
                    "    pr.product_qty,\n" +
                    "    pr.product_pricePerEach,\n" +
                    "    customer_list.customer_limit\n" +
                    "FROM pr\n" +
                    "INNER JOIN customer_list ON pr.customer_id = customer_list.customer_id \n";
            if (previousController instanceof CreateQuotationController) {
                query = query +
                        "AND pr.pr_status = 0\n" +
                        additionalQuery + "\n" +
                        "GROUP BY pr.pr_id";
            } else if (previousController instanceof SelectMenuController) {
                query = query +
                        additionalQuery + "\n" +
                        "GROUP BY pr.pr_id";
            }

            ResultSet prResultSet = database.getResultSet(query);

            while (prResultSet.next()) {
                String status = "Incomplete";
                if (prResultSet.getInt(4) == 1) {
                    status = "Complete";
                }

                PR pr = new PR(prResultSet.getString(1), prResultSet.getString(2)
                        , prResultSet.getString(3), prResultSet.getString(5)
                        , status, prResultSet.getInt(13)
                        , prResultSet.getInt(14));

                String customerStatus = "Bad";
                if (prResultSet.getBoolean(11)) {
                    customerStatus = "Good";
                }
                Customer customer = customer = new Customer(prResultSet.getString(6),
                        prResultSet.getString(7), prResultSet.getString(8),
                        prResultSet.getString(9), customerStatus,
                        prResultSet.getString(10), prResultSet.getInt(15));
                customer.setId(prResultSet.getString(5));

                PRDetail prDetail = new PRDetail(pr, customer);
                prDetail.setTotalCost(prResultSet.getInt(12));
                list.add(prDetail);
                System.out.println(prDetail.getPurchaseRequestId() + " : " + prDetail.getLimit());
            }
        } catch (SQLException sqlE) {
            sqlE.printStackTrace();
        }

        return list;
    }

    @FXML
    public void clickPRDetail(MouseEvent event) {
        if (prDetails == null) {
            System.out.println("No data.");
        }
        else if (event.getClickCount() == 2 && previousController instanceof CreateQuotationController) {

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

    @FXML
    protected void handleSearchButton(ActionEvent e) {
        String additionQuery = "WHERE customer_list.customer_firstname LIKE '%%'\n";

        if (!customerNameTextField.getText().isEmpty()) {
            String[] fullName;
            if (customerNameTextField.getText().matches(".*\\s.*")) {
                fullName = customerNameTextField.getText().split("\\s");
                additionQuery = String.format("WHERE (customer_list.customer_firstname LIKE '%%%s%%'\n" +
                        "AND customer_list.customer_lastname LIKE '%%%s%%')\n", fullName[0], fullName[1]);
            } else {
                String name = customerNameTextField.getText();
                additionQuery = String.format("WHERE (customer_list.customer_firstname LIKE '%%%s%%'\n" +
                        "OR customer_list.customer_lastname LIKE '%%%s%%')\n", name, name);
            }
        }

        int startPrice = 0;
        int endPrice = 99999;
        if (!priceStartTextField.getText().isEmpty()) {
            if (priceStartTextField.getText().matches("\\d+")) {
                startPrice = Integer.parseInt(priceStartTextField.getText());
            }

            if (startPrice > endPrice || startPrice < 0) {
                startPrice = 0;
                priceStartTextField.clear();
            }
        }
        if (!priceEndTextField.getText().isEmpty()) {
            if (priceEndTextField.getText().matches("\\d+")) {
                endPrice = Integer.parseInt(priceEndTextField.getText());
            }

            if (endPrice < startPrice || endPrice > 99999) {
                endPrice = 99999;
                priceEndTextField.clear();
            }
        }
        additionQuery += String.format("AND pr.total_cost BETWEEN %d AND %d\n", startPrice, endPrice);
//
        DateTimeFormatter dateFormatForSQL = DateTimeFormatter.ofPattern("yyyy/MM/dd");
        LocalDate startDate = LocalDate.of(2017, 1, 1);
        LocalDate endDate = LocalDate.of(9999, 12, 31);
        if (startDatePicker.getValue() != null) {
            if (!startDatePicker.getValue().isAfter(endDate)) {
                startDate = startDatePicker.getValue();
            } else {
                startDatePicker.setValue(LocalDate.of(2017, 1, 1));
            }
        }
        if (endDatePicker.getValue() != null) {
            if (!endDatePicker.getValue().isBefore(startDate)) {
                endDate = endDatePicker.getValue();
            } else {
                endDatePicker.setValue(LocalDate.of(9999, 12, 31));
            }
        }
        System.out.println(startDate.toString() + " <---> " + endDate.toString());
        additionQuery += String.format("AND pr.date BETWEEN '%s' AND '%s'\n", startDate.format(dateFormatForSQL), endDate.format(dateFormatForSQL));

        System.out.println("Additional Query : " + additionQuery);

        prDetails = getPRDetailList(additionQuery);
        prDetailTableView.setItems(prDetails);
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
