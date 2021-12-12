package jpastudy.restapi.repository;

import jpastudy.restapi.domain.Membership;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MembershipRepository extends JpaRepository<Membership,String> {
//    @Query("SELECT ms FROM MEMBER_SHIP ms WHERE ms.membershipId = :membershipId")
//    MemberShip findMemberShip(@Param("user")String membershipId);

}
