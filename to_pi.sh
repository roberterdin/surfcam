#!/usr/bin/env bash
rsync -rvz ./ pi@192.168.1.206:tmp/surfcam --exclude .git/ --exclude target/ --exclude env/ --exclude .idea/
