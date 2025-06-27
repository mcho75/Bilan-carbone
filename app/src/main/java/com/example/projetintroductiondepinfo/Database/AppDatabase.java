package com.example.projetintroductiondepinfo.Database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

/**
 * classe décrivant la base de données
 */
@Database(entities = {Emission.class, Estimation.class, Association.class}, version = 1, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {
    private final static String NOM_FICHIER = "DataBase";
    private static volatile AppDatabase INSTANCE;

    /**
     * @param context l'activity appelante
     * @return la base de données.
     * Si elle n'est pas connectée, on essaie de la connecter
     * Si elle n'existe pas, on la crée.
     */
    public static AppDatabase getDatabase(final Context context) {
        // On la connecte si ce n'est pas déjà le cas
        if (INSTANCE == null) {
            synchronized (AppDatabase.class) {
                // si la base n'existe pas encore, on la crée
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(), AppDatabase.class, NOM_FICHIER).build();
                }
            }
        }
        return INSTANCE;
    }

    public abstract EmissionDAO emissionDAO();

    public abstract EstimationDAO estimationDAO();

    public abstract AssociationDAO associationDAO();
}