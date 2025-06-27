package com.example.projetintroductiondepinfo.Database;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;


/**
 * Cette classe Estimation décrit une estimation
 * à travers l'association associée, le type d'émission et une quantité (km, kg, unités, etc).
 * Elle représente une ligne dans la Table SQL des Estimations.
 */
@Entity(foreignKeys = {
        @ForeignKey(
                entity = Association.class,
                parentColumns = "id",
                childColumns = "assoId",
                onDelete = ForeignKey.CASCADE
        ),
        @ForeignKey(entity = Emission.class,
                parentColumns = "id",
                childColumns = "itemId",
                onDelete = ForeignKey.CASCADE
        )
}
)
public class Estimation {

    @PrimaryKey(autoGenerate = true)
    public long id;

    public long assoId;
    public long itemId;
    public double amount;

    public Estimation(long assoId, long itemId, double amount) {
        this.assoId = assoId;
        this.itemId = itemId;
        this.amount = amount;
    }
}