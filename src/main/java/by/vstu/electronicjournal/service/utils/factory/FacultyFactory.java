package by.vstu.electronicjournal.service.utils.factory;

import by.vstu.electronicjournal.dto.DisciplineDTO;
import by.vstu.electronicjournal.dto.FacultyDTO;
import by.vstu.electronicjournal.entity.Discipline;
import by.vstu.electronicjournal.entity.Faculty;
import by.vstu.electronicjournal.service.utils.AbstractFactoryForRelatedResources;

public final class FacultyFactory implements AbstractFactoryForRelatedResources<Faculty, FacultyDTO> {

    public Faculty create(FacultyDTO facultyDTO) {
        Faculty faculty = new Faculty();
        faculty.setName(facultyDTO.getName());
        faculty.setIdFromSource(facultyDTO.getIdFromSource());
        faculty.setStatus(facultyDTO.getStatus());
        return faculty;
    }

    public Faculty update(Faculty faculty, FacultyDTO facultyDTO) {
        faculty.setName(facultyDTO.getName());
        faculty.setIdFromSource(facultyDTO.getIdFromSource());
        faculty.setStatus(facultyDTO.getStatus());
        return faculty;
    }
}
