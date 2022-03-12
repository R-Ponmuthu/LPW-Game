package com.it.lpw.game.paytm.beans;

import com.google.gson.annotations.SerializedName;

public class Body {

    @SerializedName("resultInfo")
    public ResultInfo resultInfo;

    @SerializedName("txnToken")
    public String txnToken;

    @SerializedName("isPromoCodeValid")
    public boolean isPromoCodeValid;

    @SerializedName("authenticated")
    public boolean authenticated;

}
