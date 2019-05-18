package com.nxccontrols.demomvvm.interfaces;

import retrofit2.Response;

public interface ResponseListener {

    void onSuccess(Response mResponse);
    void onFailure(String message);
}