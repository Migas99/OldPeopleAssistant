package pt.ipp.estg.pharmacies.medicineReminder.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TimePicker;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

import java.util.List;

import pt.ipp.estg.pharmacies.R;
import pt.ipp.estg.pharmacies.medicineReminder.interfaces.ReminderListenerInterface;
import pt.ipp.estg.pharmacies.medicineReminder.model.Medicine;

public class ReminderAddDialog extends DialogFragment {
    private ReminderListenerInterface mListener;
    private Context context;
    private List<Medicine> medicineList;
    private Medicine medicine;
    private Spinner spinner;
    private TimePicker timePicker;
    private Switch switchRepeating;
    private LinearLayout repeatingLayout;
    private EditText repeatingHour;

    public ReminderAddDialog(Context context, List<Medicine> medicineList) {
        this.context = context;
        this.medicineList = medicineList;
    }

    @NonNull
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        LayoutInflater i = getActivity().getLayoutInflater();
        View v = i.inflate(R.layout.reminder_add_dialog_layout, null);
        this.spinner = v.findViewById(R.id.reminder_add_dialog_medicine_spinner);
        this.timePicker = v.findViewById(R.id.reminder_add_dialog_time_picker);
        this.switchRepeating = v.findViewById(R.id.reminder_add_dialog_repeat_switch);
        this.repeatingLayout = v.findViewById(R.id.reminder_add_dialog_repeat_layout);
        this.repeatingHour = v.findViewById(R.id.reminder_add_dialog_repeat_value);
        this.timePicker.setIs24HourView(true);
        this.setUpSpinner();
        this.setUpSwitch();
        AlertDialog.Builder b = new AlertDialog.Builder(getActivity()).setTitle("Add Reminder")
                .setPositiveButton("Create Reminder",
                        (dialog, whichButton) -> {
                            if (spinner.getSelectedItem() != null) {
                                if (medicine.getAmount() > 0) {
                                    if (switchRepeating.isChecked()) {
                                        if (repeatingHour.getText().toString().length() > 0) {
                                            mListener.addReminder(medicine.getId(), timePicker.getHour(), timePicker.getMinute(), Integer.valueOf(repeatingHour.getText().toString()));
                                        } else {
                                            AlertDialog.Builder b2 = new AlertDialog.Builder(getActivity())
                                                    .setTitle("Warning!")
                                                    .setMessage("Please insert a repeating hour greater than zero.")
                                                    .setNegativeButton("OK", (dialog2, whichButton2) -> dialog2.dismiss());
                                            b2.create().show();
                                        }
                                    } else {
                                        mListener.addReminder(medicine.getId(), timePicker.getHour(), timePicker.getMinute());
                                    }
                                    dialog.dismiss();
                                } else {
                                    dialog.dismiss();
                                    AlertDialog.Builder b2 = new AlertDialog.Builder(getActivity()).setTitle("Warning!")
                                            .setMessage("Please select a medicine with pills in stock or add pills to stock of of this medicine, to create a reminder.")
                                            .setNegativeButton("OK", (dialog2, whichButton2) -> dialog2.dismiss());
                                    b2.create().show();
                                }
                            } else {
                                dialog.dismiss();
                                AlertDialog.Builder b2 = new AlertDialog.Builder(getActivity()).setTitle("Warning!")
                                        .setMessage("Please select a medicine, to create a reminder.")
                                        .setNegativeButton("OK", (dialog2, whichButton2) -> dialog2.dismiss());
                                b2.create().show();
                            }
                        }
                )
                .setNegativeButton("Cancel",
                        (dialog, whichButton) -> dialog.dismiss());
        b.setView(v);
        return b.create();
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof ReminderListenerInterface) {
            mListener = (ReminderListenerInterface) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement ReminderListenerInterface");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    private void setUpSpinner() {
        ArrayAdapter<Medicine> adapter = new ArrayAdapter<Medicine>(context, android.R.layout.simple_spinner_item, this.medicineList);
        adapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        this.spinner.setAdapter(adapter);
        this.spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                medicine = (Medicine) parent.getSelectedItem();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                medicine = null;
            }
        });
    }

    private void setUpSwitch() {
        this.switchRepeating.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                repeatingLayout.setVisibility(View.VISIBLE);
            }
        });
    }
}
