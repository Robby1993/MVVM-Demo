package com.nxccontrols.demomvvm.viewmodels;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.support.annotation.NonNull;
import android.util.Log;

import com.google.gson.Gson;
import com.nxccontrols.demomvvm.interfaces.Api;
import com.nxccontrols.demomvvm.models.Project;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class SecondViewModel extends ViewModel {

    public String TAG = "SecondActivityListViewModel";

    private MutableLiveData<List<Project>> projectList;

    public LiveData<List<Project>> getProject(String userid) {
        if (projectList == null) {
            projectList = new MutableLiveData<List<Project>>();
            loadlist(userid);
        }
        return projectList;
    }

    private void loadlist(String userid) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://api.github.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        Api api = retrofit.create(Api.class);
        Call<List<Project>> call = api.getProjectList(userid);


        call.enqueue(new Callback<List<Project>>() {
            @Override
            public void onResponse(@NonNull Call<List<Project>> call, @NonNull Response<List<Project>> response) {

                if (response.isSuccessful()) {
                    projectList.setValue(response.body());
                    Log.d(TAG, "reponse---" + new Gson().toJson(response.body()));

                } else {
                    switch (response.code()) {

                        case 401:
                            Log.d(TAG, "error------" + "unAuthorized ");
                            break;
                        case 404:

                            Log.d(TAG, "error" + "not found");
                            break;
                        case 500:
                            Log.d(TAG, "error" + "not logged in or server broken");
                            break;
                        default:
                            Log.d(TAG, "error" + "unknown error");
                            break;
                    }
                }
            }

            @Override
            public void onFailure(Call<List<Project>> call, Throwable t) {
                Log.d(TAG, "error" + t.getMessage());
            }
        });
    }

    private MutableLiveData<Project> projectObservable;


    public LiveData<Project> getProjectDetails(String projid, String projectName) {
        if (projectObservable == null) {
            projectObservable = new MutableLiveData<Project>();
            loadDetails(projid, projectName);
        }
        return projectObservable;
    }

    private void loadDetails(String projid, String projectName) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://api.github.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        Api api = retrofit.create(Api.class);
        Call<Project> call = api.getProjectDetails(projid, projectName);

        call.enqueue(new Callback<Project>() {
            @Override
            public void onResponse(Call<Project> call, @NonNull Response<Project> response) {

                if (response.isSuccessful()) {

                    projectObservable.setValue(response.body());
                    Log.d(TAG, "Details" + new Gson().toJson(response.body()));

                } else {
                    switch (response.code()) {

                        case 401:
                            Log.d(TAG, "error------" + "unAuthorized ");
                            break;

                        case 404:

                            Log.d(TAG, "error" + "not found");
                            break;
                        case 500:
                            Log.d(TAG, "error" + "not logged in or server broken");
                            break;
                        default:
                            Log.d(TAG, "error" + "unknown error");
                            break;
                    }
                }


            }

            @Override
            public void onFailure(@NonNull Call<Project> call, @NonNull Throwable t) {
                Log.d(TAG, "error" + t.getMessage());
            }
        });
    }


}