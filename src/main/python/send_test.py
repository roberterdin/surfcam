import requests

files = {'image': open('moepp112.jpg', 'rb')}
values = {}
r = requests.post('http://192.168.1.155:8080/image', files=files, data=values)
