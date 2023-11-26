from flask import Flask, jsonify, request
import sys
# sys.path.append("D:\ImmersionProject\FinalProject\GolaBlur")
# import golablur
# sys.path.append("D:\ImmersionProject\FinalProject\GolaBlur\API-AI\AI_API")
# from ..service import useAPIService
from delete_execute import *
import cv2
import json
import base64
import numpy as np
import matplotlib.pyplot as plt

app = Flask(__name__)

# input : FileObjectDTO
# output : FileEntity

@app.route('/delete/execute', methods=['GET','POST'])
def execute():
    list = request.get_json()
    obj_list = test(list)
    print("요청받고 보냄")
    return jsonify(obj_list)


@app.route('/test')
def test(list):
    # print(list)
    path = list.pop()
    # print(list)
    # print(list)
    obj_path = []
    masks = []
    img = None
    for i in range(len(list)):
        xtl = list[i][1]
        ytl = list[i][2]
        xbr = list[i][3]
        ybr = list[i][4]
        img = golablur.Image(xtl, ytl, xbr, ybr,list[i][0],path)
        img.show_box()
        obj_path.append(img.rm_bg())
        masks.append(img.change_black())
    # print(obj_path)
    # print(masks)
    print("================mask생성=========================")

    return obj_path

# <<<<<<< HEAD
# @app.route('/model/test')
# def model_test():
#     img = golablur.Image(263,206,409,445,'obj','C:/Users/eorl6/Documents/golablur/original.png')
#     img.m_path = 'C:/Users/eorl6/Documents/golablur/sticker.png'
#
#     img_dict = {}
#     list = img.change_black()
#
#     unique, counts = np.unique(list[0], return_counts=True)
#     print(dict(zip(unique, counts)))
#     unique, counts = np.unique(list[1], return_counts=True)
#     print(dict(zip(unique, counts)))
#     print("===================================================")
#     cv2.imwrite('ORIGINAL11111111111.png',list[0])
#     cv2.imwrite('MASK12311111111111.png',list[1])
#     mask = cv2.imread('MASK12311111111111.png')
#     img = cv2.imread('ORIGINAL11111111111.png')
#     b,g,r = cv2.split(mask)
#     mask = cv2.merge([b,g,r])
#     unique, counts = np.unique(mask, return_counts=True)
#     print(dict(zip(unique, counts)))
#     unique, counts = np.unique(img, return_counts=True)
#     print(dict(zip(unique, counts)))
#
#
#     # for i in range(len(list)):
#     #     img_str = str(base64.b64encode(cv2.imencode('.jpg', list[i])[1]).decode())
#     #     name = 'img' + str(i)
#     #     img_dict['img' + str(i)] = str(img_str)
#     # img_dict = json.dumps(img_dict)
#
#     # useAPIService.send_api('http://localhost:8884/delete/execute','POST',jsonify('ORIGINAL.png'))
#     return "true"
#
#
# =======
# >>>>>>> dackyy
@app.route('/image/delete/execute', methods=['POST','GET'])
def image_delete_execute():
    print('image_delete_execute')
    req = request.get_json()
# <<<<<<< HEAD
#     print(req)
#
#     res = delete_execute.image(req['file'],req['objectList'])
#
#     return jsonify(res)
# =======
    # print('======================요청들어옴==================')
    # print(req)
    res = delete_execute.image(req['file'],req['objectList'])
    result = res.json()
    return result
# >>>>>>> dackyy

if __name__ == "__main__":
    app.run(debug=True, host='0.0.0.0', port=8881)

# @app.route('/model/test')
# def model_test():
#     img = golablur.Image(263,206,409,445,'obj','C:/Users/eorl6/Documents/golablur/original.png')
#     img.m_path = 'C:/Users/eorl6/Documents/golablur/sticker.png'

#     img_dict = {}
#     list = img.change_black()

#     unique, counts = np.unique(list[0], return_counts=True)
#     print(dict(zip(unique, counts)))
#     unique, counts = np.unique(list[1], return_counts=True)
#     print(dict(zip(unique, counts)))
#     print("===================================================")
#     cv2.imwrite('ORIGINAL11111111111.png',list[0])
#     cv2.imwrite('MASK12311111111111.png',list[1])
#     mask = cv2.imread('MASK12311111111111.png')
#     img = cv2.imread('ORIGINAL11111111111.png')
#     b,g,r = cv2.split(mask)
#     mask = cv2.merge([b,g,r])
#     unique, counts = np.unique(mask, return_counts=True)
#     print(dict(zip(unique, counts)))
#     unique, counts = np.unique(img, return_counts=True)
#     print(dict(zip(unique, counts)))


#     # for i in range(len(list)):
#     #     img_str = str(base64.b64encode(cv2.imencode('.jpg', list[i])[1]).decode())
#     #     name = 'img' + str(i)
#     #     img_dict['img' + str(i)] = str(img_str)
#     # img_dict = json.dumps(img_dict)

#     # useAPIService.send_api('http://localhost:8884/delete/execute','POST',jsonify('ORIGINAL.png'))
#     return "true"