package com.it.lpw.game.Responsemodel;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class JoinGameResponse {

	@SerializedName("data")
	private String data;

	@SerializedName("success")
	private int success;

	public int getSuccess(){
		return success;
	}

	public String getData(){
		return data;
	}
}