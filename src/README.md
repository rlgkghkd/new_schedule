# newSchedule

스케줄 관리 API입니다.
+ 스케줄 관리
    + 생성
    + 삭제
    + 수정
    + 조회
      + 스케줄 답글
        + 생성
        + 삭제
        + 수정
        + 조회
+ 유저 관리
    + 생성
    + 삭제
    + 수정
    + 조회
 
의 기능을 지원합니다.

스케줄은 사용자에 종속됩니다.
답글은 스케줄에 종속됩니다.

JWT방식을 통해 로그인하며
유저 생성 및 로그인을 제외한 기능은 토큰으로 인증되어야만 이용 가능합니다.
토큰은 signIn에서 JSON 형태로 반환되며
받은 토큰을 요청 헤더의 Authorization 항목에 넣어 전달해야 합니다.

---

## API 명세

| 기능           | Method | URL                          | Request                                                                             | Response                                                                                                                                        | 상태코드       | 토큰 필요 여부 |
|--------------|--------|------------------------------|-------------------------------------------------------------------------------------|-------------------------------------------------------------------------------------------------------------------------------------------------|------------|----------|
| 스케줄 생성       | POST   | /schedules                   | {<br>"title":"스케줄 제목",<br>"contents":"스케줄 내용"}                                      | {<br>"id": 스케줄 id,<br>"title":"스케줄 제목",<br>"contents":"스케줄 내용",<br>"userName":"스케줄 소유자 이름",<br>}                                                | 201: Created | O        |
| 스케줄 전체 검색    | GET    | /schedules?index=            | 요청 파라미터                                                                             | [<br>{<br>"id":스케줄 id,<br>"title":"스케줄 제목",<br>"contentes":"스케줄 내용",<br>"userName":"스케줄 소유자 이름"<br>}<br>] <br><br>다수 응답                         | 302: Found | O        |
| 유저 소유 스케줄 검색 | GET    | /schedules/user?index=       | 요청 파라미터                                                                             | [<br>{<br>"id":스케줄 id,<br>"title":"스케줄 제목",<br>"contentes":"스케줄 내용",<br>"userName":"스케줄 소유자 이름"<br>}<br>] <br><br>다수 응답                         | 302: Found | O        |
| 스케줄 단건 검색    | GET    | /schedules/{scheduleId}      | 요청 파라미터                                                                             | {<br>"id":스케줄 id,<br>"title":"스케줄 제목",<br>"contentes":"스케줄 내용",<br>"userName":"스케줄 소유자 이름"<br>}                                                 | 302: Found | O        |
| 스케줄 수정       | PATCH  | /schedules/{scheduleId}      | {<br>"title":"수정할 스케줄 제목",<br>"contents":"수정할 스케줄 내용"<br>}<br><br>수정하지 않을 항목은 생략 가능 | -                                                                                                                                               | 200: OK    | O        |
| 스케줄 삭제       | DELETE | /schedule/{scheduleId}       | -                                                                                   | -                                                                                                                                               | 200: OK    | O        |
| 댓글 생성        | POST   | /comments                    | {<br>"comments":"댓글 내용",<br>"scheduleId":"스케줄 아이디"}                                 | {<br>"id": 댓글 id,<br>"contents":"댓글 내용",<br>"commentedUser":"댓글 작성자 이름",<br>"scheduleCommentedOn":"댓글 작성한 스케줄 이름",<br>}                         | 201: Created | O        |
| 스케줄 기반 댓글 검색 | GET    | /comments/{scheduleId}?index= | 요청 파라미터                                                                             | [<br>{<br>"id": 댓글 id,<br>"contents":"댓글 내용",<br>"commentedUser":"댓글 작성자 이름",<br>"scheduleCommentedOn":"댓글 작성한 스케줄 이름",<br>}<br>] <br><br>다수 응답 | 302: Found | O        |
| 댓글 수정        | PATCH  | /comments/{commentId}       | {<br>"contents":"수정할 댓글 내용"<br>}                         | -                                                                                                                                               | 200: OK    | O        |
| 댓글 삭제        | DELETE | /comments/{commentId}       | -                                                                                   | -                                                                                                                                               | 200: OK    | O        |
| 유저 생성        | POST   | /users                       | {<br>"name":"유저이름",<br>"mail":"유저 이메일",<br>"password":"패스워드"<br>}                   | {<br>"id":유저 Id,<br>"name":"유저이름",<br>"mail":"유저 이메일"<br>}                                                                                      | 201: Created | X        |
| 유저 로그인       | POST   | /users/signIn                | {<br>"password": "유저 패스워드"<br>}                                                     | {<br>"grantType":"Bearer",<br>"accessToken":"엑세스 토큰",<br>"refreshToken":"리프레쉬 토큰"<br>}                                                          | 200: OK    | X        |
| 유저 검색        | GET    | /users/{userId}              |                                                                                     | {<br>"id": 유저id,<br>"name": "유저이름",<br>"mail": "유저 메일"}                                                                                         | 302: Found | O        |
| 유저 비밀번호 수정   | PATCH  | /users/password              | {<br>"oldPassword":"구 패스워드",<br>"newPassword":"신 패스워드"<br>}                         | -                                                                                                                                               | 200: OK    | O        |
| 유저 메일 수정     | PATCH  | /users/password              | {<br>"password":"패스워드",<br>"mail":"변경할 메일 주소"<br>}                                  | -                                                                                                                                               | 200: OK    | O        |
| 유저 삭제        | DELETE | /users                       | {<br>"password": "유저 패스워드"<br>}                                                     | -                                                                                                                                               | 200: OK    | O        |
| 토큰 만료/에러     | All    | All                          | -                                                                                   | -                                                                                                                                               | 500: Internal Server Error      | -        |        |


## ERD

---

## 기술 스택
+ Spring boot
+ Thymeleaf
+ Java
+ Github
+ MySQL
