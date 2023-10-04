package com.bawi;

import java.util.Map;

public interface MyPayloadWriter {
    void write(byte[] payload, Map<String, String> headers);
}
