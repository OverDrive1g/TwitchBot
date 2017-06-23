package com.overdrive.bangtwitch.model.api;

import retrofit2.http.GET;
import retrofit2.http.Query;

public interface TwitchService {
    @GET("/oauth2/authorize")
    void auth(
            @Query("client_id")String clientId,
            @Query("redirect_uri") String redirectUri,
            @Query("response_type") String responseType,
            @Query("scope")String scope
    );
}
