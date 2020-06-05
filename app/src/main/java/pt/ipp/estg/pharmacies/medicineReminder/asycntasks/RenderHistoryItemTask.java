package pt.ipp.estg.pharmacies.medicineReminder.asycntasks;

import android.os.AsyncTask;
import android.view.View;

import java.util.List;

import pt.ipp.estg.pharmacies.medicineReminder.adapters.HistoryAdapter;
import pt.ipp.estg.pharmacies.medicineReminder.database.ViewModel;
import pt.ipp.estg.pharmacies.medicineReminder.fragments.HistoryListFragment;
import pt.ipp.estg.pharmacies.medicineReminder.model.History;
import pt.ipp.estg.pharmacies.medicineReminder.model.Medicine;

public class RenderHistoryItemTask extends AsyncTask<Void, Void, Void> {

    private int position;
    private HistoryAdapter.HistoryViewHolder viewHolder;
    private HistoryListFragment.OnCoordsGivenListener onCoordsGivenListener;
    private ViewModel viewModel;
    private List<History> historyList;
    private History history;
    private Medicine medicine;

    public RenderHistoryItemTask(int position, HistoryAdapter.HistoryViewHolder viewHolder, HistoryListFragment.OnCoordsGivenListener onCoordsGivenListener, ViewModel viewModel, List<History> historyList) {
        this.position = position;
        this.viewHolder = viewHolder;
        this.viewModel = viewModel;
        this.historyList = historyList;
        this.onCoordsGivenListener = onCoordsGivenListener;
    }

    public void onButtonPressed(double latitude, double longitude) {
        if (this.onCoordsGivenListener != null) {
            this.onCoordsGivenListener.onCoordsGiven(latitude, longitude);
        }
    }

    @Override
    protected Void doInBackground(Void... voids) {
        history = this.historyList.get(position);
        medicine = this.viewModel.getMedicine(history.getMedicineId());
        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
        String date = "";
        String time;
        String location = history.getLatitude() + "," + history.getLongitude();

        if (history.getMinute() < 10) {
            time = history.getHour() + "h:0" + history.getMinute() + "m";
        } else {
            time = history.getHour() + "h:" + history.getMinute() + "m";
        }

        if (history.getDay() < 10) {
            date += "0" + history.getDay() + "/";
        } else {
            date += history.getDay() + "/";
        }

        if (history.getMonth() < 10) {
            date += "0" + history.getMonth() + "/";
        } else {
            date += history.getMonth() + "/";
        }
        date += history.getYear();

        viewHolder.layoutView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onButtonPressed(history.getLatitude(), history.getLongitude());
            }
        });

        viewHolder.medicineView.setText(medicine.getName());
        viewHolder.dateView.setText(date);
        viewHolder.timeView.setText(time);
        viewHolder.locationView.setText(location);
    }

}
