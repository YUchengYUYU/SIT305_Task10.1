package com.demo.order.activity;

import static android.Manifest.permission.ACCESS_COARSE_LOCATION;
import static android.Manifest.permission.ACCESS_FINE_LOCATION;
import static android.Manifest.permission.CALL_PHONE;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

import androidx.core.app.ActivityCompat;

import com.demo.order.BaseBindingActivity;
import com.demo.order.DBHelper;
import com.demo.order.databinding.ActivityLoginBinding;

public class LoginActivity extends BaseBindingActivity<ActivityLoginBinding> {

    @Override
    protected void initListener() {

    }

    @Override
    protected void initData() {
        ActivityCompat.requestPermissions(this, new String[]{ACCESS_FINE_LOCATION, WRITE_EXTERNAL_STORAGE, ACCESS_COARSE_LOCATION, CALL_PHONE}, 1);
        viewBinder.tvLogin.setOnClickListener(view -> {
            String username = viewBinder.etUsername.getText().toString().trim();
            String password = viewBinder.etPassword.getText().toString().trim();
            if (username.isEmpty()) {
                toast("username is empty");
                return;
            }
            if (password.isEmpty()) {
                toast("password is empty");
                return;
            }
            if (DBHelper.getHelper().login(username, password)) {
                startActivity(MainActivity.class);
                toast("login successful");
                finish();
            } else {
                toast("wrong user name or password");
            }
        });
        viewBinder.tvSignUp.setOnClickListener(view -> {
            startActivity(SignActivity.class);
        });
    }
}