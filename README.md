# MJ
Mobile Project

#내가 추가한 기능
    권한 체크 및 부여
    디렉토리, 파일 검색 기능
    뒤로가기 키 동작
    권한체크 부여 메소드 별도의 클래스로 분리
    파일 관련 메소드 별도의 클래스로 분리
    onCreateOptionsMenuItem(Menu menu)별도의 클래스로 분리
    폴더 우선 정렬 체크박스 만듦
    확장자 확인 처리 기능
    파일 삭제 기능 수정
    saf로 파일 삭제 기능
    




5/16
permission.java 파일 추가
PermissionActivity 추가로 만들어서 거기서 권한 가져오기 기능 추가할 예정
추가로 다시묻지않음 또는 거부 버튼 눌럿을때는 권한요청해도 권한요청창 안뜨니깐 그부분 따로 코딩해서 앱 설정 페이지 접속해서
사용자가 권한 부여하도록 코딩해야 할듯

5/19
권한 부여 안될 때 스낵바로 권한 설정창 접근해서 권한 부여하도록 구현

5/20
퍼미션 권한 부여 클래스 분리
검색 기능 추가

5/21
난잡하거나 안쓰는 import 정리

5/22
refresh()메소드 별도의 클래스 파일로 분리
퍼미션창에서 뒤로가기키 2번연타 시 앱종료 구현

5/23
뒤로가기키 관련 동작 구현
정렬기능 제대로 동작 안되는거 고침
경고 뜬거 손보고 안쓰는 변수들 삭제
툴바 오버플로우 메뉴에 폴더 우선 정렬기능 추가 
onCreateOptionsMenuItem(Menu menu) 메소드 별도의 클래스 파일로 분리

6/2
확장자 확인 처리 구현
삭제기능 최적화
롱클릭 시 배경색 변경 구현

6/3
API 29이상일 때 SAF로 파일 권한 부여 후 파일 삭제 처리 구현
