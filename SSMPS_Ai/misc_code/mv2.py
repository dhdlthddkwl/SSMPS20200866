import os
import shutil

def move_files(source_dir, destination_dir, file_extension):
    for filename in os.listdir(source_dir):
        if filename.endswith(file_extension):
            source_path = os.path.join(source_dir, filename)
            destination_path = os.path.join(destination_dir, filename)
            # 파일 이동
            shutil.move(source_path, destination_path)
            # print(f"Moved {filename} to {destination_dir}.")

source_directory = r'C:\Users\freet\Documents\GitHub\SSMPS_minju\SSMPS_Ai\product\20_product\two_augmentation\product_image\image'
destination_directory = r'C:\Users\freet\Documents\GitHub\SSMPS_minju\SSMPS_Ai\product\20_product\two_augmentation\product_image\label'
file_extension = '.txt'

# 파일 이동 함수 호출
move_files(source_directory, destination_directory, file_extension)
