﻿# SmartClass
 김포고등학교 스마트 클래스 프로젝트의 소스 코드입니다.
 이 프로젝트는 김포고등학교 과학융합 프로젝트이자 동시에 자율동아리 TechStudio의 프로젝트입니다.

## 이용 안내
- app/src/main/ 에서 소스 코드를 확인하실 수 있습니다.
  - app/src/main/java/kr/hs/gimpo/smartclass/ : 본 어플리케이션의 소스 코드
  - app/src/main/java/org/hyunjun/school : [School API](https://github.com/agemor/school-api)의 소스 코드(일부 변경됨)

 이 Repository를 clone하신 후에는 Android Studio를 통해 열어보실 수 있습니다.

## [변경 기록(Changelog)](Changelog.md)

 현재 최신 버전: 3.5.1-beta (2018-06-17 기준)<br>
 [최신 버전 다운로드](app/beta/release/app-beta-release.apk)

- 3.5.1-beta 변경 사항 (2018-06-17)
  - 수정된 사항
    - 기능 추가: 영어, 일본어 번역이 제공됩니다! 이제 시스템 언어가 '영어' 또는 '일본어'인 기기에서는 각각 영어와 일본어로 어플리케이션 정보가 제공됩니다!
    - 기능 개선: '메인' 화면의 레이아웃이 개선되었습니다.
    - 기능 개선: '메인' 화면의 '학교 주변 대기상태' 란의 데이터 업데이트 매커니즘이 개선되었습니다.
    - 기능 개선: '인트로' 화면의 데이터 업데이트 매커니즘이 개선되었습니다.
    - 기능 개선: '메인' 화면의 속도가 향상되었습니다!
    - 버그 수정: '급식 정보' 화면을 오프라인에서 사용할 수 없던 버그를 수정하였습니다.
    - 버그 수정: '급식 정보' 화면에서 날짜를 바꾸었을 때 반응이 느리거나 아예 데이터를 불러올 수 없었던 버그를 수정하였습니다.
    - 버그 수정: '메인' 화면에서 업데이트 공명이 잘못 일어나 한 사람이 '학교 주변 대기상태'를 선택하면 다른 사람에게도 '학교 주변 대기상태'가 표시되는 버그를 수정하였습니다.
    - 기타 개선: 이제 어플리케이션과 소스 코드를 MIT 라이선스로 이용하실 수 있습니다.
    - 기타 개선: 급식 데이터가 2018년 5월부터 연도별/월별로 저장됩니다. 향후 급식 메뉴 빈도분석 등의 기능이 추가될 수 있습니다!
  - 알려진 버그
    - 없음.

## Reference

- 미세먼지 자료<br>
 본 어플리케이션에서는 환경부와 한국환경공단이 제공하는 대기오염정보서비스에서 받아온 자료를 제공해 드립니다.<br>
 본 어플리케이션에서 제공하는 자료는 실시간으로 관측된 정보이며, 현지사정이나 수신 상태에 의해 차이가 발생할 수 있습니다.

- 학사일정 자료<br>
 본 어플리케이션은 MIT 라이선스에 따라 [School API](https://github.com/agemor/school-api)를 사용하여 교육행정정보시스템(NEIS, 나이스) 학생서비스에서 공개하는 학교별 월간일정 자료를 제공해 드립니다.<br>
 본 어플리케이션에서 제공하는 자료는 수신 상태에 따라 차이가 발생할 수 있습니다.

## 프로젝트 기여
 
 본 프로젝트에 기여하시고자 하는 경우 [이 문서](Contribute.md)를 반드시 읽어 보시기 바랍니다.

## [라이선스(Licesne)](LICENSE)
 
## 면책 조항

 본 어플리케이션은 김포고등학교에서 공식으로 제작한 어플리케이션이 아닙니다. 본 어플리케이션을 사용함으로써 발생할 수 있는 모든 불이익에 대해 이 어플리케이션의 개발자는 책임을 지지 않습니다. 사용자는 본 어플리케이션을 사용함으로써 본 어플리케이션이 잘못된 정보를 제공할 수 있다는 사실과 이로 인해 일어날 수 있는 불이익을 명백히 인지하였으며, 이에 대한 책임이 사용자에게 있음에 동의한 것으로 간주합니다.
