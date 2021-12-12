package jpastudy.restapi.controller.request;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import lombok.Data;

@Data
public class DefaultMembershipRequest {

  @NotEmpty
  @Size(max = 20)
  String membershipId;

  @Size(max = 255)
  String membershipName;

  @Min(0) @Max(Integer.MAX_VALUE)
  @NotNull
  Integer point;


}
