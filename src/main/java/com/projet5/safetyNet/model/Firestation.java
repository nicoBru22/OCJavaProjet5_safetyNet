package com.projet5.safetyNet.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Firestation {

	@JsonProperty("address")
	private String address;
	
	@JsonProperty("station")
	private String station;
}
