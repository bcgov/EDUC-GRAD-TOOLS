package ca.bc.gov.educ.api.studentexam.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.provider.authentication.OAuth2AuthenticationDetails;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import ca.bc.gov.educ.api.studentexam.model.dto.StudentExam;
import ca.bc.gov.educ.api.studentexam.service.StudentExamService;
import ca.bc.gov.educ.api.studentexam.util.GradValidation;
import ca.bc.gov.educ.api.studentexam.util.ResponseHelper;
import ca.bc.gov.educ.api.studentexam.util.StudentExamApiConstants;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;

@CrossOrigin
@RestController
@RequestMapping(StudentExamApiConstants.STUDENT_EXAM_API_ROOT_MAPPING)
@EnableResourceServer
@OpenAPIDefinition(info = @Info(title = "API for Student Exam Data.", description = "This Read API is for Reading Student Exam data.", version = "1"), security = {@SecurityRequirement(name = "OAUTH2", scopes = {"READ_GRAD_STUDENT_EXAM_DATA"})})
public class StudentExamController {

    private static Logger logger = LoggerFactory.getLogger(StudentExamController.class);

    @Autowired
    StudentExamService studentExamService;
    
    @Autowired
	GradValidation validation;
    
    @Autowired
	ResponseHelper response;

    @GetMapping(StudentExamApiConstants.GET_STUDENT_EXAM_BY_PEN_MAPPING)
    @PreAuthorize("#oauth2.hasScope('READ_GRAD_STUDENT_EXAM_DATA')")
    public ResponseEntity<List<StudentExam>> getStudentExamByPEN(@PathVariable String pen) {
        logger.debug("#Get All Student Exams by PEN: " + pen);
        OAuth2AuthenticationDetails auth = (OAuth2AuthenticationDetails) SecurityContextHolder.getContext().getAuthentication().getDetails(); 
    	String accessToken = auth.getTokenValue();
        List<StudentExam> studentExamList =  studentExamService.getStudentExamList(pen,accessToken);
        if(studentExamList.isEmpty()) {
        	return response.NO_CONTENT();
        }
    	return response.GET(studentExamList);
    }
}
