package edu.northestern.cs5520_teamproject_iamhere.atYourService;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface AirPollutionService {

    @GET("data/2.5/air_pollution?")
    Call<AirPollutionResponse> getCurrentWeatherData(@Query("lat") String lat,
                                                @Query("lon") String lon, @Query("appid")
                                                String app_id);
}
