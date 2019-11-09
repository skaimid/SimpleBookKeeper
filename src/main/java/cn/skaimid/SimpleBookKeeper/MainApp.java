package cn.skaimid.SimpleBookKeeper;

import cn.skaimid.SimpleBookKeeper.model.Account;
import cn.skaimid.SimpleBookKeeper.util.SqlUtil;
import cn.skaimid.SimpleBookKeeper.view.CategoryPieChartController;
import cn.skaimid.SimpleBookKeeper.view.ItemEditDialogController;
import cn.skaimid.SimpleBookKeeper.view.ItemOverviewController;
import cn.skaimid.SimpleBookKeeper.view.RootLayoutController;
import javafx.application.Application;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;

// a significant bug:
// the file need to be encoded as GBK to avoid chaos especially in java 11

public class MainApp extends Application {
    private Stage primaryStage;
    private BorderPane rootLayout;

    /**
     * The data as an observable list of Account.
     */
    private ObservableList<Account> accountData;

    public MainApp() {
        accountData = SqlUtil.handleSearch("select * from account");
    }

    public ObservableList<Account> getAccountData() {
        return accountData;
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        this.primaryStage = primaryStage;
        this.primaryStage.setTitle("BookKeeperApp");

        initRootLayout();

        showItemOverview();
    }


    public void showCategoryPieChart() {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MainApp.class.getResource("view/CategoryPieChart.fxml"));
            AnchorPane page = loader.load();
            Stage dialogStage = new Stage();
            dialogStage.setTitle("Category Statistics Pie Chart");
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.initOwner(primaryStage);
            Scene scene = new Scene(page);
            dialogStage.setScene(scene);

            dialogStage.show();
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("1");
        }
    }


    /**
     * Shows the every item overview inside the root layout.
     */
    private void showItemOverview() {
        try {
            // Load person overview.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MainApp.class.getResource("view/ItemOverview.fxml"));
            AnchorPane itemOverview = loader.load();

            // Set person overview into the center of root layout.
            rootLayout.setCenter(itemOverview);

            // Give the controller access to the main app.
            ItemOverviewController controller = loader.getController();
            controller.setMainApp(this);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Opens a dialog to edit details for the specified person. If the user
     * clicks OK, the changes are saved into the provided person object and true
     * is returned.
     *
     * @param account the person object to be edited
     * @return true if the user clicked OK, false otherwise.
     */
    public boolean showItemEditDialog(Account account) {
        try {
            // Load the fxml file and create a new stage for the popup dialog.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MainApp.class.getResource("view/ItemEditDialog.fxml"));
            AnchorPane page = loader.load();

            // Create the dialog Stage.
            Stage dialogStage = new Stage();
            dialogStage.setTitle("Edit Person");
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.initOwner(primaryStage);
            Scene scene = new Scene(page);
            dialogStage.setScene(scene);

            // Set the person into the controller.
            ItemEditDialogController controller = loader.getController();
            controller.setDialogStage(dialogStage);
            controller.setAccount(account);

            // Show the dialog and wait until the user closes it
            dialogStage.showAndWait();

            return controller.isOkClicked();
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    private void initRootLayout() {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MainApp.class
                    .getResource("view/RootLayout.fxml"));
            rootLayout = loader.load();

            // Show the scene containing the root layout.
            Scene scene = new Scene(rootLayout);
            primaryStage.setScene(scene);

            // Give the controller access to the main app.
            RootLayoutController controller = loader.getController();
            controller.setMainApp(this);
            // lock windows size
            primaryStage.setResizable(false);
            primaryStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    /**
     * Returns the main stage.
     *
     * @return
     */
    public Stage getPrimaryStage() {
        return primaryStage;
    }

    public static void main(String[] args) {
        launch(args);

    }
}
