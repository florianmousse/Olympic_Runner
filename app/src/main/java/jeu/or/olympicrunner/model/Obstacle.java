package jeu.or.olympicrunner.model;

import android.content.Context;
import android.view.ViewGroup;

import androidx.appcompat.widget.AppCompatImageView;

import jeu.or.olympicrunner.ui.EcranActivity;

/**
 * Classe pour créer un obstacle et le bouger
 */
public class Obstacle extends AppCompatImageView {

    // colonne de l'obstacle
    private int colonne;
    public int getObstacleColonne() {
        return colonne;
    }

    private EcranActivity context;

    public Obstacle(EcranActivity context, int colonne, TypeObstacle typeObstacle) {
        super((Context) context);
        this.context = context;
        this.colonne = colonne;


        //redimentionner l'image
        ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(context.getEcran().getWidth() / 3, typeObstacle.getHauteur(context.getEcran().getHeight()));
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
        setY(-typeObstacle.getHauteur(context.getEcran().getHeight()));

        setBackgroundResource(typeObstacle.getDrawable());

        //ajouter l'image à la fenêtre principale
        context.getEcran().addView(this);

        bringToFront();
    }

    /**
     * Bouge l'obstacle selon la vitesse du jeu
     * @param vitesse la vitesse du jeu
     */
    public void moveObstacle(double vitesse) {
        // bouge l'obstacle selon la vitesse du jeu et la hauteur de l'écran
        if (getY() < context.getEcran().getHeight())
            setY((float) (getY() + context.getEcran().getHeight() * vitesse));
        // supprime l'obstacle s'il sort de l'écran
        if (getY() >= context.getEcran().getHeight()) {
            context.getEcran().removeView(this);
            context.getObstaclesToDelete().add(this);
        }
    }
}
