package jeu.or.olympicrunner.manager;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Gère les SharedPreferences
 */
public class PrefManager {
    private SharedPreferences pref_tuto;
    private SharedPreferences.Editor editor_tuto;

    // Nom des SharedPreferences
    private static final String PREF_NAME = "androidhive-welcome";

    private static final String IS_FIRST_TIME_LAUNCH = "IsFirstTimeLaunch";

    public PrefManager(Context context) {
        // shared pref mode
        int PRIVATE_MODE = 0;
        pref_tuto = context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor_tuto = pref_tuto.edit();
        editor_tuto.apply();
    }

    /**
     * Change les SharedPreferences
     * @param isFirstTime le boolean du premier lancement
     */
    public void setFirstTimeLaunch(boolean isFirstTime) {
        editor_tuto.putBoolean(IS_FIRST_TIME_LAUNCH, isFirstTime);
        editor_tuto.commit();
    }

    public boolean isFirstTimeLaunch() {
        return pref_tuto.getBoolean(IS_FIRST_TIME_LAUNCH, true);
    }

}
