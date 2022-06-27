package jeu.or.olympicrunner.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseUser;

import java.util.Timer;
import java.util.TimerTask;

import jeu.or.olympicrunner.R;
import jeu.or.olympicrunner.manager.UserManager;

public class ProfileActivity extends AppCompatActivity {

    private Timer timer;

    private UserManager userManager = UserManager.getInstance();

    private Button signOutButton;
    private Button deleteButton;

    private TextView usernameEditText;
    private TextView emailTextView;

    private TextView total_number_of_hours;

    private ImageView profileImageView;

    private View profileview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        signOutButton = findViewById(R.id.signOutButton);
        deleteButton = findViewById(R.id.deleteButton);

        usernameEditText = findViewById(R.id.usernameEditText);
        emailTextView = findViewById(R.id.emailTextView);

        total_number_of_hours = findViewById(R.id.total_number_of_hours);

        profileImageView = findViewById(R.id.profileImageView);

        profileview = findViewById(R.id.profileview);

        updateUIWithUserData();
        setupListeners();
    }

    private void setupListeners(){

        TextWatcher fieldValidatorTextWatcher = new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (filterLongEnough()) {
                    userManager.updateUsername(usernameEditText.getText().toString())
                            .addOnSuccessListener(aVoid -> {
                                Snackbar.make(profileview, getString(R.string.updated_data), Snackbar.LENGTH_SHORT).show();
                            });
                } else {
                    if (usernameEditText.getText().toString().trim().length() == 0) usernameEditText.setError(getString(R.string.min_1_caractere));
                    else usernameEditText.setError(getString(R.string.max_13_caractere));
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }

            private boolean filterLongEnough() {
                return usernameEditText.getText().toString().trim().length() > 0 && usernameEditText.getText().toString().trim().length() < 13;
            }
        };
        usernameEditText.addTextChangedListener(fieldValidatorTextWatcher);

        // Sign out button
        signOutButton.setOnClickListener(view -> {
            userManager.signOut(this).addOnSuccessListener(aVoid -> {
                Intent intent = new Intent(this, LoginActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            });
        });

        // Delete button
        deleteButton.setOnClickListener(view -> {
            new AlertDialog.Builder(this)
                    .setMessage(R.string.popup_message_confirmation_delete_account)
                    .setPositiveButton(R.string.popup_message_choice_yes, (dialogInterface, i) ->
                            userManager.deleteUser(ProfileActivity.this)
                                    .addOnSuccessListener(aVoid -> {
                                                Intent intent = new Intent(this, LoginActivity.class);
                                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                                startActivity(intent);
                                            }
                                    )
                    )
                    .setNegativeButton(R.string.popup_message_choice_no, null)
                    .show();

        });
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

    private void updateUIWithUserData(){
        if(userManager.isCurrentUserLogged()){
            FirebaseUser user = userManager.getCurrentUser();

            if(user.getPhotoUrl() != null){
                setProfilePicture(user.getPhotoUrl());
            }
            setTextUserData(user);
            getUserData();
        }
    }

    private void getUserData(){
        userManager.getUserData().addOnSuccessListener(user -> {
            // Set the data with the user information
            String username = TextUtils.isEmpty(user.getUsername()) ? getString(R.string.info_no_username_found) : user.getUsername();
            usernameEditText.setText(username);

            String totalSeconds = TextUtils.isEmpty(user.getTotalGameTime()) ? "0" : user.getTotalGameTime();

            long totalGameTime = Integer.parseInt(totalSeconds);

            long secondsInMilli = 1000;
            long minutesInMilli = secondsInMilli * 60;
            long hoursInMilli = minutesInMilli * 60;
            long daysInMilli = hoursInMilli * 24;

            long elapsedDays = totalGameTime / daysInMilli;
            totalGameTime = totalGameTime % daysInMilli;

            long elapsedHours = totalGameTime / hoursInMilli;
            totalGameTime = totalGameTime % hoursInMilli;

            long elapsedMinutes = totalGameTime / minutesInMilli;
            totalGameTime = totalGameTime % minutesInMilli;

            long elapsedSeconds = totalGameTime / secondsInMilli;

            String text = "";

            if (elapsedDays > 0) text += elapsedDays + getString(R.string.days);
            if (elapsedHours > 0) text += elapsedHours + getString(R.string.hours);
            if (elapsedMinutes > 0) text += elapsedMinutes + getString(R.string.minutes);

            text += elapsedSeconds + getString(R.string.secondes);

            total_number_of_hours.setText(text);
        });
    }

    private void setProfilePicture(Uri profilePictureUrl){
        Glide.with(this)
                .load(profilePictureUrl)
                .apply(RequestOptions.circleCropTransform())
                .into(profileImageView);
    }

    private void setTextUserData(FirebaseUser user){
        //Get email & username from User
        String email = TextUtils.isEmpty(user.getEmail()) ? getString(R.string.info_no_email_found) : user.getEmail();
        String username = TextUtils.isEmpty(user.getDisplayName()) ? getString(R.string.info_no_username_found) : user.getDisplayName();

        //Update views with data
        usernameEditText.setText(username);
        emailTextView.setText(email);
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus) {
            timer = new Timer();
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    //if (!isNetworkAvailable()) noConnection();
                }
            }, 0, 2000);
        } else {
            if (timer != null) timer.cancel();
        }
    }

    @Override
    public void onStop() {
        if (timer != null) timer.cancel();
        super.onStop();
    }
}