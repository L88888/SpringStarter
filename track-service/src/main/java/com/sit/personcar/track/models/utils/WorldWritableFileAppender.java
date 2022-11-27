package com.sit.personcar.track.models.utils;


import ch.qos.logback.core.rolling.RollingFileAppender;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.attribute.PosixFilePermission;
import java.util.HashSet;
import java.util.Set;

/**
 * @version 1.0
 * @description: 日志文件权限设置
 * @author: zhangbt
 * @date 2022/1/19
 */
public class WorldWritableFileAppender extends RollingFileAppender {

    private static final String OS_TYPE = "linux";
    @Override
    public synchronized void setFile(String fileName){
        super.setFile(fileName);
        File f = new File(fileName);
        String osType = System.getProperty("os.name").toLowerCase();
        if(f.exists() && osType.contains(OS_TYPE)) {
            try {
                Set<PosixFilePermission> perms = new HashSet<>();
                perms.add(PosixFilePermission.OWNER_READ);
                perms.add(PosixFilePermission.OWNER_WRITE);
                perms.add(PosixFilePermission.GROUP_READ);
                Files.setPosixFilePermissions(f.toPath(),perms);
            } catch (IOException e) {}
        }
    }
}

