package jeu.or.olympicrunner.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.Timer;
import java.util.TimerTask;

import jeu.or.olympicrunner.R;
import jeu.or.olympicrunner.manager.ScoreManager;
import jeu.or.olympicrunner.manager.UserManager;

/**
 * Activity du classement
 */
public class ClassementActivity extends AppCompatActivity {

    private ScoreManager scoreManager = ScoreManager.getInstance();
    private UserManager userManager = UserManager.getInstance();

    private Timer timer;


    private SharedPreferences preferences;


    private ConstraintLayout classementEcran;

    // pseudo, score et date de la première ligne
    private TextView bestscore_text1;
    private TextView DateBestscore_text1;
    private TextView pseudo_text1;
    // pseudo, score et date de la deuxième ligne
    private TextView bestscore_text2;
    private TextView DateBestscore_text2;
    private TextView pseudo_text2;
    // pseudo, score et date de la troisième ligne
    private TextView bestscore_text3;
    private TextView DateBestscore_text3;
    private TextView pseudo_text3;


    private TextView scorePerso1, scorePerso2, scorePerso3;
    private TextView datePerso1, datePerso2, datePerso3;

    private TextView textview, textView1, textView2, textView3, textView4, textView5, textView6;
    private TextView textView14, textView15, textView16, textView17, textView18, textView19;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_classement);

        preferences = PreferenceManager.getDefaultSharedPreferences(this);

        classementEcran = findViewById(R.id.classementEcran);

        bestscore_text1 = findViewById(R.id.bestscore_text1);
        bestscore_text2 = findViewById(R.id.bestscore_text2);
        bestscore_text3 = findViewById(R.id.bestscore_text3);
        DateBestscore_text1 = findViewById(R.id.DateBestscore_text1);
        DateBestscore_text2 = findViewById(R.id.DateBestscore_text2);
        DateBestscore_text3 = findViewById(R.id.DateBestscore_text3);
        pseudo_text1 = findViewById(R.id.pseudo_text1);
        pseudo_text2 = findViewById(R.id.pseudo_text2);
        pseudo_text3 = findViewById(R.id.pseudo_text3);



        scorePerso1 = findViewById(R.id.scorePerso1);
        scorePerso2 = findViewById(R.id.scorePerso2);
        scorePerso3 = findViewById(R.id.scorePerso3);
        datePerso1 = findViewById(R.id.datePerso1);
        datePerso2 = findViewById(R.id.datePerso2);
        datePerso3 = findViewById(R.id.datePerso3);




        textview = findViewById(R.id.textView);
        textView1 = findViewById(R.id.textView1);
        textView2 = findViewById(R.id.textView2);
        textView3 = findViewById(R.id.textView3);
        textView4 = findViewById(R.id.textView4);
        textView5 = findViewById(R.id.textView5);
        textView6 = findViewById(R.id.textView6);

        textView14 = findViewById(R.id.textview14);
        textView15 = findViewById(R.id.textview15);
        textView16 = findViewById(R.id.textview16);
        textView17 = findViewById(R.id.textview17);
        textView18 = findViewById(R.id.textview18);
        textView19 = findViewById(R.id.textview19);

        // recupère la couleur des SharedPreferecnes et la change
        String color = preferences.getString("Color", "#D2B4DE");
        changerColor(color);
    }

    @Override
    protected void onResume() {
        super.onResume();
        // supprime la barre des notifications
        View decorView = getWindow().getDecorView();
        decorView.setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
        );

        updateUIWithScoreData();
    }

    /**
     * Change la couleur de chaque élément du xml
     * @param color la couleur des SharedPreferences
     */
    public void changerColor(String color) {
        classementEcran.setBackgroundColor(Color.parseColor(color));
        pseudo_text1.setBackgroundColor(Color.parseColor(color));
        pseudo_text2.setBackgroundColor(Color.parseColor(color));
        pseudo_text3.setBackgroundColor(Color.parseColor(color));
        bestscore_text1.setBackgroundColor(Color.parseColor(color));
        bestscore_text2.setBackgroundColor(Color.parseColor(color));
        bestscore_text3.setBackgroundColor(Color.parseColor(color));
        DateBestscore_text1.setBackgroundColor(Color.parseColor(color));
        DateBestscore_text2.setBackgroundColor(Color.parseColor(color));
        DateBestscore_text3.setBackgroundColor(Color.parseColor(color));
        textview.setBackgroundColor(Color.parseColor(color));
        textView1.setBackgroundColor(Color.parseColor(color));
        textView2.setBackgroundColor(Color.parseColor(color));
        textView3.setBackgroundColor(Color.parseColor(color));
        textView4.setBackgroundColor(Color.parseColor(color));
        textView5.setBackgroundColor(Color.parseColor(color));
        textView6.setBackgroundColor(Color.parseColor(color));
        textView14.setBackgroundColor(Color.parseColor(color));
        textView15.setBackgroundColor(Color.parseColor(color));
        textView16.setBackgroundColor(Color.parseColor(color));
        textView17.setBackgroundColor(Color.parseColor(color));
        textView18.setBackgroundColor(Color.parseColor(color));
        textView19.setBackgroundColor(Color.parseColor(color));
        scorePerso1.setBackgroundColor(Color.parseColor(color));
        scorePerso2.setBackgroundColor(Color.parseColor(color));
        scorePerso3.setBackgroundColor(Color.parseColor(color));
        datePerso1.setBackgroundColor(Color.parseColor(color));
        datePerso2.setBackgroundColor(Color.parseColor(color));
        datePerso3.setBackgroundColor(Color.parseColor(color));
    }

    private void updateUIWithScoreData() {
        getUserData();
        getScoreData();
    }

    private void getUserData() {
        userManager.getUserData().addOnSuccessListener(user -> {
            String score1 = TextUtils.isEmpty(user.getScoreperso1()) ? "0" : user.getScoreperso1();
            scorePerso1.setText(score1);
            String time1 = TextUtils.isEmpty(user.getDateperso1()) ? "" : user.getDateperso1();
            datePerso1.setText(time1);

            String score2 = TextUtils.isEmpty(user.getScoreperso2()) ? "0" : user.getScoreperso2();
            scorePerso2.setText(score2);
            String time2 = TextUtils.isEmpty(user.getDateperso2()) ? "" : user.getDateperso2();
            datePerso2.setText(time2);

            String score3 = TextUtils.isEmpty(user.getScoreperso3()) ? "0" : user.getScoreperso3();
            scorePerso3.setText(score3);
            String time3 = TextUtils.isEmpty(user.getDateperso3()) ? "" : user.getDateperso3();
            datePerso3.setText(time3);
        });
    }

    private void getScoreData() {
        scoreManager.getScoreData().addOnSuccessListener(score -> {
            String score1 = TextUtils.isEmpty(score.getScore1()) ? "0" : score.getScore1();
            bestscore_text1.setText(score1);
            String user1 = TextUtils.isEmpty(score.getUser1()) ? "" : score.getUser1();
            pseudo_text1.setText(user1);
            String time1 = TextUtils.isEmpty(score.getTime1()) ? "" : score.getTime1();
            DateBestscore_text1.setText(time1);

            String score2 = TextUtils.isEmpty(score.getScore2()) ? "0" : score.getScore2();
            bestscore_text2.setText(score2);
            String user2 = TextUtils.isEmpty(score.getUser2()) ? "" : score.getUser2();
            pseudo_text2.setText(user2);
            String time2 = TextUtils.isEmpty(score.getTime2()) ? "" : score.getTime2();
            DateBestscore_text2.setText(time2);

            String score3 = TextUtils.isEmpty(score.getScore3()) ? "0" : score.getScore3();
            bestscore_text3.setText(score3);
            String user3 = TextUtils.isEmpty(score.getUser3()) ? "" : score.getUser3();
            pseudo_text3.setText(user3);
            String time3 = TextUtils.isEmpty(score.getTime3()) ? "" : score.getTime3();
            DateBestscore_text3.setText(time3);
        });
    }


    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus) {
            updateUIWithScoreData();
            timer = new Timer();
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    //if (!isNetworkAvailable()) noConnection();
                    updateUIWithScoreData();
                }
            }, 0, 2000);
        } else {
            if (timer != null) timer.cancel();
        }
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

    @Override
    public void onStop() {
        if (timer != null) timer.cancel();
        super.onStop();
    }
}