package pt.ipp.estg.pharmacies.medicineReminder.database;

import android.content.Context;

import androidx.room.Room;
import androidx.room.RoomDatabase;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import pt.ipp.estg.pharmacies.medicineReminder.model.History;
import pt.ipp.estg.pharmacies.medicineReminder.model.HistoryDao;
import pt.ipp.estg.pharmacies.medicineReminder.model.Medicine;
import pt.ipp.estg.pharmacies.medicineReminder.model.MedicineDao;
import pt.ipp.estg.pharmacies.medicineReminder.model.Reminder;
import pt.ipp.estg.pharmacies.medicineReminder.model.ReminderDao;


@androidx.room.Database(entities = {Medicine.class, Reminder.class, History.class}, version = 5, exportSchema = false)
public abstract class Database extends RoomDatabase {

    private static final int NUMBER_OF_THREADS = 4;
    private static volatile Database INSTANCE;
    public static final ExecutorService databaseWriteExecutor = Executors.newFixedThreadPool(NUMBER_OF_THREADS);

    public static Database getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (Database.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(), Database.class, "medicines_database").fallbackToDestructiveMigration().build();
                }
            }
        }
        return INSTANCE;
    }

    public abstract MedicineDao getMedicineDao();

    public abstract ReminderDao getReminderDao();

    public abstract HistoryDao getHistoryDao();

}
