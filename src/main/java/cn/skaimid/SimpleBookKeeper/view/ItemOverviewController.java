package cn.skaimid.SimpleBookKeeper.view;

import cn.skaimid.SimpleBookKeeper.MainApp;
import cn.skaimid.SimpleBookKeeper.model.Account;
import cn.skaimid.SimpleBookKeeper.util.DateUtil;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import org.controlsfx.dialog.ProgressDialog;

public class ItemOverviewController {
    @FXML
    private TableView<Account> accountTable;
    @FXML
    private TableColumn<Account, String> tagColumn;
    @FXML
    private TableColumn<Account, String> moneyColumn;

    @FXML
    private Label sumLabel;

    @FXML
    private Label moneyLabel;

    @FXML
    private Label dateLabel;

    @FXML
    private Label tagLabel;

    @FXML
    private Label descriptionLabel;

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
        // Initialize the table with the two columns.
        tagColumn.setCellValueFactory(cellData -> cellData.getValue().tagProperty());
        moneyColumn.setCellValueFactory(cellData -> cellData.getValue().moneyProperty().asString());

        // Clear Item detail
        showItemDetail(null);
        sumLabel.setText("");
        //TODO calculate sum
        // need a database!!!

        accountTable.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> showItemDetail(newValue));

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
    }

    private void showItemDetail(Account account) {
        if (account != null) {
            // Fill the label with data from account object
            moneyLabel.setText(String.valueOf(account.getMoney()));
            dateLabel.setText(DateUtil.format(account.getDate()));
            tagLabel.setText(account.getTag());
            descriptionLabel.setText(account.getDescription());
        } else {
            // not selected
            moneyLabel.setText("");
            dateLabel.setText("");
            tagLabel.setText("");
            descriptionLabel.setText("");
        }
    }


    /**
     * Called when the user clicks on the delete button.
     */
    @FXML
    private void handleDeleteItem() {
        int selectedIndex = accountTable.getSelectionModel().getSelectedIndex();
        if (selectedIndex >= 0) {
            accountTable.getItems().remove(selectedIndex);
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
            mainApp.getAccountData().add(tempaccount);
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
            boolean okClicked = mainApp.showItemEditDialog(selectedAccount);
            if (okClicked) {
                showItemDetail(selectedAccount);
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
