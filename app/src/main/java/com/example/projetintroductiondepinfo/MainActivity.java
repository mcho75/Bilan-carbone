package com.example.projetintroductiondepinfo;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.projetintroductiondepinfo.Database.AppDatabase;
import com.example.projetintroductiondepinfo.Database.Association;
import com.example.projetintroductiondepinfo.Widgets.Bouton;
import com.example.projetintroductiondepinfo.Widgets.BoutonAssociation;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    LinearLayout grille;
    ConstraintLayout mainLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // initialisation des layouts
        grille = findViewById(R.id.grille);
        mainLayout = findViewById(R.id.main);

        // Initialisation du bouton "Nouvelle asso"
        Bouton bou = new Bouton(this, getString(R.string.nouvelle_asso), this::lancer);
        mainLayout.addView(bou);
        ConstraintLayout.LayoutParams params = (ConstraintLayout.LayoutParams) bou.getLayoutParams();
        params.bottomToBottom = R.id.main;
        params.startToStart = R.id.main;
        params.endToEnd = R.id.main;
        params.setMargins(50, 0, 50, 150);
        bou.setLayoutParams(params);
        bou.zoom_apparition();

        // mise à jour de la base de données si nécessaire
        SharedPreferences sharedPreferences = getSharedPreferences("DATABASE", Context.MODE_PRIVATE);
        if (sharedPreferences.getBoolean("NEED_UPDATE", true)) {
            DataHandler dataHandler = new DataHandler();
            dataHandler.DataRetriever(this);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putBoolean("NEED_UPDATE", false);
            editor.apply();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        grille.removeAllViews();

        // Acquisition de la base de données
        AppDatabase db = AppDatabase.getDatabase(this);

        // récupération des noms d'association
        new Thread(() -> {
            List<Association> assos = db.associationDAO().getAllAssos();
            // population de la liste d'associations
            runOnUiThread(() -> {
                for (Association asso : assos) {
                    BoutonAssociation button = new BoutonAssociation(this, asso.name, asso.year);
                    button.setOnClickListener((View view) -> this.charger(view, asso.id));
                    grille.addView(button);
                    LinearLayout.LayoutParams params = (LinearLayout.LayoutParams)button.getLayoutParams();
                    params.setMargins(10, 10, 10, 10);
                    button.setLayoutParams(params);
                }
            });
        }).start();
    }

    public void lancer(View view) {
        Intent i = new Intent(this, NouvelleAssoActivity.class);
        startActivity(i);

    }

    public void charger(View ignoredView, long id) {
        Intent i = new Intent(this, ChoixCategorieActivity.class);
        i.putExtra("ID_ASSO", id);
        startActivity(i);
    }
}