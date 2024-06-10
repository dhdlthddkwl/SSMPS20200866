import requests
import json
import mysql.connector

import sqlite3
# url = "" # 주소
# data = {
#     "key": "value",
#     "key2": "value2"
# }

headers = {
    "Content-Type": "application/json"
}
#
# response = requests.post(url, data=json.dumps(data), headers=headers)
#
# if response.status_code == 201:
#     print("데이터 전송 완료")
# else:
#     print("에러 : ", response.status_code)

# MySQL 데이터베이스에 연결
connection = mysql.connector.connect(
    host="localhost",  # 데이터베이스 서버 주소 (로컬에서 실행 중인 경우)
    user="root",  # 데이터베이스 사용자 이름
    password="!dudnqlbn6600qe",  # 데이터베이스 사용자 비밀번호
    database="ssmps"  # MySQL Workbench에서 만든 데이터베이스 이름
)

# 커서 객체 생성
cursor = connection.cursor()

# SQL 쿼리 실행 (예: 테이블에서 모든 레코드를 선택)
cursor.execute("SELECT * FROM item")

# 결과 가져오기
rows = cursor.fetchall()

# 결과 출력
for row in rows:
    print(row)

# 연결 및 커서 닫기
cursor.close()
connection.close()
