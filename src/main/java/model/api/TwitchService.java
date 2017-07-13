package model.api;

import model.dto.AuthStatusDTO;
import model.dto.TokenStatus;
import model.dto.UserDTO;
import retrofit2.Call;
import retrofit2.http.*;

public interface TwitchService {

    @POST("oauth2/revoke")
    Call<AuthStatusDTO> revokeToken(
            @Query("client_id")String clientId,
            @Query("token")String token
    );

    @Headers({
            "Accept: application/vnd.twitchtv.v5+json",
            "Client-ID: 16w9wz5ge9mtipznr4rmk1cda0z4we"
    })
    @GET
    Call<TokenStatus> getTokenStatus(
            @Header("Authorization") String oauth,
            @Url String url
    );

    @Headers({
            "Accept: application/vnd.twitchtv.v5+json",
            "Client-ID: 16w9wz5ge9mtipznr4rmk1cda0z4we"
    })
    @GET("user")
    Call<UserDTO> getUserInfo(@Header("Authorization") String oauth);
}
