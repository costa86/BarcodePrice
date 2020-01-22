package com.example.barcodeprice;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface CapturaDAO {

    @Insert
    void insert(Captura captura);

    @Delete
    void delete(Captura captura);

    @Update
    void update(Captura captura);

    @Query("DELETE FROM captura_table")
    void deleteAllCaptures();

    @Query("SELECT * FROM captura_table ORDER BY note ASC")
    LiveData<List<Captura>> getAllCaptures();

}
