package pt.ipp.estg.pharmacies.medicineReminder.model;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import static androidx.room.ForeignKey.CASCADE;

@Entity(foreignKeys = @ForeignKey(entity = Medicine.class, onDelete = CASCADE, parentColumns = "id", childColumns = "medicine_id"), indices = @Index(value = "medicine_id"))
public class Reminder {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "reminder_id", index = true)
    public int reminderId;

    @ColumnInfo(name = "medicine_id")
    public int medicineId;

    public int hour;
    public int minute;

    public int repeatHour;

    public Reminder(int medicineId, int hour, int minute) {
        this.medicineId = medicineId;
        this.hour = hour;
        this.minute = minute;
        this.repeatHour = -1;
    }

    public int getReminderId() {
        return reminderId;
    }

    public int getMedicineId() {
        return medicineId;
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

    public int getRepeatHour() {
        return repeatHour;
    }

    public void setRepeatHour(int repeatHour) {
        this.repeatHour = repeatHour;
    }
}
