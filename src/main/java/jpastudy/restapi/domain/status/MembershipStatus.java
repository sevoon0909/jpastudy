package jpastudy.restapi.domain.status;

public enum MembershipStatus {
     JOIN("Y")
    ,CANCLE("N");

    String status;

    MembershipStatus(String code) {
        status = code;
    }

    public String getValue(){
        return status;
    };

}
