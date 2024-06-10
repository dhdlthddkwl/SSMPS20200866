import socket, threading
import json
import requests
import face_detection
import detect

class Server:
    def __init__(self):
        self.connections = []

    def db_request(self, msg):
        url = ""
        headers = {
            "Content-Type": "application/json"
        }

        response = requests.post(url, data=json.dumps(msg), headers=headers)

        if response.status_code == 201:
            print("Success Data Request")
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
                else:
                    print("other")
                    msg = "echo : " + msg

                data = msg.encode()
                length = len(data)
                client_socket.sendall(length.to_bytes(4, byteorder='little'))
                client_socket.sendall(data)
                print(f"send message : {msg} {length}")
        except Exception as e:
            print("except : ", addr, e)
        finally:
            self.connections.remove(client_socket)
            client_socket.close()

    def send_message_to_all(self, msg):
        for client_socket in self.connections:
            print("client socket : ", client_socket)
            data = msg.encode()
            length = len(data)
            client_socket.sendall(length.to_bytes(4, byteorder='little'))
            client_socket.sendall(data)
            print(f"send message : {msg} , length : {length}")

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
                self.connections.append(client_socket)
                th = threading.Thread(target=self.binder, args=(client_socket, addr))
                th.start()
        except:
            print("server")
        finally:
            server_socket.close()

    def detect_face(self):
        if face_detection.face_detect():
            self.send_message_to_all("start")
            print("send success")



if __name__ == "__main__":
    server = Server()
    # 각 작업을 스레드로 분리
    server_thread = threading.Thread(target=server.start)
    server_thread.start()

    while True:
        detect_thread = threading.Thread(target=server.detect_face)
        detect_thread.start()
        detect_thread.join()
        detect.detect()
        # 스레드 시작
