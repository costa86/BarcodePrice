package com.example.barcodeprice;

import android.content.Context;
import android.os.AsyncTask;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

@Database(entities = {Captura.class}, version = 1)
public abstract class CapturaDatabase extends RoomDatabase {

    private static CapturaDatabase instance;

    public abstract CapturaDAO capturaDAO();

    public static synchronized CapturaDatabase getInstance(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(context.getApplicationContext(),
                    CapturaDatabase.class, "captura_database")
                    .fallbackToDestructiveMigration()
                    .addCallback(roomCallback)
                    .build();

        }
        return instance;
    }

    private static RoomDatabase.Callback roomCallback = new RoomDatabase.Callback() {
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);
            new PopulateDbAsyncTask(instance).execute();
        }
    };

    private static class PopulateDbAsyncTask extends AsyncTask<Void, Void, Void> {
        private CapturaDAO capturaDAO;

        private PopulateDbAsyncTask(CapturaDatabase db) {
            capturaDAO = db.capturaDAO();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            capturaDAO.insert(new Captura("-12", "35", "123", "teste"));
            return null;
        }
    }

}
