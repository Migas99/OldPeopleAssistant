package pt.ipp.estg.pharmacies.Game.Room;

import android.os.AsyncTask;

public class GetTopScoreAsyncTask extends AsyncTask<Void, Void, ScoreTableModel[]> {

    ScoreDao mScoreDao;

    public GetTopScoreAsyncTask (ScoreDao mScoreDao){
        this.mScoreDao = mScoreDao;
    }

    @Override
    protected ScoreTableModel[] doInBackground(Void... Void) {
        ScoreTableModel[] scoresDiff = new ScoreTableModel[3];
        scoresDiff[0] = mScoreDao.topScoreDif1();
        scoresDiff[1] = mScoreDao.topScoreDif2();
        scoresDiff[2] = mScoreDao.topScoreDif3();

        return scoresDiff;
    }
}
