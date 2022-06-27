package jeu.or.olympicrunner.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import jeu.or.olympicrunner.R;

/**
 * Classe créant la section A Propos
 */
public class AProposActivity extends AppCompatActivity {

    private ImageButton discord;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_a_propos);

        ConstraintLayout aproposEcran = findViewById(R.id.aproposEcran);

        discord = findViewById(R.id.discord);

        discord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://discord.gg/sPZ8EE3K8q")));
            }
        });

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);

        // récupère la couleur dans les SharedPreferences
        String color = preferences.getString("Color", "#D2B4DE");

        // change la couleur de fond
        aproposEcran.setBackgroundColor(Color.parseColor(color));
    }

    @Override
    protected void onResume() {
        super.onResume();

        // enlève la barre de notification
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
}