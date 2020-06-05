package pt.ipp.estg.pharmacies.medicineReminder.dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;

import pt.ipp.estg.pharmacies.R;
import pt.ipp.estg.pharmacies.medicineReminder.interfaces.MedicineListenerInterface;

public class MedicineAddDialog extends DialogFragment {

    private MedicineListenerInterface mListener;
    private EditText name;
    private EditText description;
    private EditText amount;

    @NonNull
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder b = new AlertDialog.Builder(getActivity())
                .setTitle("Add Medicine")
                .setPositiveButton("Create",
                        (dialog, whichButton) -> {

                            if (name.getText().toString().length() == 0) {
                                dialog.dismiss();
                                AlertDialog.Builder b2 = new AlertDialog.Builder(getActivity()).setTitle("Warning!").setMessage("Please insert a name for the medicine.").setNegativeButton("OK", (dialog2, whichButton2) -> dialog.dismiss());
                                b2.create().show();
                            } else {
                                if (name.getText().toString().length() == 0) {
                                    dialog.dismiss();
                                    AlertDialog.Builder b2 = new AlertDialog.Builder(getActivity()).setTitle("Warning!").setMessage("Please insert a description for the medicine.").setNegativeButton("OK", (dialog2, whichButton2) -> dialog.dismiss());
                                    b2.create().show();
                                } else if (Integer.valueOf(amount.getText().toString()) <= 0) {
                                    dialog.dismiss();
                                    AlertDialog.Builder b2 = new AlertDialog.Builder(getActivity()).setTitle("Warning!").setMessage("Please insert at least one pill for the medicine.").setNegativeButton("OK", (dialog2, whichButton2) -> dialog.dismiss());
                                    b2.create().show();
                                } else {
                                    mListener.addMedicine(name.getText().toString(), description.getText().toString(), Integer.valueOf(amount.getText().toString()));
                                    dialog.dismiss();
                                }
                            }
                        }
                )
                .setNegativeButton("Cancel",
                        (dialog, whichButton) -> dialog.dismiss()
                );

        LayoutInflater i = getActivity().getLayoutInflater();

        View v = i.inflate(R.layout.medicine_add_dialog_layout, null);
        this.name = v.findViewById(R.id.medicine_name_add_dialog);
        this.description = v.findViewById(R.id.medicine_description_add_dialog);
        this.amount = v.findViewById(R.id.medicine_amount_add_dialog);
        b.setView(v);
        return b.create();
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof MedicineListenerInterface) {
            mListener = (MedicineListenerInterface) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnMenuFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }
}
