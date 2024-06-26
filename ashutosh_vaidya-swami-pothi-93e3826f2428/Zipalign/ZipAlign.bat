@echo off
"C:\Shri Swami\final\Zipalign\Resources\zipalign" -v 4 "C:\Shri Swami\final\BookReader.apk" "C:\Shri Swami\final\Zipalign\ZipAlignedApps\BookReader.apk"
echo You can find the zip-aligned apps in the "ZipAlignedApps" Folder
pause
del %CD%\ZipAlign.bat
