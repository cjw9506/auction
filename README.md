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
- [1.아키텍처](#1-아키텍처)
- [2.프로젝트 요구사항](#2-프로젝트-요구사항)
- [3.프로젝트 구조](#3-프로젝트-구조)
- [4.ERD](#4-erd)
- [5.프로젝트 중 고려사항](#5-프로젝트-중-고려사항)

## 1. 아키텍처
<img width="497" alt="스크린샷 2024-11-19 오후 2 05 11" src="https://github.com/user-attachments/assets/3c9c8dbf-38e5-4803-b7f9-09c15abee67c">

## 2. 프로젝트 요구사항

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
<img width="315" alt="스크린샷 2024-11-19 오후 2 02 38" src="https://github.com/user-attachments/assets/0bc886b2-54e9-4e45-bc93-225bdadcd7e2">

## 5. 프로젝트 중 고려사항

### 1. WebSocket + Redis 구조 도입

Auction 프로젝트는 `가장 높은 가격을 먼저 제시한 사용자가 구매 자격`을 얻는 실시간 경매 시스템을 구현합니다.
이를 위해 다음과 같은 요구사항을 만족하도록 설계했습니다.

**주요 요구사항**

- 우선권 부여 : 가장 높은 자격을 제시한 사용자 중 **먼저 요청한 사용자**가 구매 권한을 가짐
- 실시간 가격 업데이트 : 경매 참여자는 **현재 상품의 가격을 즉시 확인**할 수 있어야 하며, 이를 통해 입찰 혼선을 방지
  
위 요구사항을 효과적으로 구현하기 위해 WebSocket과 Redis 조합을 사용했습니다.

#### 웹소켓을 활용한 실시간 경매 업데이트
경매에서 상품 가격은 `실시간으로 관리`되어야 합니다.
참여자가 이전 가격 정보를 보고 입찰할 경우, 이미 높아진 가격으로 인해 입찰 실패가 발생할 수 있어 사용자 경험이 저하됩니다. 
이를 해결하기 위해 WebSocket을 활용하여 서버에서 클라이언트로 실시간 가격 정보를 푸시하도록 구현했습니다.

- 구현방식 : STOMP 프로토콜을 사용하여 토픽과 구독을 관리하며, 경매 참가자들에게 실시간으로 가격 정보 전달

#### Redis를 활용한 효율적인 가격 관리

경매 상품의 가격은 입찰로 인해 순간적으로 `읽기와 쓰기 요청이 급증`하는 데이터입니다.
이를 RDBMS에서 처리할 경우 성능 병목이 발생할 수 있기 때문에, 캐시를 활용하여 성능을 최적화했습니다.

- 캐싱 전략 : Redis를 캐싱 계층으로 사용하여 빠르게 최고가를 확인하고 판별할 수 있도록 함
- DB의 부하를 줄이고, 경매 요청에 신속히 응답 하도록 함

위 구조를 도입하여 경매 시스템은 실시간성을 갖춘 안정적인 사용자 경험을 제공합니다.

### 2. 비동기 처리 방식 구현

Auction의 핵심 기능인 `입찰기능은 실시간성이 가장 중요`합니다. 입찰가가 변경되면 유저들에게 빠르게 보여주는 것이 중요합니다. 

기존의 구조는 각 메시지가 도착할 때까지 동기 방식으로 처리합니다. 처리가 완료될 때까지 다음 메시지를 처리하지 않기 때문에 한 번에 많은 메시지가 도착할 경우 처리 대기 시간이 길어집니다.

Java의 CompletableFuture를 활용하여 동기적으로 일어나지 않아도 되는 프로세스를 비동기로 처리하여 실시간성을 보완할 수 있었습니다.


#### 기존의 구조
<img width="514" alt="스크린샷 2024-11-19 오후 2 35 23" src="https://github.com/user-attachments/assets/deeb39c8-7429-46ab-b954-e060b7b18563">


#### 변경된 구조
<img width="786" alt="스크린샷 2024-11-19 오후 2 42 12" src="https://github.com/user-attachments/assets/cffc200c-41e1-4092-93bb-d59a7f95508f">


