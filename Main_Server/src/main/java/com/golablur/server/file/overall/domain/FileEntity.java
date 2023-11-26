package com.golablur.server.file.overall.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Date;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class FileEntity {
    private String file_ID;
    private String user_ID;
    private String original_File_ID;
    private String real_File_Name;
    private String group_ID;
    private String file_Extension;
    private String path;
    private Date sysdate;
}
