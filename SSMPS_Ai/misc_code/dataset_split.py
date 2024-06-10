import os
import shutil
from sklearn.model_selection import train_test_split

# 이미지 카테고리 나누기

path = r'C:\Users\freet\Documents\GitHub\SSMPS_minju\SSMPS_Ai\product\20_product\two_augmentation\product_image'
label_path = f'{path}\label'
image_path = f'{path}\image'

imgs_name = os.listdir(image_path)
l = []

for img_name in imgs_name:
    l.append(img_name[:5])

l = list(set(l))
img = [[] for x in range(len(l))]
for img_name in imgs_name:
    for ll in l:
        if ll == img_name[:5]:
            idx = l.index(ll)
            img[idx].append(img_name[:-4])

# train, valid, test split
trains = []
valids = []
tests = []

for i in img:
    train, test = train_test_split(i, test_size=0.2, random_state=42, shuffle=True)
    valid, test = train_test_split(test, test_size=0.5, random_state=42, shuffle=True)
    trains.extend(train)
    valids.extend(valid)
    tests.extend(test)

set_l = ['train', 'val', 'test']
type_l = ['images', 'labels']

for s in set_l:
    os.mkdir(f'{path}/{s}')
    for t in type_l:
        os.mkdir(f'{path}/{s}/{t}')

# 파일 train, valid, test 별로 옮기기
train_img_path = f'{path}\{set_l[0]}\{type_l[0]}'
valid_img_path = f'{path}\{set_l[1]}\{type_l[0]}'
test_img_path = f'{path}\{set_l[2]}\{type_l[0]}'

train_label_path = f'{path}\{set_l[0]}\{type_l[1]}'
valid_label_path = f'{path}\{set_l[1]}\{type_l[1]}'
test_label_path = f'{path}\{set_l[2]}\{type_l[1]}'

for tr in trains:
    shutil.move(f"{image_path}/{tr}.jpg", f"{train_img_path}/{tr}.jpg")
    shutil.move(f"{label_path}/{tr}.txt", f"{train_label_path}/{tr}.txt")

for va in valids:
    shutil.move(f"{image_path}/{va}.jpg", f"{valid_img_path}/{va}.jpg")
    shutil.move(f"{label_path}/{va}.txt", f"{valid_label_path}/{va}.txt")

for te in tests:
    shutil.move(f"{image_path}/{te}.jpg", f"{test_img_path}/{te}.jpg")
    shutil.move(f"{label_path}/{te}.txt", f"{test_label_path}/{te}.txt")