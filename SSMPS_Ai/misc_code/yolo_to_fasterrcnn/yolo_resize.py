import cv2
import os
size = (416, 416)
path = r"C:\Users\freet\Desktop\detect2"
img_list = os.listdir(path)
for img_l in img_list:
    # print(f"{path}/{img_l}")
    img = cv2.imread(f"{path}/{img_l}")
    img = cv2.resize(img, size)
    cv2.imwrite(f"{path}/{img_l}", img)
    # cv2.imshow("img", img)
    # cv2.waitKey(0)
