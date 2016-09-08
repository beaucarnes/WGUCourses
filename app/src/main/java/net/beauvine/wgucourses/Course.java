package net.beauvine.wgucourses;

/**
 * Created by beau on 6/3/16.
 */
public class Course {

    private long id;
    private String course;
    private String courseStart;
    private String courseEnd;
    private String courseStatus;
    private String courseMentor;
    private long termId;

    public void setId(long id) {
        this.id = id;
    }

    public void setCourse(String course) {
        this.course = course;
    }

    public void setCourseStart(String courseStart) {
        this.courseStart = courseStart;
    }

    public void setCourseEnd(String courseEnd) {
        this.courseEnd = courseEnd;
    }

    public void setCourseStatus(String courseStatus) {
        this.courseStatus = courseStatus;
    }

    public void setCourseMentor(String courseMentor) {
        this.courseMentor = courseMentor;
    }

    public void setTermId(long termId) {
        this.termId = termId;
    }

    public long getId() {

        return id;
    }

    public String getCourse() {
        return course;
    }

    public String getCourseStart() {
        return courseStart;
    }

    public String getCourseEnd() {
        return courseEnd;
    }

    public String getCourseStatus() {
        return courseStatus;
    }

    public String getCourseMentor() {
        return courseMentor;
    }

    public long getTermId() {
        return termId;
    }

    @Override
    public String toString() {
        return course;
    }
}
