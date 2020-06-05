package pt.ipp.estg.pharmacies.medicineReminder.model;


import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface HistoryDao {

    @Insert
    void insertHistory(History... histories);

    @Query("SELECT * FROM History")
    LiveData<List<History>> loadAllHistories();

    @Query("SELECT COUNT(*) FROM History")
    LiveData<Integer> getCount();
}
