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
    private void handleExport() {
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
            cell.setCellValue(SqlTimeUtil.formate(tempAccount.getDate()));
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
        alert.setContentText("This file is part of SimpleBookKeeper.\n" +
                "Simple BookKeeper is a planning tool for book keeping\n" +
                "\n" +
                "Copyright (C) 2019 by Skaimid: skaimid@saltyfiah.tech\n" +
                "\n" +
                "Simple BookKeeper is free software: you can redistribute it and/or modify" +
                "it under the terms of the GNU General Public License as published by" +
                "the Free Software Foundation, either version 3 of the License, or" +
                "(at your option) any later version.\n" +
                "\n" +
                "You should have received a copy of the GNU General Public License" +
                "along with Simple BookKeeper.  If not, see <http://www.gnu.org/licenses/>.");

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
