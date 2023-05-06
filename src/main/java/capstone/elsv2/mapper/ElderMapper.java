package capstone.elsv2.mapper;

import capstone.elsv2.dto.elder.ElderDTO;
import capstone.elsv2.entities.Elder;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface ElderMapper {
    ElderMapper INSTANCE = Mappers.getMapper(ElderMapper.class);
    ElderDTO convertToDTO(Elder elder);//
}
