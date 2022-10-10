package by.vstu.electronicjournal.service.utils.factory;

import by.vstu.electronicjournal.dto.AcademicTitleDTO;
import by.vstu.electronicjournal.dto.DisciplineDTO;
import by.vstu.electronicjournal.entity.AcademicTitle;
import by.vstu.electronicjournal.entity.Discipline;
import by.vstu.electronicjournal.service.utils.AbstractFactoryForRelatedResources;

public final class AcademicTitleFactory implements AbstractFactoryForRelatedResources<AcademicTitle, AcademicTitleDTO> {

    public AcademicTitle create(AcademicTitleDTO academicTitleDTO) {
        AcademicTitle academicTitle = new AcademicTitle();
        academicTitle.setName(academicTitleDTO.getName());
        academicTitle.setPrice(academicTitleDTO.getPrice());
        academicTitle.setIdFromSource(academicTitleDTO.getIdFromSource());
        academicTitle.setStatus(academicTitleDTO.getStatus());
        return academicTitle;
    }

    public AcademicTitle update(AcademicTitle academicTitle, AcademicTitleDTO academicTitleDTO) {
        academicTitle.setName(academicTitleDTO.getName());
        academicTitle.setPrice(academicTitleDTO.getPrice());
        academicTitle.setIdFromSource(academicTitleDTO.getIdFromSource());
        academicTitle.setStatus(academicTitleDTO.getStatus());
        return academicTitle;
    }
}
