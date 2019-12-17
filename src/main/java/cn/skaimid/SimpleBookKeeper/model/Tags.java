package cn.skaimid.SimpleBookKeeper.model;

import javafx.beans.binding.StringBinding;

import java.util.HashMap;
import java.util.Map;


/**
 * enum class for classify
 */
public enum Tags {
    ADD(-1, "����"),
    OTHERS(0, "����"),
    FOOD(1, "������ʳ"),
    CLOTHING_AND_BEAUTY(2, "��������"),
    DAILY(3, "��������"),
    FEE(4, "����ɷ�"),
    TRANSPORT(5, "��ͨ����"),
    TELECOMS(6, "����ͨѶ"),
    AMUSEMENT(7, "��������"),
    MEDICAL(8, "ҽ�ƽ���"),
    HOUSING(9, "ס����ҵ"),
    EDUCATION(10, "�������");

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
            return "����";
        }
    }

    public static String getTagNameByCode(Integer code) {
        if (MAP.containsKey(code)) {
            return MAP.get(code).getName();
        } else {
            return "����";
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
