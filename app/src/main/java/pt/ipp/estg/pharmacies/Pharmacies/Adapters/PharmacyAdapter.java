package pt.ipp.estg.pharmacies.Pharmacies.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import pt.ipp.estg.pharmacies.Pharmacies.AsyncTasks.RetrievePharmacyIconTask;
import pt.ipp.estg.pharmacies.Pharmacies.Fragments.ListPharmaciesFragment;
import pt.ipp.estg.pharmacies.Pharmacies.Models.PharmacyModel;
import pt.ipp.estg.pharmacies.R;

public class PharmacyAdapter extends RecyclerView.Adapter<PharmacyAdapter.PharmacyViewHolder> {

    private ListPharmaciesFragment.OnPharmacyGivenListener onPharmacyGivenListener;
    private List<PharmacyModel> pharmaciesList;
    private PharmacyModel pharmacy;

    public PharmacyAdapter(ListPharmaciesFragment.OnPharmacyGivenListener onPharmacyGivenListener) {
        this.onPharmacyGivenListener = onPharmacyGivenListener;
    }

    public void onButtonPressed(String pharmacy, double latitude, double longitude) {
        if (this.onPharmacyGivenListener != null) {
            this.onPharmacyGivenListener.onPharmacyGiven(pharmacy, latitude, longitude);
        }
    }

    @Override
    public int getItemCount() {
        return pharmaciesList.size();
    }

    @Override
    public PharmacyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View contactView = inflater.inflate(R.layout.item_pharmacy, parent, false);

        return new PharmacyViewHolder(contactView);
    }

    @Override
    public void onBindViewHolder(PharmacyViewHolder viewHolder, int position) {
        this.pharmacy = this.pharmaciesList.get(position);

        LinearLayout linearLayout = viewHolder.layout;
        linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onButtonPressed(pharmacy.getDisplay_name(), pharmacy.getLatitude(), pharmacy.getLongitude());
            }
        });

        ImageView imageView = viewHolder.imageView;
        //new RetrievePharmacyIconTask(imageView).execute(this.pharmacy.getIcon();

        TextView textView = viewHolder.nameTextView;
        textView.setText(this.pharmacy.getDisplay_name());

    }

    public void setPharmaciesList(List<PharmacyModel> pharmaciesList) {
        this.pharmaciesList = pharmaciesList;
        notifyDataSetChanged();
    }

    public class PharmacyViewHolder extends RecyclerView.ViewHolder {

        private LinearLayout layout;
        private ImageView imageView;
        private TextView nameTextView;

        public PharmacyViewHolder(@NonNull View itemView) {
            super(itemView);
            layout = itemView.findViewById(R.id.pharmacyLayout);
            imageView = itemView.findViewById(R.id.imageViewPharmacy);
            nameTextView = itemView.findViewById(R.id.textViewItemPharmacy);
        }
    }

}
