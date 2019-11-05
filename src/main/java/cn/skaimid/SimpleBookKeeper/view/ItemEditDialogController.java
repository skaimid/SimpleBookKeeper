package cn.skaimid.SimpleBookKeeper.view;

import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class ItemEditDialogController {
    @FXML
    private TextField moneyField;

    @FXML
    private TextArea descriptionField;

    @FXML
    private DatePicker datePicker = new DatePicker();


    @FXML
    private ChoiceBox tagChoiceField = new ChoiceBox();

}
