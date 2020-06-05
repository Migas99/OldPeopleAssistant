package pt.ipp.estg.pharmacies;

import android.app.Activity;
import android.content.Context;
import android.location.Location;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;

import java.util.concurrent.ExecutionException;

import pt.ipp.estg.pharmacies.Pharmacies.AsyncTasks.RetrieveCityTask;

public class MenuPrincipalFragment extends Fragment {

    private Context mContext;
    private FusedLocationProviderClient mFusedLocationProviderClient;
    private LocationRequest mLocationRequest;
    private LocationCallback mLocationCallback;
    private OnCityGivenListener onCityGivenListener;
    private EditText editTextInput;
    private String city;

    public interface OnCityGivenListener {
        void onCityGiven(String input);
        void openAccount();
        void OnMenuFragmentInteractionListener(String args);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.mContext = getActivity();
        assert this.mContext != null;
        this.mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this.mContext);
        this.mLocationRequest = new LocationRequest();
        this.mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        this.mLocationCallback = new LocationCallback();

        this.mFusedLocationProviderClient.requestLocationUpdates(this.mLocationRequest, this.mLocationCallback, null);
        this.mFusedLocationProviderClient.removeLocationUpdates(this.mLocationCallback);
    }

    /**
     * Creates the content of the MenuPrincipalFragment
     *
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View myContentView = inflater.inflate(R.layout.menu_principal_fragment, container, false);

        myContentView.findViewById(R.id.PharmaciesInMyZone).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(mContext);
                mFusedLocationProviderClient.requestLocationUpdates(mLocationRequest, mLocationCallback, null);

                mFusedLocationProviderClient.getLastLocation()
                        .addOnSuccessListener((Activity) mContext, new OnSuccessListener<Location>() {
                            @Override
                            public void onSuccess(Location location) {
                                if (location != null) {
                                    try {
                                        RetrieveCityTask retrieveCityTask = new RetrieveCityTask();
                                        retrieveCityTask.execute(location);
                                        city = retrieveCityTask.get();

                                        if (city != null) {
                                            if (city.contains(" ")) {
                                                city = city.replaceAll(" ", "+");
                                            }

                                            onButtonPressed(city);
                                        } else {
                                            Toast.makeText(mContext, "Não foi possível obter a cidade em qual se encontra actualmente!", Toast.LENGTH_SHORT).show();
                                        }

                                    } catch (ExecutionException | InterruptedException e) {
                                        System.err.println(e);
                                    }
                                } else {
                                    Toast.makeText(mContext, "Erro ao obter a sua localização atual!", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });

                mFusedLocationProviderClient.removeLocationUpdates(mLocationCallback);
            }
        });

        this.editTextInput = myContentView.findViewById(R.id.inputCity);
        myContentView.findViewById(R.id.Pharmacies).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                city = editTextInput.getText().toString();
                if (!city.isEmpty()) {

                    if (city.contains(" ")) {
                        city = city.replaceAll(" ", "+");
                    }

                    onButtonPressed(city);

                } else {
                    Toast.makeText(mContext, "Nome de cidade inválido!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        ////////////////////////////////////////////////////////////////////////////////////////////
        myContentView.findViewById(R.id.entrarJogo).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onCityGivenListener.OnMenuFragmentInteractionListener("game");
            }
        });

        ////////////////////////////////////////////////////////////////////////////////////////////
        myContentView.findViewById(R.id.button_menu_medicine).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onCityGivenListener.OnMenuFragmentInteractionListener("medicineList");
            }
        });

        myContentView.findViewById(R.id.button_menu_reminder).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onCityGivenListener.OnMenuFragmentInteractionListener("reminderList");
            }
        });

        myContentView.findViewById(R.id.button_menu_history).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onCityGivenListener.OnMenuFragmentInteractionListener("historyList");
            }
        });

        myContentView.findViewById(R.id.button_menu_auth).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onCityGivenListener.openAccount();
            }
        });

        return myContentView;
    }

    public void onButtonPressed(String input) {
        if (this.onCityGivenListener != null) {
            this.onCityGivenListener.onCityGiven(input);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnCityGivenListener) {
            this.onCityGivenListener = (OnCityGivenListener) context;
        } else {
            throw new RuntimeException(context.toString());
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        this.onCityGivenListener = null;
    }
}
