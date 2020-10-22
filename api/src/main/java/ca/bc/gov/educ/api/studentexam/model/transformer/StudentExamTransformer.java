package ca.bc.gov.educ.api.studentexam.model.transformer;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import ca.bc.gov.educ.api.studentexam.model.dto.StudentExam;
import ca.bc.gov.educ.api.studentexam.model.entity.StudentExamEntity;
import ca.bc.gov.educ.api.studentexam.util.StudentExamApiUtils;

@Component
public class StudentExamTransformer {

    @Autowired
    ModelMapper modelMapper;

    public StudentExam transformToDTO (StudentExamEntity studentExamEntity) {
        StudentExam studentExam = modelMapper.map(studentExamEntity, StudentExam.class);       
        return studentExam;
    }

    public StudentExam transformToDTO ( Optional<StudentExamEntity> studentExamEntity ) {
        StudentExamEntity cae = new StudentExamEntity();

        if (studentExamEntity.isPresent())
            cae = studentExamEntity.get();

        StudentExam studentExam = modelMapper.map(cae, StudentExam.class);
        return studentExam;
    }

    public List<StudentExam> transformToDTO (Iterable<StudentExamEntity> studentExamEntities ) {

        List<StudentExam> studentExamList = new ArrayList<StudentExam>();

        for (StudentExamEntity studentExamEntity : studentExamEntities) {
            StudentExam studentExam = new StudentExam();
            studentExam = modelMapper.map(studentExamEntity, StudentExam.class);
            studentExam.setPen(studentExamEntity.getExamKey().getPen());
            studentExam.setCourseCode(studentExamEntity.getExamKey().getCourseCode());
            studentExam.setCourseLevel(studentExamEntity.getExamKey().getCourseLevel());
            studentExam.setSessionDate(StudentExamApiUtils.parseTraxDate(studentExamEntity.getExamKey().getSessionDate()).toLocaleString());
            studentExamList.add(studentExam);
        }

        return studentExamList;
    }

    public StudentExamEntity transformToEntity(StudentExam studentExam) {
        StudentExamEntity studentExamEntity = modelMapper.map(studentExam, StudentExamEntity.class);
        return studentExamEntity;
    }
}
