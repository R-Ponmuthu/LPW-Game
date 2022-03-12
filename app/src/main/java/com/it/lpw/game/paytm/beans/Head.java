package com.it.lpw.game.paytm.beans;

import com.google.gson.annotations.SerializedName;

public class Head {

    @SerializedName("responseTimestamp")
    String responseTimestamp;

    @SerializedName("version")
    String version;

    @SerializedName("signature")
    String signature;
}
