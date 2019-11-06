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
    private final IntegerProperty money;
    private final ObjectProperty<LocalDate> date;
    private final StringProperty tag;
    private final StringProperty description;

    public Account() {
        this(0);
    }

    public Account(Integer money) {
        this.money = new SimpleIntegerProperty(money);
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
    public Account(Integer money, String date, Integer tag, String description) {
        this.money = new SimpleIntegerProperty(money);
        this.date = new SimpleObjectProperty<LocalDate>(DateUtil.parse(date));
        String tagDescribition = Tags.getTagNameByCode(tag);
        this.tag = new SimpleStringProperty(tagDescribition);
        this.description = new SimpleStringProperty(description);
    }

    //getter and setter

    public int getMoney() {
        return money.get();
    }

    public IntegerProperty moneyProperty() {
        return money;
    }

    public void setMoney(int money) {
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
