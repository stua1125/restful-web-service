# 카카오페이 뿌리기 API 과제 restful-web-service

## 프로젝트 환경
* Java 1.8
* Spring Boot
* JPA
* H2 DB

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

## 뿌리기
POST http://localhost:8088/api/spread

## 받기
PATCH http://localhost:8088/api/takeMoney/{token} 

## 조회
GET http://localhost:8088/api/spread (전체조회)
GET http://localhost:8088/api/takeMoney/{token} (건별조회)
