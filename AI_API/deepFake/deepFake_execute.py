import sys
sys.path.append("D:\ImmersionProject\FinalProject\GolaBlur\API-AI\AI_API")
# from AI_API.service import awsS3Service
from service.awsS3Service import *
# from .deepFake_func import *
import uuid

class deepFake_execute:
    
    def image(file_entity, object_entity_list):
        ## s3에서 처리할 파일 다운로드
        file = bring.bring_file_from_s3(file_entity=file_entity)
        # object_list = bring.bring_object_from_s3(object_entity_list=object_entity_list)
        ### 객체의 좌표값 추출
        # coordinate = get_object_coordinate(object_entity_list=object_entity_list)
        
        ## TODO 이미지 딥페이크 기능 함수 구현 후 여기서 호출하여 기능 수행
        ## 기능 수행
        res_file = 'file과 object_list, coordinate를 통해 기능 수행!!'
        
        ## s3에 처리된 파일 업로드
        # res_file_entity = change_file_to_entity_and_store_at_s3(file=res_file, original_file_entity=file_entity)
        
        ### Test
        res_file_entity = execute_test(file_entity=file_entity)
        
        return res_file_entity
    
    def video():
        
        return


def execute_test(file_entity):
        
    res= {
        'file_ID' : uuid.uuid4(),
        'user_ID' : file_entity['user_ID'],
        'original_File_ID' : file_entity['file_ID'],
        'real_File_Name' : file_entity['real_File_Name'],
        'group_ID' : file_entity['group_ID'],
        'file_Extension' : file_entity['file_Extension'],
        'path' : file_entity['path']
    }
    
    return res