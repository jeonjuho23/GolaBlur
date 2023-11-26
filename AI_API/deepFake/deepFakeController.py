from flask import Flask, jsonify, request
from deepFake_execute import *

app = Flask(__name__)

# input : FileObjectDTO
# output : FileEntity


@app.route('/image/deepfake/execute', methods=['POST','GET'])
def image_deepfake_execute():
    print('image_deepfake_execute')
    req = request.get_json()
    ## TODO 이미지 딥페이크 구현 필요
    res = deepFake_execute.image(file_entity=req['file'], object_entity_list=req['objectList'])
    return jsonify(res)

@app.route('/video/deepfake/execute', methods=['POST','GET'])
def video_deepfake_execute():
    print('video_deepfake_execute')
    req = request.get_json()
    ## TODO 비디오 딥페이크 로직 설계 및 구현 필요
    res = deepFake_execute.video()
    return jsonify(res)



@app.route('/test')
def test():
    return '200'


if __name__ == "__main__":
    app.run(debug=True, host='0.0.0.0', port=8880)