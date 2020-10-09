package ca.bc.gov.educ.api.studentexam.model.dto;

import org.springframework.stereotype.Component;

import lombok.Data;

@Data
@Component
public class StudentExam {

	private StudentExamId examKey;
    private String gradReqMet;    
    private String courseType;    
    private Double completedCourseSchoolPercentage;    
    private Double completedCourseExamPercentage;    
    private Double completedCourseFinalPercentage;    
    private String completedCourseLetterGrade;    
    private Double interimPercent;
    private String interimLetterGrade;    
    private Integer credits;    
    private Integer creditsUsedForGrad;    
    private String reqMetLiteracyNumeracy;
    
	@Override
	public String toString() {
		return "StudentExam [examKey=" + examKey + ", gradReqMet=" + gradReqMet + ", courseType=" + courseType
				+ ", completedCourseSchoolPercentage=" + completedCourseSchoolPercentage
				+ ", completedCourseExamPercentage=" + completedCourseExamPercentage
				+ ", completedCourseFinalPercentage=" + completedCourseFinalPercentage + ", completedCourseLetterGrade="
				+ completedCourseLetterGrade + ", interimPercent=" + interimPercent + ", interimLetterGrade="
				+ interimLetterGrade + ", credits=" + credits + ", creditsUsedForGrad=" + creditsUsedForGrad
				+ ", reqMetLiteracyNumeracy=" + reqMetLiteracyNumeracy + "]";
	}
    
	
}