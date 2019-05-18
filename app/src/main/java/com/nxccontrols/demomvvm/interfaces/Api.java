package com.nxccontrols.demomvvm.interfaces;

import com.nxccontrols.demomvvm.models.Hero;
import com.nxccontrols.demomvvm.models.Project;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface Api {
 
    String BASE_URL = "https://simplifiedcoding.net/demos/";
 
    @GET("marvel")
    Call<List<Hero>> getHeroes();

    @GET("users/{user}/repos")
    Call<List<Project>> getProjectList(@Path("user") String user);

    @GET("/repos/{user}/{reponame}")
    Call<Project> getProjectDetails(@Path("user") String user,
                                    @Path("reponame") String projectName);
}