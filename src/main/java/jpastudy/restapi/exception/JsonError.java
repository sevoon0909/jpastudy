package jpastudy.restapi.exception;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class JsonError {

  String message;
  int httpStatus;
}

