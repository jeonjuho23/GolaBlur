package com.golablur.server.file.overall.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DeepFakeDTO {
    private String source_file_ID;
    private String target_file_ID;
}
