package com.golablur.server.file.overall.mapper;

import com.golablur.server.file.overall.domain.FileEntity;
import com.golablur.server.file.overall.domain.ObjectEntity;
import org.apache.ibatis.annotations.*;

import java.util.Collection;
import java.util.List;

@Mapper
public interface ObjectMapper {

    @Insert("INSERT INTO object (object_ID, file_ID, user_ID, object_Name, file_Extension, path" +
            ", xtl, ytl, xbr, ybr)" +
            " VALUES (#{object_ID}, #{file_ID}, #{user_ID}, #{object_Name}, #{file_Extension}, #{path}" +
            ", #{xtl}, #{ytl}, #{xbr}, #{ybr})")
    int storeObject(ObjectEntity object);

    @Delete("DELETE FROM object WHERE file_ID = #{id}")
    int deleteObject(@Param("id") String file_id);

    @Select("SELECT * FROM object WHERE file_ID = #{file_ID} and object_Name != 'deepFake' and object_Name != 'deleteobj'")
    List<ObjectEntity> getDetectionObjectListByFile(FileEntity file);

    @Select("SELECT * FROM object WHERE object_ID = #{id}")
    ObjectEntity getObjectByObjectID(@Param("id") String id);

    @Select("SELECT object_Name FROM object WHERE file_ID = #{file_ID} and object_Name != 'deepFake' and object_Name != 'deleteobj'")
    List<String> getObjectNameByFile(FileEntity file);

    @Select("SELECT * FROM object WHERE file_ID = #{id} and object_Name = 'person'")
    List<ObjectEntity> getPersonObjectByFile(@Param("id") String fileId);

    @Select("SELECT * FROM object WHERE file_ID = #{id} and object_Name = 'deepFake'")
    ObjectEntity getDeepFakeFileBySourceFile_ID(@Param("id") String sourceFileId);

    @Select("SELECT * FROM object WHERE file_ID = #{id} and object_Name = #{objectName}")
    List<ObjectEntity> getObjectByObjectName(@Param("id") String FileID, @Param("objectName") String objectName);

    @Select("SELECT * FROM object WHERE file_ID = #{group_ID}")
    ObjectEntity getObjectByGroup_ID(@Param("group_ID") String sourceFileGroupId);
}
