package net.beauvine.wgucourses;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

class DBOpenHelper extends SQLiteOpenHelper {


    private static final String DATABASE_NAME = "wgu.db";
    private static final int DATABASE_VERSION = 3;

    public static final String TABLE_TERMS = "terms";
    public static final String COLUMN_TERM_ID = "termId";
    public static final String COLUMN_TERM = "term";
    public static final String COLUMN_TERM_START = "termStart";
    public static final String COLUMN_TERM_END = "termEnd";

    private static final String TABLE_TERMS_CREATE =
            "CREATE TABLE " + TABLE_TERMS + " (" +
                    COLUMN_TERM_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COLUMN_TERM + " TEXT, " +
                    COLUMN_TERM_START + " TEXT, " +
                    COLUMN_TERM_END + " TEXT" +
                    ")";

    public static final String TABLE_COURSES = "courses";
    public static final String COLUMN_COURSE_ID = "courseId";
    public static final String COLUMN_COURSE = "course";
    public static final String COLUMN_COURSE_START = "courseStart";
    public static final String COLUMN_COURSE_END = "courseEnd";
    public static final String COLUMN_COURSE_STATUS = "courseStatus";
    public static final String COLUMN_COURSE_MENTOR = "mentor";


    private static final String TABLE_COURSES_CREATE =
            "CREATE TABLE " + TABLE_COURSES + " (" +
                    COLUMN_COURSE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COLUMN_COURSE + " TEXT, " +
                    COLUMN_COURSE_START + " TEXT, " +
                    COLUMN_COURSE_END + " TEXT, " +
                    COLUMN_COURSE_STATUS + " TEXT, " +
                    COLUMN_COURSE_MENTOR + " TEXT, " +
                    COLUMN_TERM_ID + " INTEGER" +
                    ")";

    public static final String TABLE_ASSESSMENTS = "assessments";
    public static final String COLUMN_ASSESSMENT_ID = "assessmentId";
    public static final String COLUMN_ASSESSMENT = "assessment";
    public static final String COLUMN_ASSESSMENT_DUE = "assessmentDue";

    private static final String TABLE_ASSESSMENTS_CREATE =
            "CREATE TABLE " + TABLE_ASSESSMENTS + " (" +
                    COLUMN_ASSESSMENT_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COLUMN_ASSESSMENT + " TEXT, " +
                    COLUMN_ASSESSMENT_DUE + " TEXT, " +
                    COLUMN_COURSE_ID + " INTEGER" +
                    ")";

    public static final String TABLE_NOTES = "notes";
    public static final String COLUMN_NOTE_ID = "noteId";
    public static final String COLUMN_NOTE = "note";
    public static final String COLUMN_IMG_PATH = "imgPath";

    private static final String TABLE_NOTES_CREATE =
            "CREATE TABLE " + TABLE_NOTES + " (" +
                    COLUMN_NOTE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COLUMN_NOTE + " TEXT, " +
                    COLUMN_IMG_PATH + " TEXT, " +
                    COLUMN_COURSE_ID + " TEXT" +
                    ")";

    public DBOpenHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(TABLE_TERMS_CREATE);
        db.execSQL(TABLE_COURSES_CREATE);
        db.execSQL(TABLE_ASSESSMENTS_CREATE);
        db.execSQL(TABLE_NOTES_CREATE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_TERMS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_COURSES);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ASSESSMENTS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NOTES);
        onCreate(db);

    }

}
