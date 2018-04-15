# This enables the modem, it's initially in storage (optical drive) mode. 
# If there is another optical drive, change sr0 to whatever it was assigned to.

# This works but has no log output
#sudo /usr/bin/sg_raw /dev/sr0 11 06 20 00 00 00 00 00 01 00

# This has log output. Run print_debug to get manufacturer and device hex numbers.
#sudo usb_modeswitch -J -v 0x12d1 -p 0x1506

# From previous config
#sudo usb_modeswitch -v 12d1 -p 1506 -M "55534243123456780000000000000011063000000100010000000000000000"
