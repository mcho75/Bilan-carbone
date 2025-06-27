package com.example.projetintroductiondepinfo.Database;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

/**
 * Cette interface permet d'interagir avec la Table SQL Association.
 */
@Dao
public interface AssociationDAO {
    @Insert
    long insert(Association association);

    @Update
    void update(Association association);

    /**
     * @return la liste de toutes les associations connues
     */
    @Query("SELECT * FROM association")
    List<Association> getAllAssos();

    /**
     * @param name nom de l'association
     * @param year année de l'association
     * @return si l'asso est déjà dans la liste
     */
    @Query("SELECT EXISTS (SELECT 1 FROM Association WHERE name=:name AND year=:year)")
    Boolean exists(String name, String year);

    /**
     * @param id l'identifiant de l'association
     * @return le nom de l'association
     */
    @Query("SELECT name from Association WHERE id=:id")
    String getName(long id);

}