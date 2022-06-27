package jeu.or.olympicrunner.ui;

import androidx.constraintlayout.widget.ConstraintLayout;

import jeu.or.olympicrunner.model.Obstacle;

import java.util.List;

/**
 * Récupère les valeurs de l'écran pour l'utiliser avec plusieurs Activity
 */
public interface EcranActivity {

    ConstraintLayout getEcran();

    List<Obstacle> getObstaclesToDelete();
}
