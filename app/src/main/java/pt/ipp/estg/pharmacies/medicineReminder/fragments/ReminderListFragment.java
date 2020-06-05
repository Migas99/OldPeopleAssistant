package pt.ipp.estg.pharmacies.medicineReminder.fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

import pt.ipp.estg.pharmacies.R;
import pt.ipp.estg.pharmacies.medicineReminder.adapters.ReminderAdapter;
import pt.ipp.estg.pharmacies.medicineReminder.database.ViewModel;
import pt.ipp.estg.pharmacies.medicineReminder.dialogs.ReminderAddDialog;
import pt.ipp.estg.pharmacies.medicineReminder.interfaces.ReminderListenerInterface;
import pt.ipp.estg.pharmacies.medicineReminder.model.Medicine;
import pt.ipp.estg.pharmacies.medicineReminder.model.Reminder;

public class ReminderListFragment extends Fragment implements ReminderListenerInterface {

    private Context context;
    private ViewModel viewModel;
    private List<Reminder> reminderList;
    private List<Medicine> medicineList;
    private RecyclerView reminderRecyclerView;
    private ReminderAdapter reminderAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.context = getActivity();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View mContentView = inflater.inflate(R.layout.reminder_list_layout, container, false);

        this.reminderRecyclerView = mContentView.findViewById(R.id.reminder_recycler_view);
        this.reminderRecyclerView.setLayoutManager(new LinearLayoutManager(this.context));

        RecyclerView.ItemDecoration itemDecoration = new DividerItemDecoration(this.context, DividerItemDecoration.VERTICAL);
        this.reminderRecyclerView.addItemDecoration(itemDecoration);

        this.reminderList = new ArrayList<>();
        this.medicineList = new ArrayList<>();

        this.viewModel = new ViewModelProvider(this).get(ViewModel.class);
        this.reminderAdapter = new ReminderAdapter(this.context, this.reminderList);
        this.viewModel.getAllMedicine().observe((LifecycleOwner) context, medicineList -> this.medicineList = medicineList);
        this.viewModel.getAllReminders().observe((LifecycleOwner) context, reminderList -> this.reminderAdapter.setReminderList(reminderList));
        this.reminderRecyclerView.setAdapter(this.reminderAdapter);

        FloatingActionButton fabReminder = mContentView.findViewById(R.id.fabReminder);
        fabReminder.setOnClickListener(view -> {
            FragmentManager fragmentManager = getChildFragmentManager();
            ReminderAddDialog newFragment = new ReminderAddDialog(context, medicineList);
            newFragment.show(fragmentManager, "reminderDialog");
        });

        return mContentView;
    }

    @Override
    public void addReminder(int medicine_id, int hour, int minute) {
        final Reminder reminder = new Reminder(medicine_id, hour, minute);
        this.viewModel.insertReminder(reminder);
    }

    @Override
    public void addReminder(int medicine_id, int hour, int minute, int hourRepeat) {
        final Reminder reminder = new Reminder(medicine_id, hour, minute);
        reminder.setRepeatHour(hourRepeat);
        this.viewModel.insertReminder(reminder);
    }

    @Override
    public void deleteReminder(int medicine_id, int hour, int minute) {
        final Reminder reminder = new Reminder(medicine_id, hour, minute);
        this.viewModel.deleteReminder(reminder);
    }

    @Override
    public void updateReminder(int medicine_id, int hour, int minute) {
        final Reminder reminder = new Reminder(medicine_id, hour, minute);
        this.viewModel.updateReminder(reminder);
    }

    @Override
    public List<Medicine> getAllMedicine() {
        List<Medicine> list = new ArrayList<>();
        this.viewModel.getAllMedicine().observe((LifecycleOwner) context, medicineList -> list.addAll(medicineList));
        return list;
    }
}
