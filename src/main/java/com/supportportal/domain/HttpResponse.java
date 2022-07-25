package com.supportportal.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

@AllArgsConstructor
@Builder
@Data
@NoArgsConstructor
public class HttpResponse {
  private int httpStatusCode; // 200, 201, 400, 500
  private HttpStatus httpStatus;
  private String reason;
  private String message;

  /*
  {
    "httpStatusCode": 200,
    "httpStatus": "OK",
    "reason": "Ok",
    "message": "Your request was successful"
  }
   */
}
