package com.fastcampus.pass.adapter;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class KakaoTalkMessageResponse {
	@JsonProperty("successful_receiver_uuids")
	private List<String> successfulReceiverUuids;

}