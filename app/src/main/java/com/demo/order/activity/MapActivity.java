package com.demo.order.activity;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;

import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;

import com.demo.order.BaseBindingActivity;
import com.demo.order.DirectionsResponse;
import com.demo.order.R;
import com.demo.order.bean.Order;
import com.demo.order.databinding.ActivityMapBinding;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.wallet.AutoResolveHelper;
import com.google.android.gms.wallet.CardRequirements;
import com.google.android.gms.wallet.PaymentData;
import com.google.android.gms.wallet.PaymentDataRequest;
import com.google.android.gms.wallet.PaymentsClient;
import com.google.android.gms.wallet.TransactionInfo;
import com.google.android.gms.wallet.Wallet;
import com.google.android.gms.wallet.WalletConstants;
import com.google.maps.GeoApiContext;
import com.google.maps.android.PolyUtil;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;



public class MapActivity extends BaseBindingActivity<ActivityMapBinding> {
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;
    private GoogleMap googleMap;
    private static final int LOAD_PAYMENT_DATA_REQUEST_CODE = 1;
    private PaymentsClient paymentsClient;

    @Override
    protected void initListener() {
        viewBinder.tvBookNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requestPayment();
            }
        });
    }

    private Order order;
    private LatLng over;

    @Override
    protected void initData() {
        // Initialize Google Pay
        initGooglePay();

        order = (Order) getIntent().getSerializableExtra("order");
        over = getIntent().getParcelableExtra("location");

        viewBinder.mapView.onCreate(null);
        viewBinder.mapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(@NonNull GoogleMap googleMap) {
                MapActivity.this.googleMap = googleMap;
                showCurrentLocation();
            }
        });
        viewBinder.tvCallDriver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_CALL);
                Uri data = Uri.parse("tel:" + "1888888888");
                intent.setData(data);
                startActivity(intent);

            }
        });
        initListener();
    }

    private void initGooglePay() {
        Wallet.WalletOptions walletOptions = new Wallet.WalletOptions.Builder()
                .setEnvironment(WalletConstants.ENVIRONMENT_TEST)
                .build();
        paymentsClient = Wallet.getPaymentsClient(this, walletOptions);
    }

    private void requestPayment() {
        PaymentDataRequest request = createPaymentDataRequest();
        if (request != null) {
            AutoResolveHelper.resolveTask(
                    paymentsClient.loadPaymentData(request),
                    this,
                    LOAD_PAYMENT_DATA_REQUEST_CODE);
        }
    }

    private PaymentDataRequest createPaymentDataRequest() {
        PaymentDataRequest.Builder request =
                PaymentDataRequest.newBuilder()
                        .setTransactionInfo(TransactionInfo.newBuilder()
                                .setTotalPriceStatus(WalletConstants.TOTAL_PRICE_STATUS_FINAL)
                                .setTotalPrice("80.00")
                                .setCurrencyCode("AUD")
                                .build())
                        // We're requesting tokenized card payment data.
                        .addAllowedPaymentMethod(WalletConstants.PAYMENT_METHOD_CARD)
                        .addAllowedPaymentMethod(WalletConstants.PAYMENT_METHOD_TOKENIZED_CARD)
                        .setCardRequirements(
                                CardRequirements.newBuilder()
                                        .addAllowedCardNetworks(Arrays.asList(
                                                WalletConstants.CARD_NETWORK_AMEX,
                                                WalletConstants.CARD_NETWORK_DISCOVER,
                                                WalletConstants.CARD_NETWORK_VISA,
                                                WalletConstants.CARD_NETWORK_MASTERCARD))
                                        .build());

        return request.build();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case LOAD_PAYMENT_DATA_REQUEST_CODE:
                switch (resultCode) {
                    case Activity.RESULT_OK:
                        PaymentData paymentData = PaymentData.getFromIntent(data);
                        String json = paymentData.toJson();
                        break;
                    case Activity.RESULT_CANCELED:
                        break;
                    case AutoResolveHelper.RESULT_ERROR:
                        Status status = AutoResolveHelper.getStatusFromIntent(data);

                        break;
                    default:
                }
                break;
            default:
        }
    }



    private void showCurrentLocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    LOCATION_PERMISSION_REQUEST_CODE);
            return;
        }
        googleMap.setMyLocationEnabled(true);
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        if (location != null) {
            double latitude = location.getLatitude();
            double longitude = location.getLongitude();
            LatLng latLng = new LatLng(latitude, longitude);
            googleMap.addMarker(new MarkerOptions().position(latLng).title("Current Location"));
            viewBinder.tvDropOff.append(location.getProvider());
            viewBinder.tvPick.append(order.location);
            googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15));
            getRouter(latLng);
        }
    }

    private void getRouter(LatLng latLng) {
        OkHttpClient client = new OkHttpClient();
        HttpUrl.Builder url = HttpUrl.parse("https://maps.googleapis.com/maps/api/directions/json").newBuilder();
        url.addQueryParameter("origin", latLng.latitude + ", " + latLng.longitude);
        url.addQueryParameter("destination", over.latitude + ", " + over.longitude);
        url.addQueryParameter("key", getString(R.string.google_maps_key));
        Request request = new Request.Builder().url(url.build()).build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                Log.d("TAG", "onFailure: " + e.getLocalizedMessage());
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                String string = response.body().string();
                Log.d("TAG", "onResponse: " + string);
                List<LatLng> decode = PolyUtil.decode(DirectionsResponse.fromJson(string).routes.get(0).overview_polyline.points);
                PolylineOptions polylineOptions = new PolylineOptions();
                polylineOptions.addAll(decode);
                runOnUiThread(() -> {
                    MarkerOptions start = new MarkerOptions();
                    start.position(decode.get(0));
                    googleMap.addMarker(start);

                    MarkerOptions end = new MarkerOptions();
                    end.position(decode.get(decode.size() - 1));
                    googleMap.addMarker(end);
                    googleMap.addPolyline(polylineOptions);
                    LatLngBounds.Builder build = LatLngBounds.builder();
                    for (LatLng lng : decode) {
                        build.include(lng);
                    }
                    googleMap.moveCamera(CameraUpdateFactory.newLatLngBounds(build.build(), 250,250,10));
                });
                Log.d("TAG", "onResponse: " + decode.size());
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                showCurrentLocation();
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        viewBinder.mapView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        viewBinder.mapView.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        viewBinder.mapView.onDestroy();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        viewBinder.mapView.onSaveInstanceState(outState);
    }


}