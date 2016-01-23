#!/usr/bin/env sh

URL=http://material-design.storage.googleapis.com/publish/material_v_4/material_ext_publish/$1
DEST=media/publish/material_v_4/material_ext_publish/$1
SDCARD_PATH=/sdcard/material/$1

curl $URL > $DEST
adb push -p $DEST $SDCARD_PATH

echo "Don't forget to copy this to app/src/release/assets/ !"
