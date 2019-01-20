package controller;


import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXTextField;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import model.DBConnecter;
import model.PODetail;
import model.PageManager;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Observable;
import java.util.Observer;

public class POListController implements Observer {
    @FXML
    protected TableView<PODetail> poTableView;
    @FXML
    protected TableColumn<PODetail, String> poIdTableColumn;
    @FXML
    protected TableColumn<PODetail, String> quotationIdTableColumn;
    @FXML
    protected TableColumn<PODetail, String> prIdTableColumn;
    @FXML
    protected TableColumn<PODetail, String> firstNameTableColumn;
    @FXML
    protected TableColumn<PODetail, String> lastNameTableColumn;
    @FXML
    protected TableColumn<PODetail, String> sendDateTableColumn;
    @FXML
    protected TableColumn<PODetail, Double> totalCostTableColumn;
    @FXML
    protected TableColumn<PODetail, String> statusTableColumn;

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
    protected Label timeLabel;

    private DBConnecter database = DBConnecter.getInstance();
    private ResultSet resultSet;
    private ObservableList<PODetail> poDetails;
    private PODetail selectedPoDetail;

    @FXML
    protected void initialize() {
        PageManager.setClockInView(timeLabel);

        poIdTableColumn.setCellValueFactory(new PropertyValueFactory<>("poId"));
        prIdTableColumn.setCellValueFactory(new PropertyValueFactory<>("prId"));
        quotationIdTableColumn.setCellValueFactory(new PropertyValueFactory<>("quotationId"));
        firstNameTableColumn.setCellValueFactory(new PropertyValueFactory<>("firstName"));
        lastNameTableColumn.setCellValueFactory(new PropertyValueFactory<>("lastName"));
        sendDateTableColumn.setCellValueFactory(new PropertyValueFactory<>("sendDate"));
        totalCostTableColumn.setCellValueFactory(new PropertyValueFactory<>("totalCost"));
        totalCostTableColumn.setStyle(" -fx-alignment: CENTER-RIGHT;");
        statusTableColumn.setCellValueFactory(new PropertyValueFactory<>("status"));
    }

    private ObservableList<PODetail> getPoDetailList(ResultSet resultSet) {
        ObservableList<PODetail> list = FXCollections.observableArrayList();

        try {
            while (resultSet.next()) {
                PODetail poDetail = new PODetail(resultSet.getString(1), resultSet.getString(2)
                        , resultSet.getString(3), resultSet.getString(4)
                        , resultSet.getString(5), resultSet.getString(6)
                        , resultSet.getDouble(7), resultSet.getString(8));

                list.add(poDetail);
            }
        } catch (SQLException sqlE) {
            sqlE.printStackTrace();
        }

        return list;
    }

    @FXML
    protected void clickPoDetail(MouseEvent event) {
        if (poDetails == null) {
            System.out.println("No data.");
        }
        else if (event.getClickCount() == 2) {
            selectedPoDetail = poTableView.getSelectionModel().getSelectedItem();
            System.out.println(selectedPoDetail.getCustomer().getFirstName() + selectedPoDetail.getLastName());
            System.out.println(">>> PR : " + selectedPoDetail.getPrId() + " Quotation : "
                    + selectedPoDetail.getQuotationId() + " PO : " + selectedPoDetail.getPoId());
            AcceptPOController controller = new AcceptPOController(this);
            PageManager.newWindow("AcceptPOView.fxml", "Confirm PO", true, controller);
        }
    }

    @FXML
    protected void handleBackButton(ActionEvent e) {
        PageManager.swapPage(e, "SelectMenuView.fxml");
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
        int endPrice = 50000;
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

            if (endPrice < startPrice || endPrice > 50000) {
                endPrice = 50000;
                priceEndTextField.clear();
            }
        }
        additionQuery += String.format("AND quotation_list.total_cost BETWEEN %d AND %d\n", startPrice, endPrice);

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
        additionQuery += String.format("AND po.send_date BETWEEN '%s' AND '%s'\n", startDate.format(dateFormatForSQL), endDate.format(dateFormatForSQL));

        System.out.println("Additional Query : " + additionQuery);

        try {
            resultSet = database.getResultSet(
                    "SELECT\n" +
                            "    po.po_id,\n" +
                            "    po.pr_id,\n" +
                            "    po.quotation_id,\n" +
                            "    customer_list.customer_firstname,\n" +
                            "    customer_list.customer_lastname,\n" +
                            "    po.send_date,\n" +
                            "    quotation_list.total_cost,\n" +
                            "    po.po_status\n" +
                            "FROM po INNER JOIN quotation_list ON po.quotation_id=quotation_list.quotation_id\n" +
                            "INNER JOIN customer_list ON quotation_list.customer_id=customer_list.customer_id\n" +
                            additionQuery + "\n" +
                            "GROUP BY po.po_id");
            poDetails = getPoDetailList(resultSet);
        } catch (SQLException sqlE) {
            System.out.println("Cannot query from customer_list.");
            sqlE.printStackTrace();
        }

        poTableView.setItems(poDetails);

        for (PODetail podetail : poDetails) {
            System.out.println(podetail.getPoId() + "\n" + podetail.getQuotationId() + "\n" + podetail.getPrId() + "\n");
        }
    }

    public PODetail getSelectedPoDetail() {
        return selectedPoDetail;
    }

    @Override
    public void update(Observable o, Object arg) {
        try {
            resultSet = database.getResultSet(
                    "SELECT\n" +
                            "    po.po_id,\n" +
                            "    quotation_list.pr_id,\n" +
                            "    po.quotation_id,\n" +
                            "    customer_list.customer_firstname,\n" +
                            "    customer_list.customer_lastname,\n" +
                            "    po.send_date,\n" +
                            "    quotation_list.total_cost,\n" +
                            "    po.po_status\n" +
                            "FROM po INNER JOIN quotation_list ON po.quotation_id=quotation_list.quotation_id\n" +
                            "INNER JOIN customer_list ON quotation_list.customer_id=customer_list.customer_id\n" +
                            "GROUP BY po.po_id");
            poDetails = getPoDetailList(resultSet);
        } catch (SQLException sqlEx) {
            sqlEx.printStackTrace();
        }

        poTableView.setItems(poDetails);
        poTableView.refresh();
    }
}