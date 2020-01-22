package com.example.barcodeprice;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.tasks.OnSuccessListener;
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
    private TextView tBarcode, tBarcodeAccepted, tBarcodeLabel, tLat, tLon;
    private Button bSearch, bSave;
    private CapturaViewModel capturaViewModel;
    private EditText eNote;
    private FusedLocationProviderClient fusedLocationProviderClient;


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
        scannerView = new ZXingScannerView(this);

        Intent intent = getIntent();
        String receivedBarcode = intent.getStringExtra(CapturaRecords.EXTRA_BARCODE);
        if (receivedBarcode != null) {
            inflateRecyclerView(receivedBarcode);
        }


        fusedLocationProviderClient = new FusedLocationProviderClient(this);
        capturaViewModel = ViewModelProviders.of(this).get(CapturaViewModel.class);


        if (!allPermissionsGranted()) {
            ActivityCompat.requestPermissions(this, REQUIRED_PERMISSIONS, REQUEST_CODE_PERMISSIONS);
            finish();
        }
    }

    public void viewCapturas(View v) {
        Intent intent = new Intent(MainActivity.this, CapturaRecords.class);
        startActivity(intent);
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

    public void saveCaptura(View v) {
        String note = eNote.getText().toString().isEmpty() ? "nota" : eNote.getText().toString();

        Captura captura = new Captura(tLat.getText().toString(), tLon.getText().toString()
                , tBarcode.getText().toString(), note);

        capturaViewModel.insert(captura);
        eNote.setVisibility(View.GONE);
        bSave.setEnabled(false);
        Toast.makeText(this, "Captura " + note + " salva com sucesso", Toast.LENGTH_SHORT).show();
    }

    public void inflateRecyclerView(String barcode) {

        retrofit = new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(base_url)
                .build();

        resultsAPI = retrofit.create(ResultsAPI.class);
        //String barcode = tBarcode.getText().toString();

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
                if (response.code() != 200) {
                    Toast.makeText(MainActivity.this, "Problemas na requisição: código " + response.code(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResultsModel> call, Throwable t) {
                Toast.makeText(MainActivity.this, "Erro na requisição", Toast.LENGTH_SHORT).show();

            }
        });

    }

    public void getCoord() {
        fusedLocationProviderClient.getLastLocation().addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                tLat.setText("nada");
                tLon.setText("nada");
                if (location != null) {
                    tLat.setText(String.valueOf(location.getLatitude()));
                    tLon.setText(String.valueOf(location.getLongitude()));
                }
            }
        });

    }

    public void loadUIElements(boolean visible) {
        tLat = findViewById(R.id.tLat);
        tLon = findViewById(R.id.tLon);
        eNote = findViewById(R.id.eNote);
        recyclerView = findViewById(R.id.recyclerView);

        base_url = "https://api.upcitemdb.com/";
        tBarcode = findViewById(R.id.tBarcode);
        tBarcodeAccepted = findViewById(R.id.tBarcodeAccepted);
        tBarcodeLabel = findViewById(R.id.tBarcodeLabel);
        bSearch = findViewById(R.id.bSearch);
        bSave = findViewById(R.id.bSave);

        if (!visible) {
            tBarcode.setVisibility(View.GONE);
            tBarcodeAccepted.setVisibility(View.GONE);
            tBarcodeLabel.setVisibility(View.GONE);
            eNote.setVisibility(View.GONE);
            bSearch.setEnabled(false);
            bSave.setEnabled(false);

        }

        bSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                inflateRecyclerView(tBarcode.getText().toString());
            }
        });

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
        getCoord();

        String[] validCodesTemp = {"EAN_13", "EAN_8", "ISBN", "UPC_E", "UPC_A"};
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
