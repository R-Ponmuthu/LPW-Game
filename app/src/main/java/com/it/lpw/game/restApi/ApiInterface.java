package com.it.lpw.game.restApi;

import com.it.lpw.game.Responsemodel.AboutResponse;
import com.it.lpw.game.Responsemodel.AppResponse;
import com.it.lpw.game.Responsemodel.BannerResponse;
import com.it.lpw.game.Responsemodel.BonusResponse;
import com.it.lpw.game.Responsemodel.CheckResponse;
import com.it.lpw.game.Responsemodel.CreditResponse;
import com.it.lpw.game.Responsemodel.GameResponse;
import com.it.lpw.game.Responsemodel.HistoryResponse;
import com.it.lpw.game.Responsemodel.LoginResponse;
import com.it.lpw.game.Responsemodel.OfferResponse;
import com.it.lpw.game.Responsemodel.RedeemResponse;
import com.it.lpw.game.Responsemodel.RewardHistoryResponse;
import com.it.lpw.game.Responsemodel.SignupResponse;
import com.it.lpw.game.Responsemodel.SpinResponse;
import com.it.lpw.game.Responsemodel.VideoResponse;
import com.it.lpw.game.Responsemodel.WebResponse;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ApiInterface {

    @GET(WebApi.Api.Offers)
    Call<OfferResponse> getOffer();

    @GET(WebApi.Api.ABOUTS)
    Call<AboutResponse> getConfig();

    @GET(WebApi.Api.APPS)
    Call<AppResponse> getOffers();

    @GET(WebApi.Api.SLIDE_BANNER)
    Call<BannerResponse> SLideBanner();

    @GET(WebApi.Api.GAMES)
    Call<GameResponse> getGame();

    @GET(WebApi.Api.VIDEOS)
    Call<VideoResponse> getVideo();

    @GET(WebApi.Api.WEB)
    Call<WebResponse> getWebLink();

    @GET(WebApi.Api.CREDIT_DAILY)
    Call<CreditResponse> getDailyCheckin();

    @POST(WebApi.Api.Login)
    Call<LoginResponse> Login();

    @POST(WebApi.Api.SIGNUP)
    Call<SignupResponse> Signup();

    @POST(WebApi.Api.VERIFY_OTP)
    Call<BonusResponse> Verify_OTP(@Query("otp") String otp,
                              @Query("email") String email);

    @POST(WebApi.Api.RESET_PASSWORD)
    Call<BonusResponse> ResetPass(@Query("email") String email);

    @POST(WebApi.Api.UPDATE_PASSWORD)
    Call<BonusResponse> UpdatePass(@Query("email") String email,
                                   @Query("password") String password);

    @POST(WebApi.Api.REDEEM)
    Call<CreditResponse> Redeem();

    @GET(WebApi.Api.USER_COIN)
    Call<CreditResponse> Mycoin();

    @POST(WebApi.Api.CHECK)
    Call<CheckResponse> Check();

    @GET(WebApi.Api.CREDIT_APP)
    Call<CreditResponse> CreditApp();

    @GET(WebApi.Api.CHECK_SPIN)
    Call<SpinResponse> CheckSpin();

    @GET(WebApi.Api.CREDIT_SPIN)
    Call<CreditResponse> CreditSpin();
    @GET(WebApi.Api.CREDIT_VIDEO)
    Call<CreditResponse> CreditVideo();
    @GET(WebApi.Api.CREDIT_WEB)
    Call<CreditResponse> CreditWeb();

    @GET(WebApi.Api.REWARDS)
    Call<RedeemResponse> getRedeem();

    @GET(WebApi.Api.HISTORY)
    Call<HistoryResponse> History();

    @GET(WebApi.Api.REWARD_HISTORY)
    Call<RewardHistoryResponse> RewardHistory();

    @GET(WebApi.Api.CREDIT_GAME)
    Call<CreditResponse> GameReward();


}
