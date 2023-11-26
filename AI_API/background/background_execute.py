import sys
sys.path.append("C:/Users/eorl6/Documents/golablur")
from diffusion import inpaint
sys.path.append("C:/Users/eorl6/Documents/golablur/AI_API")
from service import useAPIService
from service.awsS3Service import *

class background_execute:
    
    def image(file_path,entity):
        print('- background image')
        file = open(file_path,"rb")

        ## s3에 탐지된 객체 업로드
        res_file_entity = change_file_to_entity_and_store_at_s3(file=file, original_file_entity=entity)
        
        ## Test
        # object_entity_list = execute_test(file_entity=file_entity)
        # print(object_entity_list)
        
        return res_file_entity
    
    def video():
        
        return

def model(mask):
    model = inpaint.Image(mask)
    result = model.load()
    return result