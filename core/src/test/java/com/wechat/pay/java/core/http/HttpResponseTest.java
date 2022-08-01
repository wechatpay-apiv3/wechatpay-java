package com.wechat.pay.java.core.http;

import static org.junit.jupiter.api.Assertions.assertThrows;

import com.wechat.pay.java.core.exception.MalformedMessageException;
import org.junit.jupiter.api.Test;

class HttpResponseTest {

  @Test
  void throwMalformedMessageException() {
    OriginalResponse response =
        new OriginalResponse.Builder()
            .contentType("application/json")
            .request(
                new HttpRequest.Builder().httpMethod(HttpMethod.POST).url("https://test").build())
            .body("invalid body")
            .build();
    HttpResponse.Builder<Object> builder =
        new HttpResponse.Builder<>().serviceResponseType(Object.class).originalResponse(response);
    assertThrows(MalformedMessageException.class, builder::build);
  }
}
