package pt.ipp.estg.pharmacies.Pharmacies.AsyncTasks;

import android.os.AsyncTask;

import java.io.IOException;
import java.util.List;

import pt.ipp.estg.pharmacies.Pharmacies.Models.PharmacyModel;
import pt.ipp.estg.pharmacies.Pharmacies.OpenStreetMapAPI.GetRetrofit;
import pt.ipp.estg.pharmacies.Pharmacies.OpenStreetMapAPI.OpenStreetMapAPI;
import retrofit2.Call;
import retrofit2.Retrofit;

public class RetrievePharmacyListTask extends AsyncTask<String, Void, List<PharmacyModel>> {

    private List<PharmacyModel> pharmacies;

    @Override
    protected List<PharmacyModel> doInBackground(String... strings) {
        Retrofit retrofit = new GetRetrofit().getRetrofit();
        OpenStreetMapAPI api = retrofit.create(OpenStreetMapAPI.class);
        String url = "search?q=" + strings[0] + "+pharmacy&format=json";

        Call<List<PharmacyModel>> call = api.getPharmacies(url);

        try {
            this.pharmacies = call.execute().body();
        } catch (IOException e) {
            System.err.println(e);
        }

        return this.pharmacies;
    }
}