package cn.skaimid.SimpleBookKeeper.view;

import cn.skaimid.SimpleBookKeeper.model.Account;
import cn.skaimid.SimpleBookKeeper.model.Tags;
import com.sun.tools.javac.tree.JCTree;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;

public class ItemEditDialogController {
    @FXML
    private TextField moneyField;

    @FXML
    private TextArea descriptionField;

    @FXML
    private DatePicker datePicker;

    @FXML
    private ChoiceBox<String> tagChoiceField;

    private Stage dialogStage;
    private Account account;
    private Boolean okClicked = false;

    /**
     * Initializes the controller class. This method is automatically called
     * after the fxml file has been loaded.
     */
    @FXML
    private void initialize() {
        ObservableList<String> options = FXCollections.observableArrayList();
        for (int i = 0; i <= 10; i++) {
            options.add(Tags.getTagNameByCode(i));
        }
        tagChoiceField.setItems(options);
    }

    /**
     * Sets the stage of this dialog.
     *
     * @param dialogStage
     */
    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }

    /**
     * Sets the person to be edited in the dialog.
     *
     * @param account
     */
    public void setAccount(Account account) {
        this.account = account;
        moneyField.setText(String.valueOf(account.getMoney()));
        descriptionField.setText(account.getDescription());
        tagChoiceField.setValue(account.getTag());
        datePicker.setValue(account.getDate());
        datePicker.setPromptText("dd.mm.yyyy");
    }

    /**
     * Returns true if the user clicked OK, false otherwise.
     *
     * @return
     */
    public boolean isOkClicked() {
        return okClicked;
    }

    /**
     * Called when the user clicks ok.
     */
    @FXML
    private void handleOk() {
        if (isInputValid()) {
            account.setMoney(Integer.parseInt(moneyField.getText()));
            account.setDate(datePicker.getValue());
            account.setTag(tagChoiceField.getValue());
            account.setDescription(descriptionField.getText());


            okClicked = true;
            dialogStage.close();
        }
    }

    /**
     * Called when the user clicks cancel.
     */
    @FXML
    private void handleCancel() {
        dialogStage.close();
    }

    /**
     * Validates the user input in the text fields.
     *
     * @return true if the input is valid
     */
    private boolean isInputValid() {
        String errorMsg = "";

        if (moneyField.getText() == null || moneyField.getText().length() == 0) {
            errorMsg += "No valid money";
        }

        if (moneyField.getText() == null || moneyField.getText().length() == 0) {
            errorMsg += "No valid money";
        }

        //TODO other valid check is not finished

        if (errorMsg.length() == 0) {
            return true;
        } else {
            // Show the error message.
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Information Dialog");
            alert.setHeaderText("Look, an Information Dialog");
            alert.setContentText("I have a great message for you!");

            alert.showAndWait();
            return false;
        }
    }
}
