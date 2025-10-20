package dev.angelcorzo.neoparking.jpa.mappers;

import dev.angelcorzo.neoparking.jpa.config.MapperStructConfig;
import dev.angelcorzo.neoparking.jpa.users.UsersData;
import dev.angelcorzo.neoparking.model.users.Users;
import org.mapstruct.Mapper;

@Mapper(config = MapperStructConfig.class)
public interface UserMapper extends BaseMapper<Users, UsersData> {

}
