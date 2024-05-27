package com.fastcampus.demogrpcclient;

import com.google.protobuf.InvalidProtocolBufferException;
import com.google.protobuf.Message;
import com.google.protobuf.MessageOrBuilder;
import com.google.protobuf.Struct;
import com.google.protobuf.util.JsonFormat;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class JsonConverter {
    public static String toJson(MessageOrBuilder messageOrBuilder) {
        try {
            return JsonFormat.printer().print(messageOrBuilder);
        } catch (InvalidProtocolBufferException e) {
            throw new RuntimeException(e);
        }
    }

    public static <T extends Message> String toJsonList(List<T> responseList) {
        List<String> jsonList = new ArrayList<>();

        for (MessageOrBuilder response : responseList) {
            String json = null;

            try {
                json = JsonFormat.printer().print(response);
            } catch (InvalidProtocolBufferException e) {
                throw new RuntimeException(e);
            }

            jsonList.add(json);
        }

        return "[" + String.join(",", jsonList) + "]";
    }

    public static Message fromJson(String json) throws IOException {
        Message.Builder structBuilder = Struct.newBuilder();
        JsonFormat.parser().ignoringUnknownFields().merge(json, structBuilder);
        return structBuilder.build();
    }
}
