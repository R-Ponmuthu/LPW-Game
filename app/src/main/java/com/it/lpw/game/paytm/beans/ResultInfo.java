package com.it.lpw.game.paytm.beans;

import com.google.gson.annotations.SerializedName;

public class ResultInfo {

    @SerializedName("resultStatus")
    public String resultStatus;

    @SerializedName("resultCode")
    public String resultCode;

    @SerializedName("resultMsg")
    public String resultMsg;
}
