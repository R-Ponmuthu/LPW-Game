package com.it.lpw.game.paytm.beans;

import com.google.gson.annotations.SerializedName;

public class ChecksumResponse {

    @SerializedName("head")
    public Head head;

    @SerializedName("body")
    public Body body;
}

