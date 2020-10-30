package ca.bc.gov.educ.api.studentexam.model.entity;

import java.sql.Date;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

import org.hibernate.annotations.Immutable;

import lombok.Data;

@Data
@Immutable
@Entity
@Table(name = "TAB_CRSE")
public class CourseEntity {
   
	@EmbeddedId
    private CourseId courseKey;

    @Column(name = "CRSE_NAME", nullable = true)
    private String courseName;   

    @Column(name = "LANGUAGE", nullable = true)
    private String language;
    
    @Column(name = "START_DATE", nullable = true)
    private Date startDate;

    @Column(name = "END_DATE", nullable = true)
    private Date endDate;

    @Column(name = "WORK_EXP_FLAG", nullable = true)
    private String workExpFlag;
    
    @Column(name = "GENERIC_CRSE_TYPE", nullable = true)
    private String genericCourseType;
}
