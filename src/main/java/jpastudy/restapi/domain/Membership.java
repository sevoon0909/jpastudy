package jpastudy.restapi.domain;

import javax.persistence.Entity;
import javax.persistence.Id;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Membership {

  @Id
  private String membershipId;
  private String membershipName;

  public static Membership memberShipCreate(String membershipId, String membershipName) {
    Membership memberShip = new Membership();
    memberShip.setMembershipId(membershipId);
    memberShip.setMembershipName(membershipName);
    return memberShip;
  }

  @Override
  public String toString() {
    return "Membership{" +
        "membershipId='" + membershipId + '\'' +
        ", membershipName='" + membershipName + '\'' +
        '}';
  }
}
