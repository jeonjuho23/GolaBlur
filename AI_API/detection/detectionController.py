from flask import Flask, jsonify, request

# import sys
# sys.path.append("D:\ImmersionProject\FinalProject\GolaBlur\API-AI\AI_API\detection")
# from yolov5 import detect
# sys.path.append("C:/Users/eorl6/Documents/golablur/AI_API")
from detection_execute import *

import sys
sys.path.append("C:/Users/eorl6/Documents/golablur/")
from yolov5 import detect
from detection_execute import *
sys.path.append("C:/Users/eorl6/Documents/golablur/AI_API")
from service import useAPIService

app = Flask(__name__)

@app.route('/image/detection/execute', methods=['POST','GET'])
def image_detection_execute():
    print('image_detection_execute')
    req = request.get_json()
    res = detection_execute.image(req)
    return jsonify(res)

@app.route('/video/detection/execute', methods=['POST','GET'])
def video_detection_execute():
    print('video_detection_execute')
    req = request.get_json()
    res = detection_execute.video(req)
    return jsonify(res)


@app.route('/test')
def test():
    tests = objects('C:/Users/eorl6/Documents/golablur/AI_API/resources/file/download/rc-upload-1669657545782-2.jpg')
    # print(tests)
    # print("==================================")
    return "true"

if __name__ == "__main__":
    app.run(debug=True, host='0.0.0.0', port=8883)


    # dicc = {}
    # dicc['object_ID']='first_object'
    # dicc['file_ID']='test'
    # dicc['user_ID']='test_user'
    # dicc['object_Name']='TESTTESTTEST'
    # dicc['file_Extension']='.jpg'
    # dicc['path']='test.jpg'
    
    # res = [dicc]
    

# @app.route('/test')
# def test():
#     result = ""
#     list = ex_detection.objects()
#     print(list)
#     result +="탐지된 객체수 : "
#     result += str(len(list)) + "\n"
#     result += ex_detection.names(list[0][0])+" "
#     result += ex_detection.names(list[1][0])
#     return result




# @app.route('/test')
# def test():
#     list = ex_detection.objects()
#     print(list)
#     useAPIService.send_api('8881','POST',list)
#     return "true"

if __name__ == "__main__":
    app.run(debug=True, host='0.0.0.0', port=8883)
