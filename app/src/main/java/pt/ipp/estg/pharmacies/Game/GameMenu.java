package pt.ipp.estg.pharmacies.Game;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import java.util.concurrent.ExecutionException;

import pt.ipp.estg.pharmacies.Game.Room.GetTopScoreAsyncTask;
import pt.ipp.estg.pharmacies.Game.Room.ScoreDao;
import pt.ipp.estg.pharmacies.Game.Room.ScoreDataBase;
import pt.ipp.estg.pharmacies.Game.Room.ScoreTableModel;
import pt.ipp.estg.pharmacies.R;

public class GameMenu extends Fragment {


    Button easyButao;
    Button normalButao;
    Button hardButao;
    TextView textDiff1;
    TextView textDiff2;
    TextView textDiff3;
    MenuGameListener listener;
    ScoreDao scoreDao;
    ScoreTableModel[] score = new ScoreTableModel[3];
    ScoreDataBase scoreDataBase;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.game_menu, container, false);

        scoreDataBase = ScoreDataBase.getScoreDataBase(getActivity().getApplicationContext());
        scoreDao = scoreDataBase.scoreDao();

        easyButao = view.findViewById(R.id.easy_butao);
        normalButao = view.findViewById(R.id.normal_butao);
        hardButao = view.findViewById(R.id.hard_butao);
        textDiff1 = view.findViewById(R.id.topScoreDif1);
        textDiff2 = view.findViewById(R.id.topScoreDif2);
        textDiff3 = view.findViewById(R.id.topScoreDif3);

        try {
            score[0] = new GetTopScoreAsyncTask(scoreDao).execute().get()[0];
            score[1] = new GetTopScoreAsyncTask(scoreDao).execute().get()[1];
            score[2] = new GetTopScoreAsyncTask(scoreDao).execute().get()[2];
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        for (int i = 0; i < score.length; i++) {

            if (i == 0) {
                if (score[i] != null) {
                    textDiff1.setText("Easy Top Score: " + (score[i].getScore()));
                } else {
                    textDiff1.setText("Ainda não fez nenhum jogo na dificuldade EASY");
                }
            }
            if (i == 1) {
                if (score[i] != null) {
                    textDiff2.setText("Normal Top Score: " + (score[i].getScore()));
                } else {
                    textDiff2.setText("Ainda não fez nenhum jogo na dificuldade NORMAL");
                }
            }
            if (i == 2) {
                if (score[i] != null) {
                    textDiff3.setText("Hard Top Score: " + (score[i].getScore()));
                } else {
                    textDiff3.setText("Ainda não fez nenhum jogo na dificuldade HARD");
                }
            }
        }

        easyButao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.startGame(1);
            }
        });

        normalButao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.startGame(2);
            }
        });

        hardButao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.startGame(3);
            }
        });

        return view;
    }

    public interface MenuGameListener {
        void startGame(int difficulty);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof GameMenu.MenuGameListener) {
            listener = (GameMenu.MenuGameListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement MenuGameListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        listener = null;
    }
}
