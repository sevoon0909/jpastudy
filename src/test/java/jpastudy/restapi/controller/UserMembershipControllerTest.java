package jpastudy.restapi.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import javax.persistence.EntityManager;
import jpastudy.restapi.RestApiApplication;
import jpastudy.restapi.controller.request.DefaultMembershipRequest;
import jpastudy.restapi.controller.request.PointSaveMembershipRequest;
import jpastudy.restapi.domain.Membership;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = RestApiApplication.class)
@AutoConfigureMockMvc
@Transactional
public class UserMembershipControllerTest {

  @Autowired
  MockMvc mockMvc;

  @Autowired
  ObjectMapper objectMapper;

  @Autowired
  private EntityManager em;

  @BeforeEach
  @Rollback(value = false)
  public void defaultDataSave() {
        Membership memberShip1 = Membership.memberShipCreate("spc","happypoint");
        em.persist(memberShip1);
        Membership memberShip2 = Membership.memberShipCreate("shinsegae","shinsegaepoint");
        em.persist(memberShip2);
        Membership memberShip3 = Membership.memberShipCreate("cj","cjone");
        em.persist(memberShip3);
  }

  @DisplayName("정상적으로 가입")
  @Test
  public void joinMembership() throws Exception {

    DefaultMembershipRequest req = new DefaultMembershipRequest();
    req.setMembershipId("spc");
    req.setMembershipName("happypoint");
    req.setPoint(5120);
    String userId = "test1";

    mockMvc.perform(post("/api/v1/membership/")
        .header("X-USER-ID", userId)
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(req)))
        .andDo(print())
        .andExpect(status().is(201))
        .andExpect(jsonPath("response.userId").value(userId))
        .andExpect(header().string(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE))
        .andExpect(jsonPath("response.membershipId").value(req.getMembershipId()))
        .andExpect(jsonPath("response.membershipName").exists())
        .andExpect(jsonPath("response.point").value(req.getPoint()))
        .andExpect(jsonPath("response.seq").exists())
        .andExpect(jsonPath("response.membershipName").exists())
        .andExpect(jsonPath("response.startDate").exists())
        .andExpect(jsonPath("success").value(true))
        .andExpect(jsonPath("response.startDate").exists())
        .andExpect(jsonPath("error").isEmpty())
        .andExpect(jsonPath("response.membershipStatus").value("Y"));


  }

  @Test
  @DisplayName("가입실패: X-USER-ID 미존재")
  public void joinBadRequest() throws Exception {

    DefaultMembershipRequest req = new DefaultMembershipRequest();
    req.setMembershipId("spc");
    req.setMembershipName("happypoint");
    req.setPoint(5120);

    mockMvc.perform(post("/api/v1/membership/")
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(req)))
        .andDo(print())
        .andExpect(jsonPath("success").value(false))
        .andExpect(jsonPath("error.message").exists())
        .andExpect(status().is5xxServerError());

  }

  @Test
  @DisplayName("가입실패: 맴버쉽 아이디 빈값 오류")
  public void joinBadRequest2() throws Exception {

    DefaultMembershipRequest req = new DefaultMembershipRequest();

    req.setMembershipName("happypoint");
    req.setPoint(5120);
    String userId = "test1";
    mockMvc.perform(post("/api/v1/membership/")
        .header("X-USER-ID", userId)
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(req)))
        .andDo(print())
        .andExpect(jsonPath("success").value(false))
        .andExpect(jsonPath("error.message").exists())
        .andExpect(status().isBadRequest());
  }

  @Test
  @DisplayName("가입실패: 포인트  빈값 오류")
  public void joinBadRequest3() throws Exception {

    DefaultMembershipRequest req = new DefaultMembershipRequest();

    req.setMembershipName("happypoint");

    String userId = "test1";
    mockMvc.perform(post("/api/v1/membership/")
        .header("X-USER-ID", userId)
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(req)))
        .andDo(print())
        .andExpect(jsonPath("success").value(false))
        .andExpect(jsonPath("error.message").exists())
        .andExpect(status().isBadRequest());
  }

  @Test
  @DisplayName("가입실패: 존재하지 않는 맴버쉽 등록 시도 오류")
  public void joinBadRequest4() throws Exception {

    DefaultMembershipRequest req = new DefaultMembershipRequest();
    req.setMembershipId("spcxx");
    req.setMembershipName("happypoint");
    req.setPoint(5120);

    String userId = "test1";
    mockMvc.perform(post("/api/v1/membership/")
        .header("X-USER-ID", userId)
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(req)))
        .andDo(print())
        .andExpect(jsonPath("success").value(false))
        .andExpect(jsonPath("error.message").exists())
        .andExpect(status().is5xxServerError());
  }

  @DisplayName("정상적으로 전체 조회")
  @Test
  public void queryMembers() throws Exception {

    joinMembership();

    DefaultMembershipRequest req = new DefaultMembershipRequest();
    String userId = "test1";

    mockMvc.perform(get("/api/v1/membership/")
        .header("X-USER-ID", userId)
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(req)))
        .andDo(print())
        .andExpect(status().isOk())
        .andExpect(jsonPath("response[0].userId").value(userId))
        .andExpect(header().string(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE))
        .andExpect(jsonPath("response[0].membershipId").exists())
        .andExpect(jsonPath("response[0].membershipName").exists())
        .andExpect(jsonPath("error").isEmpty())
        .andExpect(jsonPath("response[0].point").exists());
  }

  @DisplayName("정상적으로 상세 조회")
  @Test
  public void queryMember() throws Exception {

    joinMembership();

    DefaultMembershipRequest req = new DefaultMembershipRequest();
    String userId = "test1";
    String membershipId = "spc";

    mockMvc.perform(get("/api/v1/membership/" + membershipId)
        .header("X-USER-ID", userId)
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(req)))
        .andDo(print())
        .andExpect(status().isOk())
        .andExpect(jsonPath("response.userId").value(userId))
        .andExpect(header().string(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE))
        .andExpect(jsonPath("response.membershipId").exists())
        .andExpect(jsonPath("response.membershipName").exists())
        .andExpect(jsonPath("error").isEmpty())
        .andExpect(jsonPath("response.point").exists());
  }

  @DisplayName("정상적으로 해제")
  @Test
  public void deleteMemberShip() throws Exception {

    joinMembership();

    DefaultMembershipRequest req = new DefaultMembershipRequest();
    String userId = "test1";
    String membershipId = "spc";

    mockMvc.perform(delete("/api/v1/membership/" + membershipId)
        .header("X-USER-ID", userId)
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(req)))
        .andDo(print())
        .andExpect(status().isOk())
        .andExpect(header().string(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE))
        .andExpect(jsonPath("success").value(true))
        .andExpect(jsonPath("response").value(true))
        .andExpect(jsonPath("error").isEmpty());
  }

  @DisplayName("포인트 적립")
  @Test
  public void savePointMember() throws Exception {

    joinMembership();

    PointSaveMembershipRequest req = new PointSaveMembershipRequest();
    String userId = "test1";
    String membershipId = "spc";

    req.setMembershipId(membershipId);
    req.setAmount(1000);

    mockMvc.perform(put("/api/v1/membership/point")
        .header("X-USER-ID", userId)
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(req)))
        .andDo(print())
        .andExpect(status().is2xxSuccessful())
        .andExpect(header().string(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE))
        .andExpect(jsonPath("success").value(true))
        .andExpect(jsonPath("response").value(true))
        .andExpect(jsonPath("error").isEmpty());
  }

//    {
//        "membershipId":"cj",
//            "amount":100
//    }
}
