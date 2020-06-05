package pt.ipp.estg.pharmacies.Game.Room;

import android.os.AsyncTask;

public class InsertAsyncTask extends AsyncTask<ScoreTableModel, Void, Void> {

    ScoreDao mScoreDao;

    public InsertAsyncTask (ScoreDao mScoreDao){
        this.mScoreDao = mScoreDao;
    }

    @Override
    protected Void doInBackground(ScoreTableModel... scoreTableModels) {

        mScoreDao.addScore(scoreTableModels[0]);
        return null;
    }
}