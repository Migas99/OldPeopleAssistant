package pt.ipp.estg.pharmacies.medicineReminder.asycntasks;

import android.os.AsyncTask;
import android.view.View;

import java.util.List;

import pt.ipp.estg.pharmacies.medicineReminder.adapters.ReminderAdapter;
import pt.ipp.estg.pharmacies.medicineReminder.database.ViewModel;
import pt.ipp.estg.pharmacies.medicineReminder.model.Medicine;
import pt.ipp.estg.pharmacies.medicineReminder.model.Reminder;

public class RenderReminderItemTask extends AsyncTask<Void, Void, Void> {
    private int position;
    private ReminderAdapter.ReminderViewHolder viewHolder;
    private ViewModel viewModel;
    private List<Reminder> reminderList;
    private Reminder reminder;
    private Medicine medicine;

    public RenderReminderItemTask(ReminderAdapter.ReminderViewHolder viewHolder, int position, ViewModel viewModel, List<Reminder> reminderList) {
        this.viewHolder = viewHolder;
        this.position = position;
        this.viewModel = viewModel;
        this.reminderList = reminderList;
    }

    @Override
    protected Void doInBackground(Void... voids) {
        reminder = this.reminderList.get(position);
        medicine = this.viewModel.getMedicine(reminder.getMedicineId());
        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
        viewHolder.medicineTextView.setText(medicine.getName());
        String time;
        if (reminder.getMinute() < 10) {
            time = reminder.getHour() + "h:0" + reminder.getMinute() + "m";
        } else {
            time = reminder.getHour() + "h:" + reminder.getMinute() + "m";
        }
        viewHolder.timeTextView.setText(time);
        if (reminder.getRepeatHour() != -1) {
            String repeat = reminder.getRepeatHour() + "h";
            viewHolder.repeatTextView.setText(repeat);
            viewHolder.repeatLayout.setVisibility(View.VISIBLE);
        } else {
            viewHolder.repeatLayout.setVisibility(View.INVISIBLE);
        }
    }
}
