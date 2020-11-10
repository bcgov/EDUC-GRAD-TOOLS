package ca.bc.gov.educ.api.studentexam.model.entity;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

import lombok.Data;

@Data
@Entity
@Table(name = "PROV_EXAM")
public class StudentExamEntity {
    @EmbeddedId
    private StudentExamId examKey;

    @Column(name = "STUDY_TYPE", nullable = true)
    private String gradReqMet;
    
    @Column(name = "CRSE_TYPE", nullable = true)
    private String courseType;
    
    @Column(name = "PROV_SCHOOL_PCT", nullable = true)
    private Double completedCourseSchoolPercentage;
    
    @Column(name = "PROV_EXAM_PCT", nullable = true)
    private Double completedCourseExamPercentage;
    
    @Column(name = "PROV_FINAL_PCT", nullable = true)
    private Double completedCourseFinalPercentage;
    
    @Column(name = "FINAL_LG", nullable = true)
    private String completedCourseLetterGrade;
    
    @Column(name = "PRED_PCT", nullable = true)
    private Double interimPercent;

    @Column(name = "PRED_LG", nullable = true)
    private String interimLetterGrade;
    
    @Column(name = "NUM_CREDITS", nullable = true)
    private Integer credits;
    
    @Column(name = "USED_FOR_GRAD", nullable = true)
    private String creditsUsedForGrad; 
    
    @Column(name = "MET_LIT_NUM_REQT", nullable = true)
    private String reqMetLiteracyNumeracy;
    
    @Column(name = "WROTE_FLAG", nullable = true)
    private String wroteFlag;
    
    @Column(name = "PROV_SPEC_CASE ", nullable = true)
    private String specialCase;
    
}
