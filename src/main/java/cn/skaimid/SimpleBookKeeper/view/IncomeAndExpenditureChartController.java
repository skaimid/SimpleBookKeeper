package cn.skaimid.SimpleBookKeeper.view;

import cn.skaimid.SimpleBookKeeper.model.Account;
import cn.skaimid.SimpleBookKeeper.util.SqlUtil;
import javafx.fxml.FXML;
import javafx.scene.chart.PieChart;

import java.util.Queue;


import static java.lang.Math.abs;

public class IncomeAndExpenditureChartController {

    @FXML
    private PieChart pieChart;


    @FXML
    private void initialize() {
        Queue<Account> accountQueue = SqlUtil.handleTraversing();
        Double income = 0.0, expenditure = 0.0;
        Double sum = 0.0;
        Account tempAccount;
        while (!accountQueue.isEmpty()) {
            tempAccount = accountQueue.poll();
            if (tempAccount.getMoney() < 0) {
                expenditure += abs(tempAccount.getMoney());
            } else {
                income += tempAccount.getMoney();
            }
        }
        pieChart.getData().add(new PieChart.Data("收入", income / (income + expenditure) * 100));
        pieChart.getData().add(new PieChart.Data("支出", expenditure / (income + expenditure) * 100));
    }
}
