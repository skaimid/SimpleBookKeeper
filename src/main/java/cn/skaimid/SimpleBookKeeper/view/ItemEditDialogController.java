package cn.skaimid.SimpleBookKeeper.view;

import cn.skaimid.SimpleBookKeeper.model.Account;
import cn.skaimid.SimpleBookKeeper.model.Tags;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
        sideOption.add("֧��");
        sideOption.add("����");
        sideChoiceField.setItems(sideOption);

        sideChoiceField.valueProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue.equals("����")) {
                ObservableList<String> tagOptions = FXCollections.observableArrayList();
                tagOptions.add("����");
                tagChoiceField.setItems(tagOptions);
                tagChoiceField.setValue("����");
            } else {
                ObservableList<String> tagOptions = FXCollections.observableArrayList();
                for (int i = 0; i <= 10; i++) {
                    tagOptions.add(Tags.getTagNameByCode(i));
                }
                tagChoiceField.setItems(tagOptions);
                if (account.getTag().equals("����")) {
                    tagChoiceField.setValue("����");
                } else {
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
            sideChoiceField.setValue("֧��");
            moneyField.setText(String.valueOf(account.getMoney()));
        } else {
            sideChoiceField.setValue("����");
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
            if (sideChoiceField.getValue().equals("����")) {
                account.setMoney(Math.abs(Double.parseDouble(moneyField.getText())));
                account.setTag("����");
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
            errorMsg += "��������";
        } else {
            Pattern pattern = Pattern.compile("^[0-9]*$");
            Matcher matcher = pattern.matcher(moneyField.getText());
            if (!matcher.find()) {
                errorMsg += "����ʽ����";
            }
        }


        //TODO other valid check is not finished

        if (errorMsg.length() == 0) {
            return true;
        } else {
            // Show the error message.
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("ע��");
            alert.setHeaderText("������");
            alert.setContentText(errorMsg);

            alert.showAndWait();
            return false;
        }
    }
}
