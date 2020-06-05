package pt.ipp.estg.pharmacies.medicineReminder.adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.PopupMenu;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelStoreOwner;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import pt.ipp.estg.pharmacies.R;
import pt.ipp.estg.pharmacies.medicineReminder.database.ViewModel;
import pt.ipp.estg.pharmacies.medicineReminder.model.Medicine;


public class MedicineAdapter extends RecyclerView.Adapter<MedicineAdapter.MedicineViewHolder> {

    private Context context;

    private ViewModel viewModel;
    private List<Medicine> medicineList;

    public MedicineAdapter(Context context, List<Medicine> medicineList) {
        this.context = context;
        this.medicineList = medicineList;
    }

    @NonNull
    @Override
    public MedicineViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View medicineView = inflater.inflate(R.layout.medicine_list_row_layout, parent, false);
        this.viewModel = new ViewModelProvider((ViewModelStoreOwner) context).get(ViewModel.class);
        return new MedicineViewHolder(medicineView);
    }

    @Override
    public void onBindViewHolder(MedicineViewHolder viewHolder, int position) {
        Medicine medicine = this.medicineList.get(position);
        viewHolder.nameTextView.setText(medicine.getName());
        viewHolder.descriptionTextView.setText(medicine.getDescription());
        int amount = medicine.getAmount();
        viewHolder.amountTextView.setText(String.valueOf(amount));
    }

    public void setMedicineList(List<Medicine> medicineList) {
        this.medicineList = medicineList;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return this.medicineList.size();
    }

    public class MedicineViewHolder extends RecyclerView.ViewHolder {
        public TextView nameTextView;
        public TextView descriptionTextView;
        public TextView amountTextView;
        public ImageButton popUpButton;

        public View editDialogView;
        public View pillManagementDialogView;

        public MedicineViewHolder(View itemView) {
            super(itemView);

            this.nameTextView = itemView.findViewById(R.id.medicine_row_name);
            this.descriptionTextView = itemView.findViewById(R.id.medicine_row_description);
            this.amountTextView = itemView.findViewById(R.id.medicine_row_amount);
            this.popUpButton = itemView.findViewById(R.id.medicine_button);

            this.popUpButton.setOnClickListener(view -> {
                PopupMenu popup = new PopupMenu(context, popUpButton);
                popup.getMenuInflater().inflate(R.menu.medicine_list_row_button_popup_menu_layout, popup.getMenu());
                popup.setOnMenuItemClickListener(item -> {
                    switch (item.getTitle().toString()) {
                        case "Edit":
                            editDialog();
                            break;
                        case "Delete":
                            deleteDialog();
                            break;
                        case "Pill Management":
                            pillManagementDialog();
                            break;
                    }
                    return true;
                });
                popup.show();
            });
        }


        private void deleteDialog() {
            AlertDialog.Builder deleteDialog = new AlertDialog.Builder(context)
                    .setTitle("Delete Medicine")
                    .setMessage("Are you sure you want to delete this medicine?")
                    .setPositiveButton("Delete",
                            (dialog, whichButton) -> {
                                int pos = getAdapterPosition();
                                final Medicine medicine = medicineList.remove(pos);
                                viewModel.deleteMedicine(medicine);
                                notifyItemRemoved(pos);
                                dialog.dismiss();
                            }
                    ).setNegativeButton("Cancel",
                            (dialog, whichButton) -> dialog.dismiss()
                    );
            /**
             LayoutInflater i = ((Activity) context).getLayoutInflater();
             View deleteDialogView = i.inflate(R.layout.medicine_delete_dialog_layout, null);
             deleteDialog.setView(deleteDialogView);
             **/
            deleteDialog.show();
        }

        private void editDialog() {
            AlertDialog.Builder editDialog = new AlertDialog.Builder(context)
                    .setTitle("Edit Medicine")
                    .setPositiveButton("Edit",
                            (dialog, whichButton) -> {

                                int pos = getAdapterPosition();
                                Medicine medicine = medicineList.get(pos);
                                EditText name = editDialogView.findViewById(R.id.medicine_name_add_dialog);
                                EditText description = editDialogView.findViewById(R.id.medicine_description_add_dialog);

                                if (name.getText().toString().length() == 0) {
                                    dialog.dismiss();
                                    android.app.AlertDialog.Builder b2 = new android.app.AlertDialog.Builder(context).setTitle("Warning!").setMessage("Please insert a name for the medicine.").setNegativeButton("OK", (dialog2, whichButton2) -> dialog.dismiss());
                                    b2.create().show();
                                } else {
                                    if (name.getText().toString().length() == 0) {
                                        dialog.dismiss();
                                        android.app.AlertDialog.Builder b2 = new android.app.AlertDialog.Builder(context).setTitle("Warning!").setMessage("Please insert a description for the medicine.").setNegativeButton("OK", (dialog2, whichButton2) -> dialog.dismiss());
                                        b2.create().show();
                                    } else {
                                        medicine.setName(name.getText().toString());
                                        medicine.setDescription(description.getText().toString());
                                        medicineList.remove(pos);
                                        medicineList.add(medicine);
                                        viewModel.updateMedicine(medicine);
                                        notifyDataSetChanged();
                                        dialog.dismiss();
                                    }
                                }
                            }
                    ).setNegativeButton("Cancel",
                            (dialog, whichButton) -> dialog.dismiss()
                    );
            setEditDialogLayout();
            editDialog.setView(editDialogView);
            editDialog.show();
        }

        private void setEditDialogLayout() {
            LayoutInflater i = ((Activity) context).getLayoutInflater();
            editDialogView = i.inflate(R.layout.medicine_add_dialog_layout, null);

            EditText name = editDialogView.findViewById(R.id.medicine_name_add_dialog);
            EditText description = editDialogView.findViewById(R.id.medicine_description_add_dialog);
            editDialogView.findViewById(R.id.medicine_amount_add_dialog).setVisibility(View.INVISIBLE);
            editDialogView.findViewById(R.id.medicine_amount_add_dialog_text).setVisibility(View.INVISIBLE);

            name.setText(this.nameTextView.getText());
            description.setText(this.descriptionTextView.getText());
        }

        private void pillManagementDialog() {
            AlertDialog.Builder pillManagementDialog = new AlertDialog.Builder(context)
                    .setTitle("Pill Management")
                    .setPositiveButton("Edit",
                            (dialog, whichButton) -> {
                                int pos = getAdapterPosition();
                                TextView amount = pillManagementDialogView.findViewById(R.id.medicine_pill_management_amount_value);
                                Medicine medicine = medicineList.remove(pos);
                                medicine.setAmount(Integer.valueOf(amount.getText().toString()));
                                medicineList.add(medicine);
                                viewModel.updateMedicine(medicine);
                                notifyDataSetChanged();
                                dialog.dismiss();
                            }
                    ).setNegativeButton("Cancel",
                            (dialog, whichButton) -> dialog.dismiss()
                    );
            setPillManagementDialogLayout();
            pillManagementDialog.setView(pillManagementDialogView);
            pillManagementDialog.show();
        }

        private void setPillManagementDialogLayout() {
            LayoutInflater i = ((Activity) context).getLayoutInflater();
            pillManagementDialogView = i.inflate(R.layout.medicine_pill_management_dialog_layout, null);
            TextView amount = pillManagementDialogView.findViewById(R.id.medicine_pill_management_amount_value);
            Button addButton = pillManagementDialogView.findViewById(R.id.medicine_pill_management_add_button);
            Button removeButton = pillManagementDialogView.findViewById(R.id.medicine_pill_management_remove_button);

            amount.setText(amountTextView.getText());

            addButton.setOnClickListener(v -> {
                int currentAmount = Integer.valueOf(amount.getText().toString());
                currentAmount++;
                amount.setText(String.valueOf(currentAmount));
            });

            removeButton.setOnClickListener(v -> {
                int currentAmount = Integer.valueOf(amount.getText().toString());
                if (currentAmount > 0) {
                    currentAmount--;
                    amount.setText(String.valueOf(currentAmount));
                }
            });
        }

    }
}


