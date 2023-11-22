# 💻 PeerNow - BE
![제목을 입력해주세요_-001 (1)](https://github.com/ystgd07/testReact2/assets/134909932/1c474c4c-a9e3-4f80-b7ce-29f366eeb72a) 

## 🧐 배경

 - 여러 프로젝트를 진행하면서, Jira와 노션 등 여러 프로젝트 협업 툴을 경험해면서 사용했던 협업 툴에서 현재 진행중인 프로젝트 정보를 한 눈에 보기 어려운 단점이 존재
 - 그래서 누구나 쉽게 프로젝트 관리를 할 수 있으며 프로젝트가 끝난 후 동료평가, 피드백, 권한 부여를 할 수 있는 새로운 서비스의 프로젝트 관리 툴을 생각해보았다.

## ⏰프로젝트 기간
<b>2023.10.23 ~ 2023.11.20</b>
최종 merge : 2023.11.22()

## 👨‍👩‍👧‍👦팀원소개
<table>
  <tbody>
    <tr>
      <td align="center">
https://github-production-user-asset-6210df.s3.amazonaws.com/134909932/284762909-1e63d620-482f-4003-b7d7-253d1c5c95fe.png)<br /><sub><b>FE & INFRA : 양성수</b></sub><br /></td>
      <td align="center">![이슬비](https://github.com/ystgd07/testReact2/assets/134909932/85e99ef7-4891-4808-8474-25e6b4017091)"><br /><sub><b>FE & INFRA : 이슬비</b></sub><br /></td>
      <td align="center">![정현욱](https://github.com/ystgd07/testReact2/assets/134909932/85e99ef7-4891-4808-8474-25e6b4017091)"><br /><sub><b>FE & INFRA : 정현욱</b></sub><br /></td>
      <td align="center">![최현희](https://github.com/ystgd07/testReact2/assets/134909932/85e99ef7-4891-4808-8474-25e6b4017091)"><br /><sub><b>FE & INFRA : 최현희</b></sub><br /></td>      
    </tr>
  </tbody>
</table>

### 🖼 시연영상
<img src="https://github.com/n0hack/readme-template/assets/112933943/5e39ccca-699f-4c53-9328-f9baef9d2973">
</img>

## 🔧기술스택
### 🎨Back-End
 ![image](https://github.com/ystgd07/testReact2/assets/112933943/d19322cf-abc0-43ce-b222-40fa3a424fb2)

### 🌐INFRA
<h3> 3tier Architecture</h3>

![image](https://github.com/ystgd07/testReact2/assets/112933943/2009523c-7321-486c-9d87-20e804e5d74c)

<h3>CI/CD 무중단배포 파이프라인</h3>


![image](https://github.com/ystgd07/testReact2/assets/112933943/0483480f-212a-427a-8a50-505fa7bf84a0)

## 💡주요기능

![image](https://github.com/ystgd07/testReact2/assets/112933943/87769f67-09f7-41a1-8bbd-5ac49d5c2b17)

![image](https://github.com/ystgd07/testReact2/assets/112933943/921da483-9442-4906-b97f-fa068a8330ea)

![image](https://github.com/ystgd07/testReact2/assets/112933943/fc304224-bc92-4aa3-bcdb-d2a94ce46b1d)

![image](https://github.com/ystgd07/testReact2/assets/112933943/5d022c8b-c397-476c-8954-71919abedfcf)

## 📜Back-End 주요 기술

### ⚙️ Spring Security + JWT 
![JWT](https://github.com/SNOW-bespinPjt/RPeerNow/assets/134913262/e12bf9f7-7a46-4090-ae80-7dbbcd4ca005)

> 리엑트를 이용한 CSR방식으로 웹 애플리케이션을 제작을 진행해보며,
> 페이지 갱신이나 성호 작용마다 서버에 잦은 요청을 하는데, 이때 세션 상태를 지속적으로 전달하는 것은 비효율적
> 그래서 서버에 상태를 저장하지 않는 Stateless한 특성을 가진 JWT를 인증방법으로 채택
- Spring Security를 통해 Request 요청 permit 설정, 권한별 제어를 진행 및 예외 처리 설정
- OncePerRequestFilter를 상속받아 Dispatcher Servlet 이전 Filter를 통해 JWT 토큰 인증 및 SecurityContextHolder를 이용해 유저 정보 저장

 ### ⚙️ Exception Handler 
![exception](https://github.com/SNOW-bespinPjt/RPeerNow/assets/134913262/35659345-711a-42f0-a2eb-5185d03f770c)

> 예외 메시지를 공통되고 일관된 메시지로 전달해 쉽게 문제를 파악하며, 예외발생 시 예상치 못한 동작들을 처리하도록 예외 처리를 진행해봄
- @RestControllerAdvice를 이용해 전역적으로 예외처리를 진행, ExceptionHandler를 통해 Exception 클래스들을 속성으로 받아 예외를 지정
- Logback-xml에 롤링 정책을 세워 디렉터리에 로그 저장, 삭제 등 생명주기 관리
  
### ⚙️ Transaction
![image](https://github.com/SNOW-bespinPjt/RPeerNow/assets/134913262/a8ee74fc-aa57-4e4a-95c1-173cb22de297)

> 프로젝트 시나리오 상 프로젝트 서비스를 삭제하게 되면, 하위 스프린트, 백로그 등의 기능들이 동시에 삭제되어야 하는 구조
- 원자성 보장을 위해 하나의 연산이라도 오류 발생시 전체 롤백 시키는 트랜잭션 어노테이션을 사용

### ⚙️ 공통 모듈
![image](https://github.com/SNOW-bespinPjt/RPeerNow/assets/134913262/0ef82dac-20a9-4f62-9311-2c76bd73541e)
> API 명세서를 작성하면서 클라이언트에 체계적이고 일관된 메시지 전달할 필요성을 느낌
- Request에 대한 Response를 공통응답 객체를 만들어서 사용
- 이러한 response 객체로 프론트와의 소통이 한층 편해지며 생산성이 높아짐

### ⚙️ AWS S3
![image](https://github.com/SNOW-bespinPjt/RPeerNow/assets/134913262/68905490-d820-4bee-a3a6-48cc9764da96)
> s3의 객체를 public하게 열어두면 인가되지 않는 사용자들이 해당 객체를 다운로드 할 수 있는 문제 존재
- 백엔드 인스턴스에 IAM Role을 부여 및 s3 객체에 접근하는 로직을 구현하여 인가된 사용자만 백엔드를 통해서 해당 객체에 접근



