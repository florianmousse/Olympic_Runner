package jeu.or.olympicrunner.model;

import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageView;

import jeu.or.olympicrunner.ui.GameActivity;
import jeu.or.olympicrunner.R;

/**
 * Classe pour créer un coeur et le bouger
 */
public class Coeur extends AppCompatImageView {

    // colonne d'apparition du coeur
    private int colonne;
    public int getCoeurColonne() {
        return colonne;
    }

    private GameActivity context;

    public Coeur(@NonNull GameActivity context, int colonne) {
        super(context);
        this.context = context;
        this.colonne = colonne;

        //redimentionner l'image
        int HEIGHT = 300;
        ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(context.getEcran().getWidth()/3, HEIGHT);
        setLayoutParams(params);

        //trouver une position
        if (colonne == 1) {
            setX(0);
        }
        if (colonne == 2) {
            setX(context.getEcran().getWidth() / 3);
        }
        if (colonne == 3) {
            setX((context.getEcran().getWidth() / 3) * 2);
        }
        setY(-HEIGHT);

        //ajouter l'image à la fenêtre principale
        context.getEcran().addView(this);

        setBackgroundResource(R.drawable.coeur);
    }

    /**
     * Bouger le coeur selon la vitesse du jeu
     * @param vitesse la vitesse du jeu
     */
    public void moveCoeur(double vitesse) {
        // bouge le coeur selon la hauteur de l'écran
        if (getY() < context.getEcran().getHeight()) setY((float) (getY() + context.getEcran().getHeight() * vitesse));
        // supprime le coeur si celui-ci sort de l'écran
        if (getY() >= context.getEcran().getHeight()) {
            context.getEcran().removeView(this);
            context.getCoeursToDelete().add(this);
        }
    }
}
