package jpastudy.restapi.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.ThrowableAssert.catchThrowable;

import java.util.List;
import javax.persistence.EntityManager;
import jpastudy.restapi.RestApiApplication;
import jpastudy.restapi.controller.request.DefaultMembershipRequest;
import jpastudy.restapi.controller.request.PointSaveMembershipRequest;
import jpastudy.restapi.domain.Membership;
import jpastudy.restapi.repository.MembershipRepository;
import jpastudy.restapi.repository.UserMembershipRepository;
import jpastudy.restapi.service.response.DefaultMemberShipResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = RestApiApplication.class)
@Transactional
public class UserMembershipServiceTest {

  @Autowired
  private UserMembershipRepository userMembershipRepository;
  @Autowired
  private MembershipRepository membershipRepository;
  @Autowired
  private EntityManager em;
  @Autowired
  private UserMembershipService userMembershipService;
  private DefaultMembershipRequest req;
  private PointSaveMembershipRequest pointReq;

  @BeforeEach
  public void defaultDataSave() {
    req = new DefaultMembershipRequest();
    pointReq = new PointSaveMembershipRequest();
    Membership memberShip1 = Membership.memberShipCreate("spc", "happypoint");
    em.persist(memberShip1);
    Membership memberShip2 = Membership.memberShipCreate("shinsegae", "shinsegaepoint");
    em.persist(memberShip2);
    Membership memberShip3 = Membership.memberShipCreate("cj", "cjone");
    em.persist(memberShip3);
  }

  @ParameterizedTest
  @DisplayName("맴버쉽가입성공")
  @CsvSource(value = {"spc:test1", "shinsegae:test1", "cj:test1"}, delimiter = ':')
  public void 맴버쉽가입성공(String membershipId, String userId) {
    req.setMembershipId(membershipId);
    DefaultMemberShipResponse res = userMembershipService.join(userId, req);
    assertThat(membershipId).isEqualTo(res.getMembershipId());
    assertThat(userId).isEqualTo(res.getUserId());
  }

  @ParameterizedTest
  @DisplayName("맴버쉽가입실패 : 존재하지 않는 맴버쉽")
  @CsvSource(value = {"x:test1", "y:test1", "z:test1"}, delimiter = ':')
  public void 맴버쉽가입실패(String membershipId, String userId) {
    req.setMembershipId(membershipId);
    Throwable thrown = catchThrowable(() -> {
      userMembershipService.join(userId, req);
    });
    assertThat(thrown).isInstanceOf(IllegalStateException.class)
        .hasMessageContaining("존재하지 않는 맴버쉽");
  }

  @ParameterizedTest
  @DisplayName("맴버쉽가입실패 : 이미 등록한 맴버쉽")
  @CsvSource(value = {"spc:test1", "shinsegae:test1", "cj:test1"}, delimiter = ':')
  public void 맴버쉽가입실패2(String membershipId, String userId) {
    req.setMembershipId(membershipId);
    userMembershipService.join(userId, req);

    Throwable thrown = catchThrowable(() -> {
      userMembershipService.join(userId, req);
    });
    assertThat(thrown).isInstanceOf(IllegalStateException.class)
        .hasMessageContaining("이미 등록한 맴버쉽");
  }

  /////////////////////////////////////////////////////////////////////////////
  @ParameterizedTest
  @DisplayName("맴버쉽취소성공")
  @CsvSource(value = {"spc:test1", "shinsegae:test1", "cj:test1"}, delimiter = ':')
  public void 맴버쉽취소성공(String membershipId, String userId) {
    req.setMembershipId(membershipId);
    userMembershipService.join(userId, req);
    boolean flag = userMembershipService.cancleMembership(userId, membershipId);
    assertThat(flag).isTrue();
  }

  @ParameterizedTest
  @DisplayName("맴버쉽취소실패 : 존재하지 않는 맴버쉽")
  @CsvSource(value = {"spc:test1:x:test1", "shinsegae:test1:x:test1",
      "cj:test1:x:test1"}, delimiter = ':')
  public void 맴버쉽취소실패(String membershipId, String userId, String fMembershipId, String fUserId) {
    req.setMembershipId(membershipId);
    userMembershipService.join(userId, req);
    Throwable thrown = catchThrowable(() -> {
      userMembershipService.cancleMembership(fUserId, fMembershipId);
    });
    assertThat(thrown).isInstanceOf(IllegalStateException.class)
        .hasMessageContaining("존재하지 않는 맴버쉽");
  }

  @ParameterizedTest
  @DisplayName("맴버쉽취소실패 : 이미 해제한 맴버쉽")
  @CsvSource(value = {"spc:test1", "shinsegae:test1", "cj:test1"}, delimiter = ':')
  public void 맴버쉽취소실패2(String membershipId, String userId) {
    req.setMembershipId(membershipId);
    userMembershipService.join(userId, req);
    userMembershipService.cancleMembership(userId, membershipId);
    Throwable thrown = catchThrowable(() -> {
      userMembershipService.cancleMembership(userId, membershipId);
    });

    assertThat(thrown).isInstanceOf(IllegalStateException.class)
        .hasMessageContaining("이미 해제 됬거나 잘못된 요청입니다.");
  }

  @ParameterizedTest
  @DisplayName("맴버쉽취소실패 : 존재하지 않는 맴버쉽 해제")
  @CsvSource(value = {"spc:test1", "shinsegae:test1", "cj:test1"}, delimiter = ':')
  public void 맴버쉽취소실패3(String membershipId, String userId) {
    req.setMembershipId(membershipId);

    Throwable thrown = catchThrowable(() -> {
      userMembershipService.cancleMembership(userId, membershipId);
    });

    assertThat(thrown).isInstanceOf(IllegalStateException.class)
        .hasMessageContaining("이미 해제 됬거나 잘못된 요청입니다.");
  }

  //////////////////////////////////////////////////////////////////////////////////////
  @ParameterizedTest
  @DisplayName("맴버쉽포인트적립 성공")
  @CsvSource(value = {"spc:test1:400:3000", "shinsegae:test1:500:100",
      "cj:test1:600:1"}, delimiter = ':')
  public void 맴버쉽포인트적립(String membershipId, String userId, Integer point, Integer amount) {
    req.setMembershipId(membershipId);
    req.setPoint(point);
    userMembershipService.join(userId, req);

    pointReq.setMembershipId(membershipId);
    pointReq.setAmount(amount);

    DefaultMemberShipResponse pointResult = userMembershipService
        .userMembership(userId, membershipId);
    System.out.println(pointResult.getPoint());
    userMembershipService.savePointForPay(userId, pointReq);

    Double d = (amount * 0.01) + pointResult.getPoint();

    DefaultMemberShipResponse res = userMembershipService.userMembership(userId, membershipId);

    assertThat(d.intValue()).isEqualTo(res.getPoint());
  }

  @ParameterizedTest
  @DisplayName("맴버쉽포인트적립 실패 : 존재하지 않는 맴버쉽")
  @CsvSource(value = {"spc:test1:x:test1", "shinsegae:test1:x:test1",
      "cj:test1:x:test1"}, delimiter = ':')
  public void 맴버쉽포인트적립_실패(String membershipId, String userId, String fMembershipId,
      String fUserId) {
    req.setMembershipId(membershipId);
    req.setPoint(0);
    pointReq.setMembershipId(fMembershipId);
    pointReq.setAmount(0);
    userMembershipService.join(userId, req);
    Throwable thrown = catchThrowable(() -> {
      userMembershipService.savePointForPay(userId, pointReq);
    });
    assertThat(thrown).isInstanceOf(IllegalStateException.class)
        .hasMessageContaining("존재하지 않는 맴버쉽");
  }

  @ParameterizedTest
  @DisplayName("맴버쉽포인트적립 실패 : 이미 해제한 맴버쉽")
  @CsvSource(value = {"spc:test1", "shinsegae:test1", "cj:test1"}, delimiter = ':')
  public void 맴버쉽포인트적립_실패2(String membershipId, String userId) {
    req.setMembershipId(membershipId);
    req.setPoint(0);
    pointReq.setMembershipId(membershipId);
    pointReq.setAmount(0);

    userMembershipService.join(userId, req);
    userMembershipService.cancleMembership(userId, membershipId);
    Throwable thrown = catchThrowable(() -> {
      userMembershipService.savePointForPay(userId, pointReq);
    });

    assertThat(thrown).isInstanceOf(IllegalStateException.class)
        .hasMessageContaining("이미 해제 됬거나 잘못된 요청입니다.");
  }

  @ParameterizedTest
  @DisplayName("맴버쉽포인트적립 실패 : 존재하지 않는 맴버쉽 해제")
  @CsvSource(value = {"spc:test1", "shinsegae:test1", "cj:test1"}, delimiter = ':')
  public void 맴버쉽포인트적립_실패3(String membershipId, String userId) {
    pointReq.setMembershipId(membershipId);
    pointReq.setAmount(0);

    Throwable thrown = catchThrowable(() -> {
      userMembershipService.savePointForPay(userId, pointReq);
    });

    assertThat(thrown).isInstanceOf(IllegalStateException.class)
        .hasMessageContaining("이미 해제 됬거나 잘못된 요청입니다.");
  }

  //////////////////////////////////////////////////////////////////////////////////
  @ParameterizedTest
  @DisplayName("맴버쉽조회")
  @CsvSource(value = {"spc:test1", "shinsegae:test1", "cj:test1"}, delimiter = ':')
  public void 맴버쉽조회(String membershipId, String userId) {
    req.setMembershipId(membershipId);
    userMembershipService.join(userId, req);

    List<DefaultMemberShipResponse> res = userMembershipService.userMemberships(userId);
    DefaultMemberShipResponse res2 = userMembershipService.userMembership(userId, membershipId);
    assertThat(membershipId).isEqualTo(res.get(0).getMembershipId());
    assertThat(userId).isEqualTo(res.get(0).getUserId());
    assertThat(membershipId).isEqualTo(res2.getMembershipId());
    assertThat(userId).isEqualTo(res2.getUserId());

  }


}
