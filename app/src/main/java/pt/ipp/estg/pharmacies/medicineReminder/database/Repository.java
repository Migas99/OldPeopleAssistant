package pt.ipp.estg.pharmacies.medicineReminder.database;

import android.app.Application;

import androidx.lifecycle.LiveData;

import java.util.List;

import pt.ipp.estg.pharmacies.medicineReminder.model.History;
import pt.ipp.estg.pharmacies.medicineReminder.model.HistoryDao;
import pt.ipp.estg.pharmacies.medicineReminder.model.Medicine;
import pt.ipp.estg.pharmacies.medicineReminder.model.MedicineDao;
import pt.ipp.estg.pharmacies.medicineReminder.model.Reminder;
import pt.ipp.estg.pharmacies.medicineReminder.model.ReminderDao;

public class Repository {

    private MedicineDao medicineDao;
    private ReminderDao reminderDao;
    private HistoryDao historyDao;
    private LiveData<List<Medicine>> medicineList;
    private LiveData<List<Reminder>> reminderList;
    private LiveData<List<History>> historyList;

    Repository(Application application) {
        Database db = Database.getDatabase(application);
        medicineDao = db.getMedicineDao();
        reminderDao = db.getReminderDao();
        historyDao = db.getHistoryDao();
        medicineList = medicineDao.loadAllMedicine();
        reminderList = reminderDao.loadAllReminders();
        historyList = historyDao.loadAllHistories();
    }

    LiveData<List<Medicine>> getAllMedicines() {
        return this.medicineList;
    }

    LiveData<List<Reminder>> getAllReminders() {
        return this.reminderList;
    }

    LiveData<List<History>> getAllHistories() {
        return this.historyList;
    }

    Medicine getMedicine(int id) {
        return medicineDao.getMedicine(id);
    }

    Reminder getReminder(int id) {
        return reminderDao.getReminder(id);
    }

    void insertMedicine(Medicine medicine) {
        Database.databaseWriteExecutor.execute(() -> {
            medicineDao.insertMedicine(medicine);
        });

    }

    void deleteMedicine(Medicine medicine) {

        Database.databaseWriteExecutor.execute(() -> {
            medicineDao.removeMedicine(medicine);
        });

    }

    void updateMedicine(Medicine medicine) {
        Database.databaseWriteExecutor.execute(() -> {
            medicineDao.updateMedicine(medicine);
        });
    }

    void insertReminder(Reminder reminder) {
        Database.databaseWriteExecutor.execute(() -> {
            reminderDao.insertReminder(reminder);
        });

    }

    void deleteReminder(Reminder reminder) {
        Database.databaseWriteExecutor.execute(() -> {
            reminderDao.removeReminder(reminder);
        });
    }

    void updateReminder(Reminder reminder) {
        Database.databaseWriteExecutor.execute(() -> {
            reminderDao.updateReminder(reminder);
        });
    }

    void insertHistory(History history) {
        Database.databaseWriteExecutor.execute(() -> {
            historyDao.insertHistory(history);
        });

    }
}
