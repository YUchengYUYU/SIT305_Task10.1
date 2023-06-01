package com.demo.order.bean;

public class Car {
    public Car(String name, int price, int face) {
        this.name = name;
        this.price = price;
        this.face = face;
    }

   public String name;
   public int price;
   public int face;

    @Override
    public String toString() {
        return "Car{" +
                "name='" + name + '\'' +
                ", price=" + price +
                ", face=" + face +
                '}';
    }
}
