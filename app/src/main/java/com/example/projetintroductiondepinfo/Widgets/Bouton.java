package com.example.projetintroductiondepinfo.Widgets;

import android.view.View;

import androidx.appcompat.widget.AppCompatButton;

import com.example.projetintroductiondepinfo.R;

public class Bouton extends AppCompatButton {

    public Bouton(android.content.Context context, String texte, View.OnClickListener fonction) {
        super(context);
        setTextColor(getResources().getColor(R.color.texte_bouton, context.getTheme()));
        setAllCaps(false);
        setText(texte);
        setBackgroundResource(R.drawable.bg_bouton_vert);
        setTypeface(getResources().getFont(R.font.montserrat));
        setOnClickListener(fonction);
        setPadding(50, 10, 50, 10);
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