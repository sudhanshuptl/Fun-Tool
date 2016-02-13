@echo off
echo Hi, I am going to trigger Startup application
timeout /t 60 /nobreak
C:\Python27\pythonw.exe C:\Python27\Pop_Notification.py %*
exit