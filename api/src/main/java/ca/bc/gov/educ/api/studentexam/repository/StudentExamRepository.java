package ca.bc.gov.educ.api.studentexam.repository;

import java.util.UUID;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import ca.bc.gov.educ.api.studentexam.model.entity.StudentExamEntity;

@Repository
public interface StudentExamRepository extends JpaRepository<StudentExamEntity, UUID> {

    Iterable<StudentExamEntity> findByPen(String pen);

    Iterable<StudentExamEntity> findByPen(String pen, Sort sort);

}
