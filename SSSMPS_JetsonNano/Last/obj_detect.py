import argparse
import time
from pathlib import Path

import json
import cv2
import torch
from numpy import random

import requests
from models.experimental import attempt_load
from utils.datasets import LoadImages
from utils.general import check_img_size, non_max_suppression, scale_coords, set_logging, increment_path
from utils.plots import plot_one_box
from utils.torch_utils import select_device, time_synchronized, TracedModel


def gstreamer_pipeline(
        capture_width=608,
        capture_height=608,
        display_width=608,
        display_height=608,
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


class Yolo:
    def __init__(self):
        parser = argparse.ArgumentParser()
        parser.add_argument('--weights', nargs='+', type=str, default='best.pt', help='model.pt path(s)')
        parser.add_argument('--source', type=str, default='source.jpg',
                            help='source')  # file/folder, 0 for webcam
        parser.add_argument('--img-size', type=int, default=608, help='inference size (pixels)')
        parser.add_argument('--conf-thres', type=float, default=0.6, help='object confidence threshold')
        parser.add_argument('--iou-thres', type=float, default=0.45, help='IOU threshold for NMS')
        parser.add_argument('--device', default='0', help='cuda device, i.e. 0 or 0,1,2,3 or cpu')
        parser.add_argument('--view-img', action='store_true', help='display results')
        parser.add_argument('--save-txt', action='store_true', help='save results to *.txt')
        parser.add_argument('--save-conf', action='store_true', help='save confidences in --save-txt labels')
        parser.add_argument('--nosave', action='store_true', help='do not save images/videos')
        parser.add_argument('--classes', nargs='+', type=int, help='filter by class: --class 0, or --class 0 2 3')
        parser.add_argument('--agnostic-nms', action='store_true', help='class-agnostic NMS')
        parser.add_argument('--augment', action='store_true', help='augmented inference')
        parser.add_argument('--update', action='store_true', help='update all models')

        parser.add_argument('--project', default='runs/detect', help='save results to project/name')
        parser.add_argument('--name', default='exp', help='save results to project/name')
        parser.add_argument('--exist-ok', action='store_true', help='existing project/name ok, do not increment')
        parser.add_argument('--no-trace', action='store_false', help='don`t trace model')
        opt = parser.parse_args()
        print(opt)

        self.source = opt.source
        self.weights = opt.weights
        self.view_img = opt.view_img
        self.save_txt = opt.save_txt
        self.imgsz = opt.img_size
        self.trace = not opt.no_trace
        self.device = opt.device
        self.name = opt.name
        self.exist_ok = opt.exist_ok
        self.project = opt.project
        self.model = None
        self.stride = None
        self.classes = opt.classes
        self.webcam = None
        self.save_img = None
        self.agnostic_nms = opt.agnostic_nms
        self.iou_thres = opt.iou_thres
        self.augment = opt.augment
        self.half = None
        self.conf_thres = opt.conf_thres
        self.names = ['코카콜라', '포카리스웨트', '아이시스', '꿀꽈배기', 'ABC초코쿠키', '고소미', '몽쉘', '빠다코코낫', '후렌치파이딸기', '후렌치파이사과', '꼬북칩',
                      '새우깡',
                      '초코빼빼로', '아몬드빼빼로', '누드초코빼빼로', '이클립스민트향', '이클립스스트로베리향', '마이쮸사과', '마이쮸포도', '해태맛동산']

        self.colors = [[random.randint(0, 255) for _ in range(3)] for _ in self.names]
        self.request_type = {"item": "/api/item", "quantity": "/api/stored/itemQuantity"}
        self.cap = None

    def setting(self):
        self.weights = "best.pt"
        # Initialize
        set_logging()
        self.device = select_device(self.device)
        self.half = self.device.type != 'cpu'  # half precision only supported on CUDA
        save_dir = Path(increment_path(Path(self.project) / self.name, exist_ok=self.exist_ok))  # increment run
        (save_dir / 'labels' if self.save_txt else save_dir).mkdir(parents=True, exist_ok=True)  # make dir
        # Load model
        self.model = attempt_load(self.weights, map_location=self.device)  # load FP32 model
        self.stride = int(self.model.stride.max())  # model stride
        self.imgsz = check_img_size(self.imgsz, s=self.stride)  # check img_size

        if self.trace:
            self.model = TracedModel(self.model, self.device, self.imgsz)

        if self.half:
            print("half 실행")
            self.model.half()  # to FP16
        if self.device.type != 'cpu':
            self.model(torch.zeros(1, 3, self.imgsz, self.imgsz).to(self.device).type_as(
                next(self.model.parameters())))  # run once

    def db_request(self, type, msg):
        url = "http://1.234.5.29:8080" + self.request_type.get(type) + f"?item_name={msg}"
        "http://1.234.5.29:8080/api/item?item_name=코카콜라"
        headers = {
            "Content-Type": "application/json; charset=utf-8"
        }
        response = requests.get(url, data="", headers=headers)

        if response.status_code == 200:
            print("Success Data Request")
            return response.text
        else:
            print("Error : ", response.status_code)

    def camera_set(self):
        # self.cap = cv2.VideoCapture(gstreamer_pipeline(), cv2.CAP_GSTREAMER)
        self.cap = cv2.VideoCapture(0)


    def camera_close(self):
        self.cap.release()

    def detect(self):

        if not self.cap.isOpened():
            print("닫힘..")
            return False
        print("Product detection")
        windows_title = "Product detection"
        cv2.namedWindow(windows_title, cv2.WINDOW_AUTOSIZE)
        ret, frame = self.cap.read()
        import numpy as np
        frame = cv2.add(frame, np.array([30.0]))
        cv2.imwrite("source.jpg", frame)
        if not ret:
            self.camera_close()
            self.camera_set()
            print("ret is false")
            return False

        # Run inference
        old_img_w = old_img_h = self.imgsz
        old_img_b = 1

        # Set Dataloader
        dataset = LoadImages(self.source, img_size=self.imgsz, stride=self.stride)

        # Get names and colorss

        # Run inference
        if self.device.type != 'cpu':
            self.model(torch.zeros(1, 3, self.imgsz, self.imgsz).to(self.device).type_as(next(self.model.parameters())))  # run once
        old_img_w = old_img_h = self.imgsz
        old_img_b = 1

        json_s = {}
        t0 = time.time()
        for path, img, im0s, vid_cap in dataset:
            img = torch.from_numpy(img).to(self.device)
            img = img.half() if self.half else img.float()  # uint8 to fp16/32
            img /= 255.0  # 0 - 255 to 0.0 - 1.0
            if img.ndimension() == 3:
                img = img.unsqueeze(0)

            # Warmup
            if self.device.type != 'cpu' and (
                    old_img_b != img.shape[0] or old_img_h != img.shape[2] or old_img_w != img.shape[3]):
                old_img_b = img.shape[0]
                old_img_h = img.shape[2]
                old_img_w = img.shape[3]
                for i in range(3):
                    self.model(img, augment=self.augment)[0]

            # Inference
            t1 = time_synchronized()
            with torch.no_grad():  # Calculating gradients would cause a GPU memory leak
                pred = self.model(img, augment=self.augment)[0]
            t2 = time_synchronized()

            # Apply NMS
            pred = non_max_suppression(pred, self.conf_thres, self.iou_thres, classes=self.classes,
                                       agnostic=self.agnostic_nms)
            t3 = time_synchronized()

            # Process detections
            for i, det in enumerate(pred):  # detections per image
                p, s, im0, frame = path, '', im0s, getattr(dataset, 'frame', 0)

                p = Path(p)  # to Path
                gn = torch.tensor(im0.shape)[[1, 0, 1, 0]]  # normalization gain whwh
                if len(det):
                    # Rescale boxes from img_size to im0 size
                    det[:, :4] = scale_coords(img.shape[2:], det[:, :4], im0.shape).round()

                    # Print results
                    for c in det[:, -1].unique():
                        n = (det[:, -1] == c).sum()  # detections per class
                        s += f"{n} {self.names[int(c)]}, "  # add to string

                    # Write results
                    for *xyxy, conf, cls in reversed(det):
                        if True:  # Add bbox to image
                            label = f'{self.names[int(cls)]} {conf:.2f}'
                            im0 = plot_one_box(xyxy, im0, label=label, color=self.colors[int(cls)], line_thickness=3)

                # Print time (inference + NMS)

                if cv2.getWindowProperty(windows_title, cv2.WND_PROP_AUTOSIZE) >= 0:
                    cv2.imshow(windows_title, im0)
                else:
                    break
                keyCode = cv2.waitKey(10) & 0xFF
                if keyCode == 27 or keyCode == ord('q'):
                    break
                for item in s.split(", "):
                    if item == "":
                        continue
                    count, name = item.strip().split(' ')

                    json_s[name] = f"{count}, {self.db_request('item', name)}"
                # Print time (inference + NMS)
                if not len(json_s):
                    print(f'Not found ({(1E3 * (t2 - t1)):.1f}ms) Inference, ({(1E3 * (t3 - t2)):.1f}ms) NMS')
                    return False
                print(f'{s}Done. ({(1E3 * (t2 - t1)):.1f}ms) Inference, ({(1E3 * (t3 - t2)):.1f}ms) NMS')
            return json.dumps(json_s, ensure_ascii=False)




        # t0 = time.time()
        # img = frame
        # print("img shape", img.shape)
        # import numpy as np
        # img = np.transpose(img, (2, 0, 1))
        # im0s = frame.copy()
        # img = torch.from_numpy(img).to(self.device)
        # img = img.half() if self.half else img.float()  # uint8 to fp16/32
        # img /= 255.0  # 0 - 255 to 0.0 - 1.0
        # if img.ndimension() == 3:
        #     img = img.unsqueeze(0)
        #
        # # Warmup
        # if self.device.type != 'cpu' and (
        #         old_img_b != img.shape[0] or old_img_h != img.shape[2] or old_img_w != img.shape[3]):
        #     old_img_b = img.shape[0]
        #     old_img_h = img.shape[2]
        #     old_img_w = img.shape[3]
        #     for i in range(3):
        #         self.model(img, augment=self.augment)[0]
        # # Inference
        # t1 = time_synchronized()
        # with torch.no_grad():  # Calculating gradients would cause a GPU memory leak
        #     pred = self.model(img, augment=self.augment)[0]
        # t2 = time_synchronized()
        #
        # # Apply NMS
        # pred = non_max_suppression(pred, self.conf_thres, self.iou_thres, classes=self.classes,
        #                            agnostic=self.agnostic_nms)
        # t3 = time_synchronized()
        # # Process detections
        # for i, det in enumerate(pred):  # detections per image
        #     s, im0, frame = '', im0s, img
        #
        #     gn = torch.tensor(im0.shape)[[1, 0, 1, 0]]  # normalization gain whwh
        #     if len(det):
        #         # Rescale boxes from img_size to im0 size
        #         det[:, :4] = scale_coords(img.shape[2:], det[:, :4], im0.shape).round()
        #
        #         # Print results
        #         for c in det[:, -1].unique():
        #             n = (det[:, -1] == c).sum()  # detections per class
        #             s += f"{n} {self.names[int(c)]}, "  # add to string
        #
        #         # Write results
        #         for *xyxy, conf, cls in reversed(det):
        #             if True:  # Add bbox to image
        #                 label = f'{self.names[int(cls)]} {conf:.2f}'
        #                 im0 = plot_one_box(xyxy, im0, label=label, color=self.colors[int(cls)], line_thickness=3)
        #
        #     print("im0 shape", im0.shape)
        #     if cv2.getWindowProperty(windows_title, cv2.WND_PROP_AUTOSIZE) >= 0:
        #         cv2.imshow(windows_title, im0)
        #     else:
        #         break
        #     keyCode = cv2.waitKey(10) & 0xFF
        #     if keyCode == 27 or keyCode == ord('q'):
        #         break
        #     for item in s.split(", "):
        #         if item == "":
        #             continue
        #         count, name = item.strip().split(' ')
        #
        #         json_s[name] = f"{count}, {self.db_request('item', name)}"
        #     # Print time (inference + NMS)
        #     if not len(json_s):
        #         print(f'Not found ({(1E3 * (t2 - t1)):.1f}ms) Inference, ({(1E3 * (t3 - t2)):.1f}ms) NMS')
        #         return False
        #     print(f'{s}Done. ({(1E3 * (t2 - t1)):.1f}ms) Inference, ({(1E3 * (t3 - t2)):.1f}ms) NMS')
        # return json.dumps(json_s, ensure_ascii=False)
