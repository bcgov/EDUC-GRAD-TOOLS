package ca.bc.gov.educ.api.studentexam.util;

import java.util.Date;

public class StudentExamApiConstants {

    //API end-point Mapping constants
    public static final String API_ROOT_MAPPING = "";
    public static final String API_VERSION = "v1";
    public static final String STUDENT_EXAM_API_ROOT_MAPPING = "/api/" + API_VERSION + "/studentexam";
    public static final String GET_STUDENT_EXAM_BY_ID_MAPPING = "/{studentExamId}";
    public static final String GET_STUDENT_EXAM_BY_PEN_MAPPING = "/pen/{pen}";

    //Attribute Constants
    public static final String STUDENT_EXAM_ID_ATTRIBUTE = "studentExamID";

    //Default Attribute value constants
    public static final String DEFAULT_CREATED_BY = "StudentAssessmentAPI";
    public static final Date DEFAULT_CREATED_TIMESTAMP = new Date();
    public static final String DEFAULT_UPDATED_BY = "StudentAssessmentAPI";
    public static final Date DEFAULT_UPDATED_TIMESTAMP = new Date();

    //Default Date format constants
    public static final String DEFAULT_DATE_FORMAT = "dd-MMM-yyyy";
}
