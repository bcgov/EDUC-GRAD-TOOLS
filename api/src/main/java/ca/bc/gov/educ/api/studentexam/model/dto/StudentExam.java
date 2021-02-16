package ca.bc.gov.educ.api.studentexam.model.dto;

import org.springframework.stereotype.Component;

import lombok.Data;

@Data
@Component
public class StudentExam {

	private String pen;
    private String courseCode;
    private String courseName;
    private String courseLevel;
    private String sessionDate;
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
    private String wroteFlag;
    private String specialCase;
    
    public String getCourseCode() {
		return courseCode != null ? courseCode.trim(): null;
	}
	public String getCourseName() {
		return courseName != null ? courseName.trim(): null; 
	}	

	public String getCourseLevel() {
		return courseLevel != null ? courseLevel.trim(): null;
	}
	
	public String getCompletedCourseLetterGrade() {
		return completedCourseLetterGrade != null ? completedCourseLetterGrade.trim(): null;
	}

	public String getInterimLetterGrade() {
		return interimLetterGrade != null ? interimLetterGrade.trim(): null;
	}
	
	public Double getCompletedCourseExamPercentage() {
		if(completedCourseExamPercentage == null) {
			return Double.valueOf("0");
		}
		return completedCourseExamPercentage; 
	}
	
	public Double getCompletedCourseFinalPercentage() {
		if(completedCourseFinalPercentage == null) {
			return Double.valueOf("0");
		}
		return completedCourseFinalPercentage; 
	}

	public Double getCompletedCourseSchoolPercentage() {
		if(completedCourseSchoolPercentage == null) {
			return Double.valueOf("0");
		}
		return completedCourseSchoolPercentage; 
	}
    
	@Override
	public String toString() {
		return "StudentExam [pen=" + pen + ", courseCode=" + courseCode + ", courseLevel=" + courseLevel
				+ ", sessionDate=" + sessionDate + ", gradReqMet=" + gradReqMet + ", courseType=" + courseType
				+ ", completedCourseSchoolPercentage=" + completedCourseSchoolPercentage
				+ ", completedCourseExamPercentage=" + completedCourseExamPercentage
				+ ", completedCourseFinalPercentage=" + completedCourseFinalPercentage + ", completedCourseLetterGrade="
				+ completedCourseLetterGrade + ", interimPercent=" + interimPercent + ", interimLetterGrade="
				+ interimLetterGrade + ", credits=" + credits + ", creditsUsedForGrad=" + creditsUsedForGrad
				+ ", reqMetLiteracyNumeracy=" + reqMetLiteracyNumeracy + "]";
	}
    
	
    
	
}