package com.example.projetintroductiondepinfo.Database;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

/**
 * Cette classe Association décrit une association
 * à travers son nom, sa description et son année.
 * Elle représente une ligne dans la Table SQL des associations.
 */
@Entity
public class Association {

    @PrimaryKey(autoGenerate = true)
    public long id;
    public String name;
    public String description;
    public String year;

    public Association(String name, String description, String year) {
        this.name = name;
        this.description = description;
        this.year = year;
    }

    public long getId() {
        return id;
    }
}
