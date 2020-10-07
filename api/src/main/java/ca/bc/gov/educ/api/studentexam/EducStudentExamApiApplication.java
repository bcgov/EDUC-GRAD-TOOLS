package ca.bc.gov.educ.api.studentexam;

import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import ca.bc.gov.educ.api.studentexam.model.dto.StudentExam;
import ca.bc.gov.educ.api.studentexam.model.entity.StudentExamEntity;

@SpringBootApplication
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

		modelMapper.typeMap(StudentExamEntity.class, StudentExam.class).addMappings(mapper -> {
			mapper.skip(StudentExam::setSessionDate);
		});

		modelMapper.typeMap(StudentExam.class, StudentExamEntity.class).addMappings(mapper -> {
			mapper.skip(StudentExamEntity::setSessionDate);
		});

		return modelMapper;
	}
}