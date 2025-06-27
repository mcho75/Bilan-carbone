package com.example.projetintroductiondepinfo;

import static java.lang.Double.max;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.LinearInterpolator;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.projetintroductiondepinfo.Database.AppDatabase;
import com.example.projetintroductiondepinfo.Database.Emission;
import com.example.projetintroductiondepinfo.Database.Estimation;
import com.example.projetintroductiondepinfo.Widgets.Bouton;

import java.util.Hashtable;

public class ChoixCategorieActivity extends AppCompatActivity {

    long ID_ASSO;
    AppDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Paramétrage de la fenêtre
        setContentView(R.layout.activity_choixcategories);
        GridLayout grille = findViewById(R.id.grille_boutons_categories);

        // Récupération de l'id de l'asso
        Intent appelant = getIntent();
        ID_ASSO = appelant.getLongExtra("ID_ASSO", -1);

        // création base de données
        db = AppDatabase.getDatabase(this);

        // réglage du nom
        new Thread(
                () -> {
                    String nameAsso = db.associationDAO().getName(ID_ASSO);
                    runOnUiThread(
                            () -> {
                                TextView textView = findViewById(R.id.nom_association);
                                textView.setText(nameAsso);
                            }
                    );
                }
        ).start();


        // Ajout bouton alimentation
        Bouton bou1 = new Bouton(this, getString(R.string.alimentation), this::action_bouton_alimentation);
        bou1.add_image(R.drawable.salad, 50);
        bou1.setHeight(200);
        grille.addView(bou1);
        GridLayout.LayoutParams params1 = (GridLayout.LayoutParams)bou1.getLayoutParams();
        params1.rowSpec = GridLayout.spec(0);
        params1.columnSpec = GridLayout.spec(0);
        params1.width = ViewGroup.LayoutParams.MATCH_PARENT;
        params1.setMargins(10, 10, 10, 10);
        bou1.setLayoutParams(params1);

        // Ajout bouton biens
        Bouton bou2 = new Bouton(this, getString(R.string.biens), this::action_bouton_biens);
        bou2.add_image(R.drawable.shirt_long_sleeve, 50);
        bou2.setHeight(200);
        grille.addView(bou2);
        GridLayout.LayoutParams params2 = (GridLayout.LayoutParams)bou2.getLayoutParams();
        params2.rowSpec = GridLayout.spec(1);
        params2.columnSpec = GridLayout.spec(0);
        params2.width = ViewGroup.LayoutParams.MATCH_PARENT;
        params2.setMargins(10, 10, 10, 10);
        bou2.setLayoutParams(params2);

        // Ajout bouton transport
        Bouton bou3 = new Bouton(this, getString(R.string.transport), this::action_bouton_transport);
        bou3.add_image(R.drawable.subway, 50);
        bou3.setHeight(200);
        grille.addView(bou3);
        GridLayout.LayoutParams params3 = (GridLayout.LayoutParams)bou3.getLayoutParams();
        params3.rowSpec = GridLayout.spec(2);
        params3.columnSpec = GridLayout.spec(0);
        params3.width = ViewGroup.LayoutParams.MATCH_PARENT;
        params3.setMargins(10, 10, 10, 10);
        bou3.setLayoutParams(params3);

    }

    public void action_bouton_alimentation(View view) {
        Intent i = new Intent(this, AlimentationActivity.class);
        i.putExtra("ID_ASSO", ID_ASSO);
        startActivity(i);
    }

    public void action_bouton_biens(View view) {
        Intent i = new Intent(this, BiensActivity.class);
        i.putExtra("ID_ASSO", ID_ASSO);
        startActivity(i);
    }

    public void action_bouton_transport(View view) {
        Intent i = new Intent(this, TransportActivity.class);
        i.putExtra("ID_ASSO", ID_ASSO);
        startActivity(i);
    }

    @Override
    public void onResume() {
        super.onResume();

        // Calcul des émissions
        new Thread(
                () -> {
                    Hashtable<String, Double> totaux = new Hashtable<>();
                    totaux.put("alimentation", 0d);
                    totaux.put("bien", 0d);
                    totaux.put("transport", 0d);

                    for (Estimation est: db.estimationDAO().getEstimations(ID_ASSO)) {
                        Emission e = db.emissionDAO().getEmissionById(est.itemId) ;
                        totaux.put(e.category, totaux.getOrDefault(e.category, 0d) + (e.ecv * est.amount));
                    }

                    runOnUiThread(
                            () -> {
                                double somme = totaux.get("alimentation") + totaux.get("bien") + totaux.get("transport");
                                ((TextView)findViewById(R.id.titre2)).setText(String.format("Bilan actuel :\n%d kg", (int) somme));
                                calcul_hauteur_barres(totaux);
                            }
                    );

                }
        ).start();
    }

    // h représente ici la quantité de carbone émise par chacune des trois catégories
    public void calcul_hauteur_barres(Hashtable<String, Double> h) {

        double h1 = h.get("alimentation"), h2 = h.get("bien"), h3 = h.get("transport");
        final int TAILLE_MAX = 300;

        TextView etiquette1 = findViewById(R.id.texte_alimentation);
        TextView etiquette2 = findViewById(R.id.texte_biens);
        TextView etiquette3 = findViewById(R.id.texte_transport);
        etiquette1.setText(String.format(getString(R.string.d_kg), (int) h1));
        etiquette2.setText(String.format(getString(R.string.d_kg), (int) h2));
        etiquette3.setText(String.format(getString(R.string.d_kg), (int) h3));

        double maximum = max(max(h1, h2), h3);
        h1 = h1 / maximum;
        h2 = h2 / maximum;
        h3 = h3 / maximum;

        ImageView barre1 = findViewById(R.id.barre_alimentation);
        ImageView barre2 = findViewById(R.id.barre_biens);
        ImageView barre3 = findViewById(R.id.barre_transport);
        GridLayout.LayoutParams params1 = (GridLayout.LayoutParams)barre1.getLayoutParams();
        GridLayout.LayoutParams params2 = (GridLayout.LayoutParams)barre2.getLayoutParams();
        GridLayout.LayoutParams params3 = (GridLayout.LayoutParams)barre3.getLayoutParams();
        params1.height = (int)(TAILLE_MAX * h1);
        params2.height = (int)(TAILLE_MAX * h2);
        params3.height = (int)(TAILLE_MAX * h3);
        barre1.setLayoutParams(params1);
        barre2.setLayoutParams(params2);
        barre3.setLayoutParams(params3);

        barre1.setPivotY((int)(TAILLE_MAX * h1));
        barre1.setScaleY(0.5f);
        barre1.animate().scaleY(1f);
        barre1.animate().setDuration(500);
        barre1.animate().setInterpolator(new LinearInterpolator());
        barre1.animate().start();

        barre2.setPivotY((int)(TAILLE_MAX * h2));
        barre2.setScaleY(0.5f);
        barre2.animate().scaleY(1f);
        barre2.animate().setDuration(500);
        barre2.animate().setInterpolator(new LinearInterpolator());
        barre2.animate().start();

        barre3.setPivotY((int)(TAILLE_MAX * h3));
        barre3.setScaleY(0.5f);
        barre3.animate().scaleY(1f);
        barre3.animate().setDuration(500);
        barre3.animate().setInterpolator(new LinearInterpolator());
        barre3.animate().start();
    }
}
