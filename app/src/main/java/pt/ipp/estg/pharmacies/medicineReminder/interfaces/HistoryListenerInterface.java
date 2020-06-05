package pt.ipp.estg.pharmacies.medicineReminder.interfaces;



public interface HistoryListenerInterface {
    void insertHistory(int medicineId, double latitude, double longitude, int day, int month, int year, int hour, int minute) ;
}
