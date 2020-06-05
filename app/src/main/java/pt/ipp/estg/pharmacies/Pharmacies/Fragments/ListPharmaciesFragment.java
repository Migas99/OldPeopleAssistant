package pt.ipp.estg.pharmacies.Pharmacies.Fragments;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.concurrent.ExecutionException;

import pt.ipp.estg.pharmacies.Pharmacies.Adapters.PharmacyAdapter;
import pt.ipp.estg.pharmacies.Pharmacies.AsyncTasks.RetrievePharmacyListTask;
import pt.ipp.estg.pharmacies.R;

public class ListPharmaciesFragment extends Fragment {

    private RecyclerView recyclerView;
    private PharmacyAdapter pharmacyAdapter;
    private DividerItemDecoration dividerItemDecoration;

    private OnPharmacyGivenListener onPharmacyGivenListener;
    private View mContentView;
    private String city;

    public interface OnPharmacyGivenListener {
        void onPharmacyGiven(String pharmacy, double latitude, double longitude);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(getArguments() != null) {
            this.city = getArguments().getString("City");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        this.mContentView = inflater.inflate(R.layout.list_pharmacies_fragment, container, false);

        try {
            this.recyclerView = mContentView.findViewById(R.id.recyclerViewListPharmacies);
            this.recyclerView.setLayoutManager(new LinearLayoutManager(mContentView.getContext()));
            this.dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(), new LinearLayoutManager(mContentView.getContext()).getOrientation());
            this.recyclerView.addItemDecoration(dividerItemDecoration);
            this.pharmacyAdapter = new PharmacyAdapter(this.onPharmacyGivenListener);

            RetrievePharmacyListTask retrievePharmacyListTask = new RetrievePharmacyListTask();
            retrievePharmacyListTask.execute(this.city);

            this.pharmacyAdapter.setPharmaciesList(retrievePharmacyListTask.get());
            recyclerView.setAdapter(pharmacyAdapter);
        } catch (ExecutionException | InterruptedException e) {
            System.err.println(e);
        }

        return mContentView;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnPharmacyGivenListener) {
            this.onPharmacyGivenListener = (OnPharmacyGivenListener) context;
        } else {
            throw new RuntimeException(context.toString());
        }
    }
}
