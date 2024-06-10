import os

folder_path_1 = r"C:\Users\freet\Documents\GitHub\SSMPS_minju\SSMPS_Ai\product\20_product\two_augmentation\product_image\image" # 첫 번째 폴더 경로 입력
folder_path_2 = r"C:\Users\freet\Documents\GitHub\SSMPS_minju\SSMPS_Ai\product\20_product\two_augmentation\product_image\label" # 두 번째 폴더 경로 입력
filename_without_ext_1 = set()

# 첫 번째 폴더의 파일 이름(확장자 제외)을 집합에 추가
for filename in os.listdir(folder_path_1):
    filename_wo_ext, ext = os.path.splitext(filename)
    filename_without_ext_1.add(filename_wo_ext)
    
# 두 번째 폴더에서 첫 번째 폴더에 없는 파일을 찾아서 출력
for filename in os.listdir(folder_path_2):
    filename_wo_ext, ext = os.path.splitext(filename)
    if filename_wo_ext not in filename_without_ext_1 and ext != "":
        print(f"'{filename}' in '{folder_path_2}' exists, but not in '{folder_path_1}'")

filename_without_ext_1 = set()

# 두 번째 폴더의 파일 이름(확장자 제외)을 집합에 추가
for filename in os.listdir(folder_path_2):
    filename_wo_ext, ext = os.path.splitext(filename)
    filename_without_ext_1.add(filename_wo_ext)
    
# 첫 번째 폴더에서 첫 번째 폴더에 없는 파일을 찾아서 출력
for filename in os.listdir(folder_path_1):
    filename_wo_ext, ext = os.path.splitext(filename)
    if filename_wo_ext not in filename_without_ext_1 and ext != "":
        print(f"'{filename}' in '{folder_path_1}' exists, but not in '{folder_path_1}'")