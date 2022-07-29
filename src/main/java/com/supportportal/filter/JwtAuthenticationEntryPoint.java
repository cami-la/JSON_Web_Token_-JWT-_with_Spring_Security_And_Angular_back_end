package com.supportportal.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.supportportal.constant.SecurityConstant;
import com.supportportal.domain.HttpResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.Http403ForbiddenEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;

@Component
public class JwtAuthenticationEntryPoint extends Http403ForbiddenEntryPoint {

  @Override
  public void commence
      (HttpServletRequest request, HttpServletResponse response, AuthenticationException exception)
      throws IOException {
    HttpResponse.builder()
        .httpStatusCode(HttpStatus.FORBIDDEN.value())
        .httpStatus(HttpStatus.FORBIDDEN)
        .reason(HttpStatus.FORBIDDEN.getReasonPhrase().toUpperCase())
        .message(SecurityConstant.FORBIDDEN_MESSAGE)
        .build();
    response.setContentType(MediaType.APPLICATION_JSON_VALUE);
    response.setStatus(HttpStatus.FORBIDDEN.value());
    OutputStream responseOutputStream = response.getOutputStream();
    ObjectMapper mapper = new ObjectMapper();
    mapper.writeValue(responseOutputStream, response);
    responseOutputStream.flush();
  }
}
