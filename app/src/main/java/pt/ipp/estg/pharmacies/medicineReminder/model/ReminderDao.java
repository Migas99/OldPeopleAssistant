package pt.ipp.estg.pharmacies.medicineReminder.model;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface ReminderDao {

    @Insert
    void insertReminder(Reminder... reminders);

    @Delete
    void removeReminder(Reminder... reminders);

    @Update
    void updateReminder(Reminder... reminders);

    @Query("SELECT * FROM Reminder")
    LiveData<List<Reminder>> loadAllReminders();

    @Query("SELECT COUNT(*) FROM Reminder")
    LiveData<Integer> getCount();

    @Query("SELECT * FROM REMINDER WHERE reminder_id = :id")
    Reminder getReminder(int id);
}
