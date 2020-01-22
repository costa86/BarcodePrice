package com.example.barcodeprice;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.Result;

import java.util.Arrays;
import java.util.List;

import me.dm7.barcodescanner.zxing.ZXingScannerView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity implements ZXingScannerView.ResultHandler {
    private RecyclerView recyclerView;
    private Retrofit retrofit;
    private String base_url;
    private ResultsRecycler resultsRecycler;
    private ResultsAPI resultsAPI;
    private ZXingScannerView scannerView;
    private TextView tBarcode, tBarcodeAccepted, tBarcodeLabel;
    private Button bSearch;

    private final String[] REQUIRED_PERMISSIONS = new String[]{
            "android.permission.CAMERA",
            "android.permission.INTERNET",
            "android.permission.ACCESS_COARSE_LOCATION",
            "android.permission.ACCESS_FINE_LOCATION"
    };
    private int REQUEST_CODE_PERMISSIONS = 101;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        loadUIElements(false);
        bSearch.setEnabled(false);
        scannerView = new ZXingScannerView(this);

        if (!allPermissionsGranted()) {
            ActivityCompat.requestPermissions(this, REQUIRED_PERMISSIONS, REQUEST_CODE_PERMISSIONS);
        }
    }

    public void scanCode(View v) {
        scannerView.setResultHandler(this);
        setContentView(scannerView);
        scannerView.startCamera();
    }

    @Override
    protected void onPause() {
        super.onPause();
        scannerView.stopCamera();
    }

    public void setBarcodeText(String barcode, TextView textView) {
        textView.setText(barcode);
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

    public void inflateRecyclerView(View view) {

        retrofit = new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(base_url)
                .build();

        resultsAPI = retrofit.create(ResultsAPI.class);
        String barcode = tBarcode.getText().toString();

        Call<ResultsModel> call = resultsAPI.getResultsList(barcode);

        call.enqueue(new Callback<ResultsModel>() {
            @Override
            public void onResponse(Call<ResultsModel> call, Response<ResultsModel> response) {
                if (response.isSuccessful() && response.body() != null) {
                    try {
                        List<Offers> offers = response.body().items.get(0).offers;
                        if (offers.size() > 0) {
                            resultsRecycler = new ResultsRecycler(offers);
                            recyclerView.setAdapter(resultsRecycler);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        Toast.makeText(MainActivity.this, "Sem lojas disponíveis", Toast.LENGTH_SHORT).show();
                    }
                }
                Toast.makeText(MainActivity.this, "Status code: " + response.code(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<ResultsModel> call, Throwable t) {
                Toast.makeText(MainActivity.this, "Erro", Toast.LENGTH_SHORT).show();

            }
        });

    }

    public void loadUIElements(boolean visible) {

        recyclerView = findViewById(R.id.recyclerView);
        base_url = "https://api.upcitemdb.com/";
        tBarcode = findViewById(R.id.tBarcode);
        tBarcodeAccepted = findViewById(R.id.tBarcodeAccepted);
        tBarcodeLabel = findViewById(R.id.tBarcodeLabel);
        bSearch = findViewById(R.id.bSearch);

        if (!visible) {
            tBarcode.setVisibility(View.GONE);
            tBarcodeAccepted.setVisibility(View.GONE);
            tBarcodeLabel.setVisibility(View.GONE);
        }
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(this, DividerItemDecoration.VERTICAL);
        recyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this));
        recyclerView.addItemDecoration(dividerItemDecoration);
    }



    @Override
    public void handleResult(Result result) {
        String resultText = result.getText();
        String resultType = result.getBarcodeFormat().toString();
        Toast.makeText(MainActivity.this, "Capturado código tipo: " + resultType, Toast.LENGTH_LONG).show();
        setContentView(R.layout.activity_main);
        String isValid = "válido";
        loadUIElements(true);

        String[] validCodesTemp = {"EAN_13", "EAN_8", "ISBN", "UPC_E","UPC_A"};
        List<String> validCodes = Arrays.asList(validCodesTemp);

        if (!validCodes.contains(resultType)) {
            bSearch.setEnabled(false);
            isValid = "inválido";
        }
        String msg = resultType + ": " + "tipo de código " + isValid + " para consulta.";

        tBarcodeAccepted.setText(msg);

        setBarcodeText(resultText, tBarcode);

        scannerView.stopCamera();
    }

}
