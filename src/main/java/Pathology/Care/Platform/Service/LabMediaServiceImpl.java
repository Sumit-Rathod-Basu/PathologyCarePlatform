package Pathology.Care.Platform.Service;
import Pathology.Care.Platform.DTO.LabMediaDTO;
import Pathology.Care.Platform.Mapper.LabMediaMapper;
import Pathology.Care.Platform.Model.LabMediaAssets;
import Pathology.Care.Platform.Repository.LabMediaRepository;
import Pathology.Care.Platform.serviceimpl.LabMediaService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LabMediaServiceImpl implements LabMediaService {
    
    @Autowired
    private  LabMediaRepository repository;
    @Autowired
    private  LabMediaMapper mapper;

    public LabMediaServiceImpl(LabMediaRepository repository,
                               LabMediaMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    @Override
    public LabMediaDTO getMedia() {
        return repository.findAll()
                .stream()
                .findFirst()
                .map(mapper::toDTO)
                .orElse(new LabMediaDTO());
    }

    @Override
    public LabMediaDTO saveOrUpdate(LabMediaDTO dto) {

        LabMediaAssets entity = repository.findAll()
                .stream()
                .findFirst()
                .orElse(new LabMediaAssets());

        mapper.updateEntityFromDto(dto, entity);

        return mapper.toDTO(repository.save(entity));
    }
}
