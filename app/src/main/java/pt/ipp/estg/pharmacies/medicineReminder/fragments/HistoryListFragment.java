package pt.ipp.estg.pharmacies.medicineReminder.fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import pt.ipp.estg.pharmacies.R;
import pt.ipp.estg.pharmacies.medicineReminder.adapters.HistoryAdapter;
import pt.ipp.estg.pharmacies.medicineReminder.database.Database;
import pt.ipp.estg.pharmacies.medicineReminder.database.ViewModel;
import pt.ipp.estg.pharmacies.medicineReminder.interfaces.HistoryListenerInterface;
import pt.ipp.estg.pharmacies.medicineReminder.model.History;

public class HistoryListFragment extends Fragment implements HistoryListenerInterface {

    private Context context;
    private OnCoordsGivenListener onCoordsGivenListener;
    private Database database;
    private HistoryAdapter historyAdapter;
    private List<History> historyList;
    private RecyclerView historyRecyclerView;
    private ViewModel viewModel;

    public interface OnCoordsGivenListener {
        void onCoordsGiven(double latitude, double longitude);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.context = getActivity();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View mContentView = inflater.inflate(R.layout.history_list_recycler_view_layout, container, false);

        this.historyRecyclerView = mContentView.findViewById(R.id.history_recycler_view);
        this.historyRecyclerView.setLayoutManager(new LinearLayoutManager(this.context));

        RecyclerView.ItemDecoration itemDecoration = new DividerItemDecoration(this.context, DividerItemDecoration.VERTICAL);
        this.historyRecyclerView.addItemDecoration(itemDecoration);
        this.historyList = new ArrayList<>();
        this.viewModel = new ViewModelProvider(this).get(ViewModel.class);
        this.historyAdapter = new HistoryAdapter(this.context, this.historyList, this.onCoordsGivenListener);
        this.viewModel.getAllHistories().observe((LifecycleOwner) context, historyList -> this.historyAdapter.setHistoryList(historyList));
        this.historyRecyclerView.setAdapter(this.historyAdapter);

        return mContentView;
    }

    @Override
    public void insertHistory(int medicineId, double latitude, double longitude, int day, int month, int year, int hour, int minute) {
        final History history = new History(medicineId, latitude, longitude, day, month, year, hour, minute);
        viewModel.insertHistory(history);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof HistoryListFragment.OnCoordsGivenListener) {
            this.onCoordsGivenListener = (OnCoordsGivenListener) context;
        } else {
            throw new RuntimeException(context.toString());
        }
    }
}
