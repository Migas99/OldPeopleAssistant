package pt.ipp.estg.pharmacies.Game.Room;

import androidx.room.Insert;
import androidx.room.Query;

@androidx.room.Dao
public interface ScoreDao {

    @Insert
    void addScore(ScoreTableModel... scoreTableModels);

    @Query("SELECT * FROM scores WHERE difficulty = 1 order by score desc limit 1")
    ScoreTableModel topScoreDif1();

    @Query("SELECT * FROM scores WHERE difficulty = 2 order by score desc limit 1")
    ScoreTableModel topScoreDif2();

    @Query("SELECT * FROM scores WHERE difficulty = 3 order by score desc limit 1")
    ScoreTableModel topScoreDif3();
}
