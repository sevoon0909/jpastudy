package jpastudy.restapi.domain;

import static jpastudy.restapi.domain.status.MembershipStatus.JOIN;

import java.time.LocalDateTime;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import jpastudy.restapi.domain.status.MembershipStatus;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Entity
@EntityListeners(AuditingEntityListener.class)
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserMembership {

  @Id
  @GeneratedValue
  private Long seq;

  private String userId;

  @OneToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "membership_id", unique = true)
  private Membership membership;

  @CreatedDate
  private LocalDateTime startDate;

  @Enumerated(EnumType.STRING)
  private MembershipStatus membershipStatus;

  private Integer point;

  public static UserMembership createUserMembership(Membership memberShip, String userId,
      Integer point) {
    UserMembership userMemberShip = new UserMembership();
    userMemberShip.setUserId(userId);
    userMemberShip.setMembership(memberShip);
    userMemberShip.setPoint(point);
    userMemberShip.setMembershipStatus(JOIN);
    return userMemberShip;
  }

  public boolean isJoin() {
    return membershipStatus == JOIN;
  }

  @Override
  public String toString() {
    return "UserMembership{" +
        "seq=" + seq +
        ", userId='" + userId + '\'' +
        ", membership=" + membership +
        ", startDate=" + startDate +
        ", membershipStatus='" + membershipStatus + '\'' +
        ", point=" + point +
        '}';
  }
}
