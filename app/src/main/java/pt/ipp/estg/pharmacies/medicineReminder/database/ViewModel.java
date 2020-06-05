package pt.ipp.estg.pharmacies.medicineReminder.database;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

import pt.ipp.estg.pharmacies.medicineReminder.model.History;
import pt.ipp.estg.pharmacies.medicineReminder.model.Medicine;
import pt.ipp.estg.pharmacies.medicineReminder.model.Reminder;

public class ViewModel extends AndroidViewModel {
    private Repository repository;
    private LiveData<List<Medicine>> medicineList;
    private LiveData<List<Reminder>> reminderList;
    private LiveData<List<History>> historyList;

    public ViewModel(Application application) {
        super(application);
        this.repository = new Repository(application);
        this.medicineList = this.repository.getAllMedicines();
        this.reminderList = this.repository.getAllReminders();
        this.historyList = this.repository.getAllHistories();
    }

    public LiveData<List<Medicine>> getAllMedicine() {
        return this.medicineList;
    }

    public LiveData<List<Reminder>> getAllReminders() {
        return this.reminderList;
    }

    public LiveData<List<History>> getAllHistories() {
        return this.historyList;
    }

    public Medicine getMedicine(int id) {
        return this.repository.getMedicine(id);
    }

    public Reminder getReminder(int id) {
        return this.repository.getReminder(id);
    }

    public void insertMedicine(Medicine medicine) {
        this.repository.insertMedicine(medicine);
    }

    public void deleteMedicine(Medicine medicine) {
        this.repository.deleteMedicine(medicine);
    }

    public void updateMedicine(Medicine medicine) {
        this.repository.updateMedicine(medicine);
    }

    public void insertReminder(Reminder reminder) {
        this.repository.insertReminder(reminder);
    }

    public void deleteReminder(Reminder reminder) {
        this.repository.deleteReminder(reminder);
    }

    public void updateReminder(Reminder reminder) {
        this.repository.updateReminder(reminder);
    }

    public void insertHistory(History history) {
        this.repository.insertHistory(history);
    }
}
