package pt.ipp.estg.pharmacies.Game.Room;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {ScoreTableModel.class}, version = 1, exportSchema = false)
public abstract class ScoreDataBase extends RoomDatabase {

    public abstract ScoreDao scoreDao();

    private static volatile ScoreDataBase scoreDataBase;

    public static ScoreDataBase getScoreDataBase(final Context context){
        if(scoreDataBase == null){
            synchronized (ScoreDataBase.class){
                if(scoreDataBase == null){
                    scoreDataBase = Room.databaseBuilder(context.getApplicationContext(),
                            ScoreDataBase.class, "scores").build();
                }
            }
        }
        return scoreDataBase;
    }
}
