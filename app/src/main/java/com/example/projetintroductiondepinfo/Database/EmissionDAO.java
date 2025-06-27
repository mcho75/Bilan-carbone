package com.example.projetintroductiondepinfo.Database;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

/**
 * Interface permettant d'accéder à la table SQL Emission.
 */
@Dao
public interface EmissionDAO {

    @Insert
    void insert(Emission emission);

    @Update
    void update(Emission emission);

    /**
     * @param id l'id de l'émission recherchée
     * @return l'émission correspondante
     */
    @Query("SELECT * FROM Emission WHERE id=:id")
    Emission getEmissionById(long id);

    /**
     * @param category la catégorie d'émission qui nous intéresse
     * @return toutes les émissions de cette catégorie
     */
    @Query("SELECT human_name FROM Emission WHERE category=:category ORDER BY human_name")
    List<String> getEmissionName(String category);

    /**
     * @param name le nom humain de l'émission
     * @return l'id correspondant
     */
    @Query("SELECT id FROM Emission WHERE human_name=:name")
    long getIdByName(String name);
}