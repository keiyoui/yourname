package com.neo;

public class NameEntitiy {
    String id;
    String chinese;//汉字
    int sum;// 笔画数
    String stroke;// 笔顺
    String UNicode;
    String GB;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getChinese() {
        return chinese;
    }

    public void setChinese(String chinese) {
        this.chinese = chinese;
    }

    public int getSum() {
        return sum;
    }

    public void setSum(int sum) {
        this.sum = sum;
    }

    public String getStroke() {
        return stroke;
    }

    public void setStroke(String stroke) {
        this.stroke = stroke;
    }

    public String getUNicode() {
        return UNicode;
    }

    public void setUNicode(String UNicode) {
        this.UNicode = UNicode;
    }

    public String getGB() {
        return GB;
    }

    public void setGB(String GB) {
        this.GB = GB;
    }
}
