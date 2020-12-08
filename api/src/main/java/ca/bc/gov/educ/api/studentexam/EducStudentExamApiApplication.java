package ca.bc.gov.educ.api.studentexam;

import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.web.client.RestTemplate;

import ca.bc.gov.educ.api.studentexam.model.dto.StudentExam;
import ca.bc.gov.educ.api.studentexam.model.entity.StudentExamEntity;

@SpringBootApplication
@EnableGlobalMethodSecurity(prePostEnabled = true)
@EnableCaching
public class EducStudentExamApiApplication {

	private static Logger logger = LoggerFactory.getLogger(EducStudentExamApiApplication.class);
	
	public static void main(String[] args) {
		logger.debug("########Starting API");
		SpringApplication.run(EducStudentExamApiApplication.class, args);
		logger.debug("########Started API");
	}

	@Bean
	public ModelMapper modelMapper() {

		ModelMapper modelMapper = new ModelMapper();

		modelMapper.typeMap(StudentExamEntity.class, StudentExam.class);
		modelMapper.typeMap(StudentExam.class, StudentExamEntity.class);
		return modelMapper;
	}
	
	@Bean
	public RestTemplate restTemplate(RestTemplateBuilder builder) {
		return builder.build();
	}
}