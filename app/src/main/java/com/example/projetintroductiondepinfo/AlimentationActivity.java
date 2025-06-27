package com.example.projetintroductiondepinfo;

import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.GridLayout;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;

import com.example.projetintroductiondepinfo.Database.AppDatabase;
import com.example.projetintroductiondepinfo.Database.Emission;
import com.example.projetintroductiondepinfo.Database.Estimation;
import com.example.projetintroductiondepinfo.Widgets.Bouton;
import com.example.projetintroductiondepinfo.Widgets.BoutonBien;

import java.util.List;

public class AlimentationActivity extends AppCompatActivity {

    final String CATEGORY = "alimentation";
    final String AMOUNT = "qt kg";
    AppDatabase db;
    long ID_ASSO;
    List<String> liste_choix;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alimentation);

        // acquisition
        LinearLayout grille = findViewById(R.id.layout_alimentation);
        db = AppDatabase.getDatabase(this);

        // Récupération ID asso
        Intent appelant = getIntent();
        ID_ASSO = appelant.getLongExtra("ID_ASSO", -1);

        // Création bouton goodies
        Bouton bou = new Bouton(this, getString(R.string.nouvel_aliment), this::ajouter_aliment);
        grille.addView(bou);
        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) bou.getLayoutParams();
        params.width = GridLayout.LayoutParams.WRAP_CONTENT;
        params.gravity = Gravity.CENTER;
        params.setMargins(0, 50, 0, 150);
        bou.setLayoutParams(params);

        // population de liste_choix
        new Thread(() -> liste_choix = db.emissionDAO().getEmissionName(CATEGORY)).start();
    }

    @Override
    public void onResume() {
        super.onResume();
        LinearLayout grille = findViewById(R.id.grille_alimentation);
        grille.removeAllViews();
        new Thread(() -> {
            List<Estimation> estimations = db.estimationDAO().getEstimations(ID_ASSO);

            for (Estimation est : estimations) {
                Emission emi = db.emissionDAO().getEmissionById(est.itemId);

                if (CATEGORY.equals(emi.category)) {
                    runOnUiThread(() -> {
                        BoutonBien boutonBien = ajouter_aliment(null);
                        boutonBien.setItem(emi.human_name);
                        boutonBien.setAmount(est.amount);
                    });
                }
            }
        }).start();


    }

    @Override
    public void onPause() {
        LinearLayout grille = findViewById(R.id.grille_alimentation);

        Thread t = new Thread(() -> {
            for (Estimation est : db.estimationDAO().getEstimations(ID_ASSO)) {
                Emission emi = db.emissionDAO().getEmissionById(est.itemId);

                if (CATEGORY.equals(emi.category)) {
                    db.estimationDAO().delete(est);
                }
            }

            for (int i = 0; i < grille.getChildCount(); i++) {
                BoutonBien boutonBien = (BoutonBien) grille.getChildAt(i);
                String item = boutonBien.getItem();
                double amount;
                try {
                    amount = boutonBien.getAmount();
                    if (!item.isBlank() && amount > 0) {
                        long itemId = db.emissionDAO().getIdByName(item);
                        Estimation estimation = new Estimation(ID_ASSO, itemId, amount);
                        db.estimationDAO().insert(estimation);
                    }
                } catch (NumberFormatException ignored) {
                }
            }
        });
        t.start();
        try {
            t.join();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } finally {
            super.onPause();
        }
    }

    public BoutonBien ajouter_aliment(View view) {
        LinearLayout grille = findViewById(R.id.grille_alimentation);
        BoutonBien bou = new BoutonBien(this, AMOUNT, liste_choix);
        grille.addView(bou);
        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) bou.getLayoutParams();
        params.width = LinearLayout.LayoutParams.MATCH_PARENT;
        params.setMargins(0, 20, 0, 0);
        bou.setLayoutParams(params);
        bou.zoom_apparition();
        return bou;
    }

}