package ketank.bloodbank.FuelReadingApi;

import ketank.bloodbank.Models.FuelRange;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ApiInterface {

//    @GET("top-headlines")
//    Call<News> getNews(
//            @Query("country") String country,
//            @Query("apiKey") String apiKey
//
//
//    );
//
//    @GET("everything")
//    Call<News> getNewsSearch(
//
//            @Query("q") String keyword,
//            @Query("language") String language,
//            @Query("sortBy") String sortyBy,
//            @Query("apiKey") String apiKey
//
//    );

    @GET("con_rate")
    Call<FuelRange> getFuelRange(
            @Query("uid") int uid
    );

}
