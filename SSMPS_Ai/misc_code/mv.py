import os
import shutil

type = ['image', 'label']
for t in type:
    # 이동할 파일이 있는 폴더 경로와 이동할 폴더 경로 설정
    folder_path = r"C:\Users\freet\Documents\GitHub\SSMPS_minju\SSMPS_Ai\product\상품 이미지"
    folder_path = f"{folder_path}/{t}"

    folders = [f for f in os.listdir(folder_path) if os.path.isdir(os.path.join(folder_path, f))]

    for foldername in folders:
        # 폴더 내 모든 파일에 대해 반복문 실행
        for filename in os.listdir(f'{folder_path}/{foldername}'):
            # 이동할 파일 경로 설정
            src_path = os.path.join(f'{folder_path}/{foldername}', filename)
            dst_path = os.path.join(folder_path, filename)
            # 파일을 이동합니다.
            shutil.move(src_path, dst_path)
