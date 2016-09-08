package net.beauvine.wgucourses;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Environment;
import android.util.Log;

class DataSource {

    private SQLiteOpenHelper dbhelper;
    private SQLiteDatabase database;
    public static final String CONTENT_ITEM_TYPE = "edit";

    private static final String[] allTermColumns = {
            DBOpenHelper.COLUMN_TERM_ID,
            DBOpenHelper.COLUMN_TERM,
            DBOpenHelper.COLUMN_TERM_START,
            DBOpenHelper.COLUMN_TERM_END};

    private static final String[] allCourseColumns = {
            DBOpenHelper.COLUMN_COURSE_ID,
            DBOpenHelper.COLUMN_COURSE,
            DBOpenHelper.COLUMN_COURSE_START,
            DBOpenHelper.COLUMN_COURSE_END,
            DBOpenHelper.COLUMN_COURSE_STATUS,
            DBOpenHelper.COLUMN_COURSE_MENTOR,
            DBOpenHelper.COLUMN_TERM_ID};

    private static final String[] allAssessmentColumns = {
            DBOpenHelper.COLUMN_ASSESSMENT_ID,
            DBOpenHelper.COLUMN_ASSESSMENT,
            DBOpenHelper.COLUMN_ASSESSMENT_DUE,
            DBOpenHelper.COLUMN_COURSE_ID};

    private static final String[] allNotesColumns = {
            DBOpenHelper.COLUMN_NOTE_ID,
            DBOpenHelper.COLUMN_NOTE,
            DBOpenHelper.COLUMN_IMG_PATH,
            DBOpenHelper.COLUMN_COURSE_ID};

    public DataSource(Context context) {
        dbhelper = new DBOpenHelper(context);
    }

    public void open() {
        database = dbhelper.getWritableDatabase();
    }

    public void close() {
        dbhelper.close();
    }

    public Term create(Term term) {
        ContentValues values = new ContentValues();
        values.put(DBOpenHelper.COLUMN_TERM, term.getTerm());
        values.put(DBOpenHelper.COLUMN_TERM_START, term.getTermStart());
        values.put(DBOpenHelper.COLUMN_TERM_END, term.getTermEnd());
        long insertid = database.insert(DBOpenHelper.TABLE_TERMS, null, values);
        term.setId(insertid);
        return term;
    }

    public Course create(Course course) {
        ContentValues values = new ContentValues();
        values.put(DBOpenHelper.COLUMN_COURSE, course.getCourse());
        values.put(DBOpenHelper.COLUMN_COURSE_START, course.getCourseStart());
        values.put(DBOpenHelper.COLUMN_COURSE_END, course.getCourseEnd());
        values.put(DBOpenHelper.COLUMN_COURSE_STATUS, course.getCourseStatus());
        values.put(DBOpenHelper.COLUMN_COURSE_MENTOR, course.getCourseMentor());
        values.put(DBOpenHelper.COLUMN_TERM_ID, course.getTermId());
        long insertid = database.insert(DBOpenHelper.TABLE_COURSES, null, values);
        course.setId(insertid);
        return course;
    }


    public Assessment create(Assessment assessment) {
        ContentValues values = new ContentValues();
        values.put(DBOpenHelper.COLUMN_ASSESSMENT, assessment.getAssessment());
        values.put(DBOpenHelper.COLUMN_ASSESSMENT_DUE, assessment.getDue());
        values.put(DBOpenHelper.COLUMN_COURSE_ID, assessment.getCourseId());
        long insertid = database.insert(DBOpenHelper.TABLE_ASSESSMENTS, null, values);
        assessment.setId(insertid);
        return assessment;
    }

    public Note create(Note note) {
        ContentValues values = new ContentValues();
        values.put(DBOpenHelper.COLUMN_NOTE, note.getNote());
        values.put(DBOpenHelper.COLUMN_IMG_PATH, note.getImgPath());
        values.put(DBOpenHelper.COLUMN_COURSE_ID, note.getCourseId());
        long insertid = database.insert(DBOpenHelper.TABLE_NOTES, null, values);
        note.setId(insertid);
        note.setImgPath("" + Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES) + "/"+ note.getCourseId() + note.getId() +".jpg");
        values.put(DBOpenHelper.COLUMN_IMG_PATH, "" + Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES) + "/"+ note.getCourseId() + note.getId() +".jpg");
        database.update(DBOpenHelper.TABLE_NOTES, values, DBOpenHelper.COLUMN_NOTE_ID + "="+note.getId(), null);


        return note;
    }

    public void update(Term term) {
        ContentValues values = new ContentValues();
        values.put(DBOpenHelper.COLUMN_TERM, term.getTerm());
        values.put(DBOpenHelper.COLUMN_TERM_START, term.getTermStart());
        values.put(DBOpenHelper.COLUMN_TERM_END, term.getTermEnd());
        database.update(DBOpenHelper.TABLE_TERMS, values, DBOpenHelper.COLUMN_TERM_ID + "="+term.getId(), null);

    }

    public void update(Course course) {
        ContentValues values = new ContentValues();
        values.put(DBOpenHelper.COLUMN_COURSE, course.getCourse());
        values.put(DBOpenHelper.COLUMN_COURSE_START, course.getCourseStart());
        values.put(DBOpenHelper.COLUMN_COURSE_END, course.getCourseEnd());
        values.put(DBOpenHelper.COLUMN_COURSE_STATUS, course.getCourseStatus());
        values.put(DBOpenHelper.COLUMN_COURSE_MENTOR, course.getCourseMentor());
        values.put(DBOpenHelper.COLUMN_TERM_ID, course.getTermId());
        database.update(DBOpenHelper.TABLE_COURSES, values, DBOpenHelper.COLUMN_COURSE_ID + "="+course.getId(), null);

    }

    public void update(Assessment assessment) {
        ContentValues values = new ContentValues();
        values.put(DBOpenHelper.COLUMN_ASSESSMENT, assessment.getAssessment());
        values.put(DBOpenHelper.COLUMN_ASSESSMENT_DUE, assessment.getDue());
        values.put(DBOpenHelper.COLUMN_COURSE_ID, assessment.getCourseId());
        database.update(DBOpenHelper.TABLE_ASSESSMENTS, values, DBOpenHelper.COLUMN_ASSESSMENT_ID + "="+assessment.getId(), null);

    }

    public void update(Note note) {
        ContentValues values = new ContentValues();
        values.put(DBOpenHelper.COLUMN_NOTE, note.getNote());
        values.put(DBOpenHelper.COLUMN_IMG_PATH, note.getImgPath());
        values.put(DBOpenHelper.COLUMN_COURSE_ID, note.getCourseId());
        database.update(DBOpenHelper.TABLE_NOTES, values, DBOpenHelper.COLUMN_NOTE_ID + "="+note.getId(), null);

    }

    public List<Term> findAll() {
        List<Term> terms = new ArrayList<>();

        Cursor cursor = database.query(DBOpenHelper.TABLE_TERMS, allTermColumns,
                null, null, null, null, null);

        if (cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                Term term = new Term();
                term.setId(cursor.getLong(cursor.getColumnIndex(DBOpenHelper.COLUMN_TERM_ID)));
                term.setTerm(cursor.getString(cursor.getColumnIndex(DBOpenHelper.COLUMN_TERM)));
                term.setTermStart(cursor.getString(cursor.getColumnIndex(DBOpenHelper.COLUMN_TERM_START)));
                term.setTermEnd(cursor.getString(cursor.getColumnIndex(DBOpenHelper.COLUMN_TERM_END)));
                terms.add(term);
            }
        }
        return terms;
    }

    public List<Course> findAllCourses(long termId) {
        List<Course> courses = new ArrayList<>();
        Log.i("logging", "findAllCourses: termId = " + termId);
        Cursor cursor = database.query(DBOpenHelper.TABLE_COURSES, allCourseColumns,
                DBOpenHelper.COLUMN_TERM_ID + " = ?",
                new String[] { String.valueOf(termId) }, null, null, null);


        if (cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                Course course = new Course();
                course.setId(cursor.getLong(cursor.getColumnIndex(DBOpenHelper.COLUMN_COURSE_ID)));
                course.setCourse(cursor.getString(cursor.getColumnIndex(DBOpenHelper.COLUMN_COURSE)));
                course.setCourseStart(cursor.getString(cursor.getColumnIndex(DBOpenHelper.COLUMN_COURSE_START)));
                course.setCourseEnd(cursor.getString(cursor.getColumnIndex(DBOpenHelper.COLUMN_COURSE_END)));
                course.setCourseStatus(cursor.getString(cursor.getColumnIndex(DBOpenHelper.COLUMN_COURSE_STATUS)));
                course.setCourseMentor(cursor.getString(cursor.getColumnIndex(DBOpenHelper.COLUMN_COURSE_MENTOR)));
                course.setTermId(cursor.getLong(cursor.getColumnIndex(DBOpenHelper.COLUMN_TERM_ID)));
                courses.add(course);
            }
        }
        return courses;
    }

    public List<Assessment> findAllAssessments(long courseId) {
        List<Assessment> assessments = new ArrayList<>();
        Log.i("logging", "findAllAssessments: courseId = " + courseId);
        Cursor cursor = database.query(DBOpenHelper.TABLE_ASSESSMENTS, allAssessmentColumns,
                DBOpenHelper.COLUMN_COURSE_ID + " = ?",
                new String[] { String.valueOf(courseId) }, null, null, null);


        if (cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                Assessment assessment = new Assessment();
                assessment.setId(cursor.getLong(cursor.getColumnIndex(DBOpenHelper.COLUMN_ASSESSMENT_ID)));
                assessment.setAssessment(cursor.getString(cursor.getColumnIndex(DBOpenHelper.COLUMN_ASSESSMENT)));
                assessment.setDue(cursor.getString(cursor.getColumnIndex(DBOpenHelper.COLUMN_ASSESSMENT_DUE)));
                assessment.setCourseId(cursor.getLong(cursor.getColumnIndex(DBOpenHelper.COLUMN_COURSE_ID)));
                assessments.add(assessment);
            }
        }
        return assessments;
    }

    public List<Note> findAllNotes(long courseId) {
        List<Note> notes = new ArrayList<>();
        Cursor cursor = database.query(DBOpenHelper.TABLE_NOTES, allNotesColumns,
                DBOpenHelper.COLUMN_COURSE_ID + " = ?",
                new String[] { String.valueOf(courseId) }, null, null, null);


        if (cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                Note note = new Note();
                note.setId(cursor.getLong(cursor.getColumnIndex(DBOpenHelper.COLUMN_NOTE_ID)));
                note.setNote(cursor.getString(cursor.getColumnIndex(DBOpenHelper.COLUMN_NOTE)));
                note.setImgPath(cursor.getString(cursor.getColumnIndex(DBOpenHelper.COLUMN_IMG_PATH)));
                note.setCourseId(cursor.getLong(cursor.getColumnIndex(DBOpenHelper.COLUMN_COURSE_ID)));
                notes.add(note);
            }
        }
        return notes;
    }


    public Term getTermById(long id) {
        Cursor cursor = database.query(DBOpenHelper.TABLE_TERMS, allTermColumns,
                DBOpenHelper.COLUMN_TERM_ID + " = ?",
                new String[] { String.valueOf(id) }, null, null, null);
        if (cursor != null) {
            cursor.moveToFirst();
        }

        Term term = cursorToTerm(cursor);
        return term;
    }

    public Course getCourseById(long id) {
        Cursor cursor = database.query(DBOpenHelper.TABLE_COURSES, allCourseColumns,
                DBOpenHelper.COLUMN_COURSE_ID + " = ?",
                new String[] { String.valueOf(id) }, null, null, null);
        if (cursor != null) {
            cursor.moveToFirst();
        }

        Course course = cursorToCourse(cursor);
        return course;
    }

    public Assessment getAssessmentById(long id) {
        Cursor cursor = database.query(DBOpenHelper.TABLE_ASSESSMENTS, allAssessmentColumns,
                DBOpenHelper.COLUMN_ASSESSMENT_ID + " = ?",
                new String[] { String.valueOf(id) }, null, null, null);
        if (cursor != null) {
            cursor.moveToFirst();
        }

        Assessment assessment = cursorToAssessment(cursor);
        return assessment;
    }

    public Note getNoteById(long id) {
        Cursor cursor = database.query(DBOpenHelper.TABLE_NOTES, allNotesColumns,
                DBOpenHelper.COLUMN_NOTE_ID + " = ?",
                new String[] { String.valueOf(id) }, null, null, null);
        if (cursor != null) {
            cursor.moveToFirst();
        }

        Note note = cursorToNote(cursor);
        return note;
    }

    private Term cursorToTerm(Cursor cursor) {

                Term term = new Term();
                term.setId(cursor.getLong(cursor.getColumnIndex(DBOpenHelper.COLUMN_TERM_ID)));
                term.setTerm(cursor.getString(cursor.getColumnIndex(DBOpenHelper.COLUMN_TERM)));
                term.setTermStart(cursor.getString(cursor.getColumnIndex(DBOpenHelper.COLUMN_TERM_START)));
                term.setTermEnd(cursor.getString(cursor.getColumnIndex(DBOpenHelper.COLUMN_TERM_END)));

        return term;
    }

    private Course cursorToCourse(Cursor cursor) {

        Course course = new Course();
        course.setId(cursor.getLong(cursor.getColumnIndex(DBOpenHelper.COLUMN_COURSE_ID)));
        course.setCourse(cursor.getString(cursor.getColumnIndex(DBOpenHelper.COLUMN_COURSE)));
        course.setCourseStart(cursor.getString(cursor.getColumnIndex(DBOpenHelper.COLUMN_COURSE_START)));
        course.setCourseEnd(cursor.getString(cursor.getColumnIndex(DBOpenHelper.COLUMN_COURSE_END)));
        course.setCourseStatus(cursor.getString(cursor.getColumnIndex(DBOpenHelper.COLUMN_COURSE_STATUS)));
        course.setCourseMentor(cursor.getString(cursor.getColumnIndex(DBOpenHelper.COLUMN_COURSE_MENTOR)));

        return course;
    }

    private Assessment cursorToAssessment(Cursor cursor) {

        Assessment assessment = new Assessment();
        assessment.setId(cursor.getLong(cursor.getColumnIndex(DBOpenHelper.COLUMN_ASSESSMENT_ID)));
        assessment.setAssessment(cursor.getString(cursor.getColumnIndex(DBOpenHelper.COLUMN_ASSESSMENT)));
        assessment.setDue(cursor.getString(cursor.getColumnIndex(DBOpenHelper.COLUMN_ASSESSMENT_DUE)));
        assessment.setCourseId(cursor.getLong(cursor.getColumnIndex(DBOpenHelper.COLUMN_COURSE_ID)));

        return assessment;
    }

    private Note cursorToNote(Cursor cursor) {

        Note note = new Note();
        note.setId(cursor.getLong(cursor.getColumnIndex(DBOpenHelper.COLUMN_NOTE_ID)));
        note.setNote(cursor.getString(cursor.getColumnIndex(DBOpenHelper.COLUMN_NOTE)));
        note.setImgPath(cursor.getString(cursor.getColumnIndex(DBOpenHelper.COLUMN_IMG_PATH)));
        note.setCourseId(cursor.getLong(cursor.getColumnIndex(DBOpenHelper.COLUMN_COURSE_ID)));

        return note;
    }

    public boolean delete(Term term) {
        String where = DBOpenHelper.COLUMN_TERM_ID + "=" + term.getId();
        int result = database.delete(DBOpenHelper.TABLE_TERMS, where, null);
        return (result == 1);
    }

    public boolean delete(Course course) {
        String where = DBOpenHelper.COLUMN_COURSE_ID + "=" + course.getId();
        int result = database.delete(DBOpenHelper.TABLE_COURSES, where, null);
        return (result == 1);
    }

    public boolean delete(Assessment assessment) {
        String where = DBOpenHelper.COLUMN_ASSESSMENT_ID + "=" + assessment.getId();
        int result = database.delete(DBOpenHelper.TABLE_ASSESSMENTS, where, null);
        return (result == 1);
    }

    public boolean delete(Note note) {
        String where = DBOpenHelper.COLUMN_NOTE_ID + "=" + note.getId();
        int result = database.delete(DBOpenHelper.TABLE_NOTES, where, null);
        return (result == 1);
    }

}
