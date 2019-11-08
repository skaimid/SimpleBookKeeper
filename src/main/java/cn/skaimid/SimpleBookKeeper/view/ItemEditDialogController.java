package cn.skaimid.SimpleBookKeeper.view;

import cn.skaimid.SimpleBookKeeper.model.Account;
import cn.skaimid.SimpleBookKeeper.model.Tags;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;

public class ItemEditDialogController {
    @FXML
    private ChoiceBox<String> sideChoiceField;


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
        ObservableList<String> sideOption = FXCollections.observableArrayList();
        sideOption.add("支出");
        sideOption.add("收入");
        sideChoiceField.setItems(sideOption);

        sideChoiceField.valueProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if (newValue.equals("收入")) {
                    ObservableList<String> tagOptions = FXCollections.observableArrayList();
                    tagOptions.add("收入");
                    tagChoiceField.setItems(tagOptions);
                    tagChoiceField.setValue("收入");
                } else {
                    ObservableList<String> tagOptions = FXCollections.observableArrayList();
                    for (int i = 0; i <= 10; i++) {
                        tagOptions.add(Tags.getTagNameByCode(i));
                    }
                    tagChoiceField.setItems(tagOptions);
                    tagChoiceField.setValue(account.getTag());
                }
            }
        });


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
        if (account.getMoney() < 0) {
            sideChoiceField.setValue("支出");
            moneyField.setText(String.valueOf(account.getMoney()));
        } else {
            sideChoiceField.setValue("收入");
            moneyField.setText(String.valueOf(account.getMoney()));
        }
        moneyField.setText(String.valueOf(account.getMoney()));
        descriptionField.setText(account.getDescription());
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
            if (sideChoiceField.getValue().equals("收入")) {
                account.setMoney(Math.abs(Double.parseDouble(moneyField.getText())));
                account.setTag("收入");
            } else {
                account.setMoney(-1 * Math.abs(Double.parseDouble(moneyField.getText())));
                account.setTag(tagChoiceField.getValue());
            }

            account.setDate(datePicker.getValue());

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
