package com.demo.order.bean;

import java.io.Serializable;

public class Order implements Serializable {
    public int id;
    public int user_id;
    public int face;
    public String receiverName;
    public String location;
    public String timestamp;
    public String type;
    public String weight;
    public String width;
    public String length;
    public String height;
    public String vehicle;

    @Override
    public String toString() {
        return "Order{" +
                "id=" + id +
                ", user_id=" + user_id +
                ", receiverName='" + receiverName + '\'' +
                ", location='" + location + '\'' +
                ", timestamp='" + timestamp + '\'' +
                ", type='" + type + '\'' +
                ", weight='" + weight + '\'' +
                ", width='" + width + '\'' +
                ", length='" + length + '\'' +
                ", height='" + height + '\'' +
                ", vehicle='" + vehicle + '\'' +
                '}';
    }
}
