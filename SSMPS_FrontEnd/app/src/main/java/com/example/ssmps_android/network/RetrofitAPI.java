package com.example.ssmps_android.network;

import com.example.ssmps_android.domain.CenterItem;
import com.example.ssmps_android.domain.Item;
import com.example.ssmps_android.domain.Location;
import com.example.ssmps_android.domain.Manager;
import com.example.ssmps_android.domain.Store;
import com.example.ssmps_android.dto.CenterItemResponse;
import com.example.ssmps_android.dto.LocationRequest;
import com.example.ssmps_android.dto.LoginRequest;
import com.example.ssmps_android.dto.LoginResponse;
import com.example.ssmps_android.dto.RegistItemRequest;
import com.example.ssmps_android.dto.StoreRequest;
import com.example.ssmps_android.dto.StoreResponse;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface RetrofitAPI {
    @POST("api/join")
    Call<Manager> join(@Body Manager manager);

    @GET("api/manager/login")
    Call<LoginResponse> login(@Query("id") String id, @Query("password") String password);

    @POST("api/manager/test")
    Call<String> testPosting();

    @GET("api/loginFirst")
    Call<LoginResponse> loginFirst(@Query("id") String id, @Query("password") String password);

    @GET("api/storeList/{id}")
    Call<List<StoreResponse>> findStoreList(@Path("id") Long id);

    @GET("api/store/{name}")
    Call<List<Store>> findStoreByName(@Path("name") String name);

    @GET("api/manager/store/{name}")
    Call<List<Store>> findManagerStoreByName(@Path("name") String name, @Query("id") Long managerId);

    @GET("api/centerItemList")
    Call<List<CenterItemResponse>> findAllCenterItem();

    @GET("api/centerItem/{name}")
    Call<List<CenterItemResponse>> findCenterItemByName(@Path("name") String name);

    @POST("api/item")
    Call<Item> registItem(@Query("store") Long storeId, @Query("item") Long itemId);

    @GET("api/mana/item")
    Call<List<Item>> findItemByName(@Query("name") String name, @Query("store_id") Long storeId);

    @GET("api/itemList")
    Call<List<Item>> findAllItem(@Query("storeId") Long storeId);

    @GET("api/storeList")
    Call<List<Store>> findAllStore();

    @GET("api/store/location/{id}")
    Call<List<Location>> findStoreLocation(@Path("id") Long storeId);

    @POST("api/store/location")
    Call<String> registStoreLocation(@Query("store_id") Long id, @Body Location location);
    @DELETE("api/store/location")
    Call<String> deleteStoreLocation(@Query("location_id") Long locationId);

    @PUT("api/store/location")
    Call<String> updateStoreLocation(@Body Location location);


    @DELETE("api/store/item/{id}")
    Call<Item> deleteItem(@Path("id") Long itemId);
    @PUT("api/store/item")
    Call<Item> modifyItemQuantity(@Body Item item);

    @PUT("api/location/item/{id}")
    Call<Location> modifyItemLocation(@Body List<Item> item, @Path("id") Long locationId);
}