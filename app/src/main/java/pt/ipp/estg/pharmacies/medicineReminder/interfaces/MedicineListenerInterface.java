package pt.ipp.estg.pharmacies.medicineReminder.interfaces;


public interface MedicineListenerInterface {
    void addMedicine(String name, String description, int amount);

    void deleteMedicine(String name, String description, int amount);

    void updateMedicine(String name, String description, int amount);
}

