package ca.bc.gov.educ.api.studentexam.model.dto;

import java.sql.Date;

import org.springframework.stereotype.Component;

import lombok.Data;

@Data
@Component
public class Course {

	private String courseCode;
    private String courseLevel;
    private String courseName;
    private String language;    
    private Date startDate;
    private Date endDate;
    private String workExpFlag;    
    private String genericCourseType;
    
	@Override
	public String toString() {
		return "Course [courseCode=" + courseCode + ", courseLevel=" + courseLevel + ", courseName=" + courseName
				+ ", language=" + language + ", startDate=" + startDate + ", endDate=" + endDate + ", workExpFlag="
				+ workExpFlag + ", genericCourseType=" + genericCourseType + "]";
	}		
}
