
# Project "Kelog"

## Introduction of Project
### What
- [velog](https://velog.io/) Clone Coding Project

### When
- 2022.08.20 ~ 

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
       -

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
  * 8/23 ~ 8/25 : 예외처리/깃액션
- 조원영
  * 8/20 ~ 8/22 : 로그인/회원가입 - 유효성 검사 
- 김훈
