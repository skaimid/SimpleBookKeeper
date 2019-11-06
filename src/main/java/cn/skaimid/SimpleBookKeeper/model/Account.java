package cn.skaimid.SimpleBookKeeper.model;


import java.time.*;

import cn.skaimid.SimpleBookKeeper.util.DateUtil;
import javafx.beans.property.*;

/**
 * Model class for a account.
 *
 * @author Skaimid
 */
public class Account {

    private final IntegerProperty id;
    private final DoubleProperty money;
    private final ObjectProperty<LocalDate> date;
    private final StringProperty tag;
    private final StringProperty description;

    public Account() {
        this(0.0);
    }

    public Account(Double money) {
        this.id = new SimpleIntegerProperty(0);
        this.money = new SimpleDoubleProperty(money);
        this.date = new SimpleObjectProperty<LocalDate>(LocalDate.now());
        String tagDescribition = Tags.getTagNameByCode(0);
        this.tag = new SimpleStringProperty(tagDescribition);
        this.description = new SimpleStringProperty("No description was given.");
    }

    /**
     * Constructor with some initial data.
     *
     * @param money
     * @param date
     * @param tag
     * @param description
     */
    // code below might not be used
//    public Account(Integer id,Double money, String date, Integer tag, String description) {
//        this.id = new SimpleIntegerProperty(id);
//        this.money = new SimpleDoubleProperty(money);
//        this.date = new SimpleObjectProperty<LocalDate>(DateUtil.parse(date));
//        String tagDescribition = Tags.getTagNameByCode(tag);
//        this.tag = new SimpleStringProperty(tagDescribition);
//        this.description = new SimpleStringProperty(description);
//    }
    public Account(Integer id, Double money, LocalDate date, Integer tag, String description) {
        this.id = new SimpleIntegerProperty(id);
        this.money = new SimpleDoubleProperty(money);
        this.date = new SimpleObjectProperty<LocalDate>(date);
        String tagDescribition = Tags.getTagNameByCode(tag);
        this.tag = new SimpleStringProperty(tagDescribition);
        this.description = new SimpleStringProperty(description);
    }

    //getter and setter


    public int getId() {
        return id.get();
    }

    public IntegerProperty idProperty() {
        return id;
    }

    public double getMoney() {
        return money.get();
    }

    public void setId(int id) {
        this.id.set(id);
    }

    public DoubleProperty moneyProperty() {
        return money;
    }

    public void setMoney(double money) {
        this.money.set(money);
    }

    public LocalDate getDate() {
        return date.get();
    }

    public ObjectProperty<LocalDate> dateProperty() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date.set(date);
    }

    public String getTag() {
        return tag.get();
    }

    public StringProperty tagProperty() {
        return tag;
    }

    public void setTag(Integer tag) {
        this.tag.set(Tags.getTagNameByCode(tag));
    }

    public void setTag(String tag) {
        this.tag.set(tag);
    }

    public String getDescription() {
        return description.get();
    }

    public StringProperty descriptionProperty() {
        return description;
    }

    public void setDescription(String description) {
        this.description.set(description);
    }
}
