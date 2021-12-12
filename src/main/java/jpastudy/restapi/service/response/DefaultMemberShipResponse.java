package jpastudy.restapi.service.response;

import static jpastudy.restapi.domain.status.MembershipStatus.JOIN;

import java.time.LocalDateTime;
import jpastudy.restapi.domain.UserMembership;
import lombok.Data;

@Data
public class DefaultMemberShipResponse {

  private Long seq;
  private String userId;
  private String membershipId;
  private String membershipName;
  private LocalDateTime startDate;
  private String membershipStatus;
  private Integer point;

  public DefaultMemberShipResponse(UserMembership o) {
    seq = o.getSeq();
    userId = o.getUserId();
    membershipId = o.getMembership().getMembershipId();
    membershipName = o.getMembership().getMembershipName();
    startDate = o.getStartDate();
    membershipStatus = o.getMembershipStatus() == JOIN ? "Y" : "N";
    point = o.getPoint();
  }

//    "seq":3,
//            "membershipId":"cj",
//            "userId":"test1",
//            "membershipName":"cjone",
//            "startDate":"2021-07-01T16:33:21.291",
//            "membershipStatus":"Y",
//            "point":5210

}
