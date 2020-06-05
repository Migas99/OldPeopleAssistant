package pt.ipp.estg.pharmacies.medicineReminder.model;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import static androidx.room.ForeignKey.CASCADE;

@Entity(foreignKeys = @ForeignKey(entity = Medicine.class, onDelete = CASCADE, parentColumns = "id", childColumns = "medicine_id"), indices = @Index(value = "medicine_id"))
public class History {
    @PrimaryKey(autoGenerate = true)
    public int id;

    @ColumnInfo(name = "medicine_id")
    public int medicineId;

    public double latitude;
    public double longitude;
    public int day;
    public int month;
    public int year;
    public int hour;
    public int minute;

    public History(int medicineId, double latitude, double longitude, int day, int month, int year, int hour, int minute) {
        this.medicineId = medicineId;
        this.latitude = latitude;
        this.longitude = longitude;
        this.day = day;
        this.month = month;
        this.year = year;
        this.hour = hour;
        this.minute = minute;
    }

    public int getMedicineId() {
        return medicineId;
    }

    public void setMedicineId(int medicineId) {
        this.medicineId = medicineId;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public int getHour() {
        return hour;
    }

    public void setHour(int hour) {
        this.hour = hour;
    }

    public int getMinute() {
        return minute;
    }

    public void setMinute(int minute) {
        this.minute = minute;
    }
}
