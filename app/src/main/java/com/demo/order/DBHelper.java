package com.demo.order;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.demo.order.bean.Order;
import com.demo.order.bean.User;

import java.util.ArrayList;
import java.util.List;

public class DBHelper extends SQLiteOpenHelper {
    private static DBHelper helper = new DBHelper();

    public static DBHelper getHelper() {
        return helper;
    }

    private DBHelper() {
        super(App.app, "car.db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("create table if not exists user (" +
                "id integer primary key autoincrement," +
                "full_name text," +
                "username text," +
                "password text," +
                "face text," +
                "phone text)");
        sqLiteDatabase.execSQL("create table if not exists car_order (" +
                "id integer primary key autoincrement," +
                "user_id integer," +
                "receiver_name text," +
                "timestamp text," +
                "type text," +
                "location text," +
                "face integer," +
                "weight text," +
                "width text," +
                "length text," +
                "height text," +
                "vehicle text)");


    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
    }

    public boolean signUp(String fullName, String username, String password, String phone, String face) {
        SQLiteDatabase writableDatabase = getWritableDatabase();
        Cursor cursor = writableDatabase.rawQuery("select * from user where phone=? or username=?", new String[]{phone, username});
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                cursor.close();
                return false;
            }
        }
        writableDatabase.execSQL("insert into user(full_name,username,password,face,phone) values(?,?,?,?,?)",
                new Object[]{fullName, username, password, face, phone});
        return true;
    }

    @SuppressLint("Range")
    public boolean login(String username, String password) {
        SQLiteDatabase writableDatabase = getWritableDatabase();
        Cursor cursor = writableDatabase.rawQuery("select * from user where username=?", new String[]{username});
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                if (getAnString(cursor, "password").equals(password)) {
                    User user = new User();
                    user.id = getAnInt(cursor, "id");
                    user.username = getAnString(cursor, "username");
                    user.fullName = getAnString(cursor, "full_name");
                    user.password = getAnString(cursor, "password");
                    user.phone = getAnString(cursor, "phone");
                    user.face = getAnString(cursor, "face");
                    App.login(user);
                }
                return true;
            }
        }
        return false;
    }

    @SuppressLint("Range")
    private String getAnString(Cursor cursor, String index) {
        return cursor.getString(cursor.getColumnIndex(index));
    }

    @SuppressLint("Range")
    private int getAnInt(Cursor cursor, String index) {
        return cursor.getInt(cursor.getColumnIndex(index));
    }

    public void createOrder(Order order) {
//     ("create table if not exists car_order (" +
//                "id integer primary key autoincrement," +
//                "user_id integer," +
//                "receiver_name text," +
//                "timestamp text," +
//                "type text," +
//                "location text," +
//                "weight text," +
//                "width text," +
//                "length text," +
//                "height text," +
//                "vehicle text)");
        ContentValues contentValues = new ContentValues();
        contentValues.put("receiver_name", order.receiverName);
        contentValues.put("timestamp", order.timestamp);
        contentValues.put("type", order.type);
        contentValues.put("location", order.location);
        contentValues.put("weight", order.weight);
        contentValues.put("width", order.width);
        contentValues.put("length", order.length);
        contentValues.put("height", order.height);
        contentValues.put("vehicle", order.vehicle);
        contentValues.put("user_id", App.user.id);
        contentValues.put("face", order.face);
        SQLiteDatabase writableDatabase = getWritableDatabase();
        writableDatabase.insert("car_order", null, contentValues);
    }

    public List<Order> queryMyOrder() {
        ArrayList<Order> orders = new ArrayList<>();
        SQLiteDatabase writableDatabase = getWritableDatabase();
        Cursor cursor = writableDatabase.rawQuery("select * from car_order where user_id = ?", new String[]{App.user.id + ""});
        if (cursor != null) {
            while (cursor.moveToNext()) {
                Order order = new Order();
                order.id = getAnInt(cursor, "id");
                order.receiverName = getAnString(cursor, "receiver_name");
                order.timestamp = getAnString(cursor, "timestamp");
                order.type = getAnString(cursor, "type");
                order.location = getAnString(cursor, "location");
                order.weight = getAnString(cursor, "weight");
                order.width = getAnString(cursor, "width");
                order.length = getAnString(cursor, "length");
                order.height = getAnString(cursor, "height");
                order.vehicle = getAnString(cursor, "vehicle");
                order.user_id = getAnInt(cursor, "user_id");
                order.face = getAnInt(cursor, "face");
                orders.add(order);
            }
        }
        return orders;
    }

    public List<Order> queryAllOrder() {
        ArrayList<Order> orders = new ArrayList<>();
        SQLiteDatabase writableDatabase = getWritableDatabase();
        Cursor cursor = writableDatabase.rawQuery("select * from car_order where user_id = ?", new String[]{App.user.id + ""});
        if (cursor != null) {
            while (cursor.moveToNext()) {
                Order order = new Order();
                order.id = getAnInt(cursor, "id");
                order.receiverName = getAnString(cursor, "receiver_name");
                order.timestamp = getAnString(cursor, "timestamp");
                order.type = getAnString(cursor, "type");
                order.location = getAnString(cursor, "location");
                order.weight = getAnString(cursor, "weight");
                order.width = getAnString(cursor, "width");
                order.length = getAnString(cursor, "length");
                order.height = getAnString(cursor, "height");
                order.vehicle = getAnString(cursor, "vehicle");
                order.user_id = getAnInt(cursor, "user_id");
                order.face = getAnInt(cursor, "face");
                orders.add(order);
            }
        }
        return orders;
    }
}
