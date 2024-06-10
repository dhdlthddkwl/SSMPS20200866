import os
import xml.etree.ElementTree as ET

folder = r'C:\Users\freet\Documents\GitHub\SSMPS_minju\SSMPS_Ai\product\상품 이미지\label'
txt_folder = r'C:\Users\freet\Documents\GitHub\SSMPS_minju\SSMPS_Ai\product\상품 이미지\label_txt'
files = [f for f in os.listdir(folder)]
category = ['10036', '10037', '10050', '30066', '30093', '30094', '30098', '30099', '35044', '35045', '40089', '40095', '40105', '40106', '40110', '40130', '40131', '40133', '40134', '40156']

def normalize_bbox(x_min, y_min, x_max, y_max, width, height):
    
    x_center = (x_min + x_max) / 2 / width
    y_center = (y_min + y_max) / 2 / height
    w = (x_max - x_min) / width
    h = (y_max - y_min) / height
    
    return round(x_center, 5), round(y_center, 5), round(w, 5), round(h, 5)

for file_name in files:

    tree = ET.parse(f'{folder}/{file_name}')
    root = tree.getroot()

    with open(f'{txt_folder}/{file_name[:-9]}.txt', 'w') as f:
        name = root.find('div_cd/item_no').text
        name = category.index(name)
        width = float(root.find('annotation/size/width').text)
        height = float(root.find('annotation/size/height').text)

        # XML 파일에서 라벨링 정보가 들어있는 태그 찾기
        for obj in root.iter('object'):
            
            # 해당 태그에서 필요한 정보를 추출
            xmin = float(obj.find('bndbox/xmin').text)
            ymin = float(obj.find('bndbox/ymin').text)
            xmax = float(obj.find('bndbox/xmax').text)
            ymax = float(obj.find('bndbox/ymax').text)

            # normalize_bbox
            x_center, y_center, w, h = normalize_bbox(xmin, ymin, xmax, ymax, width, height)
            
            line = f"{name} {x_center} {y_center} {w} {h}\n"
            f.write(line)

        f.close()
