package jeu.or.olympicrunner.manager;

import jeu.or.olympicrunner.model.Score;
import jeu.or.olympicrunner.repository.ScoreRepository;
import com.google.android.gms.tasks.Task;

public class ScoreManager {
    private static volatile ScoreManager instance;
    private ScoreRepository scoreRepository;

    private ScoreManager() {
        scoreRepository = ScoreRepository.getInstance();
    }

    public static ScoreManager getInstance() {
        ScoreManager result = instance;
        if (result != null) {
            return result;
        }
        synchronized(ScoreManager.class) {
            if (instance == null) {
                instance = new ScoreManager();
            }
            return instance;
        }
    }

    public void createScore(){
        scoreRepository.createScore();
    }

    public Task<Score> getScoreData(){
        return scoreRepository.getScoreData().continueWith(task -> task.getResult().toObject(Score.class)) ;
    }

    public Task<Void> updateScore1(String score){
        return scoreRepository.updateScore1(score);
    }

    public Task<Void> updateUser1(String user){
        return scoreRepository.updateUser1(user);
    }

    public Task<Void> updateTime1(String time){
        return scoreRepository.updateTime1(time);
    }

    public Task<Void> updateScore2(String score){
        return scoreRepository.updateScore2(score);
    }

    public Task<Void> updateUser2(String user){
        return scoreRepository.updateUser2(user);
    }

    public Task<Void> updateTime2(String time){
        return scoreRepository.updateTime2(time);
    }

    public Task<Void> updateScore3(String score){
        return scoreRepository.updateScore3(score);
    }

    public Task<Void> updateUser3(String user){
        return scoreRepository.updateUser3(user);
    }

    public Task<Void> updateTime3(String time){
        return scoreRepository.updateTime3(time);
    }
}
