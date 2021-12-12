package jpastudy.restapi.service;

import static jpastudy.restapi.domain.status.MembershipStatus.CANCLE;
import static jpastudy.restapi.domain.status.MembershipStatus.JOIN;

import java.util.List;
import java.util.stream.Collectors;
import jpastudy.restapi.controller.request.DefaultMembershipRequest;
import jpastudy.restapi.controller.request.PointSaveMembershipRequest;
import jpastudy.restapi.domain.Membership;
import jpastudy.restapi.domain.UserMembership;
import jpastudy.restapi.repository.MembershipRepository;
import jpastudy.restapi.repository.UserMembershipRepository;
import jpastudy.restapi.service.response.DefaultMemberShipResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class UserMembershipService {

  public static final Double POINT_RATE = 0.01;
  private final UserMembershipRepository userMemberShipRepository;
  private final MembershipRepository membershipRepository;

  @Transactional
  public DefaultMemberShipResponse join(String userId, DefaultMembershipRequest request) {

    Membership membership = memberShip(request.getMembershipId());

    UserMembership userMembership = userMemberShipRepository
        .findMembership(userId, request.getMembershipId())
        .orElse(
            UserMembership.createUserMembership(membership, userId, request.getPoint())
        );
    if (!userMembership.isJoin()) {
      userMembership.setMembershipStatus(JOIN);
    }

    Long seq = userMemberShipRepository.save(userMembership).getSeq();
    return new DefaultMemberShipResponse(userMembership);
  }

  @Transactional
  public boolean cancleMembership(String userId, String membershipId) {

    Membership membership = memberShip(membershipId);

    UserMembership alreadyMembership = userMemberShipRepository.findMembership(userId, membershipId)
        .orElseThrow(() -> new IllegalStateException("이미 해제 됬거나 잘못된 요청입니다."));

    alreadyMembership.setMembershipStatus(CANCLE);

    Long seq = userMemberShipRepository.save(alreadyMembership).getSeq();

    return true;
  }

  public List<DefaultMemberShipResponse> userMemberships(String userId) {

    List<UserMembership> userMemberships = userMemberShipRepository
        .findByUserId(userId);

    List<DefaultMemberShipResponse> collect = userMemberships.stream()
        .filter(m -> m.isJoin())
        .map(m -> new DefaultMemberShipResponse(m))
        .collect(Collectors.toList());

    return collect;
  }

  public DefaultMemberShipResponse userMembership(String userId, String membershipId) {
    UserMembership userMembership = userMemberShipRepository.findMembership(userId, membershipId)
        .orElseThrow(() -> new IllegalStateException("이미 해제 됬거나 잘못된 요청입니다."));

    return new DefaultMemberShipResponse(userMembership);
  }

  @Transactional
  public boolean savePointForPay(String userId, PointSaveMembershipRequest request) {

    memberShip(request.getMembershipId());

    UserMembership cancleMembership = userMemberShipRepository
        .findMembership(userId, request.getMembershipId())
        .orElseThrow(() -> new IllegalStateException("이미 해제 됬거나 잘못된 요청입니다."));

    Double calcuratePoint = request.getAmount() * POINT_RATE;
    cancleMembership.setPoint(cancleMembership.getPoint() + calcuratePoint.intValue());
    userMemberShipRepository.save(cancleMembership).getSeq();

    return true;
  }

  private Membership memberShip(String membershipId) {
    return membershipRepository.findById(membershipId)
        .orElseThrow(() -> new IllegalStateException("존재하지 않는 맴버쉽"));
  }


}
