package com.golablur.server.file.overall.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProcessingFileObjectDTO {
    private String file_ID;
    private List<String> object_IDList;
}
