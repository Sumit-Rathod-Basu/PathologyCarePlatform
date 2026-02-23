package Pathology.Care.Platform.Mapper;
import Pathology.Care.Platform.DTO.LabMediaDTO;
import Pathology.Care.Platform.Model.LabMediaAssets;
import org.mapstruct.Mapper;
import org.mapstruct.BeanMapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring")
public interface LabMediaMapper {

    LabMediaDTO toDTO(LabMediaAssets entity);

    LabMediaAssets toEntity(LabMediaDTO dto);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateEntityFromDto(LabMediaDTO dto,
                             @MappingTarget LabMediaAssets entity);
}