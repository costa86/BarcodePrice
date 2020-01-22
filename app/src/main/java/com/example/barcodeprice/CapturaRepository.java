package com.example.barcodeprice;


import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import java.util.List;

public class CapturaRepository {

    private CapturaDAO capturaDAO;

    private LiveData<List<Captura>> allCapturas;

    public CapturaRepository(Application application) {
        CapturaDatabase capturaDatabase = CapturaDatabase.getInstance(application);
        capturaDAO = capturaDatabase.capturaDAO();
        allCapturas = capturaDAO.getAllCaptures();
    }

    public void insert(Captura captura) {
        new InsertCapturaAsync(capturaDAO).execute(captura);
    }

    public void delete(Captura captura) {
        new DeleteCapturaAsync(capturaDAO).execute(captura);
    }

    public void update(Captura captura) {
        new UpdateCapturaAsync(capturaDAO).execute(captura);
    }

    public void deleteAllCapturas() {
        new DeleteAllCapturasAsync(capturaDAO).execute();
    }

    public LiveData<List<Captura>> getAllCapturas() {
        return allCapturas;

    }

    private static class InsertCapturaAsync extends AsyncTask<Captura, Void, Void> {
        private CapturaDAO capturaDAO;

        public InsertCapturaAsync(CapturaDAO capturaDAO) {
            this.capturaDAO = capturaDAO;
        }

        @Override
        protected Void doInBackground(Captura... capturas) {
            capturaDAO.insert(capturas[0]);
            return null;
        }
    }

    private static class DeleteCapturaAsync extends AsyncTask<Captura, Void, Void> {
        private CapturaDAO capturaDAO;

        public DeleteCapturaAsync(CapturaDAO capturaDAO) {
            this.capturaDAO = capturaDAO;
        }

        @Override
        protected Void doInBackground(Captura... capturas) {
            capturaDAO.delete(capturas[0]);
            return null;
        }
    }

    private static class UpdateCapturaAsync extends AsyncTask<Captura, Void, Void> {
        private CapturaDAO capturaDAO;

        public UpdateCapturaAsync(CapturaDAO capturaDAO) {
            this.capturaDAO = capturaDAO;
        }

        @Override
        protected Void doInBackground(Captura... capturas) {
            capturaDAO.update(capturas[0]);
            return null;
        }
    }

    private static class DeleteAllCapturasAsync extends AsyncTask<Void, Void, Void> {
        private CapturaDAO capturaDAO;

        public DeleteAllCapturasAsync(CapturaDAO capturaDAO) {
            this.capturaDAO = capturaDAO;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            capturaDAO.deleteAllCaptures();
            return null;
        }
    }
}

