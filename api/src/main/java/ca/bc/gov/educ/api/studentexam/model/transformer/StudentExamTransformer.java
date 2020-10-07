package ca.bc.gov.educ.api.studentexam.model.transformer;

import ca.bc.gov.educ.api.studentexam.model.dto.StudentExam;
import ca.bc.gov.educ.api.studentexam.model.entity.StudentExamEntity;
import ca.bc.gov.educ.api.studentexam.util.StudentExamApiUtils;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Component
public class StudentExamTransformer {

    @Autowired
    ModelMapper modelMapper;

    public StudentExam transformToDTO (StudentExamEntity studentCourseEntity) {
        StudentExam studentCourse = modelMapper.map(studentCourseEntity, StudentExam.class);
        studentCourse.setSessionDate(StudentExamApiUtils.formatDate(studentCourseEntity.getSessionDate()));

        return studentCourse;
    }

    public StudentExam transformToDTO ( Optional<StudentExamEntity> courseAchievementEntity ) {
        StudentExamEntity cae = new StudentExamEntity();

        if (courseAchievementEntity.isPresent())
            cae = courseAchievementEntity.get();

        StudentExam courseAchievement = modelMapper.map(cae, StudentExam.class);
        courseAchievement.setSessionDate(StudentExamApiUtils.formatDate(cae.getSessionDate()));

        return courseAchievement;
    }

    public List<StudentExam> transformToDTO (Iterable<StudentExamEntity> courseAchievementEntities ) {

        List<StudentExam> courseAchievementList = new ArrayList<StudentExam>();

        for (StudentExamEntity courseAchievementEntity : courseAchievementEntities) {
            StudentExam courseAchievement = new StudentExam();
            courseAchievement = modelMapper.map(courseAchievementEntity, StudentExam.class);
            courseAchievement.setSessionDate(StudentExamApiUtils.formatDate(courseAchievementEntity.getSessionDate()));
            courseAchievementList.add(courseAchievement);
        }

        return courseAchievementList;
    }

    public StudentExamEntity transformToEntity(StudentExam studentCourse) {
        StudentExamEntity courseAchievementEntity = modelMapper.map(studentCourse, StudentExamEntity.class);
        courseAchievementEntity.setSessionDate(StudentExamApiUtils.parseDate(studentCourse.getSessionDate()));
        return courseAchievementEntity;
    }
}
