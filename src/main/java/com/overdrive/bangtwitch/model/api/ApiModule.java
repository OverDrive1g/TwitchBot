package com.overdrive.bangtwitch.model.api;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public final class ApiModule {
    private static final String BASE_URL = "https://api.twitch.tv/kraken/";

    private ApiModule() {
    }

    public static TwitchService getTwitchApiInterface(){

        Retrofit.Builder builder = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create());

        Retrofit retrofit = builder.build();

        return retrofit.create(TwitchService.class);
    }

}
