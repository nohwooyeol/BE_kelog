
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
- Infinite Scroll
- Login/SignUp
- Post CRUD
- Post Likes
- Comment CRUD
- Image Upload
- Image Resizing
- Dark&Light Mode
- Shared Url

### 주요 기능 설명
#### 1. Image Resizing
- `browser-image-compression` 패키지 사용한 이미지 용량 압축
  * 2021년 8월 패키지가 나온 이후로 (약 주 48,000회) 2022년 8월 현재(약 주 75,000회) : 사용량 계속적인 증가
  * 자바스크립트 기반으로 된 이미지 압축 라이브러리
  * 문서 간결, API 사용법 직관적
  * 계속적인 버전 릴리즈 업데이트
- 이미지 사이즈 줄임으로서 서버 부담 최소화 목적
- 네트워크 비용 효율화
- 데이터 로딩 속도 고려
- 실제 이미지 사이즈 줄인 사례
<img src="https://github.com/YooJinRa/FE_kelog/blob/posting/document/imageResizing.png" width="800" />

----

### 기본 세팅
#### Style
- color
```
--bg-color: #F8F9FA;
--subBg-color: #ffffff;
--primary-color: #12B886;
--title-color: #212529;
--text-color: #495057;
--subText-color: #868E96;
--border-style: 1px solid #F1F3F5;
--subBorder-style: 1px solid #DEE2E6;
```

#### Packages
- 아이콘 적용 : yarn add react-icons
- 이미지 리사이징 : yarn add browser-image-compression


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
- 조원영
  * 8/20 ~ 8/22 : 로그인/회원가입 - 유효성 검사 
- 김훈
