package jeu.or.olympicrunner.manager;

import android.app.Activity;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;

import java.util.HashMap;

import jeu.or.olympicrunner.R;

/**
 * Gère la musique, la joue et la stop
 */
public class MusicManager implements SoundPool.OnLoadCompleteListener {

    public Activity context;

    // musique devant être jouée
    public int music;

    private boolean soundPlaying;

    private MediaPlayer mediaPlayer;

    // Initialisation des petits sons
    private SoundPool soundPool;
    private HashMap mapDeSons;

    private boolean soundPoolLoaded;

    public MusicManager(Activity context) {
        this.context = context;

        soundPlaying = false;

        soundPool = new SoundPool(2, AudioManager.STREAM_MUSIC, 100);
        mapDeSons = new HashMap<String, Integer>(1);

        // premier son est mis dans le HashMap
        mapDeSons.put("oof", soundPool.load(context, R.raw.oof,1));

        soundPool.setOnLoadCompleteListener(this);
        soundPoolLoaded = false;
    }

    /**
     * Stop la musique
     */
    public void stop() {
        if (soundPlaying) {
            mediaPlayer.stop();
            mediaPlayer.release();
            soundPlaying = false;
        }
    }

    /**
     * Démarre la musique
     *
     * @param music la musique choisie
     */
    public void start(int music) {
        if (!soundPlaying) {
            this.music = music;
            mediaPlayer = MediaPlayer.create(context.getApplicationContext(), music);
            mediaPlayer.setVolume(0.5f, 0.5f);
            mediaPlayer.setLooping(true);
            mediaPlayer.start();
            soundPlaying = true;
        }
    }

    /**
     *
     * @param soundPool
     * @param sampleId
     * @param status
     */
    @Override
    public void onLoadComplete(SoundPool soundPool, int sampleId, int status) {
        soundPoolLoaded = true;
    }

    /**
     * Joue le son
     * @param pSoundName le son devant être joué
     */
    public void playSound(String pSoundName) {
        if (soundPoolLoaded) {
            soundPool.play((Integer) mapDeSons.get(pSoundName), 1, 1, 1, 0, 1f);
        }
    }
}
