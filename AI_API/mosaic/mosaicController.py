from flask import Flask, jsonify, request
from mosaic_execute import *

app = Flask(__name__)

# input : FileObjectDTO
# output : FileEntity


@app.route('/image/mosaic/execute', methods=['POST', 'GET'])
def image_mosaic_execute():
    print('image_mosaic_execute')
    req = request.get_json()
    res = mosaic_execute.image(file_entity=req['file'], object_entity_list= req['objectList'])
    return res

@app.route('/video/mosaic/execute', methods=['POST', 'GET'])
def video_mosaic_execute():
    print('video_mosaic_execute')
    req = request.get_json()
    res = mosaic_execute.video(file_entity=req['file'], object_entity_list= req['objectList'])
    return res

@app.route('/test')
def test():
    return '200'


if __name__ == "__main__":
    app.run(debug=True, host='0.0.0.0', port=8882)