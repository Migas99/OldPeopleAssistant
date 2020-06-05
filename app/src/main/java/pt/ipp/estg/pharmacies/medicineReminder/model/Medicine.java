package pt.ipp.estg.pharmacies.medicineReminder.model;


import androidx.room.Entity;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(indices = {@Index(value = {"id"}, unique = true)})
public class Medicine {

    @PrimaryKey(autoGenerate = true)
    public int id;

    public String name;
    public String description;
    public int amount;


    public Medicine(String name, String description, int amount) {
        this.name = name;
        this.description = description;
        this.amount = amount;
    }


    public void addPills(int amount) {
        if (amount >= 0) this.amount += amount;
    }

    public void addPill() {
        this.amount++;
    }

    public void removePills(int amount) {
        if ((this.amount - amount) >= 0) this.amount -= amount;
    }

    public void removePill() {
        this.amount--;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int stock) {
        this.amount = stock;
    }

    @Override
    public String toString() {
        return getName();
    }
}
