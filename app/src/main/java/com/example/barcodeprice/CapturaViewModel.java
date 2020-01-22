package com.example.barcodeprice;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

public class CapturaViewModel extends AndroidViewModel {
    private CapturaRepository capturaRepository;
    private LiveData<List<Captura>> allCapturas;

    public CapturaViewModel(@NonNull Application application) {
        super(application);
        capturaRepository = new CapturaRepository(application);
        allCapturas = capturaRepository.getAllCapturas();
    }

    public void insert(Captura captura) {
        capturaRepository.insert(captura);
    }

    public void delete(Captura captura) {
        capturaRepository.delete(captura);
    }

    public void update(Captura captura) {
        capturaRepository.update(captura);
    }

    public void deleteAllCapturas() {
        capturaRepository.getAllCapturas();
    }

    public LiveData<List<Captura>> getAllCapturas() {
        return allCapturas;
    }
}
