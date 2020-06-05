package pt.ipp.estg.pharmacies.Game.Room;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "scores")
public class ScoreTableModel {

    @PrimaryKey(autoGenerate = true)
    private int id;

    private int numRightQuestions;
    private int difficulty;
    private int numTotalQuestions;
    private int score;

    public ScoreTableModel(int numRightQuestions, int difficulty, int numTotalQuestions, int score){
        this.id = 0;
        this.numRightQuestions = numRightQuestions;
        this.difficulty = difficulty;
        this.numTotalQuestions = numTotalQuestions;
        this.score = score;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getNumRightQuestions() {
        return numRightQuestions;
    }

    public void setNumRightQuestions(int numRightQuestions) {
        this.numRightQuestions = numRightQuestions;
    }

    public int getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(int difficulty) {
        this.difficulty = difficulty;
    }

    public int getNumTotalQuestions() {
        return numTotalQuestions;
    }

    public void setNumTotalQuestions(int numTotalQuestions) {
        this.numTotalQuestions = numTotalQuestions;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }
}
