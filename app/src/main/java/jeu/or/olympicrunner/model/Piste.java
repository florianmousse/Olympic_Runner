package jeu.or.olympicrunner.model;

import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageView;

import jeu.or.olympicrunner.ui.GameActivity;
import jeu.or.olympicrunner.R;

/**
 * Classe pour créer le fond d'écran et le bouger
 */
public class Piste extends AppCompatImageView {

    private GameActivity context;

    public Piste(@NonNull GameActivity context) {
        super(context);
        this.context = context;

        setBackgroundResource(R.drawable.piste);

        //redimentionner l'image
        ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(context.getEcran().getWidth(), context.getEcran().getHeight());
        setLayoutParams(params);

        //trouver une position
        setX(0);
        setY(-context.getEcran().getHeight());

        //ajouter l'image à la fenêtre principale
        context.getEcran().addView(this);
    }

    /**
     * Bouge la piste en fond au début du jeu
     * @param vitesse la vitesse du jeu
     */
    public void movePiste(double vitesse) {
        setY((float) (getY() + context.getEcran().getHeight() * vitesse));
    }

}