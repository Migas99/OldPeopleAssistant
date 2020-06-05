package pt.ipp.estg.pharmacies.Game;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import pt.ipp.estg.pharmacies.Game.Room.InsertAsyncTask;
import pt.ipp.estg.pharmacies.Game.Room.ScoreDao;
import pt.ipp.estg.pharmacies.Game.Room.ScoreDataBase;
import pt.ipp.estg.pharmacies.Game.Room.ScoreTableModel;
import pt.ipp.estg.pharmacies.R;


public class GameScore extends Fragment {
    Button backButton;
    TextView showScore;
    TextView showRightQuestions;
    TextView showTotalQuestions;
    int calculateScore;
    int difficulty;
    int numRightQuestions;
    int numTotalQuestions;
    ScoreTableModel score;
    ScoreDataBase dataBase;
    ScoreDao scoreDao;
    GameScoreListener listener;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.game_score, container, false);

        showScore = view.findViewById(R.id.score);
        showRightQuestions = view.findViewById(R.id.rightQuestions);
        showTotalQuestions = view.findViewById(R.id.totalQuestions);
        backButton = view.findViewById(R.id.backGameMenu);

        dataBase = ScoreDataBase.getScoreDataBase(getActivity().getApplicationContext());
        scoreDao = dataBase.scoreDao();

        difficulty = getArguments().getInt("difficulty");
        numRightQuestions = getArguments().getInt("numRightQuestions");
        numTotalQuestions = getArguments().getInt("numTotalQuestions");
        calculateScore = (numRightQuestions*difficulty);

        score = new ScoreTableModel(numRightQuestions, difficulty, numTotalQuestions, calculateScore);

        new InsertAsyncTask(scoreDao).execute(score);

        showScore.setText("SCORE: " + calculateScore);
        showTotalQuestions.setText("RESPONDESTE A " + numTotalQuestions + " QUESTOES");
        showRightQuestions.setText("ACERTASTE A " + numRightQuestions + " QUESTOES");

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.backGameMenu();
            }
        });

        return view;
    }

    public interface GameScoreListener {
        void backGameMenu();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof GameMenu.MenuGameListener) {
            listener = (GameScoreListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement GameScoreListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        listener = null;
    }
}
