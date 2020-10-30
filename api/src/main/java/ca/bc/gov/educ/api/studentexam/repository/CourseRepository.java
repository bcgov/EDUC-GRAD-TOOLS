package ca.bc.gov.educ.api.studentexam.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import ca.bc.gov.educ.api.studentexam.model.entity.CourseEntity;



@Repository
public interface CourseRepository extends JpaRepository<CourseEntity, UUID> {

    List<CourseEntity> findAll();
    
    @Query("select c from CourseEntity c where c.courseKey.courseCode=:courseCode and c.courseKey.courseLevel=:courseLevel")
    Optional<CourseEntity> findByCourseCode(String courseCode,String courseLevel);

}
