package cn.skaimid.SimpleBookKeeper.view;

import cn.skaimid.SimpleBookKeeper.MainApp;
import cn.skaimid.SimpleBookKeeper.model.Account;
import cn.skaimid.SimpleBookKeeper.model.Tags;
import cn.skaimid.SimpleBookKeeper.util.SqlTimeUtil;
import cn.skaimid.SimpleBookKeeper.util.SqlUtil;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.text.DecimalFormat;
import java.time.LocalDate;


public class ItemOverviewController {
    private ObservableList<Account> accountData;

    @FXML
    public TableView<Account> accountTable;

    @FXML
    public TableColumn<Account, String> tagColumn;
    @FXML
    public TableColumn<Account, String> moneyColumn;
    @FXML
    public TableColumn<Account, String> timeColumn;
    @FXML
    public TableColumn<Account, String> descriptionColumn;

    @FXML
    public Label sumLabel;

    @FXML
    public CheckBox filterCheckBox;

    @FXML
    public DatePicker startDatePicker;

    @FXML
    public DatePicker endDatePicker;

    @FXML
    public ChoiceBox<String> categoryCheckBox;

    @FXML
    public Button filterConfirmButton;

    // Reference to the main application.
    private MainApp mainApp;

    /**
     * The constructor.
     * The constructor is called before the initialize() method.
     */
    public ItemOverviewController() {
    }

    private void disableFilter(boolean isDisable) {
        filterConfirmButton.setDisable(isDisable);
        categoryCheckBox.setDisable(isDisable);
        endDatePicker.setDisable(isDisable);
        startDatePicker.setDisable(isDisable);
    }

    private Alert initialAlert;

    @FXML
    public void initialize() {
        // set Table
        initialAlert = new Alert(Alert.AlertType.INFORMATION);
        initialAlert.setTitle("加载中");
        initialAlert.setHeaderText("加载中...");
        initialAlert.setContentText("请耐心等待...");

        initialAlert.show();


        // set filter value
        disableFilter(true);
        initializeFilter();
        // Initialize the table with the two columns.
        tagColumn.setCellValueFactory(cellData -> cellData.getValue().tagProperty());
        moneyColumn.setCellValueFactory(cellData -> cellData.getValue().moneyProperty().asString());
        timeColumn.setCellValueFactory(cellDate -> cellDate.getValue().dateProperty().asString());
        descriptionColumn.setCellValueFactory(cellData -> cellData.getValue().descriptionProperty());
        filterCheckBox.selectedProperty().addListener(((observable, oldValue, newValue) -> {
            if (newValue) {
                disableFilter(false);
                startDatePicker.setValue(LocalDate.now());
                endDatePicker.setValue(LocalDate.now());

            } else {
                disableFilter(true);
                handleFilter();
            }
        }));
        startDatePicker.valueProperty().addListener(observable -> handleFilter());
        endDatePicker.valueProperty().addListener(observable -> handleFilter());
        categoryCheckBox.valueProperty().addListener(observable -> handleFilter());


        handleFilter();
        accountTable.getSelectionModel().select(0);

        accountTable.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2 && accountTable.getSelectionModel() != null) {
                handleSeeDetail();
            }
        });
        // Clear Item detail
        //showItemDetail(null);
        //accountTable.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> showItemDetail(newValue));

    }


    // Is called by the main application to give a reference back to itself.
    public void setMainApp(MainApp mainApp) {
        this.mainApp = mainApp;
        Thread thread = new Thread(() -> {
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            Platform.runLater(() -> initialAlert.close());
        });
        thread.start();
    }

    private void setTableAndSum() {
        // Add observable list data to the table
        accountTable.setItems(accountData);
        // calculate sum
        // total money
        double sum = 0.0;
        for (Account currentAccount :
                accountData) {
            sum += currentAccount.getMoney();

        }
        sumLabel.setText(new DecimalFormat("0.00").format(sum));
    }


//    private void showItemDetail(Account account) {
//        if (account != null) {
//            // Fill the label with data from account object
//            moneyLabel.setText(String.valueOf(account.getMoney()));
//            dateLabel.setText(DateUtil.format(account.getDate()));
//            tagLabel.setText(account.getTag());
//            descriptionLabel.setText(account.getDescription());
//        } else {
//            // not selected
//            moneyLabel.setText("");
//            dateLabel.setText("");
//            tagLabel.setText("");
//            descriptionLabel.setText("");
//        }
//    }

    @FXML
    public void handleFilter() {
        if (filterCheckBox.isSelected()) {
            if (categoryCheckBox.getValue().equals("全部")) {
                accountData = SqlUtil.handleSearch("select * from account " +
                        "where time >= '" + SqlTimeUtil.formate(startDatePicker.getValue()) + "'" +
                        "and time <= '" + SqlTimeUtil.formate(endDatePicker.getValue()) + "' order by time asc");
            } else {
                accountData = SqlUtil.handleSearch("select * from account " +
                        "where time >= '" + SqlTimeUtil.formate(startDatePicker.getValue()) + "'" +
                        "and time <= '" + SqlTimeUtil.formate(endDatePicker.getValue()) + "' " +
                        "and tag == '" + Tags.getCodeByTagName(categoryCheckBox.getValue()) + "' order by time asc");
            }
        } else {
            accountData = SqlUtil.handleSearch("select * from account order by time asc");
        }
        setTableAndSum();

    }

    @FXML
    public void handleReset() {
        filterCheckBox.setSelected(false);
        initializeFilter();
        accountData = SqlUtil.handleSearch("select * from account order by time asc");
        setTableAndSum();
    }

    private void initializeFilter() {
        ObservableList<String> tagOptions = FXCollections.observableArrayList();
        tagOptions.add("全部");
        for (int i = -1; i <= 10; i++) {
            tagOptions.add(Tags.getTagNameByCode(i));
        }
        categoryCheckBox.setItems(tagOptions);
        categoryCheckBox.setValue("全部");
        startDatePicker.setValue(null);
        endDatePicker.setValue(null);
    }

    @FXML
    public void handleSeeDetail() {
        handleEditItem();
    }


    @FXML
    public void handleCategoryPieChart() {
        mainApp.showCategoryPieChart();
    }

    @FXML
    public void handleIncomeAndExpenditureChart() {
        mainApp.showIncomeAndExpenditureChart();
    }

    /**
     * Called when the user clicks on the delete button.
     */
    @FXML
    public void handleDeleteItem() {
        int selectedIndex = accountTable.getSelectionModel().getSelectedIndex();
        if (selectedIndex >= 0) {
            int id = accountTable.getSelectionModel().selectedItemProperty().getValue().getId();
            SqlUtil.handleDelete(id);
            handleFilter();
        } else {
            // Nothing selected.
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("注意");
            alert.setHeaderText("注意");
            alert.setContentText("请选择一条账目");

            alert.showAndWait();
        }
    }

    /**
     * Called when the user clicks the new button. Opens a dialog to edit
     * details for a new person.
     */
    @FXML
    public void handleNewItem() {
        Account tempAccount = new Account();
        boolean okClicked = mainApp.showItemEditDialog(tempAccount);
        if (okClicked) {
            SqlUtil.handleAdd(tempAccount);
            tempAccount.setId(SqlUtil.getLastId());
            handleFilter();
        }
    }

    /**
     * Called when the user clicks the edit button. Opens a dialog to edit
     * details for the selected person.
     */
    @FXML
    public void handleEditItem() {
        Account selectedAccount = accountTable.getSelectionModel().getSelectedItem();
        if (selectedAccount != null) {
            int tempId = selectedAccount.getId();
            boolean okClicked = mainApp.showItemEditDialog(selectedAccount);
            if (okClicked) {
                //showItemDetail(selectedAccount);
                SqlUtil.handleDelete(tempId);
                SqlUtil.handleAdd(selectedAccount);
                selectedAccount.setId(SqlUtil.getLastId());
                handleFilter();
            }

        } else {
            // Nothing selected.
            // Nothing selected.
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("注意");
            alert.setHeaderText("注意");
            alert.setContentText("请选择一条账目");

            alert.showAndWait();
        }
    }
}
