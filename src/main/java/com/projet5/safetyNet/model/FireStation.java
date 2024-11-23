package com.projet5.safetyNet.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class FireStation {

	@JsonProperty("address")
	private String address;
	
	@JsonProperty("station")
	private Long station;
}
