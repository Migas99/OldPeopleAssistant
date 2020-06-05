package pt.ipp.estg.pharmacies.Pharmacies.OpenStreetMapAPI;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class GetRetrofit {

    public final String URL = "https://nominatim.openstreetmap.org";
    private Retrofit retrofit = null;

    public GetRetrofit(){
            this.retrofit = new Retrofit.Builder()
                    .baseUrl(URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
    }

    public Retrofit getRetrofit(){
        return retrofit;
    }

}
