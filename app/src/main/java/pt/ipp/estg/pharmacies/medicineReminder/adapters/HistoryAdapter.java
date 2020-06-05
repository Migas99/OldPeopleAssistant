package pt.ipp.estg.pharmacies.medicineReminder.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelStoreOwner;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import pt.ipp.estg.pharmacies.R;
import pt.ipp.estg.pharmacies.medicineReminder.asycntasks.RenderHistoryItemTask;
import pt.ipp.estg.pharmacies.medicineReminder.database.ViewModel;
import pt.ipp.estg.pharmacies.medicineReminder.fragments.HistoryListFragment;
import pt.ipp.estg.pharmacies.medicineReminder.model.History;

public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.HistoryViewHolder> {
    private Context context;
    private HistoryListFragment.OnCoordsGivenListener onCoordsGivenListener;
    private ViewModel viewModel;
    private List<History> historyList;

    public HistoryAdapter(Context context, List<History> historyList, HistoryListFragment.OnCoordsGivenListener onCoordsGivenListener) {
        this.context = context;
        this.historyList = historyList;
        this.onCoordsGivenListener = onCoordsGivenListener;
    }

    @NonNull
    @Override
    public HistoryViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View historyView = inflater.inflate(R.layout.history_list_row_layout, parent, false);
        this.viewModel = new ViewModelProvider((ViewModelStoreOwner) context).get(ViewModel.class);
        return new HistoryViewHolder(historyView);
    }

    @Override
    public void onBindViewHolder(HistoryViewHolder viewHolder, int position) {
        RenderHistoryItemTask task = new RenderHistoryItemTask(position, viewHolder, this.onCoordsGivenListener, this.viewModel, this.historyList);
        task.execute();
    }

    public void setHistoryList(List<History> historyList) {
        this.historyList = historyList;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return this.historyList.size();
    }

    public class HistoryViewHolder extends RecyclerView.ViewHolder {

        public LinearLayout layoutView;
        public TextView medicineView;
        public TextView dateView;
        public TextView timeView;
        public TextView locationView;

        public HistoryViewHolder(View itemView) {
            super(itemView);
            this.layoutView = itemView.findViewById(R.id.history_item_layout);
            this.medicineView = itemView.findViewById(R.id.history_medicine_value);
            this.dateView = itemView.findViewById(R.id.history_date_value);
            this.timeView = itemView.findViewById(R.id.history_time_value);
            this.locationView = itemView.findViewById(R.id.history_location_value);
        }
    }
}