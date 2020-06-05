package pt.ipp.estg.pharmacies.medicineReminder.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import pt.ipp.estg.pharmacies.R;

public class HistoryMapFragment extends Fragment implements OnMapReadyCallback {

    private SupportMapFragment mMapFragment;
    private GoogleMap mGoogleMap;
    private LatLng coordinates;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            this.coordinates = new LatLng(getArguments().getDouble("Latitude"), getArguments().getDouble("Longitude"));
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View mContentView = inflater.inflate(R.layout.map_fragment, container, false);

        FragmentManager fragmentManager = getChildFragmentManager();
        this.mMapFragment = (SupportMapFragment) fragmentManager.findFragmentById(R.id.map);
        this.mMapFragment.getMapAsync(this);

        return mContentView;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.mGoogleMap = googleMap;
        this.mGoogleMap.addMarker(new MarkerOptions().position(this.coordinates).title("Local onde tomou o remédio!"));
        this.mGoogleMap.getUiSettings().setZoomControlsEnabled(true);
        this.mGoogleMap.moveCamera(CameraUpdateFactory.newLatLng(this.coordinates));
    }
}
