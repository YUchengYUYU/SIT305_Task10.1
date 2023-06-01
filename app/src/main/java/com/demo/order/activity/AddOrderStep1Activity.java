package com.demo.order.activity;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

import android.content.Intent;
import android.os.Build;
import android.util.Log;

import com.demo.order.BaseBindingActivity;
import com.demo.order.bean.Order;
import com.demo.order.databinding.ActivityAddOrderStep1Binding;

import java.util.Calendar;

public class AddOrderStep1Activity extends BaseBindingActivity<ActivityAddOrderStep1Binding> {

    @Override
    protected void initListener() {

    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void initData() {

        viewBinder.tvNext.setOnClickListener(view -> {
            String receiver = viewBinder.etReceiver.getText().toString().trim();
            String location = viewBinder.etLocation.getText().toString().trim();
            int year = viewBinder.dpDate.getYear();
            int month = viewBinder.dpDate.getMonth() + 1;
            int day = viewBinder.dpDate.getDayOfMonth();
            int hour = viewBinder.tpTime.getCurrentHour();
            int minute = viewBinder.tpTime.getCurrentMinute();
            Calendar calendar = Calendar.getInstance();
            calendar.set(Calendar.YEAR, year);
            calendar.set(Calendar.MONTH, month);
            calendar.set(Calendar.DAY_OF_MONTH, day);
            calendar.set(Calendar.HOUR_OF_DAY, hour);
            calendar.set(Calendar.MINUTE, minute);
            calendar.set(Calendar.SECOND, 0);

            if (receiver.isEmpty()) {
                toast("receiver is empty");
                return;
            }
            if (location.isEmpty()) {
                toast("location is empty");
                return;
            }
            Order order = new Order();
            order.receiverName = receiver;
            order.location = location;
            order.timestamp = String.valueOf(calendar.getTimeInMillis());
            Log.d("TAG", "initData: " + order);
            startActivityForResult(AddOrderStep2Activity.class, intent -> {
                intent.putExtra("order", order);
            }, 100);

        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            setResult(RESULT_OK);
            finish();
        }
    }
}