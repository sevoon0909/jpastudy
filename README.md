# API 호출 명세

## 맴버쉽 서비스

```
 * 맴버쉽 종류 : cj , shinsegae , spc
 
 * success sample
   {
    "success": true,
    "response": [
        {
           ....
        }
    ],
    "error": null
  }
  
 * error sample
    {
      "success": false,
      "response": null,
      "error": {
          "message": "존재하지 않는 맴버쉽",
          "httpStatus": 500
      }
    }
```

### /api/v1/membership/

#### 1. 맴버쉽 전체 조회

##### request

* url : /
* method : GET
* body : {}
* header : X-USER-ID

##### response

```
{
    "success": true,
    "response": [
        {
            "seq": 4,
            "userId": "test1",
            "membershipId": "cj",
            "membershipName": "cjone",
            "startDate": "2021-12-12T20:16:30.801217",
            "membershipStatus": "Y",
            "point": 9210
        }
    ],
    "error": null
}
```

#### 2. 맴버쉽 가입

##### request

* url : /
* method : POST
* header : X-USER-ID

* body :
  ```
  {
  "membershipId":"cj",
  "membershipName":"cjone",
  "point":9210 
  }
  ```

##### response

#### 3. 특정 맴버쉽 조회

##### request

* url : /{membershipId}
* method : GET
* header : X-USER-ID
* body : {}

##### response

```
{
    "success": true,
    "response": {
        "seq": 4,
        "userId": "test1",
        "membershipId": "cj",
        "membershipName": "cjone",
        "startDate": "2021-12-12T20:16:30.801217",
        "membershipStatus": "Y",
        "point": 9210
    },
    "error": null
}
```

#### 4. 특정 맴버쉽 취소

##### request

* url : /{membershipId}
* method : DELETE
* header : X-USER-ID
* body : {}

##### response

```
{
    "success": true,
    "response": true,
    "error": null
}
```

#### 5. 포인트 충전

##### request

* amount의 1% 적립
* url : /{membershipId}
* method : PUT
* header : X-USER-ID
* body :
  ```
  {
    "membershipId":"cj",
    "amount":10000
  }
  ```

##### response

```
{
    "success": true,
    "response": true,
    "error": null
}
```
