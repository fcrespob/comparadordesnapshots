#!/bin/bash
if [ $(lvdisplay | grep "VG Name" | grep -v grep | wc -l) -eq 1 ]; then
mount /dev/TEST_VG/lv_test /logs
chmod 777 /logs
exit 0
else
echo "n
p
1
1
+10G
w
" | fdisk /dev/xvdf

pvcreate /dev/xvdf1
vgcreate TEST_VG /dev/xvdf1 
lvcreate -L 10G -n lv_test TEST_VG
mkfs.ext4 /dev/TEST_VG/lv_test
mount /dev/TEST_VG/lv_test /logs
chmod 777 /logs
chown -R awsadmin:apps /logs
exit 0
fi

