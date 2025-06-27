package com.example.projetintroductiondepinfo.Database;

import androidx.annotation.Nullable;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

/**
 * Cette classe décrit les différentes émissions de CO2 possibles.
 * Elle représente une ligne de la Table SQL Emission.
 */
@Entity
public class Emission {

    @PrimaryKey(autoGenerate = true)
    public long id;
    public String category;
    @Nullable
    public String subcategory;
    public String internal_name;
    public String human_name;
    public double ecv;

    /**
     * @param category la catégorie de l'émission. Pour l'instant parmi {"bien", "transport", "category"}
     * @param subcategory La sous-catégorie de l'émission, eg transport ferroviaire, transport routier, transport aérien.
     *                    N'est pas utilisée pour l'instant.
     * @param internal_name le nom interne unique, qui correspond au champ slug
     * @param human_name nom lisible pour l'utilisateur, avec espaces et accents
     * @param ecv équivalent carbone en kg de CO2e, exprimé par km/kg/unité
     */
    public Emission(String category, @Nullable String subcategory, String internal_name, String human_name, double ecv) {
        this.category = category;
        this.subcategory = subcategory;
        this.human_name = human_name;
        this.internal_name = internal_name;
        this.ecv = ecv;
    }
}
