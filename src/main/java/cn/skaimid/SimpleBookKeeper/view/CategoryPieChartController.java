package cn.skaimid.SimpleBookKeeper.view;

import cn.skaimid.SimpleBookKeeper.model.Account;
import cn.skaimid.SimpleBookKeeper.model.Tags;
import cn.skaimid.SimpleBookKeeper.util.SqlUtil;
import javafx.fxml.FXML;
import javafx.scene.chart.PieChart;

import java.util.Queue;
import java.util.stream.IntStream;

public class CategoryPieChartController {

    @FXML
    public PieChart pieChart;


    @FXML
    public void initialize() {
        Queue<Account> accountQueue = SqlUtil.handleTraversing();
        Double[] categoryData = IntStream.range(0, 11).mapToObj(i -> 0.0).toArray(Double[]::new);
        Double sum = 0.0;
        Account tempAccount;
        while (!accountQueue.isEmpty()) {
            tempAccount = accountQueue.poll();
            if (tempAccount.getMoney() < 0) {
                categoryData[Tags.getCodeByTagName(tempAccount.getTag())] += tempAccount.getMoney();
                sum += tempAccount.getMoney();
            }
        }
        for (int i = 0; i < 11; i++) {
            pieChart.getData().add(new PieChart.Data(Tags.getTagNameByCode(i), (categoryData[i] / sum) * 100));
        }

    }
}
