package net.beauvine.wgucourses;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;

import java.util.Calendar;
import java.util.List;

public class EditCourseActivity extends AppCompatActivity {

    private String action;
    private EditText courseName, courseStart, courseEnd, courseStatus, courseMentor;
    private Spinner courseTerm;
    private String courseFilter;
    private String oldCourse;
    private Course courseEdit;
    private boolean editing = true;
    private long id = 1L;
    private long termId;
    private static final int EDITOR_REQUEST_CODE = 1001;
    private DataSource datasource;
    private List<Term> terms;
    private String[] termNames;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_course);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        courseName = (EditText) findViewById(R.id.course);
        courseStart = (EditText) findViewById(R.id.courseStart);
        courseEnd = (EditText) findViewById(R.id.courseEnd);
        courseStatus = (EditText) findViewById(R.id.courseStatus);
        courseMentor = (EditText) findViewById(R.id.courseMentor);
        courseTerm = (Spinner) findViewById(R.id.courseTerm);
        Button assessmentsButton = (Button) findViewById(R.id.assessments);
        Button viewButton = (Button) findViewById(R.id.viewNotes);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Intent intent = getIntent();
        id = intent.getLongExtra(DataSource.CONTENT_ITEM_TYPE, -1L);
        Log.i("logging", "getting term ID from intent  " + termId);
        termId = intent.getLongExtra("term", -1L);


        datasource = new DataSource(this);
        datasource.open();
        terms = datasource.findAll();
        termNames = new String[terms.size()];

        for (int i = 0; i < terms.size(); i++) {
            termNames[i] = terms.get(i).getTerm();
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, termNames);

        courseTerm.setAdapter(adapter);

        courseTerm.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // TODO Auto-generated method stub
            }
        });
        Log.i("logging", "edit course term: term ID is  " + termId);
        courseTerm.setSelection(adapter.getPosition(datasource.getTermById(termId).getTerm()));


        if (id == -1) {
            action = Intent.ACTION_INSERT;
            setTitle("Add New Course");

            courseName.setEnabled(true);
            courseStart.setEnabled(true);
            courseEnd.setEnabled(true);
            courseMentor.setEnabled(true);
            courseStatus.setEnabled(true);
            courseTerm.setEnabled(true);
            assessmentsButton.setAlpha(.3f);
            assessmentsButton.setClickable(false);
            viewButton.setAlpha(.3f);
            viewButton.setClickable(false);

        } else {
            DataSource datasource = new DataSource(this);
            datasource.open();
            action = Intent.ACTION_EDIT;
            courseEdit = datasource.getCourseById(id);
            courseName.setText(courseEdit.getCourse());

            courseStart.setText(courseEdit.getCourseStart());
            courseEnd.setText(courseEdit.getCourseEnd());
            courseStatus.setText(courseEdit.getCourseStatus());
            courseMentor.setText(courseEdit.getCourseMentor());
            courseName.requestFocus();
            datasource.close();
            courseName.setEnabled(false);
            courseStart.setEnabled(false);
            courseEnd.setEnabled(false);
            courseMentor.setEnabled(false);
            courseStatus.setEnabled(false);
            courseTerm.setEnabled(false);
        }
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }

    private void finishEditing() {
        String newCourse = courseName.getText().toString().trim();
        String newCourseStart = courseStart.getText().toString().trim();
        String newCourseEnd = courseEnd.getText().toString().trim();
        String newCourseStatus = courseStatus.getText().toString().trim();
        String newCourseMentor = courseMentor.getText().toString().trim();


        switch (action) {
            case Intent.ACTION_INSERT:
                if (newCourse.length() == 0) {
                    setResult(RESULT_CANCELED);
                } else {
                    DataSource datasource = new DataSource(this);
                    datasource.open();
                    Course course = new Course();
                    course.setCourse(newCourse);
                    course.setCourseStart(newCourseStart);
                    course.setCourseEnd(newCourseEnd);
                    course.setCourseMentor(newCourseMentor);
                    course.setCourseStatus(newCourseStatus);
                    course.setTermId(termId);
                    datasource.create(course);
                    setResult(RESULT_OK);
                    datasource.close();
                }
                break;
            case Intent.ACTION_EDIT:

                DataSource datasource = new DataSource(this);
                datasource.open();
                Course course = new Course();
                course.setCourse(newCourse);
                course.setCourseStart(newCourseStart);
                course.setCourseEnd(newCourseEnd);
                course.setId(id);
                course.setCourseStatus(newCourseStatus);
                course.setCourseMentor(newCourseMentor);
                course.setTermId(terms.get(courseTerm.getSelectedItemPosition()).getId());
                datasource.update(course);
                setResult(RESULT_OK);
                datasource.close();

        }

        finish();


    }

    @Override
    public void onBackPressed() {
        finishEditing();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case android.R.id.home:
                finishEditing();
                break;
            case R.id.action_edit:
                if (editing) {
                    editing = false;
                    courseName.setEnabled(true);
                    courseStart.setEnabled(true);
                    courseEnd.setEnabled(true);
                    courseMentor.setEnabled(true);
                    courseStatus.setEnabled(true);
                    courseTerm.setEnabled(true);
                } else {
                    String newCourse = courseName.getText().toString().trim();
                    String newCourseStart = courseStart.getText().toString().trim();
                    String newCourseEnd = courseEnd.getText().toString().trim();
                    String newCourseStatus = courseStatus.getText().toString().trim();
                    String newCourseMentor = courseMentor.getText().toString().trim();
                    DataSource datasource = new DataSource(this);
                    datasource.open();
                    Course course = new Course();
                    course.setCourse(newCourse);
                    course.setCourseStart(newCourseStart);
                    course.setCourseEnd(newCourseEnd);
                    course.setId(id);
                    course.setCourseStatus(newCourseStatus);
                    course.setCourseMentor(newCourseMentor);
                    course.setTermId(terms.get(courseTerm.getSelectedItemPosition()).getId());
                    datasource.update(course);
                    setResult(RESULT_OK);
                    datasource.close();
                    courseName.setEnabled(false);
                    courseStart.setEnabled(false);
                    courseEnd.setEnabled(false);
                    courseMentor.setEnabled(false);
                    courseStatus.setEnabled(false);
                    courseTerm.setEnabled(false);
                    editing = true;

                }

                break;
            case R.id.action_delete:
                deleteCourse();
                break;
            case R.id.action_alert_start:
                String startDate = courseStart.getText().toString().trim();
                Calendar cal = Calendar.getInstance();
                cal.set(Calendar.YEAR, Integer.parseInt(startDate.substring(6)));
                cal.set(Calendar.MONTH, Integer.parseInt(startDate.substring(0, 2)));
                cal.set(Calendar.DAY_OF_MONTH, Integer.parseInt(startDate.substring(3, 5)));

                AlarmManager alarmMgr = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
                Intent intent = new Intent(this, AlertReceiver.class);
                intent.putExtra("event", courseEdit.getCourse());
                intent.putExtra("time", "Start");
                PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, intent, 0);


                alarmMgr.set(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(), pendingIntent);

                break;

            case R.id.action_alert_end:
                String endDate = courseEnd.getText().toString().trim();
                Calendar cal2 = Calendar.getInstance();
                cal2.set(Calendar.YEAR, Integer.parseInt(endDate.substring(6)));
                cal2.set(Calendar.MONTH, Integer.parseInt(endDate.substring(0, 2)));
                cal2.set(Calendar.DAY_OF_MONTH, Integer.parseInt(endDate.substring(3, 5)));

                AlarmManager alarmMgr2 = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
                Intent intent2 = new Intent(this, AlertReceiver.class);
                intent2.putExtra("event", courseEdit.getCourse());
                intent2.putExtra("time", "End");
                PendingIntent pendingIntent2 = PendingIntent.getBroadcast(this, 0, intent2, 0);


                alarmMgr2.set(AlarmManager.RTC_WAKEUP, cal2.getTimeInMillis(), pendingIntent2);

                break;
        }

        return true;
    }

    private void deleteCourse() {
        DataSource datasource = new DataSource(this);
        datasource.open();
        datasource.delete(courseEdit);
        Toast.makeText(this, "Course deleted!", Toast.LENGTH_SHORT).show();
        setResult(RESULT_OK);
        finish();
    }

    @Override
    protected void onStop() {
        super.onStop();
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "EditCourse Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app URL is correct.
                Uri.parse("android-app://net.beauvine.wgucourses/http/host/path")
        );
        AppIndex.AppIndexApi.end(client, viewAction);
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.disconnect();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (action.equals(Intent.ACTION_EDIT)) {
            getMenuInflater().inflate(R.menu.menu_term_edit, menu);
        }
        return true;
    }

    public void viewAssessments(View view) {
        Intent getCourseScreenIntent = new Intent(EditCourseActivity.this, AssessmentsActivity.class);
        getCourseScreenIntent.putExtra(DataSource.CONTENT_ITEM_TYPE, courseEdit.getId());
        startActivityForResult(getCourseScreenIntent, EDITOR_REQUEST_CODE);

    }

    public void viewNotes(View view) {
        Intent getCourseScreenIntent = new Intent(EditCourseActivity.this, NotesActivity.class);
        getCourseScreenIntent.putExtra(DataSource.CONTENT_ITEM_TYPE, courseEdit.getId());
        startActivityForResult(getCourseScreenIntent, EDITOR_REQUEST_CODE);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.i("logging", "editcourseactivity onactivityresult  " + termId);
        Intent intent = getIntent();
        id = intent.getLongExtra(DataSource.CONTENT_ITEM_TYPE, -1L);
        termId = intent.getLongExtra("term", -1L);
    }

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "EditCourse Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app URL is correct.
                Uri.parse("android-app://net.beauvine.wgucourses/http/host/path")
        );
        AppIndex.AppIndexApi.start(client, viewAction);
    }
}
