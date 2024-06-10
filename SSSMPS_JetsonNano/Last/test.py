import cv2
import numpy as np

# BackgroundSubtractorMOG2 객체 생성
# history - 마지막 몇 프레임을 고려할 것인지
# varThreshold - 새로운 프레임과 배경 사이의 Mahalanobis 거리가 varThreshold보다 크면 해당 픽셀을 전경으로 판단
# detectShadows - 그림자 검출 여부. True로 설정하면 그림자를 검출하고, 그림자 부분은 회색으로 표시됨.
fgbg = cv2.createBackgroundSubtractorMOG2(varThreshold=16, detectShadows=True)

cap = cv2.VideoCapture(0)

while True:
    ret, frame = cap.read()
    bright_img = cv2.add(frame, np.array([30.0]))

    cv2.imshow('frame', frame)
    cv2.imshow('bright_img', bright_img)

    k = cv2.waitKey(30) & 0xff
    if k == 27:
        break

cap.release()
cv2.destroyAllWindows()