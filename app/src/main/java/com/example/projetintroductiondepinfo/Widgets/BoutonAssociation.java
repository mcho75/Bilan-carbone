package com.example.projetintroductiondepinfo.Widgets;

import android.view.Gravity;

import androidx.appcompat.widget.AppCompatButton;

import com.example.projetintroductiondepinfo.R;

import java.util.Objects;

public class BoutonAssociation extends AppCompatButton {

    public BoutonAssociation(android.content.Context context, String nom, String annee) {
        super(context);
        setTextColor(getResources().getColor(R.color.fg, context.getTheme()));
        setAllCaps(false);
        if (Objects.equals(annee, "")) {
            setText(nom);
        }
        else {
            setText(nom+"\n"+annee);
        }
        setGravity(Gravity.CENTER);
        setBackgroundResource(R.drawable.bg_bouton_beige);
        setTypeface(getResources().getFont(R.font.montserrat));
        setPadding(10, 20, 10, 20);
    }

    public void add_image(int image, int padding) {
        setCompoundDrawablesWithIntrinsicBounds(image, 0, 0, 0);
        setCompoundDrawablePadding(padding);
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
}