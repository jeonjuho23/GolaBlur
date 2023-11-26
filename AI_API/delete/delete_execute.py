import sys
sys.path.append("C:/Users/eorl6/Documents/golablur/AI_API")
from service import useAPIService
from service.awsS3Service import *
import cv2
sys.path.append("C:/Users/eorl6/Documents/golablur")
import golablur
import numpy as np
import uuid
import json


class delete_execute:
    
    def image(file_entity, object_entity_list):
        print('- delete execute image')
        # print('======================image 들어옴======================')
        ## s3에서 처리할 파일 다운로드
        file = bring.bring_file_from_s3(file_entity=file_entity)

        object_list = bring.bring_object_from_s3(object_entity_list=object_entity_list)

        ### 객체의 좌표값 추출
# <<<<<<< HEAD
#         # coordinate = get_object_coordinate(object_entity_list=object_entity_list)
#
# =======
        coordinate = get_object_coordinate(object_entity_list=object_entity_list)
        # print("file:",file.name)
        # print("object_list:",object_list)
        # print("coordinate:",coordinate)
# >>>>>>> dackyy
        ## TODO 기능 수행
        res_file_entity = delete_object(file.name,object_list[0].name,coordinate[0],file_entity)
        
        ## s3에 처리된 파일 업로드
        # res_file_entity = change_file_to_entity_and_store_at_s3(file=res_file, original_file_entity=file_entity)
        
        ### Test  -  return  FileEntity
        # res_file_entity = execute_test(file_entity=file_entity)
        
        print(res_file_entity)
        
        return res_file_entity


def execute_test(file_entity):

    res = {
        'file_ID' : file_entity['file_ID'],
        'user_ID' : file_entity['user_ID'],
        'original_File_ID' : file_entity['file_ID'],
        'real_File_Name' : file_entity['real_File_Name'],
        'group_ID' : file_entity['group_ID'],
        'file_Extension' : file_entity['file_Extension'],
        'path' : file_entity['path']
    }
    
    return res


# <<<<<<< HEAD
#
# def delete_object(file_path, mask_path, xyxy):
#     file_path = 'C:/Users/eorl6/Documents/golablur/original.png'
#     mask_path = 'C:/Users/eorl6/Documents/golablur/sticker.png'
#     img = golablur.Image(xyxy['xtl'],xyxy['ytl'],xyxy['xbr'],xyxy['ybr'],'obj',file_path)
# =======
def delete_object(file_path, mask_path, xyxy,entity):

    img = golablur.Image(int(xyxy['xtl']),int(xyxy['ytl']),int(xyxy['xbr']),int(xyxy['ybr']),'obj',file_path)
# >>>>>>> dackyy
    img.m_path = mask_path

    img_dict = {}
    lista = img.change_black()

    # print("===================================================")
    cv2.imwrite('ORIGINAL.png',lista[0])
    cv2.imwrite('MASK.png',lista[1])
    mask = cv2.imread('MASK.png')
    img = cv2.imread('ORIGINAL.png')
    # b,g,r = cv2.split(mask)
    # unique, counts = np.unique(b, return_counts=True)
    # print(list(zip(unique, counts)))
    # mask = cv2.merge([b,g,r])

    path = 'C:/Users/eorl6/Documents/golablur/AI_API/delete/'
    original = path+'ORIGINAL.png'
    mask = path+'MASK.png'
    dict = {
        'original':original,
        'mask':mask,
        'entity':entity
    }
    # for i in range(len(list)):
    #     img_str = str(base64.b64encode(cv2.imencode('.jpg', list[i])[1]).decode())
    #     name = 'img' + str(i)
    #     img_dict['img' + str(i)] = str(img_str)
    # img_dict = json.dumps(img_dict)

# <<<<<<< HEAD
#     # useAPIService.send_api('http://localhost:8884/delete/execute','POST',jsonify('ORIGINAL.png'))
#
# =======
    return useAPIService.send_api('http://localhost:8884/delete/execute','POST',dict)
# >>>>>>> dackyy
