package com.example.projetintroductiondepinfo.Widgets;

import android.content.Context;
import android.text.InputType;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.GridLayout;

import com.example.projetintroductiondepinfo.R;
import com.google.android.material.textfield.TextInputLayout;

import java.util.List;

public class BoutonBien extends GridLayout {

    EditText edittext;
    TextInputLayout textimputlayout;
    TextInputLayout liste_deroulante;
    AutoCompleteTextView auto_comp;
    ArrayAdapter<String> adapter;

    public BoutonBien(Context context, String etiquette, List<String> liste_choix) {
        super(context);
        setBackgroundResource(R.drawable.cadre);
        setColumnCount(2);

        // creation de la liste a choix
        liste_deroulante = new TextInputLayout(context);
        liste_deroulante.setEndIconActivated(true);
        liste_deroulante.setEndIconMode(TextInputLayout.END_ICON_DROPDOWN_MENU);
        auto_comp = new AutoCompleteTextView(context);
        adapter = new ArrayAdapter<>(context, android.R.layout.simple_dropdown_item_1line, liste_choix);
        textimputlayout = new TextInputLayout(context);
        textimputlayout.setHint(etiquette);
        edittext = new EditText(context);
        auto_comp.setAdapter(adapter);
        auto_comp.setThreshold(0);
        edittext.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);

        // ajout des views
        addView(liste_deroulante);
        liste_deroulante.addView(auto_comp);
        addView(textimputlayout);
        textimputlayout.addView(edittext);

        // modification des parametres de layout
        GridLayout.LayoutParams params1 = (GridLayout.LayoutParams) liste_deroulante.getLayoutParams();
        GridLayout.LayoutParams params2 = (GridLayout.LayoutParams) textimputlayout.getLayoutParams();
        ViewGroup.LayoutParams params3 = textimputlayout.getLayoutParams();
        params1.setMargins(50, 20, 0, 20);
        params1.width = 0;
        params1.rowSpec = GridLayout.spec(0);
        params1.columnSpec = GridLayout.spec(0, 3f);
        params1.setGravity(Gravity.FILL);
        params2.width = 0;
        params2.rowSpec = GridLayout.spec(0);
        params2.columnSpec = GridLayout.spec(1, 1f);
        params2.setMargins(0, 20, 50, 20);
        params2.setGravity(Gravity.FILL);
        params3.width = 0;
        liste_deroulante.setLayoutParams(params1);
        textimputlayout.setLayoutParams(params2);
        textimputlayout.setLayoutParams(params3);
    }

    public void zoom_apparition() {
        setScaleX(0.5f);
        setScaleY(0.5f);
        animate().scaleX(1f);
        animate().scaleY(1f);
        animate().setDuration(200);
        animate().setInterpolator(new android.view.animation.OvershootInterpolator());
        animate().start();
    }

    public void zoom_disparition() {
        setScaleX(1f);
        setScaleY(1f);
        animate().scaleX(0.5f);
        animate().scaleY(0.5f);
        animate().setDuration(200);
        animate().setInterpolator(new android.view.animation.AnticipateInterpolator());
        animate().start();
    }

    public String getItem() {
        return liste_deroulante.getEditText().getText().toString();
    }

    public double getAmount() {
        return Double.parseDouble(edittext.getText().toString());
    }

    public void setItem(String humanName) {
        liste_deroulante.getEditText().setText(humanName);
    }

    public void setAmount(double amount) {
        textimputlayout.getEditText().setText(String.valueOf(amount));
    }
}