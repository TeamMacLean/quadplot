# QuadPlot

<img height="300" src="https://github.com/wookoouk/quadplot/blob/master/Art/logo.png">


## Simulator
```
dronekit-sitl copter --home=52.6223762,1.2230954,584,353
sudo mavproxy.py --master=tcp:127.0.0.1:5760 --out=/dev/tty.usbserial-DN0098CB,57600
OR
sudo mavproxy.py --master=tcp:127.0.0.1:5760 --out=/dev/tty.SLAB_USBtoUART,57600 //for replacement TX

    mode guided
    arm throttle
    takeoff 40

python ~/Documents/workspace/kohpahyluht/monitor.py
```
