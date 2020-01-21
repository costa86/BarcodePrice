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
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.zxing.Result;

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
    private ResultsAPI resultsAPI;
    private ZXingScannerView scannerView;
    private String base_url;
    private TextView tBarcodeText, tBarcodeType;
    private ResultsRecycler resultsRecycler;

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

        //base_url = "http://tour-pedia.org/";
        base_url = "https://api.upcitemdb.com/";
        tBarcodeText = findViewById(R.id.tBarcodeText);
        tBarcodeType = findViewById(R.id.tBarcodeType);

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(this,DividerItemDecoration.VERTICAL);

        recyclerView = findViewById(R.id.recycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this));
        recyclerView.addItemDecoration(dividerItemDecoration);
        //https://api.upcitemdb.com/prod/trial/lookup?upc=9781586170349

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

    public void inflateRecyclerView(View v) {

        retrofit = new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(base_url)
                .build();

        resultsAPI = retrofit.create(ResultsAPI.class);

        String barcode = tBarcodeText.getText().toString();

        Call<List<ResultsModel>> call = resultsAPI.getResultsList(barcode);

        call.enqueue(new Callback<List<ResultsModel>>() {
            @Override
            public void onResponse(Call<List<ResultsModel>> call, Response<List<ResultsModel>> response) {
                if (response.isSuccessful()) {
                    resultsRecycler = new ResultsRecycler(response.body());
                    recyclerView.setAdapter(resultsRecycler);
                }
                Toast.makeText(MainActivity.this, "Code: " + response.code(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<List<ResultsModel>> call, Throwable t) {
                Toast.makeText(MainActivity.this, "Erro", Toast.LENGTH_SHORT).show();

            }

        });
    }
}
