package com.nxccontrols.demomvvm.viewmodels;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.support.annotation.NonNull;
import android.util.Log;

import com.google.gson.Gson;
import com.nxccontrols.demomvvm.interfaces.Api;
import com.nxccontrols.demomvvm.models.Hero;

import java.io.IOException;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.HttpException;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class HeroesViewModel extends ViewModel {

    public String TAG = "HeroesViewModel";
    //this is the data that we will fetch asynchronously 
    private MutableLiveData<List<Hero>> heroList;

    public LiveData<List<Hero>> getHeroes() {
        if (heroList == null) {
            heroList = new MutableLiveData<List<Hero>>();
            //we will load it asynchronously from server in this method
            loadHeroes();
        }
        //finally we will return the list
        return heroList;
    }

    private void loadHeroes() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Api.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        Api api = retrofit.create(Api.class);
        Call<List<Hero>> call = api.getHeroes();


        call.enqueue(new Callback<List<Hero>>() {
            @Override
            public void onResponse(Call<List<Hero>> call, @NonNull Response<List<Hero>> response) {
                if (response.isSuccessful()) {
                    //finally we are setting the list to our MutableLiveData
                    heroList.setValue(response.body());

                    Log.d(TAG, "reponse---" + new Gson().toJson(response.body()));
                } else {
                    heroList.setValue(null);
                    switch (response.code()) {
                        case 401:
                            Log.d(TAG, "error------" + "unAuthorized ");
                            break;

                        case 404:
                            Log.d(TAG, "error------" + "not found");
                            break;
                        case 500:
                            Log.d(TAG, "error---" + "not logged in or server broken");
                            break;
                        default:
                            Log.d(TAG, "error-----" + "unknown error");
                            break;
                    }
                }
            }

            @Override
            public void onFailure(Call<List<Hero>> call, Throwable t) {
                Log.d(TAG, "error-----" + t.getMessage());
                heroList.setValue(null);
                if (t instanceof HttpException) {
                    HttpException httpException = (HttpException) t;
                    Response response = httpException.response();
                    Log.i(TAG, "error----" + t.getMessage() + " / " + t.getClass());
                }
                // A network error happened
                if (t instanceof IOException) {
                    Log.i(TAG, "error-----" + t.getMessage() + " / " + t.getClass());
                }

            }
        });
    }
}