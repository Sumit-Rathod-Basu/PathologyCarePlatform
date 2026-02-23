package Pathology.Care.Platform.serviceimpl;

import Pathology.Care.Platform.DTO.LabMediaDTO;

public interface LabMediaService {

    LabMediaDTO getMedia();

    LabMediaDTO saveOrUpdate(LabMediaDTO dto);
}
