from flask import Flask, jsonify, request
import base64
import numpy as np
import cv2
from background_execute import *
# sys.path.append("C:/Users/eorl6/Documents/golablur/AI_API")
# from delete import deleteController
app = Flask(__name__)
# np.set_printoptions(threshold=sys.maxsize)

@app.route('/delete/execute', methods=['GET','POST'])
def execute():
    print("받음")
    file_and_object_list = request.get_json()
    print(file_and_object_list)
    img = file_and_object_list['original']
    mask = file_and_object_list['mask']
    entity = file_and_object_list['entity']
    print(entity)
    img_list = []
    img_list = [img,mask]

    result = model(img_list)
    print(result)
    print(type(result))
    res = background_execute.image(file_path=result,entity=entity)
    return res

@app.route('/test')
def test():
    return '200'


if __name__ == "__main__":
    app.run(debug=True, host='0.0.0.0', port=8884)