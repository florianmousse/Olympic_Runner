package jeu.or.olympicrunner.model;

import jeu.or.olympicrunner.R;

/**
 * Enum le type de chaque obstacle
 */
public enum TypeObstacle {
    COURT (260, 0.11, R.drawable.obstacle_court),
    LONG (500, 0.21, R.drawable.obstacle_long);

    // Hauteur de l'obstacle
    private int hauteur;
    // Multiplicateur de la taille de l'obstacle par rapport à l'écran
    private double multiplicateur;
    // Image de l'obstacle
    private int drawable;

    TypeObstacle(int hauteur, double multiplicateur, int drawable) {
        this.hauteur = hauteur;
        this.multiplicateur = multiplicateur;
        this.drawable = drawable;
    }

    public int getHauteur() {
        return hauteur;
    }

    public int getDrawable() {
        return drawable;
    }

    /**
     * Récupère la hauteur d'un obstacle (long ou court)
     * @param hauteur la hauteur de l'obstacle
     * @return la hauteur de chaque obstacle
     */
    public int getHauteur(int hauteur) {
        return (int) Math.floor(multiplicateur * (double) hauteur);
    }
}
