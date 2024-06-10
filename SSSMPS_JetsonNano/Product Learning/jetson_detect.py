import torch
from models.experimental import attempt_load
from utils.general import non_max_suppression, scale_coords, check_img_size
from utils.torch_utils import select_device
import cv2
import numpy as np
import random

def detect(save_img=False):
    # Initialize
    device = select_device('0')
    half = device.type != 'cpu'  # half precision only supported on CUDA

    # Load model
    model = attempt_load('yolov7.pt', map_location=device)  # load FP32 model
    imgsz = check_img_size(640, s=model.stride.max())  # check img_size
    if half:
        model.half()  # to FP16

    # Set Dataloader
    vid_path, vid_writer = None, None
    dataset = LoadImages('image.jpg', img_size=imgsz)

    # Get names and colors
    names = ['코카콜라', '포카리스웨트', '아이시스', '꿀꽈배기', '초코쿠키', '고소미', '몽쉘', '빠다코코낫', '후렌치파이딸기', '후렌치파이사과', '꼬북칩', '새우깡', '초코빼빼로', '아몬드빼빼로', '누드초코빼빼로', '이클립스민트향', '이클립스스트로베리향', '마이쮸사과', '마이쮸포도', '해태맛동산']
    colors = [[random.randint(0, 255) for _ in range(3)] for _ in names]

    # Run inference
    img = torch.zeros((1, 3, imgsz, imgsz), device=device)  # init img
    _ = model(img.half() if half else img) if device.type != 'cpu' else None  # run once

    for path, img, im0s, vid_cap in dataset:
        img = torch.from_numpy(img).to(device)
        img = img.half() if half else img.float()  # uint8 to fp16/32
        img /= 255.0  # 0 - 255 to 0.0 - 1.0
        if img.ndimension() == 3:
            img = img.unsqueeze(0)

        # Inference
        pred = model(img, augment=False)[0]

        # Apply NMS
        pred = non_max_suppression(pred, 0.4, 0.5, classes=None, agnostic=False)

        # Process detections
        for i, det in enumerate(pred):  # detections per image
            p, s, im0 = path, '', im0s.copy()

            if len(det):
                # Rescale boxes from img_size to im0 size
                det[:, :4] = scale_coords(img.shape[2:], det[:, :4], im0.shape).round()

                # Write results
                for *xyxy, conf, cls in reversed(det):
                    label = f'{names[int(cls)]} {conf:.2f}'
                    plot_one_box(xyxy, im0, label=label, color=colors[int(cls)], line_thickness=3)

        cv2.imshow("Image", im0)
        cv2.waitKey(0)
        cv2.destroyAllWindows()

detect()
