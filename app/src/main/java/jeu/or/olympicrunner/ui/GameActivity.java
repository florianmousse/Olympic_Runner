package jeu.or.olympicrunner.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.view.GestureDetectorCompat;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import jeu.or.olympicrunner.manager.MusicManager;
import jeu.or.olympicrunner.manager.ScoreManager;
import jeu.or.olympicrunner.manager.UserManager;
import jeu.or.olympicrunner.model.MyCountDownTimer;
import jeu.or.olympicrunner.R;
import jeu.or.olympicrunner.model.TypeObstacle;
import jeu.or.olympicrunner.model.AnimatedPlayer;
import jeu.or.olympicrunner.model.Coeur;
import jeu.or.olympicrunner.model.Obstacle;
import jeu.or.olympicrunner.model.Piste;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;


public class GameActivity extends AppCompatActivity implements GestureDetector.OnGestureListener, EcranActivity {

    private ScoreManager scoreManager = ScoreManager.getInstance();
    private UserManager userManager = UserManager.getInstance();


    private String startTime;


    private SharedPreferences preferences;
    private MusicManager musicManager;
    private GestureDetectorCompat gestureDetector;
    private AnimatedPlayer player;
    private Piste piste;
    private MyCountDownTimer countDownTimer;

    // Initialisation des éléments du xml
    private ConstraintLayout ecran;
    public ConstraintLayout getEcran() {
        return ecran;
    }
    private TextView score, score_bestscore, textAuMilieu;
    private ImageView coeur1, coeur2, coeur3;
    private ImageView piste_depart, count_pannel;

    // Partie initialisée ou non
    private boolean initialized;

    // Partie en cours ou non
    private boolean partieEnCours;

    // Compteur de la partie
    private static int compteur;
    private int compteurtemp;

    // nombre de vies du player
    private static int nbVies;

    // int, String et boolean des SharedPreferences
    private String bestscore1, bestscore2, bestscore3;
    private String dateBestScore1, dateBestScore2;
    private String usernameBestScore1, usernameBestScore2;


    private String scorePerso1, scorePerso2, scorePerso3;
    private String datePerso1, datePerso2;
    private String username;
    private String totalGameTime;


    private boolean sound_preferences;

    // Vitesse des obstacles et vitesse du countDownTimer
    private double vitesse = 0.004;
    private double vitesseReelle;
    private static long OBSTACLE_MOVEMENT = 1000 / 200;

    // Listes des obstacles et des coeurs
    private List<Obstacle> obstacles, obstaclesToDelete;
    public List<Obstacle> getObstaclesToDelete() {
        return obstaclesToDelete;
    }
    private List<Coeur> coeurs, coeursToDelete;
    public List<Coeur> getCoeursToDelete() {
        return coeursToDelete;
    }

    // Colonne du premier et du deuxième obstacle
    private int colonneObstacle1, colonneObstacle2;

    // play offline
    private boolean play_offline;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

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


        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale.getDefault());
        startTime = sdf.format(new Date());


        //Instantiation du gesture detector comme l'écouteur des évènements de MainActivity
        gestureDetector = new GestureDetectorCompat(this, this);



        if (!play_offline) getFirebaseData();




        preferences = PreferenceManager.getDefaultSharedPreferences(this);

        // extrait la valeur du son s'il peut être joué ou non
        sound_preferences = preferences.getBoolean("Sound", true);

        ecran = findViewById(R.id.gameEcran);
        score = findViewById(R.id.score);
        score_bestscore = findViewById(R.id.score_bestscore);
        textAuMilieu = findViewById(R.id.decompte);
        coeur1 = findViewById(R.id.coeur1);
        coeur2 = findViewById(R.id.coeur2);
        coeur3 = findViewById(R.id.coeur3);
        piste_depart = findViewById(R.id.piste_depart);
        player = findViewById(R.id.animatedPlayer);
        count_pannel = findViewById(R.id.count_pannel);

        obstacles = new ArrayList<>();
        obstaclesToDelete = new ArrayList<>();
        coeurs = new ArrayList<>();
        coeursToDelete = new ArrayList<>();

        // Initialise le nombre de vies et le compteur
        nbVies = 3;
        compteurtemp = 0;
        compteur = 0;

        // la partie n'est pas en cours et n'est pas initialisée
        partieEnCours = false;
        initialized = false;

        // Couleur et taille du score et du best_score s'il est battu
        score.setTextSize(14);
        score.setTextColor(Color.YELLOW);
        score_bestscore.setTextSize(12);
        score_bestscore.setTextColor(Color.YELLOW);

        // Affiche les coeurs
        coeur1.setBackgroundResource(R.drawable.coeur_plein);
        coeur2.setBackgroundResource(R.drawable.coeur_plein);
        coeur3.setBackgroundResource(R.drawable.coeur_plein);

        // Initialise la musique si les SharedPreferences le permettent
        if (sound_preferences) musicManager = new MusicManager(this);

        // Met en avant chaque élément
        bringAllToFront();
    }

    private void getFirebaseData() {
        scoreManager.getScoreData().addOnSuccessListener(score -> {
            //extraire la valeur de Score ou 0 si le score n'est pas sauvegardé
            bestscore1 = TextUtils.isEmpty(score.getScore1()) ? "0" : score.getScore1();
            bestscore2 = TextUtils.isEmpty(score.getScore2()) ? "0" : score.getScore2();
            bestscore3 = TextUtils.isEmpty(score.getScore3()) ? "0" : score.getScore3();
            dateBestScore1 = TextUtils.isEmpty(score.getTime1()) ? "" : score.getTime1();
            dateBestScore2 = TextUtils.isEmpty(score.getTime2()) ? "" : score.getTime2();
            usernameBestScore1 = TextUtils.isEmpty(score.getUser1()) ? "" : score.getUser1();
            usernameBestScore2 = TextUtils.isEmpty(score.getUser2()) ? "" : score.getUser2();
        });


        userManager.getUserData().addOnSuccessListener(user -> {
            username = TextUtils.isEmpty(user.getUsername()) ? "" : user.getUsername();
            scorePerso1 = TextUtils.isEmpty(user.getScoreperso1()) ? "0" : user.getScoreperso1();
            scorePerso2 = TextUtils.isEmpty(user.getScoreperso2()) ? "0" : user.getScoreperso2();
            scorePerso3 = TextUtils.isEmpty(user.getScoreperso3()) ? "0" : user.getScoreperso3();
            datePerso1 = TextUtils.isEmpty(user.getDateperso1()) ? "" : user.getDateperso1();
            datePerso2 = TextUtils.isEmpty(user.getDateperso2()) ? "" : user.getDateperso2();
            totalGameTime = TextUtils.isEmpty(user.getTotalGameTime()) ? "0" : user.getTotalGameTime();
        });
    }

    /**
     * Créer un obstacle suivant un pourcentage dans une colone au hasard
     */
    public void creerObstacle() {
        Random rand = new Random();
        colonneObstacle1 = rand.nextInt(3) + 1;
        if (colonneObstacle1 == 1) {
            if (Math.random() <= 0.5) colonneObstacle2 = 2;
            else colonneObstacle2 = 3;
        } else if (colonneObstacle1 == 2) {
            if (Math.random() <= 0.5) colonneObstacle2 = 1;
            else colonneObstacle2 = 3;
        } else {
            if (Math.random() <= 0.5) colonneObstacle2 = 1;
            else colonneObstacle2 = 2;
        }

        boolean alreadyPlaced = false;

        // Créer un obstacle long sinon court
        if (Math.random() <= 0.30 && compteur > 5000)
            obstacles.add(new Obstacle(this, colonneObstacle1, TypeObstacle.LONG));
        else obstacles.add(new Obstacle(this, colonneObstacle1, TypeObstacle.COURT));

        // Créer un deuxième obstacle court dans une colonne différente
        if (Math.random() <= 0.30 && compteur < 10000 && compteur > 2000) {
            obstacles.add(new Obstacle(this, colonneObstacle2, TypeObstacle.COURT));
            alreadyPlaced = true;
        }

        // Sinon créer un deuxième obstacle court avec moins de chance
        if (Math.random() <= 0.20 && compteur > 2000 && !alreadyPlaced) {
            obstacles.add(new Obstacle(this, colonneObstacle2, TypeObstacle.COURT));
            alreadyPlaced = true;
        }

        // Sinon créer un deuxième obstacle long avec encore moins de chance
        if (Math.random() <= 0.10 && compteur > 2000 && !alreadyPlaced) {
            obstacles.add(new Obstacle(this, colonneObstacle2, TypeObstacle.LONG));
        }
    }

    /**
     * Créer un coeur dans une colonne au hasard
     */
    public void creerCoeur() {
        Random rand = new Random();
        int colonneCoeur;
        if (Math.random() <= 0.01 && nbVies < 3) {
            // tant que c'est pas dans une colonne vide
            do {
                colonneCoeur = rand.nextInt(3) + 1;
            } while (colonneCoeur == colonneObstacle1 || colonneCoeur == colonneObstacle2);

            coeurs.add(new Coeur(this, colonneCoeur));
        }

    }

    /**
     * Regarde et agit selon les collisions et les objets
     */
    public void collision() {
        // Pour chaque obstacles
        for (Obstacle obstacle : obstacles) {
            // Si l'obstacle est dans la même colonne que le joueur, qu'il est en collision avec et que son nombre de vie est supérieur à 1
            if (obstacle.getObstacleColonne() == player.getColonne() && player.isCollidingWithObstacle(obstacle) && nbVies > 1) {
                // Joue un son
                if (sound_preferences) musicManager.playSound("oof");

                // Supprime l'image de l'obsatcle
                obstaclesToDelete.add(obstacle);
                for (Obstacle obstacle2 : obstaclesToDelete) obstacles.remove(obstacle2);
                obstaclesToDelete.clear();
                ecran.removeView(obstacle);
                // Enlève une vie
                nbVies -= 1;

                // Affiche un Toast avc le nombre de vies restantes
                if (nbVies == 2)
                    Toast.makeText(this, nbVies + getString(R.string.vies_restante), Toast.LENGTH_SHORT).show();
                else if (nbVies == 1)
                    Toast.makeText(this, nbVies + getString(R.string.vie_restante), Toast.LENGTH_SHORT).show();

                // Enlève une vie à l'affichage
                enleverCoeurs();

                return;
            }

            // Si le nombre de vie est égal à 1
            if (obstacle.getObstacleColonne() == player.getColonne() && player.isCollidingWithObstacle(obstacle) && nbVies == 1) {

                // Enlève le dernier coeur de l'affichage
                nbVies = 0;
                enleverCoeurs();

                // Si compteur bat un des meilleurs scores alors on affiche bravo sinon on demande de recommencer
                if (!play_offline) {
                    if (compteur > Integer.parseInt(bestscore3)) textAuMilieu.setText(R.string.bravo_mort);
                    else textAuMilieu.setText(R.string.recommencer_mort);
                } else {
                    textAuMilieu.setText(R.string.recommencer_mort);
                }
                textAuMilieu.setTextSize(50);

                // Stop timer et arrête la partie
                countDownTimer.stopTimer();
                partieEnCours = false;

                new CountDownTimer(2000, 1000) {

                    @Override
                    public void onTick(long millisUntilFinished) {
                    }

                    @Override
                    public void onFinish() {
                        // Change les préférences et termine l'Activity
                        if (!play_offline) {
                            changePreferences();
                            finish();
                        }
                    }
                }.start();

                if (play_offline) startActivity(new Intent(this, NoConnectionActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
            }
        }


        for (Coeur coeur : coeurs) {
            if (coeur.getCoeurColonne() == player.getColonne() && player.isCollidingWithCoeur(coeur) && nbVies > 0) {

                coeursToDelete.add(coeur);
                for (Coeur coeur2 : coeursToDelete) coeurs.remove(coeur2);
                coeursToDelete.clear();

                ecran.removeView(coeur);
                ajouterCoeurs();
                nbVies += 1;

                return;
            }
        }
    }

    /**
     * Change les images de coeur lors d'une suppression
     */
    private void enleverCoeurs() {
        // Si nombre de vies est égal à 2 alors on met un coeur vide à la première image
        if (nbVies == 2) {
            coeur1.setBackgroundResource(R.drawable.coeur_vide);
        }
        // Si nombre de vies est égal à 1 alors on met un coeur vide à la deuxième image
        if (nbVies == 1) {
            coeur2.setBackgroundResource(R.drawable.coeur_vide);
        }
        // Si nombre de vies est égal à 0 alors on met un coeur vide à la troisième image
        if (nbVies == 0) {
            coeur3.setBackgroundResource(R.drawable.coeur_vide);
        }
    }

    /**
     * Change les images de coeurs lors d'un ajout
     */
    private void ajouterCoeurs() {
        // Si nombre de vies est égal à 2 alors on met un coeur plein à la première image
        if (nbVies == 2) {
            coeur1.setBackgroundResource(R.drawable.coeur_plein);
        }
        // Si nombre de vies est égal à 1 alors on met un coeur plein à la deuxième image
        if (nbVies == 1) {
            coeur2.setBackgroundResource(R.drawable.coeur_plein);
        }
    }

    /**
     * Change les scores du classement
     */
    private void changePreferences() {
        // Récupère la date et l'heure actuelle
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault());
        String endGameTime = dateFormat.format(new Date());

        getFirebaseData();

        // si compteur est supérieur au meilleur score
        if (compteur > Integer.parseInt(bestscore1)) {
            scoreManager.updateScore3(bestscore2);
            scoreManager.updateUser3(usernameBestScore2);
            scoreManager.updateTime3(dateBestScore2);

            scoreManager.updateScore2(bestscore1);
            scoreManager.updateUser2(usernameBestScore1);
            scoreManager.updateTime2(dateBestScore1);

            scoreManager.updateUser1(username);
            scoreManager.updateScore1(String.valueOf(compteur));
            scoreManager.updateTime1(endGameTime);

        } else if (compteur > Integer.parseInt(bestscore2)) {

            scoreManager.updateScore3(bestscore2);
            scoreManager.updateUser3(usernameBestScore2);
            scoreManager.updateTime3(dateBestScore2);

            scoreManager.updateUser2(username);
            scoreManager.updateScore2(String.valueOf(compteur));
            scoreManager.updateTime2(endGameTime);

        } else if (compteur > Integer.parseInt(bestscore3)) {
            scoreManager.updateUser3(username);
            scoreManager.updateScore3(String.valueOf(compteur));
            scoreManager.updateTime3(endGameTime);
        }



        if (compteur > Integer.parseInt(scorePerso1)) {
            userManager.updateScorePerso3(scorePerso2);
            userManager.updateDatePerso3(datePerso2);

            userManager.updateScorePerso2(scorePerso1);
            userManager.updateDatePerso2(datePerso1);

            userManager.updateScorePerso1(String.valueOf(compteur));
            userManager.updateDatePerso1(endGameTime);
        } else if (compteur > Integer.parseInt(scorePerso2)) {
            userManager.updateScorePerso3(scorePerso2);
            userManager.updateDatePerso3(datePerso2);

            userManager.updateScorePerso2(String.valueOf(compteur));
            userManager.updateDatePerso2(endGameTime);
        } else if (compteur > Integer.parseInt(scorePerso3)) {
            userManager.updateScorePerso3(String.valueOf(compteur));
            userManager.updateDatePerso3(endGameTime);
        }

        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale.getDefault());
        String endTime = sdf.format(new Date());

        Date startDate = null;
        Date endDate = null;
        try {
            startDate = sdf.parse(startTime);
            endDate = sdf.parse(endTime);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        long timeDiff = (endDate != null ? endDate.getTime() : 0) - (startDate != null ? startDate.getTime() : 0);

        int tempGameTime = (int) (Integer.parseInt(totalGameTime) + timeDiff);

        userManager.updateTotalGameTime(String.valueOf(tempGameTime));
    }

    /**
     * Méthode principale mettant en mouvement le jeu
     */
    private void startGameTimer() {

        countDownTimer = new MyCountDownTimer(4 * 1000, 1000) {
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
                // compteur du départ, affiche go quand c'est finit sinon le nombre de secondes
                if (millisUntilFinished / 1000 == 0) textAuMilieu.setText(R.string.go_start_game);
                else textAuMilieu.setText("" + millisUntilFinished / 1000);
                textAuMilieu.setTextSize(60);
            }

            @Override
            public void onFinish() {
                textAuMilieu.setText("");

                // commecne la musique après le décompte
                if (sound_preferences) musicManager.start(R.raw.music);

                // la partie est en cours
                partieEnCours = true;

                timer = new Timer();

                timer.schedule(new TimerTask() {

                    @Override
                    public void run() {
                        GameActivity.this.runOnUiThread(new Runnable() {
                            public void run() {

                                //if (!isNetworkAvailable()) noConnection();

                                compteurtemp += compteur;
                                // créer les obstacles et les coeurs suivant la vitesse
                                if (compteur % (int) ((1000.0 / OBSTACLE_MOVEMENT) * (0.666 - vitesse)) == 0) {
                                    if (compteurtemp > 100 || compteur <= 100) {
                                        creerObstacle();
                                        creerCoeur();
                                        compteurtemp = 0;
                                    }
                                }

                                // augmente la vitesse de chaque obstacle tous les 600
                                for (Obstacle obstacle : obstacles) {
                                    if (compteur % 600 == 0) {
                                        vitesse += 0.000034;
                                    }
                                    // vitesseReelle prend la valeur de vitesse pour bloquer la vitesse max des obstacles
                                    vitesseReelle = vitesse;
                                    if (vitesseReelle > 0.012) vitesseReelle = 0.012;
                                    obstacle.moveObstacle(vitesseReelle);
                                }
                                // bouge l'écran de départ jusqu'au milieu de l'écran
                                if (piste_depart.getY() <= (float) ecran.getHeight() / 2)
                                    piste_depart.setY((float) (piste_depart.getY() + ecran.getHeight() * vitesseReelle));
                                if (piste.getY() + piste.getHeight() <= (float) ecran.getHeight() / 2)
                                    piste.movePiste(vitesseReelle);
                                // Bouge chaque coeur à la vitesse du jeu
                                for (Coeur coeur : coeurs) {
                                    coeur.moveCoeur(vitesseReelle);
                                }

                                // supprime les obstacles dans une autre liste
                                for (Obstacle obstacle : obstaclesToDelete)
                                    obstacles.remove(obstacle);
                                obstaclesToDelete.clear();
                                // supprime les coeurs dans une autre liste
                                for (Coeur coeur : coeursToDelete)
                                    coeurs.remove(coeur);
                                coeursToDelete.clear();

                                // change les images du player
                                player.update(compteur);

                                compteur++;

                                // affiche le compteur du jeu
                                score.setText("" + compteur);
                                // affiche si le joueur bat un de ses records
                                if (!play_offline) {
                                    if (compteur > Integer.parseInt(bestscore1) && Integer.parseInt(bestscore1) != 0)
                                        score_bestscore.setText(R.string.first_record);
                                    else if (compteur > Integer.parseInt(bestscore2) && Integer.parseInt(bestscore2) != 0)
                                        score_bestscore.setText(R.string.second_record);
                                    else if (compteur > Integer.parseInt(bestscore3) && Integer.parseInt(bestscore3) != 0)
                                        score_bestscore.setText(R.string.third_record);
                                }

                                // Si le player se cogne avec un obstacle
                                collision();

                                // Met les éléments en avant pour que les barrières passent derrière
                                bringAllToFront();
                            }
                        });
                    }
                }, 0, OBSTACLE_MOVEMENT);
            }
        };
        countDownTimer.start();
    }

    private void noConnection() {
        Intent intent = new Intent(this, NoConnectionActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }


    /**
     * Met au premier plan plusieurs éléments pour pas que les barrières passent devant
     */
    public void bringAllToFront() {
        coeur1.bringToFront();
        coeur2.bringToFront();
        coeur3.bringToFront();
        count_pannel.bringToFront();
        score.bringToFront();
        score_bestscore.bringToFront();
        textAuMilieu.bringToFront();
    }

    @Override
    protected void onPause() {
        countDownTimer.stopTimer();
        if (sound_preferences) musicManager.stop();
        partieEnCours = false;

        if (!play_offline) changePreferences();
        finish();

        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Supprime la barre de notification
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
    public void onBackPressed() {
        super.onBackPressed();

        countDownTimer.stopTimer();
        if (sound_preferences) musicManager.stop();
        partieEnCours = false;

        if (!play_offline) changePreferences();
        finish();
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);

        if (hasFocus) {
            if (!initialized) {
                // Débute et arrête les mouvements
                startGameTimer();

                piste = new Piste(this);

                // Définit hauteur du player
                int hauteurPerso = (int) Math.floor(0.103 * (double) ecran.getHeight());

                player.initAnimatedPlayer(this, new int[]{
                        R.drawable.player_un,
                        R.drawable.player_deux,
                        R.drawable.player_trois,
                        R.drawable.player_quatre,
                        R.drawable.player_cinq,
                        R.drawable.player_six,
                        R.drawable.player_sept,
                        R.drawable.player_huit,
                        R.drawable.player_neuf,
                        R.drawable.player_dix,
                        R.drawable.player_onze,
                        R.drawable.player_douze,
                        R.drawable.player_treize,
                        R.drawable.player_quatorze,
                        R.drawable.player_quinze,
                        R.drawable.player_seize,
                        R.drawable.player_dixsept,
                        R.drawable.player_dixhuit,
                        R.drawable.player_dixneuf,
                        R.drawable.player_vingt,
                        R.drawable.player_vingtun,
                        R.drawable.player_vingtdeux,
                        R.drawable.player_vingttrois,
                        R.drawable.player_vingtquatre,
                        R.drawable.player_vingtcinq,
                        R.drawable.player_vingtsix,
                        R.drawable.player_vingtsept,
                        R.drawable.player_vingthuit,
                        R.drawable.player_vingtneuf,
                        R.drawable.player_trente,
                        R.drawable.player_trenteun,
                        R.drawable.player_trentedeux,
                        R.drawable.player_trentetrois,
                        R.drawable.player_trentequatre,
                        R.drawable.player_trentecinq,
                        R.drawable.player_trentesix,
                        R.drawable.player_trentesept,
                        R.drawable.player_trentehuit,
                        R.drawable.player_trenteneuf,
                        R.drawable.player_quarante,
                        R.drawable.player_quaranteun,
                        R.drawable.player_quarantedeux
                }, hauteurPerso);

                // dessine le player
                player.drawFrame();

                // Définit la taille du panneau et des écritures en fonction de l'écran
                count_pannel.getLayoutParams().width = ecran.getWidth() / 3;
                score.getLayoutParams().width = ecran.getWidth() / 3;
                score_bestscore.getLayoutParams().width = ecran.getWidth() / 3;

                initialized = true;
            }
        }
    }


    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        // Si swipe à gauche et player ne sort pas de l'écran
        if (e1.getX() - e2.getX() > 0 && player.getX() > ecran.getWidth() / 3) {
            // Si partie en cours alors perso bouge à gauche
            if (partieEnCours) player.moveToLeft();
        }

        // Si swipe à droite et player ne sort pas de l'écran
        if (e1.getX() - e2.getX() < 0 && player.getX() < (ecran.getWidth() - ecran.getWidth() / 3)) {
            // Si partie en cours alors perso bouge à droite
            if (partieEnCours) player.moveToRight();
        }

        return false;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        gestureDetector.onTouchEvent(event);
        return super.onTouchEvent(event);
    }

    @Override
    public boolean onDown(MotionEvent e) {
        return false;
    }

    @Override
    public void onShowPress(MotionEvent e) {

    }

    @Override
    public boolean onSingleTapUp(MotionEvent e) {
        return false;
    }

    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
        return false;
    }

    @Override
    public void onLongPress(MotionEvent e) {

    }

}