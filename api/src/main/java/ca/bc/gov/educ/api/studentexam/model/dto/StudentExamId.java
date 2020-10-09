package ca.bc.gov.educ.api.studentexam.model.dto;

import java.io.Serializable;

import lombok.Data;

@Data
public class StudentExamId implements Serializable {

    private static final long serialVersionUID = 1L;

    private String pen;
    private String courseCode;
    private String courseLevel;
    private String sessionDate;

    public StudentExamId() {
    }

    /**
     * Constructor method used by JPA to create a composite primary key.
     *
     * @param studNo
     * @param crseCode
     * @param crseLevel
     * @param crseSession
     */
    public StudentExamId(String studNo, String crseCode, String crseLevel, String crseSession) {
        this.pen = studNo;
        this.courseCode = crseCode;
        this.courseLevel = crseLevel;
        this.sessionDate = crseSession;
    }
}
