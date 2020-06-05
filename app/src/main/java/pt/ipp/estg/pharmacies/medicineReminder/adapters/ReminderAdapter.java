package pt.ipp.estg.pharmacies.medicineReminder.adapters;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelStoreOwner;
import androidx.recyclerview.widget.RecyclerView;

import java.util.Calendar;
import java.util.List;

import pt.ipp.estg.pharmacies.R;
import pt.ipp.estg.pharmacies.medicineReminder.asycntasks.RenderReminderItemTask;
import pt.ipp.estg.pharmacies.medicineReminder.database.ViewModel;
import pt.ipp.estg.pharmacies.medicineReminder.model.Reminder;
import pt.ipp.estg.pharmacies.medicineReminder.notification.alarm.AlarmScheduler;

public class ReminderAdapter extends RecyclerView.Adapter<ReminderAdapter.ReminderViewHolder> {

    private Context context;
    private ViewModel viewModel;
    private List<Reminder> reminderList;
    private AlarmScheduler alarmScheduler;

    public ReminderAdapter(Context context, List<Reminder> reminderList) {
        this.context = context;
        this.reminderList = reminderList;
        this.alarmScheduler = new AlarmScheduler();
    }

    @NonNull
    @Override
    public ReminderViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View reminderView = inflater.inflate(R.layout.reminder_list_row_layout, parent, false);
        this.viewModel = new ViewModelProvider((ViewModelStoreOwner) context).get(ViewModel.class);
        return new ReminderViewHolder(reminderView);
    }

    @Override
    public void onBindViewHolder(@NonNull ReminderViewHolder viewHolder, int position) {
        RenderReminderItemTask task = new RenderReminderItemTask(viewHolder, position, viewModel, reminderList);
        task.execute();
        setUpAlarm(position);
    }

    @Override
    public int getItemCount() {
        return this.reminderList.size();
    }

    public class ReminderViewHolder extends RecyclerView.ViewHolder {
        public TextView medicineTextView;
        public TextView timeTextView;
        public TextView repeatTextView;
        public ImageButton popUpButton;
        public LinearLayout repeatLayout;

        public ReminderViewHolder(View itemView) {
            super(itemView);
            this.medicineTextView = itemView.findViewById(R.id.reminder_list_row_medicine_value);
            this.timeTextView = itemView.findViewById(R.id.reminder_list_row_time_interval_value);
            this.repeatTextView = itemView.findViewById(R.id.reminder_list_row_repeat_time_interval_value);
            this.repeatLayout = itemView.findViewById(R.id.linear_layout_reminder_repeat_views);
            this.popUpButton = itemView.findViewById(R.id.reminder_list_row_options_img);

            this.popUpButton.setOnClickListener(view -> {
                PopupMenu popup = new PopupMenu(context, popUpButton);
                popup.getMenuInflater().inflate(R.menu.reminder_list_row_button_popup_menu_layout, popup.getMenu());
                popup.setOnMenuItemClickListener(item -> {
                    switch (item.getTitle().toString()) {
                        case "Delete":
                            deleteDialog();
                            break;
                    }
                    return true;
                });
                popup.show();
            });
        }

        private void deleteDialog() {
            AlertDialog.Builder deleteDialog = new AlertDialog.Builder(context)
                    .setTitle("Delete Reminder")
                    .setMessage("Are you sure you want to delete this reminder?")
                    .setPositiveButton("Delete",
                            (dialog, whichButton) -> {
                                int pos = getAdapterPosition();
                                final Reminder reminder = reminderList.remove(pos);
                                viewModel.deleteReminder(reminder);
                                notifyItemRemoved(pos);
                                alarmScheduler.cancelAlarm(context, reminder);
                                dialog.dismiss();
                            }
                    ).setNegativeButton("Cancel",
                            (dialog, whichButton) -> dialog.dismiss()
                    );
            deleteDialog.show();
        }
    }

    private void setUpAlarm(int position) {
        Reminder reminder = this.reminderList.get(position);
        Calendar c = Calendar.getInstance();
        c.set(Calendar.HOUR_OF_DAY, reminder.getHour());
        c.set(Calendar.MINUTE, reminder.getMinute());
        c.set(Calendar.SECOND, 0);
        if (reminder.getRepeatHour() == -1) {
            alarmScheduler.cancelAlarm(context, reminder);
            alarmScheduler.setAlarm(context, c.getTimeInMillis() , reminder);
        } else {
            alarmScheduler.cancelAlarm(context, reminder);
            long mill = reminder.getReminderId() * 3600000;
            alarmScheduler.setRepeatAlarm(context, c.getTimeInMillis(), reminder, mill);
        }
    }

    public void setReminderList(List<Reminder> reminderList) {
        this.reminderList = reminderList;
        notifyDataSetChanged();
    }
}