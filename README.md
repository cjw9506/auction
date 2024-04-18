# Auction - 실시간 경매 서비스

<br>

<div align="center">
<img src="https://img.shields.io/badge/Java-ED8B00?style=for-the-badge&logo=openjdk&logoColor=white"/></a>
<img src="https://img.shields.io/badge/Spring Boot -6DB33F?style=for-the-badge&logo=spring&logoColor=white"/></a>
<img src="https://img.shields.io/badge/Spring Security-6DB33F?style=for-the-badge&logo=spring-security&logoColor=white"/></a>
<img src="https://img.shields.io/badge/Spring Data JPA-gray?style=for-the-badge&logoColor=white"/></a>
<img src="https://img.shields.io/badge/Junit-25A162?style=for-the-badge&logo=JUnit5&logoColor=white"/></a>
</div>
<div align="center">
<img src="https://img.shields.io/badge/Redis-6DB33F?style=for-the-badge&logo=spring-security&logoColor=white"/></a>
<img src="https://img.shields.io/badge/WebSocket-ED8B00?style=for-the-badge&logo=openjdk&logoColor=white"/></a>
<img src="https://img.shields.io/badge/MySQL 8-4479A1?style=for-the-badge&logo=MySQL&logoColor=white"/></a>
<img src="https://img.shields.io/badge/GitHub-100000?style=for-the-badge&logo=github&logoColor=white"/></a>
<img src="https://img.shields.io/badge/JWT-black?style=for-the-badge&logo=JSON%20web%20tokens"/></a>
</div>

<br>

Auction은 실시간 경매 시스템입니다. 판매자가 물건과 금액을 책정하여 경매방을 만들고, 참여자들이 정해진 시간에 보다 높은 가격을 제시한 사람에게 구매 자격을 부여하는 거래체결방식입니다.
<br>
<br>

## 0. 목차
- [1.프로젝트 요구사항](#1-프로젝트-요구사항)
- [2.프로젝트 구조](#2-프로젝트-구조)
- [3.ERD](#3-erd)
- [4.동작예시](#4-동작예시)

## 1. 프로젝트 요구사항

- 사용자
  - 회원가입 및 로그인 기능 구현.
  - JWT를 이용한 사용자 인증.

- 경매방
  - 사용자의 경매방 생성.

- 경매 참여
  - 참여자들은 자유롭게 입찰 가능
  - 입찰에 대한 즉각적인 피드백   

- 경매품 낙찰
  - 정해진 시간에 최고 입찰 참여자 낙찰

## 3. 프로젝트 구조

<details>
    <summary>자세히</summary>

```
└── auction
    ├── AuctionApplication.java
    ├── auth
    │   ├── config
    │   ├── controller
    │   ├── domain
    │   ├── dto
    │   ├── filter
    │   ├── jwt
    │   ├── repository
    │   └── service
    ├── bid
    │   └── task
    ├── chat
    │   ├── controller
    │   ├── domain
    │   ├── dto
    │   ├── repository
    │   └── service
    ├── config
    │   ├── WebSocketConfig.java
    │   └── WebSocketEventListener.java
    └── room
        ├── controller
        ├── domain
        ├── dto
        ├── repository
        └── service

```

</details>

## 4. ERD
추가 예정
## 5. 동작예시
추가 예정
  
