import datetime
import requests
import time
import picamera

with picamera.PiCamera() as camera:
    camera.resolution = (1024, 768)
    camera.framerate = 30
    camera.start_preview()
    time.sleep(2)
    print('starting capture')
    timestamp = datetime.datetime.now().strftime("%Y-%m-%d_%H-%M-%S")
    images = ['{}-{}.jpg'.format(timestamp, x) for x in range(1, 4)]
    camera.capture_sequence(images)
    print('done')

files = {'image': open(images[0], 'rb')}
values = {}
r = requests.post('http://192.168.1.155:8080/image', files=files, data=values)
