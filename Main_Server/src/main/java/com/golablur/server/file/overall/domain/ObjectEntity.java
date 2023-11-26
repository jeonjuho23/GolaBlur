package com.golablur.server.file.overall.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ObjectEntity {
    private String object_ID;
    private String file_ID;
    private String user_ID;
    private String object_Name;
    private String file_Extension;
    private String path;
    // 좌표값
    private String xtl;
    private String xbr;
    private String ytl;
    private String ybr;
}
