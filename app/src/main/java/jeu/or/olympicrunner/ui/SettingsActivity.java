package jeu.or.olympicrunner.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import jeu.or.olympicrunner.manager.PrefManager;
import jeu.or.olympicrunner.R;
import jeu.or.olympicrunner.settings.CouleurActivity;
import jeu.or.olympicrunner.ui.TutoActivity;

/**
 * Créer l'Activity des Settings, pour naviguer vers plusieurs paramètres changeable
 */
public class SettingsActivity extends AppCompatActivity {

    private PrefManager prefManager;
    private SharedPreferences preferences;

    private ConstraintLayout settingsEcran;

    // TextView des titres de LinearLayout
    private TextView jeu_textview;
    private TextView interface_textview;

    // LinearLayout regroupant les boutons du tuto et pour changer pseudo et le titre jeu
    private LinearLayout jeu_linearlayout;
    // LinearLayout regroupant le bouton du changement de couleur et le titre interface
    private LinearLayout interface_linearlayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        prefManager = new PrefManager(this);
        preferences = PreferenceManager.getDefaultSharedPreferences(this);


        jeu_textview = findViewById(R.id.jeu_textview);
        interface_textview = findViewById(R.id.interface_textview);
        jeu_linearlayout = findViewById(R.id.jeu_linearlayout);
        interface_linearlayout = findViewById(R.id.interface_linearlayout);
        settingsEcran = findViewById(R.id.settingsEcran);

        Button tuto_button = findViewById(R.id.tuto_button);
        Button couleur_button = findViewById(R.id.couleur_button);

        Button retour_button = findViewById(R.id.retour_button);

        prefManager.setFirstTimeLaunch(true);
        ouvrirTutoActivity();

        /**
         * Ouvre la TutoActivity quand clique sur le bouton
         */
        tuto_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                prefManager.setFirstTimeLaunch(true);
                ouvrirTutoActivity();
            }
        });

        /**
         * Ouvre la CouleurActivity quand clique sur le bouton
         */
        couleur_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ouvrirCouleurActivity();
            }
        });

        /**
         * Ferme l'activity quand clique sur le bouton
         */
        retour_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    public void ouvrirTutoActivity() {
        Intent intent = new Intent(this, TutoActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

    public void ouvrirCouleurActivity() {
        Intent intent = new Intent(this, CouleurActivity.class);
        startActivity(intent);
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

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);

        // change la couleur de fond des éléments de l'activity
        String color = preferences.getString("Color", "#D2B4DE");
        changeColor(color);
    }

    /**
     * Change la couleur de fond de l'écran et des LinearLayout
     * @param color la couleur des SharedPreferences
     */
    public void changeColor (String color) {
        settingsEcran.setBackgroundColor(Color.parseColor(color));
        jeu_textview.setBackgroundColor(Color.parseColor(color));
        interface_textview.setBackgroundColor(Color.parseColor(color));
        jeu_linearlayout.setBackgroundColor(Color.parseColor(color));
        interface_linearlayout.setBackgroundColor(Color.parseColor(color));
    }
}