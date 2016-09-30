import time

import picamera

import threading

def setInterval(func,time) :

    e = threading.Event()
    while not e.wait(time) :
        func()

def cameraExecute() :
    with picamera.PiCamera() as camera:

    	camera.start_preview()

    	time.sleep(0)

    	camera.capture('/home/pi/Desktop/Menzap/Images/image_'+time.strftime('%H%M%S')+'.jpg')

    	camera.stop_preview()

# using
setInterval(cameraExecute, 60)
