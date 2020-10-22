package ca.bc.gov.educ.api.studentexam.service;


import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
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
     * Get all student exams by PEN populated in Student Exam DTO
     *
     * @return Student Exam 
     * @throws java.lang.Exception
     */
    public List<StudentExam> getStudentExamList(String pen) {
        List<StudentExam> studentExam  = new ArrayList<StudentExam>();

        try {
        	studentExam = studentExamTransformer.transformToDTO(studentExamRepo.findByPen(pen));
            logger.debug(studentExam.toString());
        } catch (Exception e) {
            logger.debug("Exception:" + e);
        }

        return studentExam;
    }
}
