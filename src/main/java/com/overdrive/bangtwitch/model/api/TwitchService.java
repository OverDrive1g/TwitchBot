package com.overdrive.bangtwitch.model.api;

import com.overdrive.bangtwitch.model.dto.AuthStatusDTO;
import retrofit2.Call;
import retrofit2.http.*;

public interface TwitchService {

    @POST("oauth2/revoke")
    Call<AuthStatusDTO> authorize(
            @Query("client_id")String clientId,
            @Query("token")String token
    );
}
