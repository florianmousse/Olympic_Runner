package jeu.or.olympicrunner.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.material.appbar.AppBarLayout;

import jeu.or.olympicrunner.manager.MusicManager;
import jeu.or.olympicrunner.manager.PrefManager;
import jeu.or.olympicrunner.manager.UserManager;
import jeu.or.olympicrunner.model.MyCountDownTimer;
import jeu.or.olympicrunner.R;
import jeu.or.olympicrunner.model.TypeObstacle;
import jeu.or.olympicrunner.model.Obstacle;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Activity du menu
 */
public class MenuActivity extends AppCompatActivity implements EcranActivity {

    private String myLanguage = "en";

    private ImageButton language;

    private UserManager userManager = UserManager.getInstance();

    private String username;

    private PrefManager prefManager;
    private MusicManager musicManager;

    // Ecran du menu
    private ConstraintLayout menuEcran;
    private AppBarLayout appbar;

    public ConstraintLayout getEcran() {
        return menuEcran;
    }

    // Compteur permettant de générer les obstacles
    private static int compteur;

    //vitesse du thread
    private static long OBSTACLE_MOVEMENT = 1000 / 160;

    // Vitesse des obstacles
    private double vitesse = 0.004;

    // Liste des obstacles
    private List<Obstacle> obstacles;
    private List<Obstacle> obstaclesToDelete;

    public List<Obstacle> getObstaclesToDelete() {
        return obstaclesToDelete;
    }

    // CountDownTimer pour le Menu
    private MyCountDownTimer countDownTimer;

    // ImageButton de l'interface
    private Button play_button;
    private ImageButton classement_button;
    private ImageButton settings_button;
    private ImageButton info_button;
    private ImageButton sound_button;
    private ImageButton account_button;

    // boolean pour savoir si l'icône du son doit être changée
    private boolean change_sound_ic;

    // Récupère le booléen pour jouer le son dans les SharedPreferences
    private boolean sound_preferences;

    // ImageView du compteur
    private ImageView compteur_imageview;

    // LinearLayout des titres
    private LinearLayout titres;

    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;

    // play offline
    private boolean play_offline;
    private TextView warning_playing_offline;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        prefManager = new PrefManager(this);
        preferences = PreferenceManager.getDefaultSharedPreferences(this);


        warning_playing_offline = findViewById(R.id.warning_playing_offline);

        if (savedInstanceState == null) {
            Bundle extras = getIntent().getExtras();
            if (extras == null) {
                play_offline = false;
            } else {
                play_offline = extras.getBoolean("PLAY_OFFLINE");
            }
        } else {
            play_offline = (boolean) savedInstanceState.getSerializable("PLAY_OFFLINE");
        }


        language = findViewById(R.id.language);


        // Récupère les SharedPreferences
        sound_preferences = preferences.getBoolean("Sound", true);

        // Initialisation de l'écran
        menuEcran = findViewById(R.id.menuEcran);
        appbar = findViewById(R.id.appbar);

        compteur_imageview = findViewById(R.id.compteur_imageview);
        titres = findViewById(R.id.titres);
        TextView titre1 = findViewById(R.id.titre1);
        TextView titre2 = findViewById(R.id.titre2);
        titre1.setTextColor(Color.YELLOW);
        titre2.setTextColor(Color.YELLOW);

        // Initialisation des listes d'obstacles
        obstacles = new ArrayList<>();
        obstaclesToDelete = new ArrayList<>();

        // Initialisation des ImageButton présents sur l'écran
        play_button = findViewById(R.id.play_button);
        classement_button = findViewById(R.id.classement_button);
        settings_button = findViewById(R.id.settings_button);
        info_button = findViewById(R.id.info_button);
        sound_button = findViewById(R.id.sound_button);
        account_button = findViewById(R.id.account_button);

        // Si c'est true dans les SharedPreferences alors on met l'icône du son on
        if (sound_preferences) {
            sound_button.setImageDrawable(getDrawable(R.drawable.ic_soundon));
            change_sound_ic = true;
        }
        // Sinon icône off
        else {
            sound_button.setImageDrawable(getDrawable(R.drawable.ic_soundoff));
            change_sound_ic = false;
        }


        // Si c'est hors ligne, on cache le classement et le profil
        if (play_offline) {
            warning_playing_offline.setVisibility(View.VISIBLE);
            classement_button.setVisibility(View.GONE);
            account_button.setVisibility(View.GONE);
        } else {
            warning_playing_offline.setVisibility(View.GONE);
            classement_button.setVisibility(View.VISIBLE);
            account_button.setVisibility(View.VISIBLE);

            // Quand clique sur l'ImageButton du classement, on ouvre ClassementActivity
            classement_button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ouvrirClassementActivity();
                }
            });

            account_button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    openProfileActivity();
                }
            });
        }


        // Quand clique sur l'ImageButton du jeu
        play_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ouvrirGameActivity();
            }
        });

        // Quand clique sur l'ImageButton des settings, on ouvre SettingsActivity
        settings_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                prefManager.setFirstTimeLaunch(true);
                ouvrirTutoActivity();
                //ouvrirSettingsActivity();
            }
        });

        // Quand clique sur l'ImageButton de l'info, on ouvre AProposActivity
        info_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ouvrirAProposActivity();
            }
        });


        // Quand clique sur l'ImageButton du son, on change l'image et les SharedPreferences
        sound_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Si c'est true alors on met l'icône off et on arrête le son
                if (change_sound_ic) {
                    sound_button.setImageDrawable(getDrawable(R.drawable.ic_soundoff));
                    change_sound_ic = false;
                    musicManager.stop();
                    editor = preferences.edit();
                    editor.putBoolean("Sound", false);
                    editor.apply();
                }
                // Sinon on met l'icône on et on joue le son
                else {
                    sound_button.setImageDrawable(getDrawable(R.drawable.ic_soundon));
                    change_sound_ic = true;
                    musicManager = new MusicManager(MenuActivity.this);
                    musicManager.start(R.raw.music_accueil);
                    editor = preferences.edit();
                    editor.putBoolean("Sound", true);
                    editor.apply();
                }
            }
        });



        Resources res = getResources();
        Configuration conf = res.getConfiguration();
        setLocale(conf.locale.toString().split("_")[0], false);

        language.setOnClickListener(view -> {
            if (myLanguage.equals("fr")) setLocale("en", true);
            else setLocale("fr", true);
        });
    }


    public void setLocale(String lang, boolean reload) {
        switch (lang) {
            case "fr":
                language.setBackgroundResource(R.drawable.fr);
                break;
            case "en":
                language.setBackgroundResource(R.drawable.en);
                break;
        }
        myLanguage = lang;

        if (reload) {
            SharedPreferences.Editor editor = preferences.edit();
            editor.putString("language", lang);
            editor.apply();
            Resources res = getResources();
            DisplayMetrics dm = res.getDisplayMetrics();
            Configuration conf = res.getConfiguration();
            conf.locale = new Locale(lang);
            res.updateConfiguration(conf, dm);
            Intent refresh = new Intent(this, MenuActivity.class);
            if (!isNetworkAvailable()) {
                if (play_offline) {
                    refresh.putExtra("PLAY_OFFLINE", true);
                    refresh.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    finish();
                    startActivity(refresh);
                } else noConnection();
            } else {
                finish();
                startActivity(refresh);
            }
        }
    }


    /**
     * Méthode permettant de générer les obstacles
     */
    public void createObstacle() {
        // Choisit la colonne de l'obstacle
        Random rand = new Random();
        int colonneObstacle = rand.nextInt(3) + 1;
        obstacles.add(new Obstacle(this, colonneObstacle, TypeObstacle.COURT));
    }


    /**
     * Méthode mettant en mouvement les barrières en arrière plan et le menu en général
     */
    private void startMenuTimer() {

        countDownTimer = new MyCountDownTimer(0, 0) {
            private Timer timer;

            @Override
            public void stopTimer() {
                if (timer != null) {
                    timer.cancel();
                    timer.purge();
                    timer = null;
                }
            }

            @Override
            public void onTick(long millisUntilFinished) {
            }

            @Override
            public void onFinish() {
                timer = new Timer();

                timer.schedule(new TimerTask() {

                    @Override
                    public void run() {
                        MenuActivity.this.runOnUiThread(new Runnable() {
                            public void run() {
                                //if (!isNetworkAvailable()) noConnection();
                                // Met en avant les éléments pour pas que les barrières passent devant
                                bringAllToFront();
                                // Si compteur est un multiple de 600 alors on créer un obstacle
                                if (compteur % 600 == 0) {
                                    createObstacle();
                                }
                                // Fait bouger les obstacles de la liste
                                for (Obstacle obstacle : obstacles) {
                                    obstacle.moveObstacle(vitesse);
                                }
                                // Supprime les obstacles de la liste obstacles
                                for (Obstacle obstacle : obstaclesToDelete)
                                    obstacles.remove(obstacle);
                                obstaclesToDelete.clear();

                                compteur++;
                            }
                        });
                    }
                }, 0, OBSTACLE_MOVEMENT);
            }
        };
        countDownTimer.start();
    }

    /**
     * Méthode mettant en avant les éléments de l'activité
     */
    public void bringAllToFront() {
        compteur_imageview.bringToFront();
        titres.bringToFront();
        play_button.bringToFront();
        appbar.bringToFront();
    }


    @Override
    protected void onPause() {
        super.onPause();
        countDownTimer.stopTimer();
        sound_preferences = preferences.getBoolean("Sound", true);
        if (sound_preferences) musicManager.stop();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (sound_preferences) {
            musicManager = new MusicManager(this);
            musicManager.start(R.raw.music_accueil);
        }
    }

    public void ouvrirGameActivity() {
        if (!isNetworkAvailable()) {
            if (play_offline) {
                Intent i = new Intent(MenuActivity.this, GameActivity.class);
                i.putExtra("PLAY_OFFLINE", true);
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(i);
            } else noConnection();
        } else {
            if (username.length() > 0 && username.length() < 13) {
                Intent intent = new Intent(this, GameActivity.class);
                startActivity(intent);
            }
        }
    }

    public void ouvrirClassementActivity() {
        //lancer ClassementActivity
        Intent intent = new Intent(this, ClassementActivity.class);
        startActivity(intent);
    }

    public void ouvrirSettingsActivity() {
        //lancer SettingsActivity
        Intent intent = new Intent(this, SettingsActivity.class);
        startActivity(intent);
    }

    public void ouvrirTutoActivity() {
        Intent i = new Intent(this, TutoActivity.class);
        i.putExtra("PLAY_OFFLINE", true);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(i);
    }

    public void ouvrirAProposActivity() {
        // lancer AProposActivity
        Intent intent = new Intent(this, AProposActivity.class);
        startActivity(intent);
    }

    public void openProfileActivity() {
        // lancer AProposActivity
        Intent intent = new Intent(this, ProfileActivity.class);
        startActivity(intent);
    }


    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        // Mettre l'activtié en plein écran
        fullscreen();
        if (hasFocus) {
            // Commencer à faire bouger les obstacles en arrière plan
            startMenuTimer();
        }

        userManager.getUserData().addOnSuccessListener(user -> {
            username = TextUtils.isEmpty(user.getUsername()) ? getString(R.string.info_no_username_found) : user.getUsername();

            if (username.length() > 0 && username.length() < 13) {
                play_button.setText(getString(R.string.play));
                play_button.setTextColor(getResources().getColor(R.color.textApp));
            } else {
                play_button.setText(getString(R.string.vous_devez_changer_de_pseudo));
                play_button.setTextColor(Color.RED);
            }
        });
    }

    private void noConnection() {
        Intent intent = new Intent(this, NoConnectionActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

    @Override
    public void onBackPressed() {
        AlertDialog alerte = new AlertDialog.Builder(this).create();
        alerte.setTitle(getString(R.string.quitter));
        alerte.setMessage(getString(R.string.voulez_vous_vraiment_quitter));

        alerte.setButton(DialogInterface.BUTTON_POSITIVE, getString(R.string.ok), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                finishAffinity();
            }
        });
        alerte.setButton(DialogInterface.BUTTON_NEGATIVE, getString(R.string.annuler), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
            }
        });
        alerte.show();
    }

    /**
     * Méthode servant à mettre l'activité en plein écran
     */
    public void fullscreen() {
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


    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
}