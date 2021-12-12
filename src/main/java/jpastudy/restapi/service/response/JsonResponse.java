package jpastudy.restapi.service.response;

import jpastudy.restapi.exception.JsonError;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class JsonResponse<T> {
    boolean success;
    T response;
    JsonError error;
}
