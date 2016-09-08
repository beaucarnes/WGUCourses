package net.beauvine.wgucourses;

/**
 * Created by beau on 6/3/16.
 */
public class Assessment {
    private long id;
    private String assessment;
    private String due;
    private long courseId;

    public String getAssessment() {
        return assessment;
    }

    public void setAssessment(String assessment) {
        this.assessment = assessment;
    }


    public void setCourseId(long courseId) {
        this.courseId = courseId;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setDue(String due) {
        this.due = due;
    }

    public long getId() {

        return id;
    }

    public String getDue() {
        return due;
    }

    public long getCourseId() {
        return courseId;
    }

    @Override
    public String toString() {
        return assessment + "\nDue: " + due;
    }
}
