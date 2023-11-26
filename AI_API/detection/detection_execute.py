import sys
sys.path.append("C:/Users/eorl6/Documents/golablur/AI_API")
from service import useAPIService
from service.awsS3Service import *

from yolov5 import detect

class detection_execute:
    
    def image(file_entity):
        ## s3에서 처리할 파일 다운로드
        print('- detection image')
        file = bring.bring_file_from_s3(file_entity=file_entity)
        # print("========================================================================")
        # print(file.name)
        ## TODO 기능 수행        
        print("========detection요청===============")
        print(file.name)
        object_list = objects(file.name)

        ## s3에 탐지된 객체 업로드
        object_entity_list = change_object_to_entity_and_store_at_s3(object_list=object_list, file_entity=file_entity)
        
        ## Test
        # object_entity_list = execute_test(file_entity=file_entity)
        # print(object_entity_list)

        return object_entity_list
    
    def video():
        
        return


### TODO TEST
def execute_test(file_entity):
    object_list = [{
        'object_ID' : uuid.uuid4(),
        'file_ID' : file_entity['file_ID'],
        'user_ID' : file_entity['user_ID'],
        'object_Name' : 'ex',
        'file_Extension' : file_entity['file_Extension'],
        'path' : file_entity['path']
    },{
        'object_ID' : uuid.uuid4(),
        'file_ID' : file_entity['file_ID'],
        'user_ID' : file_entity['user_ID'],
        'object_Name' : 'ex',
        'file_Extension' : file_entity['file_Extension'],
        'path' : file_entity['path']
    },{
        'object_ID' : uuid.uuid4(),
        'file_ID' : file_entity['file_ID'],
        'user_ID' : file_entity['user_ID'],
        'object_Name' : 'ex',
        'file_Extension' : file_entity['file_Extension'],
        'path' : file_entity['path']
    }]
    
    ## 좌표는 없음
    
    return object_list

def names(num):
    num = int(num)
    names={
        0: "person",
        1: "bicycle",
        2: "car",
        3: "motorcycle",
        4: "airplane",
        5: "bus",
        6: "train",
        7: "truck",
        8: "boat",
        9: "traffic light",
        10: "fire hydrant",
        11: "stop sign",
        12: "parking meter",
        13: "bench",
        14: "bird",
        15: "cat",
        16: "dog",
        17: "horse",
        18: "sheep",
        19: "cow",
        20: "elephant",
        21: "bear",
        22: "zebra",
        23: "giraffe",
        24: "backpack",
        25: "umbrella",
        26: "handbag",
        27: "tie",
        28: "suitcase",
        29: "frisbee",
        30: "skis",
        31: "snowboard",
        32: "sports ball",
        33: "kite",
        34: "baseball bat",
        35: "baseball glove",
        36: "skateboard",
        37: "surfboard",
        38: "tennis racket",
        39: "bottle",
        40: "wine glass",
        41: "cup",
        42: "fork",
        43: "knife",
        44: "spoon",
        45: "bowl",
        46: "banana",
        47: "apple",
        48: "sandwich",
        49: "orange",
        50: "broccoli",
        51: "carrot",
        52: "hot dog",
        53: "pizza",
        54: "donut",
        55: "cake",
        56: "chair",
        57: "couch",
        58: "potted plant",
        59: "bed",
        60: "dining table",
        61: "toilet",
        62: "tv",
        63: "laptop",
        64: "mouse",
        65: "remote",
        66: "keyboard",
        67: "cell phone",
        68: "microwave",
        69: "oven",
        70: "toaster",
        71: "sink",
        72: "refrigerator",
        73: "book",
        74: "clock",
        75: "vase",
        76: "scissors",
        77: "teddy bear",
        78: "hair drier",
        79: "toothbrush"
    }
    return str(names[num])

def objects(path):
    print('- objects')
    list = detect.run(source=path,weights='yolov5n6.pt', save_txt=True)
    list.append(path)
    # print(list)
    lists = []
    stickers = useAPIService.send_api('http://localhost:8881/delete/execute','POST',list)
    # print(stickers.json())
    # print("=============================")
    # print(list)
    for i in range(len(list)-1):
        # print(i)
        dict = {}
        list[i][0] = names(list[i][0])
        dict["object_Path"] = stickers.json()[i]
        dict["object_Name"] = list[i][0]
        dict["xtl"] = list[i][1]
        dict["ytl"] = list[i][2]
        dict["xbr"] = list[i][3]
        dict["ybr"] = list[i][4]
        lists.append(dict)
# <<<<<<< HEAD
#     print(lists)
#     return lists
# =======
    # print(lists)
    return lists
# >>>>>>> dackyy
