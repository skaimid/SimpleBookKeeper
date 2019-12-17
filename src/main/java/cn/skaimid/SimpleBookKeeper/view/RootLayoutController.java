package cn.skaimid.SimpleBookKeeper.view;

import cn.skaimid.SimpleBookKeeper.MainApp;
import cn.skaimid.SimpleBookKeeper.model.Account;
import cn.skaimid.SimpleBookKeeper.util.SqlTimeUtil;
import cn.skaimid.SimpleBookKeeper.util.SqlUtil;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.stage.FileChooser;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.Queue;
import java.util.stream.IntStream;


public class RootLayoutController {
    private MainApp mainApp;

    public void setMainApp(MainApp mainApp) {
        this.mainApp = mainApp;
    }

    @FXML
    public void handleExport() {
        FileChooser fileChooser = new FileChooser();

        FileChooser.ExtensionFilter extensionFilter = new FileChooser.ExtensionFilter(
                "Excel 工作簿", "*.xlsx");
        fileChooser.getExtensionFilters().add(extensionFilter);
        File file = fileChooser.showSaveDialog(mainApp.getPrimaryStage());
        if (!file.getPath().endsWith(".xlsx")) {
            file = new File(file.getPath() + ".xlsx");
        }

        Workbook wb = new XSSFWorkbook();
        try (OutputStream fileOut = new FileOutputStream(file.getPath())) {
            wb.write(fileOut);
        } catch (Exception e) {
            e.printStackTrace();
        }

        Sheet sheet = wb.createSheet();

        Queue<Account> accountQueue = SqlUtil.handleTraversing();
        Account tempAccount;
        Row row = sheet.createRow(0);
        row.createCell(0).setCellValue("编号");
        row.createCell(1).setCellValue("金额");
        row.createCell(2).setCellValue("标签");
        row.createCell(3).setCellValue("日期");
        row.createCell(4).setCellValue("描述");
        int i = 1;
        CellStyle cellStyle = wb.createCellStyle();
        CreationHelper createHelper = wb.getCreationHelper();
        cellStyle.setDataFormat(
                createHelper.createDataFormat().getFormat("yyyy-MM-dd"));
        while (!accountQueue.isEmpty()) {
            tempAccount = accountQueue.poll();
            row = sheet.createRow(i);
            row.createCell(0).setCellValue(i);
            row.createCell(1).setCellValue(tempAccount.getMoney());
            row.createCell(2).setCellValue(tempAccount.getTag());
            Cell cell = row.createCell(3);
            cell.setCellValue(SqlTimeUtil.format(tempAccount.getDate()));
            cell.setCellStyle(cellStyle);
            row.createCell(4).setCellValue(tempAccount.getDescription());
            i++;
        }
        sheet.autoSizeColumn(1);
        IntStream.range(3, 5).forEach(sheet::autoSizeColumn);

        try (OutputStream fileOut = new FileOutputStream(file.getPath())) {
            wb.write(fileOut);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleExit() {
        System.exit(0);
    }

    @FXML
    private void handleAbout() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Simple BookKeeper");
        alert.setHeaderText("About");
        alert.setContentText("Simple Book Keeper \n" +
                "Version: '1.0-SNAPSHOT'\n\n" +
                "Author: Skaimid\n" +
                "Website: saltyfish.tech\n" +
                "Github: null(");

        alert.showAndWait();
    }

    @FXML
    private void handleLicense() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Simple BookKeeper");
        alert.setHeaderText("License");
        alert.setHeight(400);
        alert.setWidth(300);
        alert.setContentText("Simple BookKeeper\n" +
                "　　版权所有（C）2019 skaimid\n" +
                "　　本程序为自由软件，在自由软件联盟发布的GNU通用公共许可协议的约束下，你可以对其进行再发布及修改。协议版本为第三版或（随你）更新的版本。\n" +
                "　　我们希望发布的这款程序有用，但不保证，甚至不保证它有经济价值和适合特定用途。" +
                "详情参见GNU通用公共许可协议。\n" +
                "<https://www.gnu.org/licenses/gpl-3.0.html>");

        alert.showAndWait();
    }

    @FXML
    private void handleCategoryPieChart() {
        mainApp.showCategoryPieChart();
    }

    @FXML
    private void handleIncomeAndExpenditureChart() {
        mainApp.showIncomeAndExpenditureChart();
    }
}
