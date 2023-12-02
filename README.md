# 예산 관리 어플리케이션

![1](https://github.com/soyeong125/budget-management/assets/57309311/4445a78d-e891-47b7-94ea-7eb497f5a910)
## 목차

1. [개발 기간](#개발-기간)
2. [기술 스택](#기술-스택)
3. [프로젝트 개요](#프로젝트-개요)
4. [프로젝트 일정관리](#프로젝트-일정관리)
5. [구현 기능 목록](#구현-기능-목록)
6. [ERD](#erd)
7. [구현 과정](#구현-과정)
8. [API 명세](#api-명세)
9. [테스트](#테스트)

## 개발 기간

2023-11-21 ~ 2023-12-03

## 기술 스택

<img src="https://img.shields.io/badge/spring-6DB33F?style=for-the-badge&logo=spring&logoColor=white" alt="spring"/> <img src="https://img.shields.io/badge/spring data jpa-6DB33F?style=for-the-badge&logo=spring&logoColor=white" alt="spring data jpa"/><img src="https://img.shields.io/badge/spring security-6DB33F?style=for-the-badge&logo=springSecurity&logoColor=white" alt="spring security"/> <img src="https://img.shields.io/badge/mysql-4479A1?style=for-the-badge&logo=mysql&logoColor=white" alt="mysql"/>

## 코드 컨벤션

코드 컨벤션 관한 노션 정리

https://mizuirohoshi7.notion.site/b298797bef954741bd7ab33a047ba01a

## 프로젝트 개요

예산을 관리하고, 지출을 추적하는 데 도움을 주는 애플리케이션입니다. 

이 앱은 사용자들이 예산을 설정할 수 있고, 지출을 모니터링하며 재무 목표를 달성하는 데 도움을 주고자 합니다.

## 프로젝트 일정관리

**Git Projects 사용**

https://github.com/users/soyeong125/projects/1/views/1

## 구현 기능 목록

* 유저
    * 회원가입
    * 로그인 및 로그아웃


* 예산
    * 예산 설정 
    * 예산 설계 추천


* 지출
    * 지출 CRUD

* 지출 컨설팅
    * 오늘 지출 추천 (설정한 월별 예산을 만족하기 위해 오늘 지출 가능한 금액 안내)
    * 오늘 지출 안내 (오늘 지출한 내용을 총액과 카테고리 별 금액을 안내)


## ERD

**Erd Cloud**

![image](https://github.com/soyeong125/budget-management/assets/57309311/5d738b73-9e9b-4515-8d63-0f44ea493630)

## 구현 과정

1. [프로젝트 환경 설정](https://github.com/soyeong125/budget-management/issues/1)
    * application.yml 설정
    * P6Spy 설정
    * RestDocs 설정
    * Response Api Format 설정
    * 공통 예외 처리


2. 유저 기능 구현
    * [회원가입](https://github.com/soyeong125/budget-management/issues/6)
    * [로그인](https://github.com/soyeong125/budget-management/issues/8)


3. 예산 기능 구현
    * [예산 설정](https://github.com/soyeong125/budget-management/issues/12)
    * [예산 설계 추천](https://github.com/soyeong125/budget-management/issues/12)


4. 지출
    * [지출 CRUD](https://github.com/soyeong125/budget-management/issues/18)


5. 지출 컨설팅
   * [오늘 지출 추천](https://github.com/soyeong125/budget-management/issues/19)
   * [오늘 지출 안내](https://github.com/soyeong125/budget-management/issues/23)

    

## API 명세

**Spring Rest Docs 기반 API 명세서**
* 사진 추가 예정

## 테스트

### ✅ 24/24 (2초 33ms)

* 사진 추가 예정

단위 테스트로 각 계층을 분리했습니다.
