package com.example.projetintroductiondepinfo.Database;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

/**
 * Cette interface permet d'interagir avec la Table SQL Estimation.
 */
@Dao
public interface EstimationDAO {
    @Insert
    void insert(Estimation estimation);

    @Update
    void update(Estimation estimation);

    @Delete
    void delete(Estimation estimation);

    /**
     * @param assoId l'id de l'association qui nous intéresse
     * @return toutes les estimations qui correspondent à l'association
     */
    @Query("SELECT * FROM estimation WHERE assoId=:assoId")
    List<Estimation> getEstimations(long assoId);

}