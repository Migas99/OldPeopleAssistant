package pt.ipp.estg.pharmacies.Pharmacies.AsyncTasks;

import android.location.Location;
import android.os.AsyncTask;

import java.io.IOException;

import pt.ipp.estg.pharmacies.Pharmacies.Models.LocalizationModel;
import pt.ipp.estg.pharmacies.Pharmacies.OpenStreetMapAPI.GetRetrofit;
import pt.ipp.estg.pharmacies.Pharmacies.OpenStreetMapAPI.OpenStreetMapAPI;
import retrofit2.Call;
import retrofit2.Retrofit;

public class RetrieveCityTask extends AsyncTask<Location, Void, String> {

    private String city;

    @Override
    protected String doInBackground(Location... locations) {
        Retrofit retrofit = new GetRetrofit().getRetrofit();
        OpenStreetMapAPI api = retrofit.create(OpenStreetMapAPI.class);
        String url = "reverse?format=json&lat=" + locations[0].getLatitude() + "&lon=" + locations[0].getLongitude();
        this.city = null;

        Call<LocalizationModel> call = api.getCity(url);

        try {
            this.city = call.execute().body().getAddress().getCity();
        } catch (IOException e) {
            System.err.println(e);
        }

        return this.city;
    }
}
