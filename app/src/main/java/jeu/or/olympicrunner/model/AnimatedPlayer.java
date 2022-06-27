package jeu.or.olympicrunner.model;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.util.AttributeSet;
import android.view.ViewGroup;

import jeu.or.olympicrunner.ui.GameActivity;

/**
 * Classe du player avec son positionnement, son mouvement et sa taille
 */
public class AnimatedPlayer extends androidx.appcompat.widget.AppCompatImageView {

    private int[] frames;      // tableau des images
    private int frameNr;        // nombre d'images de l'animation
    private int currentFrame;   // image actuelle
    private int updateEvery = 4; // change d'image

    // colonne dans laquelle le player apparaîtra au début
    private int colonne;
    public int getColonne() {
        return colonne;
    }

    public AnimatedPlayer(Context context) {
        super(context);
    }

    public AnimatedPlayer(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public AnimatedPlayer(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    private GameActivity context;

    /**
     * Initialise le player
     * @param context dans quelle activity
     * @param frames toutes les images
     * @param height hauteur du player
     */
    public void initAnimatedPlayer(GameActivity context, int[] frames, int height) {
        // nombre total d'images dans le tableau
        int frameCount = frames.length;

        this.context = context;
        this.frames = frames;

        frameNr = frameCount;
        currentFrame = 0;

        // Dimensions du player
        ViewGroup.LayoutParams params = getLayoutParams();
        if (params == null) params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, height);
        params.width = ViewGroup.LayoutParams.WRAP_CONTENT;
        params.height = height;
        setLayoutParams(params);

        // Apparaîtra au milieu
        colonne = 2;
    }

    /**
     * Change d'image selon le compteur, la dessine et diminue le changement d'image
     * @param counter le compteur du jeu
     */
    public void update(int counter) {

        if (counter % 4000 == 0 && updateEvery > 1) updateEvery -= 1;

        if (counter % updateEvery == 0) {
            currentFrame++;
            // si ça dépasse la longueur max on revient à l'image de départ
            if (currentFrame >= frameNr) currentFrame = 0;
            drawFrame();
        }
    }

    /**
     * Dessine l'image actuelle
     */
    public void drawFrame() {
        setBackgroundResource(frames[currentFrame]);
    }

    /**
     * Bouge le player dans la colonne à sa gauche
     */
    public void moveToLeft() {
        ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(this, "x", getX(), getX() - context.getEcran().getWidth() / 3);
        objectAnimator.setDuration(50);
        objectAnimator.start();
        colonne -= 1;
    }

    /**
     * Bouge le player dans la colonne à sa droite
     */
    public void moveToRight() {
        ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(this, "x", getX(), getX() + (context.getEcran().getWidth() - (context.getEcran().getWidth() / 3) * 2));
        objectAnimator.setDuration(50);
        objectAnimator.start();
        colonne += 1;
    }

    /**
     * Collision entre le player et un obstacle
     * @param obstacle obstacle de la liste
     * @return true si collision, false sinon
     */
    public boolean isCollidingWithObstacle(Obstacle obstacle) {
        // comparaison entre le point en haut du player et l'obstacle
        if (getY() > obstacle.getY() && getY() < (obstacle.getY() + obstacle.getHeight()))
            return true;
        // comparaison entre le point en bas du player et l'obstacle
        if (getY() + getHeight() > obstacle.getY() && getY() + getHeight() < (obstacle.getY() + obstacle.getHeight()))
            return true;
        // comparaison entre le point en haut et en bas du player et l'obstacle
        if (getY() < obstacle.getY() && getY() + getHeight() > (obstacle.getY() + obstacle.getHeight()))
            return true;

        return false;
    }

    /**
     * Collision entre le player et un coeur
     * @param coeur de la liste de coeur
     * @return true si collision, false sinon
     */
    public boolean isCollidingWithCoeur(Coeur coeur) {
        // comparaison entre le point en haut du player et le coeur
        if (getY() > coeur.getY() && getY() < (coeur.getY() + coeur.getHeight())) return true;
        // comparaison entre le point en bas du player et le coeur
        if (getY() + getHeight() > coeur.getY() && getY() + getHeight() < (coeur.getY() + coeur.getHeight()))
            return true;
        // comparaison entre le point en haut et en bas du player et le coeur
        if (getY() < coeur.getY() && getY() + getHeight() > (coeur.getY() + coeur.getHeight()))
            return true;

        return false;
    }
}
