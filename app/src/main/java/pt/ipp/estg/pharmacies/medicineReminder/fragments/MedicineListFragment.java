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
import pt.ipp.estg.pharmacies.medicineReminder.adapters.MedicineAdapter;
import pt.ipp.estg.pharmacies.medicineReminder.database.ViewModel;
import pt.ipp.estg.pharmacies.medicineReminder.dialogs.MedicineAddDialog;
import pt.ipp.estg.pharmacies.medicineReminder.interfaces.MedicineListenerInterface;
import pt.ipp.estg.pharmacies.medicineReminder.model.Medicine;

public class MedicineListFragment extends Fragment implements MedicineListenerInterface {

    private Context context;
    private MedicineAdapter medicineAdapter;
    private List<Medicine> medicineList;
    private RecyclerView medicineRecyclerView;
    private ViewModel viewModel;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.context = getActivity();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View mContentView = inflater.inflate(R.layout.medicine_list_layout, container, false);

        this.medicineRecyclerView = mContentView.findViewById(R.id.medicine_recycler_view);
        this.medicineRecyclerView.setLayoutManager(new LinearLayoutManager(this.context));

        RecyclerView.ItemDecoration itemDecoration = new DividerItemDecoration(this.context, DividerItemDecoration.VERTICAL);
        this.medicineRecyclerView.addItemDecoration(itemDecoration);
        this.medicineList = new ArrayList<>();
        this.viewModel = new ViewModelProvider(this).get(ViewModel.class);
        this.medicineAdapter = new MedicineAdapter(this.context, this.medicineList);
        this.viewModel.getAllMedicine().observe((LifecycleOwner) context, medicineList -> this.medicineAdapter.setMedicineList(medicineList));
        this.medicineRecyclerView.setAdapter(this.medicineAdapter);

        FloatingActionButton fabMedicine = mContentView.findViewById(R.id.fabMedicine);
        fabMedicine.setOnClickListener(view -> {
            FragmentManager fragmentManager = getChildFragmentManager();
            MedicineAddDialog newFragment = new MedicineAddDialog();
            newFragment.show(fragmentManager, "medicineDialog");
        });

        return mContentView;
    }

    @Override
    public void addMedicine(String name, String description, int amount) {
        final Medicine medicine = new Medicine(name, description, amount);
        viewModel.insertMedicine(medicine);
    }

    @Override
    public void deleteMedicine(String name, String description, int amount) {
        final Medicine medicine = new Medicine(name, description, amount);
        viewModel.deleteMedicine(medicine);
    }

    @Override
    public void updateMedicine(String name, String description, int amount) {
        final Medicine medicine = new Medicine(name, description, amount);
        viewModel.updateMedicine(medicine);
    }
}
