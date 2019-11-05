package cn.skaimid.SimpleBookKeeper;

import cn.skaimid.SimpleBookKeeper.model.Account;
import cn.skaimid.SimpleBookKeeper.view.ItemEditDialogController;
import cn.skaimid.SimpleBookKeeper.view.ItemOverviewController;

import javafx.application.Application;
import javafx.collections.FXCollections;
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
    private ObservableList<Account> accountData = FXCollections.observableArrayList();

    public MainApp() {
        // TODO 通过数据库来读取 account 条目数据
        accountData.add(new Account(5, "01.01.2077", 0, "cyberpunk 2077"));
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

    /**
     * Initializes the root layout.
     */
    private void initRootLayout() {
        try {
            // Load root layout from fxml file.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MainApp.class.getResource("view/RootLayout.fxml"));
            rootLayout = (BorderPane) loader.load();

            // Show the scene containing the root layout.
            Scene scene = new Scene(rootLayout);
            primaryStage.setScene(scene);
            primaryStage.show();
        } catch (IOException e) {
            e.printStackTrace();
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
            AnchorPane itemOverview = (AnchorPane) loader.load();

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
