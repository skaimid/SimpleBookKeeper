package cn.skaimid.SimpleBookKeeper.model;

import javafx.beans.binding.StringBinding;

import java.util.HashMap;
import java.util.Map;


/**
 * enum class for classify
 */
public enum Tags {
    ADD(-1, "收入"),
    OTHERS(0, "其他"),
    FOOD(1, "餐饮美食"),
    CLOTHING_AND_BEAUTY(2, "服饰美容"),
    DAILY(3, "生活日用"),
    FEE(4, "生活缴费"),
    TRANSPORT(5, "交通出行"),
    TELECOMS(6, "物流通讯"),
    AMUSEMENT(7, "休闲娱乐"),
    MEDICAL(8, "医疗健康"),
    HOUSING(9, "住房物业"),
    EDUCATION(10, "文体教育");

    private Integer code;
    private String name;

    private static final Map<Integer, Tags> MAP = new HashMap<>();


    static {
        for (Tags tags : Tags.values()) {
            MAP.put(tags.getCode(), tags);
        }
    }

    Tags(Integer code, String name) {
        this.code = code;
        this.name = name;
    }

    public Integer getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    public static String getTagNameByCode(StringBinding code) {
        if (MAP.containsKey(code)) {
            return MAP.get(code).getName();
        } else {
            return "其他";
        }
    }

    public static String getTagNameByCode(Integer code) {
        if (MAP.containsKey(code)) {
            return MAP.get(code).getName();
        } else {
            return "其他";
        }
    }

    public static Integer getCodeByTagName(String tag) {
        for (int i = -1; i <= 10; i++) {
            if (getTagNameByCode(i).equals(tag)) {
                return i;
            }
        }
        return 0;
    }
}
