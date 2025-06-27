package com.example.projetintroductiondepinfo;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.example.projetintroductiondepinfo.Database.AppDatabase;
import com.example.projetintroductiondepinfo.Database.Association;
import com.example.projetintroductiondepinfo.Widgets.Bouton;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicLong;

public class NouvelleAssoActivity extends AppCompatActivity {

    private final AppDatabase db = AppDatabase.getDatabase(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nouvelleasso);

        // Initialisation du bouton
        Bouton bou = new Bouton(this, "Créer l'association", this::creer_liste);
        ((ConstraintLayout) (findViewById(R.id.nouvelle_liste))).addView(bou);
        ConstraintLayout.LayoutParams params = (ConstraintLayout.LayoutParams) bou.getLayoutParams();
        params.topToBottom = R.id.grille;
        params.startToStart = R.id.nouvelle_liste;
        params.endToEnd = R.id.nouvelle_liste;
        params.topMargin = 150;
        bou.setLayoutParams(params);

        String[] liste_dates = {"2024-2025", "2025-2026", "2026-2027", "2027-2028"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line, liste_dates);
        ((AutoCompleteTextView)findViewById(R.id.entry_annee)).setAdapter(adapter);
        ((AutoCompleteTextView)findViewById(R.id.entry_annee)).setThreshold(0);
    }

    public void creer_liste(View view) {

        // Récupération du contenu des Entry
        String nom = ((EditText) findViewById(R.id.entry_nom)).getText().toString();
        String description = ((EditText) findViewById(R.id.entry_description)).getText().toString();
        String annee = ((EditText) findViewById(R.id.entry_annee)).getText().toString();

        // On vérifie si l'asso existe déjà, pour éviter les doublons
        AtomicBoolean isDoublon = new AtomicBoolean(Boolean.FALSE);
        Thread t = new Thread(() -> isDoublon.set(db.associationDAO().exists(nom, annee)));
        t.start();
        try {
            t.join();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        // si il y a doublon, on notifie l'utilisateur;
        // si c'est ok, on ajoute l'asso à la base de données et on passe à la suite
        if (nom.isEmpty() || isDoublon.get()) {
            Toast.makeText(this, "Cette association existe déjà !", Toast.LENGTH_SHORT).show();
        } else {
            Association association = new Association(nom, description, annee);
            AtomicLong id = new AtomicLong();

            new Thread(() -> {
                id.set(db.associationDAO().insert(association));
                Intent i = new Intent(this, ChoixCategorieActivity.class);
                i.putExtra("ID_ASSO", id.get());
                startActivity(i);
                finish();
            }).start();
        }
    }
}
