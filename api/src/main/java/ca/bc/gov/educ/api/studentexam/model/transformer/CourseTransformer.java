package ca.bc.gov.educ.api.studentexam.model.transformer;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import ca.bc.gov.educ.api.studentexam.model.dto.Course;
import ca.bc.gov.educ.api.studentexam.model.entity.CourseEntity;

@Component
public class CourseTransformer {

    @Autowired
    ModelMapper modelMapper;

    public Course transformToDTO (CourseEntity studentCourseEntity) {
        Course studentCourse = modelMapper.map(studentCourseEntity, Course.class);
        return studentCourse;
    }

    public Course transformToDTO ( Optional<CourseEntity> courseAchievementEntity ) {
        CourseEntity cae = new CourseEntity();

        if (courseAchievementEntity.isPresent())
            cae = courseAchievementEntity.get();

        Course courseAchievement = modelMapper.map(cae, Course.class);
        return courseAchievement;
    }

	public List<Course> transformToDTO (Iterable<CourseEntity> courseEntities ) {

        List<Course> courseList = new ArrayList<Course>();

        for (CourseEntity courseEntity : courseEntities) {
            Course course = new Course();
            course = modelMapper.map(courseEntity, Course.class);
           
            course.setCourseCode(courseEntity.getCourseKey().getCourseCode());
            course.setCourseLevel(courseEntity.getCourseKey().getCourseLevel());
            
            courseList.add(course);
        }

        return courseList;
    }

    public CourseEntity transformToEntity(Course studentCourse) {
        CourseEntity courseAchievementEntity = modelMapper.map(studentCourse, CourseEntity.class);
        return courseAchievementEntity;
    }
}
