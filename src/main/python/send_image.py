import datetime
import grequests
import time
import picamera
import logging

IMAGE_COUNT = 4

with picamera.PiCamera() as camera:
    camera.resolution = (1024, 768)
    camera.framerate = 30
    camera.start_preview()
    time.sleep(2)
    print('starting capture')
    timestamp = datetime.datetime.now().strftime("%Y-%m-%d_%H-%M-%S")
    images = ['{}-{}.jpg'.format(timestamp, x) for x in range(1, IMAGE_COUNT + 1)]
    camera.capture_sequence(images)
    print('done')

values = {'foo': 'bar'}
requests = (grequests.post('http://192.168.1.155:8080/image',
                            files={'image': open(images[x], 'rb')},
                            params=values)
             for x in range(0, IMAGE_COUNT - 1))
responses = grequests.map(requests)
print(responses)
# r = grequests.post('http://192.168.1.155:8080/image', files=files, params=values)
