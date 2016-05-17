package com.fachrur_122.rxdabinmodule.Interface;

import com.fachrur_122.rxdabinmodule.Model.PlacesDetail;
import com.fachrur_122.rxdabinmodule.Model.PlacesResult;

import retrofit2.http.GET;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by fachrur_122 on 17/05/2016.
 *
 */
public interface GooglePlacesClient {
    @GET("/maps/api/place/autocomplete/json")
    Observable<PlacesResult> autocomplete(
            @Query("key") String key,
            @Query("input") String input
    );

    @GET("/maps/api/place/details/json")
    Observable<PlacesDetail> getDetail(
            @Query("key") String key,
            @Query("placeid") String input
    );
}
