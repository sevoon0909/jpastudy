package jpastudy.restapi.repository;


import java.util.List;
import java.util.Optional;
import jpastudy.restapi.domain.UserMembership;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface UserMembershipRepository extends JpaRepository<UserMembership, Long> {

  @Query(" SELECT m " +
      " FROM UserMembership m JOIN fetch m.membership ms " +
      " WHERE m.userId = :userId AND ms.membershipId = :membershipId ")
  Optional<UserMembership> findMembership(@Param("userId") String userId
      , @Param("membershipId") String membershipId);

  List<UserMembership> findByUserId(String userId);

}
