package jpastudy.restapi.controller;

import java.net.URI;
import javax.validation.Valid;
import javax.validation.constraints.Size;
import jpastudy.restapi.controller.request.DefaultMembershipRequest;
import jpastudy.restapi.controller.request.PointSaveMembershipRequest;
import jpastudy.restapi.service.UserMembershipService;
import jpastudy.restapi.service.response.DefaultMemberShipResponse;
import jpastudy.restapi.service.response.JsonResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@RestController
@RequestMapping(value = "/api/v1/membership", produces = "application/json")
@RequiredArgsConstructor
@EnableJpaAuditing
@Validated
public class UserMembershipController {

  private final UserMembershipService userMembershipService;

  @GetMapping
  public ResponseEntity queryMembershipAll(
      @RequestHeader(value = "X-USER-ID") @Size(min = 1) String userId
  ) {
    return ResponseEntity.ok(
        new JsonResponse(true
            , userMembershipService.userMemberships(userId)
            , null));
  }

  @GetMapping("/{membershipId}")
  public ResponseEntity queryMembershipOne(@PathVariable String membershipId,
      @RequestHeader(value = "X-USER-ID") @Size(min = 1) String userId
  ) {
    return ResponseEntity.ok(new JsonResponse(true, userMembershipService
        .userMembership(userId, membershipId), null));
  }

  @PostMapping
  public ResponseEntity saveMembership(@RequestBody @Valid DefaultMembershipRequest request,
      @RequestHeader(value = "X-USER-ID") @Size(min = 1) String userId
  ) {
    DefaultMemberShipResponse response = userMembershipService.join(userId, request);

    URI location = ServletUriComponentsBuilder
        .fromCurrentRequest()
        .build()
        .toUri();

    return ResponseEntity.created(location).body(new JsonResponse(true, response, null));
  }

  @DeleteMapping("/{membershipId}")
  public ResponseEntity deleteMembership(@PathVariable String membershipId,
      DefaultMembershipRequest request, Errors errors,
      @RequestHeader(value = "X-USER-ID") String userId
  ) {
    return ResponseEntity.ok(new JsonResponse(
        true
        , userMembershipService.cancleMembership(userId, membershipId)
        , null));
  }

  @PutMapping("/point")
  public ResponseEntity savePointForPay(@RequestBody @Valid PointSaveMembershipRequest request
      , @RequestHeader(value = "X-USER-ID") String userId
  ) {

    boolean isSavePoint = userMembershipService.savePointForPay(userId, request);

    URI location = ServletUriComponentsBuilder
        .fromCurrentRequest()
        .build()
        .toUri();

    return ResponseEntity.created(location).body(new JsonResponse(
        true
        , isSavePoint
        , null));
  }

}
