package com.demo.order.activity;

import androidx.annotation.Nullable;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;

import com.demo.order.BaseBindingActivity;
import com.demo.order.DBHelper;
import com.demo.order.databinding.ActivitySignBinding;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class SignActivity extends BaseBindingActivity<ActivitySignBinding> {
    @Override
    protected void initListener() {

    }

    private String face = "";

    @Override
    protected void initData() {
        viewBinder.clTakePhoto.setOnClickListener(view -> {
            new AlertDialog.Builder(this).setMessage("Allow the app to access photos,media and files on your device?").setNegativeButton("Allow", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    Intent intent = new Intent(Intent.ACTION_PICK);
                    intent.setType("image/*");
                    startActivityForResult(intent, 100);
                }
            }).setPositiveButton("Deny", null).show();

        });

        viewBinder.tvCrateAccount.setOnClickListener(view -> {
            String fullName = viewBinder.etFullName.getText().toString().trim();
            String username = viewBinder.etUsername.getText().toString().trim();
            String password = viewBinder.etPassword.getText().toString().trim();
            String phone = viewBinder.etPhone.getText().toString().trim();
            String confirmPassword = viewBinder.etConfirmPassword.getText().toString().trim();
            if (fullName.isEmpty()) {
                toast("full name is empty");
                return;
            }
            if (username.isEmpty()) {
                toast("username is empty");
                return;
            }
            if (password.isEmpty()) {
                toast("password is empty");
                return;
            }
            if (confirmPassword.isEmpty()) {
                toast("confirm password is empty");
                return;
            }
            if (!password.equals(confirmPassword)) {
                toast("confirm password error");
                return;
            }
            if (phone.isEmpty()) {
                toast("phone is empty");
                return;
            }
            if (face.isEmpty()) {
                toast("please select an avatar");
                return;
            }
            if (DBHelper.getHelper().signUp(fullName, username, password, phone, face)) {
                toast("registration success");
            } else {
                toast("Username or mobile number already in use");
            }

        });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            Uri imgUri = data.getData();
            viewBinder.cream.setImageURI(imgUri);
            try {
                InputStream inputStream = getContentResolver().openInputStream(imgUri);
                byte[] bytes = new byte[inputStream.available()];
                inputStream.read(bytes);
                File file = new File(getExternalCacheDir(), System.currentTimeMillis() + ".jpg");
                FileOutputStream fileOutputStream = new FileOutputStream(file);
                fileOutputStream.write(bytes);
                fileOutputStream.flush();
                fileOutputStream.close();
                face = file.getPath();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}