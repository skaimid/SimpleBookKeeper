package cn.skaimid.SimpleBookKeeper.view;

import cn.skaimid.SimpleBookKeeper.MainApp;
import cn.skaimid.SimpleBookKeeper.model.Account;
import cn.skaimid.SimpleBookKeeper.model.Tags;
import cn.skaimid.SimpleBookKeeper.util.SqlTimeUtil;
import cn.skaimid.SimpleBookKeeper.util.SqlUtil;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.time.LocalDate;


public class ItemOverviewController {
    private ObservableList<Account> accountData;

    @FXML
    private TableView<Account> accountTable;

    @FXML
    private TableColumn<Account, String> tagColumn;
    @FXML
    private TableColumn<Account, String> moneyColumn;
    @FXML
    private TableColumn<Account, String> timeCollumn;
    @FXML
    private TableColumn<Account, String> descriptionCollumn;

    @FXML
    private Label sumLabel;

    @FXML
    private CheckBox filterCheckBox;

    @FXML
    private DatePicker startDatePicker;

    @FXML
    private DatePicker endDatePicker;

    @FXML
    private ChoiceBox<String> categoryCheckBox;

    @FXML
    private Button filterConfirmButton;

    // total money
    private double sum = 0;

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


    @FXML
    public void initialize() {
        accountData = SqlUtil.handleSearch("select * from account");

        // set filter value
        disableFilter(true);
        initializeFilter();
        // Initialize the table with the two columns.
        tagColumn.setCellValueFactory(cellData -> cellData.getValue().tagProperty());
        moneyColumn.setCellValueFactory(cellData -> cellData.getValue().moneyProperty().asString());
        timeCollumn.setCellValueFactory(cellDate -> cellDate.getValue().dateProperty().asString());
        descriptionCollumn.setCellValueFactory(cellData -> cellData.getValue().descriptionProperty());
        filterCheckBox.selectedProperty().addListener(((observable, oldValue, newValue) -> {
            if (newValue) {
                disableFilter(false);

            } else {
                disableFilter(true);
            }
        }));


        // set Table
        setTableAndSum();
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



    /**
     * Is called by the main application to give a reference back to itself.
     *
     * @param mainApp
     */

    // TODO func below need to be cleaned
    public void setMainApp(MainApp mainApp) {
        this.mainApp = mainApp;

    }

    private void setTableAndSum() {
        // Add observable list data to the table
        accountTable.setItems(accountData);
        // calculate sum
        sum = 0;
        ObservableList<Account> tempAccountData = accountData;
        for (Account currentAccount : tempAccountData) {
            sum += currentAccount.getMoney();
        }
        sumLabel.setText(String.valueOf(sum));
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
    private void handleFilter() {

        if (categoryCheckBox.getValue().equals("全部")) {
            accountData = SqlUtil.handleSearch("select * from account " +
                    "where time >= '" + SqlTimeUtil.formate(startDatePicker.getValue()) + "'" +
                    "and time <= '" + SqlTimeUtil.formate(endDatePicker.getValue()) + "' ");
        } else {
            accountData = SqlUtil.handleSearch("select * from account " +
                    "where time >= '" + SqlTimeUtil.formate(startDatePicker.getValue()) + "'" +
                    "and time <= '" + SqlTimeUtil.formate(endDatePicker.getValue()) + "' " +
                    "and tag == '" + Tags.getCodeByTagName(categoryCheckBox.getValue()) + "'");
        }
        setTableAndSum();

    }

    @FXML
    private void handleReset() {
        filterCheckBox.setSelected(false);
        initializeFilter();
        accountData = SqlUtil.handleSearch("select * from account");
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
        startDatePicker.setValue(LocalDate.now());
        endDatePicker.setValue(LocalDate.now());
    }

    @FXML
    private void handleSeeDetail() {
        System.out.println("233");
    }


    @FXML
    private void handleCategoryPieChart() {
        mainApp.showCategoryPieChart();
    }

    /**
     * Called when the user clicks on the delete button.
     */
    @FXML
    private void handleDeleteItem() {
        int selectedIndex = accountTable.getSelectionModel().getSelectedIndex();
        if (selectedIndex >= 0) {
            int id = accountTable.getSelectionModel().selectedItemProperty().getValue().getId();
            SqlUtil.handleDelete(id);
            sum -= accountTable.getSelectionModel().selectedItemProperty().getValue().getMoney();
            setTableAndSum();
        } else {
            // Nothing selected.
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Information Dialog");
            alert.setHeaderText("Look, an Information Dialog");
            alert.setContentText("I have a great message for you!");

            alert.showAndWait();
        }
    }

    /**
     * Called when the user clicks the new button. Opens a dialog to edit
     * details for a new person.
     */
    @FXML
    private void handleNewItem() {
        Account tempaccount = new Account();
        boolean okClicked = mainApp.showItemEditDialog(tempaccount);
        if (okClicked) {
            SqlUtil.handleAdd(tempaccount);
            tempaccount.setId(SqlUtil.getLastId());
            setTableAndSum();
        }
    }

    /**
     * Called when the user clicks the edit button. Opens a dialog to edit
     * details for the selected person.
     */
    @FXML
    private void handleEditItem() {
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
                setTableAndSum();
            }

        } else {
            // Nothing selected.
            // Nothing selected.
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Information Dialog");
            alert.setHeaderText("Look, an Information Dialog");
            alert.setContentText("I have a great message for you!");

            alert.showAndWait();
        }
    }
}
