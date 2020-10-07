package ca.bc.gov.educ.api.studentexam.service;


import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import ca.bc.gov.educ.api.studentexam.model.dto.StudentExam;
import ca.bc.gov.educ.api.studentexam.model.entity.StudentExamEntity;
import ca.bc.gov.educ.api.studentexam.model.transformer.StudentExamTransformer;
import ca.bc.gov.educ.api.studentexam.repository.StudentExamRepository;

@Service
public class StudentExamService {

    @Autowired
    private StudentExamRepository studentExamRepo;

    Iterable<StudentExamEntity> studentExamEntities;

    @Autowired
    private StudentExamTransformer studentExamTransformer;

    private static Logger logger = LoggerFactory.getLogger(StudentExamService.class);

     /**
     * Get all student courses by PEN populated in Student Course DTO
     *
     * @return Student Course 
     * @throws java.lang.Exception
     */
    public List<StudentExam> getStudentExamList(String pen) {
        List<StudentExam> studentExam  = new ArrayList<StudentExam>();

        try {
        	studentExam = studentExamTransformer.transformToDTO(
        			studentExamRepo.findByPen(
                            pen, Sort.by(Sort.Order.asc("pen"),
                                    Sort.Order.asc("courseCode"),
                                    Sort.Order.desc("completedCoursePercentage"),
                                    Sort.Order.asc("sessionDate")
                            )
                    )
            );
            logger.debug(studentExam.toString());
        } catch (Exception e) {
            logger.debug("Exception:" + e);
        }

        return studentExam;
    }
}
