package com.demo.order;

import android.app.Application;

import com.demo.order.bean.Order;
import com.demo.order.bean.User;

import java.util.Random;

public class App extends Application {
    public static App app;
    public static User user;

    @Override
    public void onCreate() {
        super.onCreate();
        app = this;

    }

    public static void login(User log) {
        user = log;
        if (DBHelper.getHelper().queryMyOrder().isEmpty()) {
            randomGenerateOrder();
        }
    }

    private static void randomGenerateOrder() {
        for (int i = 0; i < 6; i++) {
            Order order = new Order();
            order.receiverName = randomGenerateReceiver();
            order.timestamp = String.valueOf(randomTime());
            order.location = randomLocation();
            order.type = randomGoodsType();
            order.face = randomOrderFace();
            order.height = String.valueOf(randomInteger());
            order.weight = String.valueOf(randomInteger());
            order.width = String.valueOf(randomInteger());
            order.length = String.valueOf(randomInteger());
            order.vehicle = randomVehicle();
            order.user_id = App.user.id;
            DBHelper.getHelper().createOrder(order);
        }
    }

    private static String randomGoodsType() {
        String[] type = {"Furniture"
                , "Dry Goods"
                , "Food"
                , "Building Material"};
        int i = new Random().nextInt(type.length);
        return type[i];
    }

    private static String randomVehicle() {
        String[] type = {
                "Truck"
                , "Van"
                , "Refrigerated Truck"
                , "Mini Truck"};
        int i = new Random().nextInt(type.length);
        return type[i];
    }

    public static String randomGenerateReceiver() {
        String[] names = new String[]{
                "Jack",
                "Tom",
                "Aaron",
                "Alex",
                "Cecil",
                "Christian",
                "Denny",
                "Dylan",
                "Edison",
                "Daniel",
                "Ford",
                "Gary",
                "Lorin",
                "Hunk",
                "Jordan",
                "Keith",
                "Shawn",
                "Mars",
                "Tommy",
                "Emily"};
        int i = new Random().nextInt(names.length);
        return names[i];
    }

    public static int randomInteger() {
        return new Random().nextInt(200) + 1;
    }

    private static long randomTime() {
        int i = new Random().nextInt(60) + 1;
        return System.currentTimeMillis() - i * 60 * 1000;
    }

    private static String randomLocation() {
        String[] names = new String[]{
                "Melbourne",
                "Sydney",
                "Beijing",
                "Los Angeles",
                "Chicago",
                "Houston",
                "Philadelphia",
                "Boston",
                "Munich",
                "England",
                "Hamburg",
                "Denmark",
                "Athens"};
        int i = new Random().nextInt(names.length);
        return names[i];
    }

    private static int randomOrderFace() {
        int[] faces = new int[]{
                R.drawable.goods_type_1,
                R.drawable.goods_type_2,
                R.drawable.goods_type_3};
        int i = new Random().nextInt(faces.length);
        return faces[i];
    }

    public static int randomCarFace() {
        int[] faces = new int[]{
                R.drawable.car_1,
                R.drawable.car_2,
                R.drawable.car_3,
                R.drawable.car_4,
                R.drawable.car_5,
                R.drawable.car_6};
        int i = new Random().nextInt(faces.length);
        return faces[i];
    }

    public static User getUser() {
        return user;
    }
}
