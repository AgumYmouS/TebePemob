package com.schwarzschild.absenonline;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import java.util.List;

@Dao
interface AbsenMatkulDao {

    @Query("SELECT * FROM mahasiswas")
    List<AbsenMatkul> getAllAbsenMatkul();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAllMahasiswa(AbsenMatkul absenMatkul);
}
