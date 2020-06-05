package pt.ipp.estg.pharmacies.Pharmacies.OpenStreetMapAPI;

import java.util.List;
import pt.ipp.estg.pharmacies.Pharmacies.Models.LocalizationModel;
import pt.ipp.estg.pharmacies.Pharmacies.Models.PharmacyModel;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Url;

public interface OpenStreetMapAPI {

    @GET
    Call<LocalizationModel> getCity(
            @Url String url
    );

    @GET
    Call<List<PharmacyModel>> getPharmacies(
            @Url String url
    );
}
