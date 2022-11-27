package com.sit.client.tests;

import com.sit.client.filemanager.service.FileManagerService;
import com.sit.client.filemanager.service.IFileManager;
import org.junit.Test;

/**
 * @program: spring-starter
 * @description:
 * @author: LIULEI-TGL[知行合一]
 * @create: 2021-11-24 11:27:
 **/
public class FileManagers {

    @Test
    public void testFileUpload(){
        IFileManager iFileManager = new FileManagerService();
        iFileManager.fileUpload("","");
    }
}
