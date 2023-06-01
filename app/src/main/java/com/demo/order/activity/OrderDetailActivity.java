package com.demo.order.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.demo.order.App;
import com.demo.order.BaseBindingActivity;
import com.demo.order.R;
import com.demo.order.bean.Order;
import com.demo.order.databinding.ActivityOrderDetialBinding;
import com.google.android.gms.maps.model.LatLng;
import com.google.maps.GeoApiContext;
import com.google.maps.GeocodingApi;
import com.google.maps.PendingResult;
import com.google.maps.model.GeocodingResult;

import java.text.SimpleDateFormat;
import java.util.Locale;

public class OrderDetailActivity extends BaseBindingActivity<ActivityOrderDetialBinding> {

    private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy.MM.dd hh:mm:ss a", Locale.ENGLISH);

    @Override
    protected void initListener() {

    }

    @Override
    protected void initData() {

        Order detail = (Order) getIntent().getSerializableExtra("order_detail");
        viewBinder.tvSender.setText("Form sender " + App.user.username);
        viewBinder.tvStartTime.setText("Pick up time: " + dateFormat.format(Long.parseLong(detail.timestamp)));
        viewBinder.tvReceiverName.setText("To receiver: " + detail.receiverName);
        viewBinder.tvEndTime.setText("Drop off time: " + dateFormat.format(Long.parseLong(detail.timestamp) + 60 * 1000 * 60 * 2));
        viewBinder.tvWeight.setText("Weight\n" + detail.weight + "kg");
        viewBinder.tvWidth.setText("Width\n" + detail.width + "m");
        viewBinder.tvHeight.setText("Height\n" + detail.height + "m");
        viewBinder.tvLength.setText("Length\n" + detail.length + "m");
        viewBinder.tvQuantity.setText("Quantity\n26");
        viewBinder.tvType.setText("Type\n" + detail.type);
        viewBinder.face.setImageResource(detail.face);
        viewBinder.tvCreateOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ProgressDialog progressDialog = new ProgressDialog(OrderDetailActivity.this);
                progressDialog.setMessage("获取经纬度中....");
                progressDialog.show();
                // 创建 GeoApiContext 对象，使用自己的 API 密钥
                GeoApiContext geoApiContext = new GeoApiContext.Builder()
                        .apiKey(getString(R.string.google_maps_key))
                        .build();

                String address = detail.location; // 要解析的地址

// 发起地址解析请求
                GeocodingApi.geocode(geoApiContext, address).setCallback(new PendingResult.Callback<GeocodingResult[]>() {
                    @Override
                    public void onResult(GeocodingResult[] results) {
                        progressDialog.dismiss();
                        if (results != null && results.length > 0) {
                            GeocodingResult result = results[0];
                            LatLng location = new LatLng(result.geometry.location.lat, result.geometry.location.lng);
                            Log.d("TAG", "onResult: " + result.formattedAddress + "---" + location);
                            Intent intent = new Intent(OrderDetailActivity.this, MapActivity.class);
                            intent.putExtra("order", detail);
                            intent.putExtra("location", location);
                            startActivity(intent);
                            // 在这里可以使用获取到的经纬度进行后续操作
                        }
                    }

                    @Override
                    public void onFailure(Throwable e) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                progressDialog.dismiss();
                                // 处理错误情况
                                Toast.makeText(OrderDetailActivity.this, "获取经纬度错误", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                });
            }
        });
    }
}