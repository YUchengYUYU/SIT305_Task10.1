package com.demo.order.activity;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;

import com.demo.order.BaseBindingActivity;
import com.demo.order.DBHelper;
import com.demo.order.bean.Order;
import com.demo.order.R;
import com.demo.order.databinding.ActivityAddOrderStep2Binding;

import java.util.Random;

public class AddOrderStep2Activity extends BaseBindingActivity<ActivityAddOrderStep2Binding> {

    private int randomOrderFace() {
        int[] faces = new int[]{
                R.drawable.goods_type_1,
                R.drawable.goods_type_2,
                R.drawable.goods_type_3};
        int i = new Random().nextInt(faces.length);
        return faces[i];
    }

    @Override
    protected void initListener() {
        viewBinder.etTypeOther.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (!editable.toString().trim().isEmpty()) {
                    changeGoodsType(5);
                }
            }
        });
        viewBinder.etVehicleOther.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (!editable.toString().trim().isEmpty()) {
                    changeVehicleType(5);
                }
            }
        });
        viewBinder.tvFurniture.setOnClickListener(view -> {
            changeGoodsType(1);
        });
        viewBinder.tvDryGoods.setOnClickListener(view -> {
            changeGoodsType(2);
        });
        viewBinder.tvFood.setOnClickListener(view -> {
            changeGoodsType(3);
        });
        viewBinder.tvBuildingMaterial.setOnClickListener(view -> {
            changeGoodsType(4);
        });
        viewBinder.tvTruck.setOnClickListener(view -> {
            changeVehicleType(1);
        });
        viewBinder.tvVan.setOnClickListener(view -> {
            changeVehicleType(2);
        });
        viewBinder.tvRefrigerated.setOnClickListener(view -> {
            changeVehicleType(3);
        });
        viewBinder.tvMiniTruck.setOnClickListener(view -> {
            changeVehicleType(4);
        });
        viewBinder.tvCreateOrder.setOnClickListener(view -> {
            String weight = viewBinder.etWeight.getText().toString().trim();
            String width = viewBinder.etWidth.getText().toString().trim();
            String length = viewBinder.etLength.getText().toString().trim();
            String height = viewBinder.etHeight.getText().toString().trim();

            if (weight.isEmpty()) {
                toast("weight is empty");
                return;
            }
            if (width.isEmpty()) {
                toast("width is empty");
                return;
            }
            if (length.isEmpty()) {
                toast("length is empty");
                return;
            }
            if (height.isEmpty()) {
                toast("height is empty");
                return;
            }
            if (currentGoodsTpe.isEmpty()) {
                currentGoodsTpe = viewBinder.etTypeOther.getText().toString().trim();
                if (currentGoodsTpe.isEmpty()) {
                    toast("goods type is empty");
                    return;
                }
            }
            if (currentVehicleTpe.isEmpty()) {
                currentVehicleTpe = viewBinder.etVehicleOther.getText().toString().trim();
                if (currentVehicleTpe.isEmpty()) {
                    toast("vehicle type is empty");
                    return;
                }
            }
            order.weight = weight;
            order.width = width;
            order.length = length;
            order.height = height;
            order.vehicle = currentVehicleTpe;
            order.type = currentGoodsTpe;
            order.face = randomOrderFace();
            Log.d("TAG", "initListener: " + order);
            DBHelper.getHelper().createOrder(order);
            setResult(RESULT_OK);
            finish();
        });
    }

    private Order order;

    @Override
    protected void initData() {
        order = (Order) getIntent().getSerializableExtra("order");

    }

    private String currentGoodsTpe = "";
    private String currentVehicleTpe = "";

    private void changeVehicleType(int type) {
        switch (type) {
            case 1:
                currentVehicleTpe = "Truck";
                break;
            case 2:
                currentVehicleTpe = "Van";
                break;
            case 3:
                currentVehicleTpe = "Refrigerated Truck";
                break;
            case 4:
                currentVehicleTpe = "Mini Truck";
                break;
            case 5:
                currentVehicleTpe = "";
                break;
        }
        viewBinder.tvTruck.setBackgroundColor(type == 1 ? getResources().getColor(R.color.select) : -1);
        viewBinder.tvVan.setBackgroundColor(type == 2 ? getResources().getColor(R.color.select) : -1);
        viewBinder.tvRefrigerated.setBackgroundColor(type == 3 ? getResources().getColor(R.color.select) : -1);
        viewBinder.tvMiniTruck.setBackgroundColor(type == 4 ? getResources().getColor(R.color.select) : -1);
    }

    private void changeGoodsType(int type) {
        switch (type) {
            case 1:
                currentGoodsTpe = "Furniture";
                break;
            case 2:
                currentGoodsTpe = "Dry Goods";
                break;
            case 3:
                currentGoodsTpe = "Food";
                break;
            case 4:
                currentGoodsTpe = "Building Material";
                break;
            case 5:
                currentGoodsTpe = "";
                break;
        }
        viewBinder.tvFurniture.setBackgroundColor(type == 1 ? getResources().getColor(R.color.select) : -1);
        viewBinder.tvDryGoods.setBackgroundColor(type == 2 ? getResources().getColor(R.color.select) : -1);
        viewBinder.tvFood.setBackgroundColor(type == 3 ? getResources().getColor(R.color.select) : -1);
        viewBinder.tvBuildingMaterial.setBackgroundColor(type == 4 ? getResources().getColor(R.color.select) : -1);
    }
}