package by.vstu.electronicjournal.service.utils.factory;

import by.vstu.electronicjournal.dto.DisciplineDTO;
import by.vstu.electronicjournal.dto.ForeignLanguageDTO;
import by.vstu.electronicjournal.entity.Discipline;
import by.vstu.electronicjournal.entity.ForeignLanguage;
import by.vstu.electronicjournal.service.utils.AbstractFactoryForRelatedResources;

public final class ForeignLanguageFactory implements AbstractFactoryForRelatedResources<ForeignLanguage, ForeignLanguageDTO> {

    public ForeignLanguage create(ForeignLanguageDTO foreignLanguageDTO) {
        ForeignLanguage foreignLanguage = new ForeignLanguage();
        foreignLanguage.setName(foreignLanguageDTO.getName());
        foreignLanguage.setIdFromSource(foreignLanguageDTO.getIdFromSource());
        foreignLanguage.setStatus(foreignLanguageDTO.getStatus());
        return foreignLanguage;
    }

    public ForeignLanguage update(ForeignLanguage foreignLanguage, ForeignLanguageDTO foreignLanguageDTO) {
        foreignLanguage.setName(foreignLanguageDTO.getName());
        foreignLanguage.setIdFromSource(foreignLanguageDTO.getIdFromSource());
        foreignLanguage.setStatus(foreignLanguageDTO.getStatus());
        return foreignLanguage;
    }
}
