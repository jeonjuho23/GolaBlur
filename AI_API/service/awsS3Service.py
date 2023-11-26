import boto3
from configuration.config import *
import os
import uuid
import shutil

### Mask 파일의 경로 설정
def get_mask_path(object_entity):
    path = object_entity['path'][:len(object_entity['path'])-len('.png')] + '_mask.png'
    return path

### Mask 파일 S3 업로드
def use_mask_path_store_mask_at_s3(mask_path,object_entity):
    mask = open(mask_path)
    store.store_object_mask_at_s3(mask=mask, object_entity=object_entity)
    
### S3 Connection ###
def s3_connection():
    print("s3_connection")
    s3_bucket = boto3.client('s3',
                          aws_access_key_id = AWS_ACCESS_KEY,
                          aws_secret_access_key = AWS_SECRET_KEY)
    return s3_bucket


def set_s3_file_name(entity):
    return entity['file_ID'] + entity['file_Extension']


### 파일 정보를 통해 파일 다운로드
def bring_file_object_process(entity_of_file_and_object):
    ### 반환할 딕셔너리
    file_object = {}
    
    ### 이미지 가져와 딕셔너리에 저장
    file_object['file'] = bring.bring_file_from_s3(file_entity = entity_of_file_and_object['file'])
    
    ### 처리할 객체 리스트 가져와 딕셔너리에 저장
    file_object['object_list'] = bring.bring_object_from_s3(object_list = entity_of_file_and_object['processingObjectList'])
    
    return file_object


### FileEntity 형식으로 파일 정보를 추출
def change_file_to_file_entity(original_file_entity):
    file_entity = {}
    
    file_entity['file_ID'] = str(uuid.uuid4())
    file_entity['user_ID'] = original_file_entity['user_ID']
    file_entity['original_File_ID'] = original_file_entity['file_ID']
    file_entity['real_File_Name'] = original_file_entity['real_File_Name']
    file_entity['group_ID'] = original_file_entity['group_ID']
    file_entity['file_Extension'] = '.png'
    file_entity['path'] = original_file_entity['path'][:len(original_file_entity['path'])
                                                    -len(original_file_entity['file_ID'] + original_file_entity['file_Extension'])
                                                    ] + 'result/' + file_entity.get('file_ID') + '.png'
    
    return file_entity

### ObjectEntity의 리스트 형식으로 파일 정보를 추출
def change_object_to_object_entity(object_list, file_entity):
    object_entity_list = []
    for object_file in object_list:
        object_entity = {}
        
        object_entity['object_ID'] = str(uuid.uuid4())
        object_entity['file_ID'] = file_entity['file_ID']
        object_entity['user_ID'] = file_entity['user_ID']
        object_entity['file_Extension'] = os.path.splitext(object_file.get('object_Path'))[1]
        object_entity['path'] = file_entity['path'][:len(file_entity['path'])
                                                    -len(file_entity['file_ID'] + file_entity['file_Extension'])
                                                    ] + 'object/' +object_entity.get('object_ID') + '/'+object_entity.get('object_ID')+'.png'
        object_entity['object_Name'] = object_file.get('object_Name')
        object_entity['xtl'] = object_file.get('xtl')
        object_entity['xbr'] = object_file.get('xbr')
        object_entity['ytl'] = object_file.get('ytl')
        object_entity['ybr'] = object_file.get('ybr')
        
        object_entity_list.append(object_entity)
    return object_entity_list

### 파일 형태를 entity 형태로 변환시킨 후 s3에 업로드
def change_file_to_entity_and_store_at_s3(file, original_file_entity):
    ### 파일 형태를 entity 형태로 변환
    file_entity = change_file_to_file_entity(original_file_entity= original_file_entity)
    ### S3에 업로드
    store.store_file_at_s3(file=file, file_entity=file_entity)
    return file_entity

### 객체 파일을 entity 형태로 변환시킨 후 s3에 업로드
def change_object_to_entity_and_store_at_s3(object_list, file_entity):
    ### 객체 파일 리스트를 entity 형태로 변환
    object_entity_list = change_object_to_object_entity(object_list=object_list, file_entity=file_entity)
    ### S3에 업로드
    object_file_list = []
    for object_ in object_list:
        # print(object_.get('object_Path'))
        # print("===================================================")
        object_file = open(object_.get('object_Path'),"rb") #,encoding='여기가아니야 시발'
        object_file_list.append(object_file)
    store.store_object_at_s3(object_list=object_file_list, object_entity_list=object_entity_list)
    return object_entity_list

### 객체 파일의 좌표 생성
def get_object_coordinate(object_entity_list):
    object_coordinate_list = []
    for object_entity in object_entity_list:
        object_coordinate = {
            'xtl' : object_entity['xtl'],
            'xbr' : object_entity['xbr'],
            'ytl' : object_entity['ytl'],
            'ybr' : object_entity['ybr']
        }
        object_coordinate_list.append(object_coordinate)
    return object_coordinate_list

### TODO 로컬 스토리지에서 resources 디렉토리 초기화
def initialization():
    ### resources 폴더 삭제
    shutil.rmtree(r"D:\ImmersionProject\FinalProject\GolaBlur\API-AI\AI_API\resources")
    ### resources 폴더 생성
    # os.makedirs('')



### S3에 파일 업로드 후 DB에 저장할 형태로 반환
class store:
    
    def store_file_at_s3(file_entity, file):
        print("store_file_at_s3")
        
        s3 = s3_connection()
        
        ### 다운로드할 파일의 s3에서의 경로
        file_name = set_s3_file_name(entity= file_entity)
        path = file_entity['path']
        
        s3.put_object(
            Bucket = BUCKET_NAME,
            Body = file,
            Key = path,
            ACL = 'public-read'
        )
    
    def store_object_at_s3(object_entity_list, object_list):
        print("store_object_at_s3")
        
        s3 = s3_connection()
        
        ### s3 업로드
        for i in range(len(object_list)):
            object_file = object_list[i]
            object_entity = object_entity_list[i]
            
            s3.put_object(
                Bucket = BUCKET_NAME,
                Body = object_file,
                Key = object_entity['path'],
                ACL = 'public-read'
            )
            
    def store_object_mask_at_s3(mask, object_entity):
        print("store_object_mask_at_s3")
        
        s3 = s3_connection()
        
        ### 저장 경로 {user_ID}/{file_ID}/object/{object_ID}/{object_ID}_mask.png
        mask_path = get_mask_path(object_entity=object_entity)
        
        ### s3 업로드
        s3.put_object(
            Bucket = BUCKET_NAME,
            Body = mask,
            Key = mask_path,
            ACL = 'public-read'
        )

class bring:
    
    def bring_file_from_s3(file_entity):
        print("bring_file_from_s3")
        
        s3 = s3_connection()
        
        ### 가져올 파일의 경로
        file_name = set_s3_file_name(entity=file_entity)
        s3_path = file_entity['path']        
        
        ### 저장할 경로 및 파일 명

        local_path = 'C:/Users/eorl6/Documents/golablur/AI_API/resources/file/download/' + file_name
        
        # print("before download file")
        
        # print("file_name : " + file_name)
        # print("s3_path : " + s3_path)
        # print("local_path : " + local_path)
        
        s3.download_file(
            BUCKET_NAME,
            s3_path,
            local_path
        )
        # print("after download file")
        
        return open(local_path, 'rb')
    
    def bring_object_from_s3(object_entity_list):
        print("bring_object_from_s3")
        
        ### object file list
        object_file_list = []
        
        s3 = s3_connection()
        
        ### for each  문을 통해 list 내의 객체들을 다운로드 후 리스트에 추가하여 반환
        for object_entity in object_entity_list:
            ### 가져올 파일의 경로   
            file_name = set_s3_file_name(object_entity)
            s3_path = object_entity['path']
            ### 저장할 경로 및 파일 명

            local_path = 'C:/Users/eorl6/Documents/golablur/AI_API/resources/object/download/' + file_name

            s3.download_file(
                BUCKET_NAME,
                s3_path,
                local_path
            )
            
            object_file_list.append(open(local_path,'rb'))
        
        return object_file_list

    def bring_mask_from_s3(object_entity):
        print("bring_mask_from_s3")
        
        s3 = s3_connection()
        
        s3_path = get_mask_path(object_entity=object_entity)
        
        ### 저장할 로컬 경로
        local_path = 'C:/Users/eorl6/Documents/golablur/AI_API/resources/diffusion/mask/' + object_entity['object_ID'] + '/' + 'mask.png'
        
        
        s3.download_file(
            BUCKET_NAME,
            s3_path,
            local_path
        )
        
        ## 경로  - 파일이 필요하면 open 함수 사용
        return local_path





# class tests3:
    
#     def test_upload_image():
        
#         s3 = s3_connection()
        
#         s3.put_object(
#             Bucket = BUCKET_NAME,
#             Body = file,
#             Key = 'hihi/ttt.jpg',
#             ACL = 'public-read'
#         )
        
#         return '200'
        
#     def test_download_image():
        
#         s3 = s3_connection()
        
#         s3.download_file(
#             BUCKET_NAME, 
#             'hihi/ttt.jpg', 
#             'resources/file/download/wow.jpg'
#             )
        
#         return '200'
    
    
#     def test_upload_video():
        
#         s3 = s3_connection()
        
#         s3.put_object(
#             Bucket = BUCKET_NAME,
#             Body = file,
#             Key = 'video/hi.mp4',
#             ACL = 'public-read'
#         )
        
#         return
    
#     def test_download_video():
        
#         s3 = s3_connection()
        
#         s3.download_file(
#             BUCKET_NAME,
#             'video/hi.mp4',
#             'resources/file/download/yaya.mp4'
#             )
        
#         return
    