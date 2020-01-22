package com.example.barcodeprice;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import java.util.List;

public class CapturaRecords extends AppCompatActivity {
    RecyclerView recyclerView;
    CapturaViewModel capturaViewModel;
    public static final String EXTRA_BARCODE = "com.example.barcodeprice.extra.BARCODE";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_captura_records);

        recyclerView = findViewById(R.id.recycler_view_captura);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        final CapturaAdapter capturaAdapter = new CapturaAdapter();
        recyclerView.setAdapter(capturaAdapter);

        capturaViewModel = ViewModelProviders.of(this).get(CapturaViewModel.class);
        capturaViewModel.getAllCapturas().observe(this, new Observer<List<Captura>>() {
            @Override
            public void onChanged(List<Captura> capturas) {
                capturaAdapter.setCapturas(capturas);
            }
        });


        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                Captura captura = capturaAdapter.getCapturaAt(viewHolder.getAdapterPosition());
                capturaViewModel.delete(captura);
                Toast.makeText(CapturaRecords.this, "Captura apagada: " + captura.getId(), Toast.LENGTH_SHORT).show();
            }

        }).attachToRecyclerView(recyclerView);


        capturaAdapter.setOnItemClickListener(new CapturaAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Captura captura) {
                Intent intent = new Intent(CapturaRecords.this,MainActivity.class);
                intent.putExtra(EXTRA_BARCODE,captura.getBarcode());
                //Toast.makeText(CapturaRecords.this, captura.getBarcode(), Toast.LENGTH_SHORT).show();
                startActivity(intent);
            }
        });

    }
}
