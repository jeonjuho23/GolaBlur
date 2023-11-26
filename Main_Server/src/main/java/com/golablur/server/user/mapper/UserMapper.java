package com.golablur.server.user.mapper;

import com.golablur.server.user.domain.UserEntity;
import org.apache.ibatis.annotations.*;


@Mapper
public interface UserMapper {

    @Select("SELECT * FROM user WHERE User_ID = #{id}")
    public UserEntity findUser(@Param("id") String id);

    @Insert("INSERT INTO user VALUES (#{id},#{name},#{pw})")
    public int signUp(@Param("id") String id, @Param("name") String name, @Param("pw") String pw);

    @Select("SELECT * FROM user WHERE User_ID = #{id} and User_PW = #{pw}")
    public UserEntity login(@Param("id") String id, @Param("pw") String pw);

    @Update("UPDATE file SET user_ID = #{id} WHERE user_ID = #{sessionToken}")
    int updateFileData(@Param("id") String id, @Param("sessionToken") String sessionToken);

    @Update("UPDATE object SET user_ID = #{id} WHERE user_ID = #{sessionToken}")
    int updateObjectData(@Param("id") String id, @Param("sessionToken") String sessionToken);

    @Select("SELECT * FROM user WHERE User_ID = #{id} and User_PW = #{pw}")
    public UserEntity findUserPw(@Param("id") String id, @Param("pw") String pw);


    @Delete("DELETE FROM user WHERE User_ID = #{id}")
    public int deleteUser(@Param("id") String user_id);


}
