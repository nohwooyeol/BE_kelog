
# Project "Kelog"

## Introduction of Project
### What
- [velog](https://velog.io/) Clone Coding Project

### When
- 2022.08.19 ~ 2022.08.25

### Who
- FE : 김주형, 나유진
- BE : 노우열, 김훈, 조원영

### How
- FE : ReactJS
- BE : Spring

### Why

----

### 주요 기능 구현


### 주요 기능 설명
#### 1. paging
       - 사용 이유 : 프론트에서 요구하는 infinite scrolling 기능을 위해 사용함
       - 기능 설명 : 설정한 한페이지에 들어갈 정보량과 원하는 페이지를 받을 수 있게 한다.
#### 2. JWT-Token
       - 사용 이유 : 사용자의 인증과 인증상태 유지를 위해 사용하며, 토큰을 통해 사용자의 정보를 얻기위해 사용함
       - 기능 설명 : 토큰을 통해 사용자의 인증상태를 확인하고, 토큰을 통해 현재 사용자의 정보를 확인한다.
#### 3. Git-Action
       - 사용 이유 : 서버 실행 상태를 유지한 채, 서버의 변경사항을 적용하기 위해 사용한다.
       - 기능 설명 : 깃허브에서 파일을 빌드하여, S3에 전송하고 그 파일을 CodeDeploy를 통하여 EC2 서버에 업로드하여 실행한다.

----

### 기본 세팅

#### Packages
<pre>
<code>
Controller
├── /MemberController  /PostController /CommentController /HeartController
Cors
├── /WebConfig
Domain
├── /Member /Post /Comment /Heart /Tags /TimeStamped
Filter
├── /AppConfig /HtmlCharacterEscapes
Repository
├── /MemberRepository /PostRepository /CommentRepository /TagsRepository /HeartRepository
Request
├── /CommentRequestDto /IdCheckDto /LoginDto /PostRequestDto /SignupRequestDto
Response
├── /CommentCoutnResponseDto /CommentResponseDto /MemberResponseDto /PostAllByMemberResponseDto /PostAllByResponseDto /PostResponseDto /ResponseDto
Security
├── /SecurityConfiguration /UserDetailsImpl /userDetailsServiceImpl //JWT/JwtConfiguration /JWT/JwtFilter /JWT/TokenProvider 
Service
├── /MemberService /PostService /CommentService /HeartService 
Shared
├── /Authority /CommonUtils /Swaggerconfig
Tag
├── /TagNameAndCount /TagResponseDto
Util
├── /CheckUtil /UserCheck
</code>
</pre>

### Schedule
#### FE
- 김주형
  * 8/20 ~ : 로그인/회원가입 모달
- 나유진
  * 8/20 ~ 8/21 : 게시글 등록(이미지 업로드, 이미지 리사이징, 태그 추가/삭제 기능), 게시글 등록 서버 테스트
  * 8/21 ~ : 게시글 상세 페이지
  * !

#### BE
- 노우열
  * 8/20 ~ 8/22 : 댓글CRUD/XXS Filter  
  * 8/23 ~ 8/25 : 예외처리/깃액션/프론트엔드와 협업
- 조원영
  * 8/20 ~ 8/22 : 로그인/회원가입 - 유효성 검사 
  * 8/23 ~ 8/23 : 좋아요 
  * 8/24 ~ 8/25 : 서버관리/프론트엔드와 협업
- 김훈
  * 8/20 ~ 8/21 : 게시글 CRUD
  * 8/22 ~ 8/25 : 게시글 카테고리/프론트엔드와 협업
