package ca.bc.gov.educ.api.studentexam.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import ca.bc.gov.educ.api.studentexam.model.dto.StudentExam;
import ca.bc.gov.educ.api.studentexam.service.StudentExamService;
import ca.bc.gov.educ.api.studentexam.util.StudentExamApiConstants;

@CrossOrigin
@RestController
@RequestMapping(StudentExamApiConstants.STUDENT_EXAM_API_ROOT_MAPPING)
public class StudentExamController {

    private static Logger logger = LoggerFactory.getLogger(StudentExamController.class);

    @Autowired
    StudentExamService studentAssessmentService;

    @GetMapping(StudentExamApiConstants.GET_STUDENT_EXAM_BY_PEN_MAPPING)
    public List<StudentExam> getStudentExamByPEN(@PathVariable String pen) {
        logger.debug("#Get All Student Exams by PEN: " + pen);
        return studentAssessmentService.getStudentExamList(pen);
    }
}
