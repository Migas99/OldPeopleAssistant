package pt.ipp.estg.pharmacies.medicineReminder.interfaces;

import java.util.List;

import pt.ipp.estg.pharmacies.medicineReminder.model.Medicine;

public interface ReminderListenerInterface {
    void addReminder(int medicine_id, int hour, int minute);

    void addReminder(int medicine_id, int hour, int minute, int hourRepeat);

    void deleteReminder(int medicine_id, int hour, int minute);

    void updateReminder(int medicine_id, int hour, int minute);

    List<Medicine> getAllMedicine();
}
