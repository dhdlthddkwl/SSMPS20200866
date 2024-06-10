import json
import socket
import threading
import time

import requests

import face_detection
import obj_detect


def gstreamer_pipeline(
        capture_width=640,
        capture_height=640,
        display_width=640,
        display_height=640,
        framerate=10,
        flip_method=0,
):
    return (
            "nvarguscamerasrc ! "
            "video/x-raw(memory:NVMM), "
            "width=(int)%d, height=(int)%d, framerate=(fraction)%d/1 ! "
            "nvvidconv flip-method=%d ! "
            "video/x-raw, width=(int)%d, height=(int)%d, format=(string)BGRx ! "
            "videoconvert ! "
            "video/x-raw, format=(string)BGR ! appsink drop=True"
            % (
                capture_width,
                capture_height,
                framerate,
                flip_method,
                display_width,
                display_height,
            )
    )


class Server:
    def __init__(self):
        self.img = None
        self.connections = []
        self.begin = False
        self.end = False
        self.request_type = {"item": "/api/item", "quantity": "/api/stored/itemQuantity"}

    def get_connect(self):
        return self.connections

    def get_begin(self):
        return self.begin

    def get_end(self):
        return self.end

    def db_request(self, type, msg):
        url = "http://1.234.5.29:8080" + self.request_type.get(type)
        "http://1.234.5.29:8080/api/item?item_name=코카콜라"
        headers = {
            "Content-Type": "application/json; charset=utf-8"
        }

        data = {
            "itemName": f"{msg[0]}",
            "quantity": msg[1],
            "storeName": f"{msg[2]}"
        }
        print(data)

        response = requests.put(url, data=json.dumps(data), headers=headers)

        if response.status_code == 200:
            print("Success Data Request")
            return response.text
        else:
            print("Error : ", response.status_code)

    def binder(self, client_socket, addr):
        print('connected : ', addr)
        try:
            while True:
                data = client_socket.recv(4)
                length = int.from_bytes(data, "little")
                data = client_socket.recv(length)
                msg = data.decode()
                if msg == "":
                    continue
                print('Response message : ', addr, msg)

                try:
                    msg_dict = json.loads(msg)
                except json.JSONDecodeError:
                    print("Does not Json Type")
                    continue

                if msg_dict.get("type") == "start":
                    print("start")
                    msg = "ok"
                    self.begin = True
                elif msg_dict.get("type") == "get":
                    print("end")
                    msg_dict.pop("type")
                    for key, value in msg_dict.items():
                        msg = [key]
                        cnt = value.split(":")[0]
                        msg.append(cnt)
                        msg.append("GS25 천안대점")
                        self.db_request("quantity", msg)
                    self.begin = False
                    self.end = False
                    break

                else:
                    print("other")
                    msg = "get : " + msg

                data = msg.encode()
                length = len(data)
                client_socket.sendall(length.to_bytes(4, byteorder='little'))
                client_socket.sendall(data)
        except Exception as e:
            print("except : ", addr, e)
        finally:
            self.connections.remove(client_socket)
            client_socket.close()

    def send_message_to_all(self, msg):
        for client_socket in self.connections:
            try:  # try-except block to handle the exception
                data = msg.encode()
                length = len(data)
                client_socket.sendall(length.to_bytes(4, byteorder='little'))
                client_socket.sendall(data)
                print(f"send message : {msg} , length : {length}")
            except BrokenPipeError:
                print("Broken Pipe Error: The client may be disconnected")
                self.connections.remove(client_socket)
            except ConnectionAbortedError:
                print("ConnectionAbortedError")

    def start(self):
        server_socket = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
        ipget_socket = socket.socket(socket.AF_INET, socket.SOCK_STREAM)

        # 소켓 레벨과 데이터 형태를 설정한다.
        server_socket.setsockopt(socket.SOL_SOCKET, socket.SO_REUSEADDR, 1)
        ipget_socket.connect(("pwnbit.kr", 443))
        server_socket.bind((ipget_socket.getsockname()[0], 12345))

        server_socket.listen(1)

        try:
            print("connect success")
            while True:
                client_socket, addr = server_socket.accept()
                self.connections.clear()
                self.connections.append(client_socket)
                th = threading.Thread(target=self.binder, args=(client_socket, addr))
                th.start()
        except:
            print("server")

    def detect_face(self):
        while True:
            if face_detection.face_detect() or self.begin:
                self.send_message_to_all("start")
                print("얼굴인식 : 결제 시작")
                self.begin = False
                self.end = True
                return

    def detect_object(self, obj):
        obj.camera_set()
        while True:
            if self.end:
                data = obj.detect()
                if type(data) == bool:
                    continue
                self.send_message_to_all(data)
                time.sleep(6)
            else:
                print("종료됨")
                obj.camera_close()
                return


if __name__ == "__main__":
    end_chk = False

    server = Server()
    # 각 작업을 스레드로 분리
    server_thread = threading.Thread(target=server.start)
    server_thread.start()

    obj = obj_detect.Yolo()
    print("Yolo 생성 완료")
    obj.setting()
    print("Yolo 세팅 완료")

    while True:
        if len(server.get_connect()) == 0:
            print("입력 대기중..")
            time.sleep(2)
            continue
        server.detect_face()
        print("face detect 실행")
        if server.get_end():
            print("obj.detect start ")
            server.detect_object(obj)

        # 스레드 시작
