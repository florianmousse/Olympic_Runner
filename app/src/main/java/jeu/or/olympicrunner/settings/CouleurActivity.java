package jeu.or.olympicrunner.settings;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;

import jeu.or.olympicrunner.R;

/**
 * Créer l'Activity des couleurs, permettant de changer la couleur de l'application
 */
public class CouleurActivity extends AppCompatActivity {

    private SharedPreferences preferences;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_couleur);

        Button rouge_button = findViewById(R.id.rouge_button);
        Button bleu_button = findViewById(R.id.bleu_button);
        Button violet_button = findViewById(R.id.violet_button);
        Button marron_button = findViewById(R.id.marron_button);
        Button vert_button = findViewById(R.id.vert_button);
        Button gris_button = findViewById(R.id.gris_button);

        ConstraintLayout couleurEcran = findViewById(R.id.couleurEcran);

        Button retour_button = findViewById(R.id.retour_button);

        preferences = PreferenceManager.getDefaultSharedPreferences(this);

        // récupère la couleur des SharedPreferences
        String color = preferences.getString("Color", "#D2B4DE");

        // Change la couleur du fond
        couleurEcran.setBackgroundColor(Color.parseColor(color));

        /**
         * Change la couleur des SharedPreferences quand clique sur un des boutons de couleur
         */
        rouge_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                newCouleur("#FF4646");
            }
        });
        bleu_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                newCouleur("#4F7FFF");
            }
        });
        violet_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                newCouleur("#D2B4DE");
            }
        });
        marron_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                newCouleur("#BA4A00");
            }
        });
        vert_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                newCouleur("#71FF55");
            }
        });
        gris_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                newCouleur("#CBCBCB");
            }
        });


        retour_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }


    @Override
    protected void onResume() {
        super.onResume();
        // supprime la barre de notification
        View decorView = getWindow().getDecorView();
        decorView.setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
        );
    }

    /**
     * Change la couleur des SharedPreferences
     * @param couleur la nouvelle couleur
     */
    public void newCouleur(String couleur) {
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("Color", couleur);
        editor.apply();
        finish();
    }
}