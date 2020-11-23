# 카카오페이 뿌리기 API 과제 restful-web-service

## 프로젝트 환경
* Java 1.8
* Spring Boot
* JPA
* H2 DB

## 요구 사항
1. 뿌리기, 받기, 조회 기능을 수행하는 REST API를 구현합니다.
2. 작성한 어플리케이션이 다수의 서버에 다수의 인스턴스로 동작하더라도 기능에 문제가 없도록 설계되어야 합니다.
3. 각 기능 및 제약사항에 대한 단위테스트를 반드시 작성합니다.

## 상세 구현 요건 및 제약 사항
1. 뿌리기 API - @PostMapping("/spread")
  ● 다음 조건을 만족하는 뿌리기 API를 만들어 주세요.
    ○ 뿌릴 금액, 뿌릴 인원을 요청값으로 받습니다.
    ○ 뿌리기 요청건에 대한 고유 token을 발급하고 응답값으로 내려줍니다.
    ○ 뿌릴 금액을 인원수에 맞게 분배하여 저장합니다. (분배 로직은 자유롭게
    구현해 주세요.)
    ○ token은 3자리 문자열로 구성되며 예측이 불가능해야 합니다.

2. 받기 API - @PatchMapping("/takeMoney")
  ● 다음 조건을 만족하는 받기 API를 만들어 주세요.
    ○ 뿌리기 시 발급된 token을 요청값으로 받습니다.
    ○ token에 해당하는 뿌리기 건 중 아직 누구에게도 할당되지 않은 분배건 하나를
    API를 호출한 사용자에게 할당하고, 그 금액을 응답값으로 내려줍니다.
    ○ 뿌리기 당 한 사용자는 한번만 받을 수 있습니다.
    ○ 자신이 뿌리기한 건은 자신이 받을 수 없습니다.
    ○ 뿌린기가 호출된 대화방과 동일한 대화방에 속한 사용자만이 받을 수
    있습니다.
    ○ 뿌린 건은 10분간만 유효합니다. 뿌린지 10분이 지난 요청에 대해서는 받기
    실패 응답이 내려가야 합니다.
    
3. 조회 API - @GetMapping("/spread/{token}")
  ● 다음 조건을 만족하는 조회 API를 만들어 주세요.
    ○ 뿌리기 시 발급된 token을 요청값으로 받습니다.
    ○ token에 해당하는 뿌리기 건의 현재 상태를 응답값으로 내려줍니다. 현재
    상태는 다음의 정보를 포함합니다.
    ○ 뿌린 시각, 뿌린 금액, 받기 완료된 금액, 받기 완료된 정보 ([받은 금액, 받은
    사용자 아이디] 리스트)
    ○ 뿌린 사람 자신만 조회를 할 수 있습니다. 다른사람의 뿌리기건이나 유효하지
    않은 token에 대해서는 조회 실패 응답이 내려가야 합니다.
    ○ 뿌린 건에 대한 조회는 7일 동안 할 수 있습니다.


## 모델 구조

Spread
| Type       | 값 (예)          | KEY |
| --------   | ---------------- | --- |
| String     | token            |  Y  |
| Integer    | X_USER_ID        |     |
| String     | X_ROOM_ID        |     |
| Integer    | amountOfMoney    |     |
| Integer    | receive_room_id  |     |
| Integer    | numOfPeople      |     | 
| Date       | SpreadDate       |     | 
| String     | takeMoneyList    |     | 

Receive
| Type       | 값 (예)          | KEY |
| --------   | ---------------- | --- |
| Integer    | sq               |  Y  |
| Spread     | token            |     |
| String     | tokenValue       |     |
| Integer    | receive_id       |     |
| String     | receive_room_id  |     |
| Integer    | receiveOfMoney   |     |

## 구현내용

### 뿌리기
POST http://localhost:8088/api/spread
인서트를 위한 repo.save()를 사용

### 받기
PATCH http://localhost:8088/api/takeMoney/{token} 
업데이트를 위한 repo.flush()를 사용

### 조회
GET http://localhost:8088/api/spread (전체조회)
조회를 위한 repo.findAll()를 사용
GET http://localhost:8088/api/takeMoney/{token} (건별조회)
조회를 위한 repo.findByToken()를 사용


## 에러 정의 
```
public enum ExceptionStatus {
    ERORR500(500, "뿌리기당 한 사용자는 한번만 받을 수 있습니다."),
    ERORR501(501, "자신이 뿌리기한 건은 자신이 받을 수 없습니다."),
    ERORR502(502, "뿌리기가 호출된 대화방과 동일한 대화방에 속한 사용자만이 받을 수 있습니다."),
    ERORR503(503, "뿌린 건은 10분간만 유효합니다."),
    ERORR504(504, "뿌리기당 한 사용자는 한번만 받을 수 있습니다."),
    ERORR505(505, "뿌린 사람 자신만 조회를 할 수 있습니다."),
    ERORR506(506, "뿌린 건에 대한 조회는 7일 동안 할 수 있습니다."),
    ERORR507(507, "서버 요청 에러입니다."),
    ERORR508(508, "존재하지 않는 토큰 입니다.");;
    ...
```

```json
{
    "timestamp": "2020-11-23T14:28:17.662+0000",
    "message": "뿌린 건에 대한 조회는 7일 동안 할 수 있습니다.",
    "detailes": "uri=/api/spread/6RA"
}
```
