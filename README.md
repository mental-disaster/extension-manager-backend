# Extension Policy Backend

파일 확장자 차단 정책을 관리하는 Kotlin Spring Boot 기반 백엔드 서비스입니다.  
고정 확장자 차단 여부 설정과 사용자 지정 확장자 추가/삭제 기능을 제공합니다.



## 기술 스택

 - Java 21
 - Kotlin 1.9
 - Spring Boot 3.5.7  
 - SQLite 3.51



## 주요 기능

고정 확장자 관리
 - exe, sh, bat 등 사전 정의된 확장자 목록 제공
 - 각 확장자에 대해 차단 여부 활성/비활성 설정

커스텀 확장자 관리
 - 사용자 커스텀 확장자 관리
 - 확장자 정규화(소문자, 선행 점 제거, 허용 문자 제한, 길이 20 이하)
 - 중복 방지
 - 최대 200개



## 설치 및 구동 방법

1. **사전 준비**
   - JDK 21 설치
 
2. **의존성 다운로드 및 빌드**
   ```bash
   ./gradlew clean build
   ```

3. **애플리케이션 실행**
   ```bash
   ./gradlew bootRun
   ```

4. **테스트 실행(선택)**
   ```bash
   ./gradlew test
   ```
