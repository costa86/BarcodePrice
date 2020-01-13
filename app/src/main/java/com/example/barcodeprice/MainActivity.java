package com.example.barcodeprice;

import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.zxing.Result;

import me.dm7.barcodescanner.zxing.ZXingScannerView;

public class MainActivity extends AppCompatActivity implements ZXingScannerView.ResultHandler {
    private ZXingScannerView scannerView;
    private final String[] REQUIRED_PERMISSIONS = new String[]{
            "android.permission.CAMERA",
            "android.permission.INTERNET",
            "android.permission.ACCESS_COARSE_LOCATION",
            "android.permission.ACCESS_FINE_LOCATION"
    };
    private int REQUEST_CODE_PERMISSIONS = 101;

    private Button bStart, bSearch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bStart = findViewById(R.id.bStart);
        bSearch = findViewById(R.id.bSearch);
        
        bSearch.setVisibility(View.GONE);

        if (allPermissionsGranted()) {
            bStart.setEnabled(true);
        } else {
            ActivityCompat.requestPermissions(this, REQUIRED_PERMISSIONS, REQUEST_CODE_PERMISSIONS);
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == REQUEST_CODE_PERMISSIONS) {
            allPermissionsGranted();
        }
    }

    private Boolean allPermissionsGranted() {
        for (String permission : REQUIRED_PERMISSIONS) {
            if (ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {
                return false;
            }
        }
        return true;
    }

    public void scanCode(View v) {
        scannerView = new ZXingScannerView(this);
        scannerView.setResultHandler(this);
        setContentView(scannerView);
        scannerView.startCamera();
    }

    @Override
    protected void onPause() {
        super.onPause();
        scannerView.stopCamera();
    }

    @Override
    public void handleResult(Result result) {
        String resultText = result.getText();
        String resultType = result.getBarcodeFormat().toString();
        String success = getResources().getString(R.string.success);
        Toast.makeText(MainActivity.this, success, Toast.LENGTH_LONG).show();
        setContentView(R.layout.activity_main);

        TextView tBarcodeText = findViewById(R.id.tBarcodeText);
        TextView tBarcodeType = findViewById(R.id.tBarcodeType);
        Button bUpdated = findViewById(R.id.bSearch);

        tBarcodeText.setVisibility(View.VISIBLE);
        tBarcodeType.setVisibility(View.VISIBLE);
        bUpdated.setVisibility(View.VISIBLE);

        tBarcodeText.setText(tBarcodeText.getText() + ": " + resultText);
        tBarcodeType.setText(tBarcodeType.getText() + ": " + resultType);
        bUpdated.setText(bUpdated.getText() + " " + resultText);

        scannerView.stopCamera();
    }
}
