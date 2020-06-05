package pt.ipp.estg.pharmacies.medicineReminder.model;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface MedicineDao {

    @Insert
    void insertMedicine(Medicine... medicines);

    @Delete
    void removeMedicine(Medicine... medicines);

    @Update
    void updateMedicine(Medicine... medicines);

    @Query("SELECT * FROM Medicine")
    LiveData<List<Medicine>> loadAllMedicine();

    @Query("SELECT COUNT(*) FROM Medicine")
    LiveData<Integer> getCount();

    @Query("SELECT * FROM MEDICINE WHERE id = :id")
    Medicine getMedicine(int id);
}
