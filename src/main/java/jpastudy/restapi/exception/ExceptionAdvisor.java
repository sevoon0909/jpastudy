package jpastudy.restapi.exception;

import jpastudy.restapi.service.response.JsonResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingRequestHeaderException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@Slf4j
@ControllerAdvice
public class ExceptionAdvisor extends ResponseEntityExceptionHandler {

  @ExceptionHandler(MissingRequestHeaderException.class)
  public ResponseEntity<JsonResponse> handleException(MissingRequestHeaderException e) {
    return new ResponseEntity<JsonResponse>(
        new JsonResponse(
            false, null, new JsonError(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR.value())
        ), new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR);
  }

  @ExceptionHandler(value = {IllegalArgumentException.class})
  protected ResponseEntity<JsonResponse> IllegalArgumentException(RuntimeException e,
      WebRequest request) {
    return new ResponseEntity<JsonResponse>(
        new JsonResponse(
            false, null, new JsonError(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR.value())
        ), new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR);
  }

  @ExceptionHandler(value = {Exception.class})
  protected ResponseEntity<JsonResponse> handleConflictAll(RuntimeException e, WebRequest request) {
    return new ResponseEntity<JsonResponse>(
        new JsonResponse(
            false, null, new JsonError(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR.value())
        ), new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR);
  }

  @ExceptionHandler(value = {IllegalStateException.class})
  protected ResponseEntity<JsonResponse> IllegalStateException(RuntimeException e,
      WebRequest request) {

    return new ResponseEntity<JsonResponse>(
        new JsonResponse(
            false, null, new JsonError(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR.value())
        ), new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR);
  }

  @Override
  protected ResponseEntity<Object> handleMethodArgumentNotValid(
      MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatus status,
      WebRequest request) {

    return new ResponseEntity(
        new JsonResponse(
            false, null, new JsonError(ex.getMessage(), HttpStatus.NOT_FOUND.value())
        ), new HttpHeaders(), HttpStatus.NOT_FOUND);
  }

  @Override
  protected ResponseEntity<Object> handleHttpRequestMethodNotSupported(
      HttpRequestMethodNotSupportedException ex, HttpHeaders headers, HttpStatus status,
      WebRequest request) {
    return new ResponseEntity(
        new JsonResponse(
            false, null, new JsonError(ex.getMessage(), HttpStatus.NOT_FOUND.value())
        ), new HttpHeaders(), HttpStatus.NOT_FOUND);

  }


}
