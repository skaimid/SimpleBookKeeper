package cn.skaimid.SimpleBookKeeper.view;

import cn.skaimid.SimpleBookKeeper.MainApp;
import cn.skaimid.SimpleBookKeeper.model.Account;
import cn.skaimid.SimpleBookKeeper.model.Tags;
import cn.skaimid.SimpleBookKeeper.util.SqlUtil;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.time.LocalDate;

public class ItemOverviewController {
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


    @FXML
    public void initialize() {
        filterConfirmButton.setDisable(true);
        categoryCheckBox.setDisable(true);
        endDatePicker.setDisable(true);
        startDatePicker.setDisable(true);
        ObservableList<String> tagOptions = FXCollections.observableArrayList();
        tagOptions.add("ȫ��");
        for (int i = -1; i <= 10; i++) {
            tagOptions.add(Tags.getTagNameByCode(i));
        }
        categoryCheckBox.setItems(tagOptions);
        categoryCheckBox.setValue("ȫ��");
        startDatePicker.setValue(LocalDate.now());
        endDatePicker.setValue(LocalDate.now());
        // Initialize the table with the two columns.
        tagColumn.setCellValueFactory(cellData -> cellData.getValue().tagProperty());
        moneyColumn.setCellValueFactory(cellData -> cellData.getValue().moneyProperty().asString());
        timeCollumn.setCellValueFactory(cellDate -> cellDate.getValue().dateProperty().asString());
        descriptionCollumn.setCellValueFactory(cellData -> cellData.getValue().descriptionProperty());
        filterCheckBox.selectedProperty().addListener(((observable, oldValue, newValue) -> {
            if (newValue) {
                filterConfirmButton.setDisable(false);
                categoryCheckBox.setDisable(false);
                endDatePicker.setDisable(false);
                startDatePicker.setDisable(false);

            } else {
                filterConfirmButton.setDisable(true);
                categoryCheckBox.setDisable(true);
                endDatePicker.setDisable(true);
                startDatePicker.setDisable(true);
            }
        }));
        // Clear Item detail
        //showItemDetail(null);
        //accountTable.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> showItemDetail(newValue));
    }



    /**
     * Is called by the main application to give a reference back to itself.
     *
     * @param mainApp
     */
    public void setMainApp(MainApp mainApp) {
        this.mainApp = mainApp;

        // Add observable list data to the table
        accountTable.setItems(mainApp.getAccountData());

        // calculate sum

        ObservableList<Account> accountData = this.mainApp.getAccountData();
        for (Account currentAccount : accountData) {
            sum += currentAccount.getMoney();
        }
        sumLabel.setText(String.valueOf(sum));
        accountTable.getSelectionModel().select(0);

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

        // TODO ɾ�� main �ڵ� accountData �����б��йص�����ȫ���ŵ� ItemOverviewController ����
        System.out.println("1");
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
            accountTable.getItems().remove(selectedIndex);
            sumLabel.setText(String.valueOf(sum));
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
            mainApp.getAccountData().add(tempaccount);
            sum += tempaccount.getMoney();
            sumLabel.setText(String.valueOf(sum));
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
            double tempMoney = selectedAccount.getMoney();
            boolean okClicked = mainApp.showItemEditDialog(selectedAccount);
            if (okClicked) {
                //showItemDetail(selectedAccount);
                sum = sum + selectedAccount.getMoney() - tempMoney;
                sumLabel.setText(String.valueOf(sum));
                SqlUtil.handleDelete(tempId);
                SqlUtil.handleAdd(selectedAccount);
                selectedAccount.setId(SqlUtil.getLastId());
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
