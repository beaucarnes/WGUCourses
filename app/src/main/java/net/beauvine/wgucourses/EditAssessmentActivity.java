package net.beauvine.wgucourses;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.util.Calendar;

public class EditAssessmentActivity extends AppCompatActivity {

    private  String action;
    private EditText assessment, assessmentDue;
    private Assessment assessmentEdit;
    private boolean editing = true;
    private long id = 1L;
    private long courseId;
    private static final int EDITOR_REQUEST_CODE = 1001;
    private DataSource datasource;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_assessment);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        assessment = (EditText) findViewById(R.id.assessment);
        assessmentDue = (EditText) findViewById(R.id.assessmentDue);



        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Intent intent = getIntent();
        id = intent.getLongExtra(DataSource.CONTENT_ITEM_TYPE, -1L);
        courseId = intent.getLongExtra("course", -1L);


        datasource = new DataSource(this);
        datasource.open();
        Course courseEdit = datasource.getCourseById(courseId);

        if (id == -1) {
            action = Intent.ACTION_INSERT;
            setTitle("Add New Assessment for " +  courseEdit.getCourse());

            assessment.setEnabled(true);
            assessmentDue.setEnabled(true);


        } else {
            DataSource datasource = new DataSource(this);
            datasource.open();
            action = Intent.ACTION_EDIT;
            assessmentEdit = datasource.getAssessmentById(id);
            assessment.setText(assessmentEdit.getAssessment());

            assessmentDue.setText(assessmentEdit.getDue());
            assessment.requestFocus();
            datasource.close();
            assessment.setEnabled(false);
            assessmentDue.setEnabled(false);

        }
    }

    private void finishEditing() {
        String newAssessment = assessment.getText().toString().trim();
        String newAssessmentDue = assessmentDue.getText().toString().trim();


        switch (action) {
            case  Intent.ACTION_INSERT:
                if (newAssessment.length() == 0) {
                    setResult(RESULT_CANCELED);
                } else {
                    DataSource datasource = new DataSource(this);
                    datasource.open();
                    Assessment assessment = new Assessment();
                    assessment.setAssessment(newAssessment);
                    assessment.setDue(newAssessmentDue);
                    assessment.setCourseId(courseId);
                    datasource.create(assessment);
                    setResult(RESULT_OK);
                    datasource.close();
                }
                break;
            case Intent.ACTION_EDIT:
                if (newAssessment.length() == 0) {
                    deleteAssessment();
                } else {
                    DataSource datasource = new DataSource(this);
                    datasource.open();
                    Assessment assessment = new Assessment();
                    assessment.setAssessment(newAssessment);
                    assessment.setDue(newAssessmentDue);
                    assessment.setId(id);
                    assessment.setCourseId(courseId);
                    datasource.update(assessment);
                    setResult(RESULT_OK);
                    datasource.close();
                }
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
                    assessment.setEnabled(true);
                    assessmentDue.setEnabled(true);

                } else {
                    String newAssessment = assessment.getText().toString().trim();
                    String newAssessmentStart = assessmentDue.getText().toString().trim();
                    DataSource datasource = new DataSource(this);
                    datasource.open();
                    Assessment assessmentNew = new Assessment();
                    assessmentNew.setAssessment(newAssessment);
                    assessmentNew.setDue(newAssessmentStart);
                    assessmentNew.setId(id);
                    assessmentNew.setCourseId(courseId);
                    datasource.update(assessmentNew);
                    setResult(RESULT_OK);
                    datasource.close();
                    assessment.setEnabled(false);
                    assessmentDue.setEnabled(false);
                    editing = true;

                }

                break;
            case R.id.action_delete:
                deleteAssessment();
                break;

            case R.id.action_alert_end:
                String endDate = assessmentDue.getText().toString().trim();
                Calendar cal2 = Calendar.getInstance();
                cal2.set(Calendar.YEAR, Integer.parseInt(endDate.substring(6)));
                cal2.set(Calendar.MONTH, Integer.parseInt(endDate.substring(0, 2)));
                cal2.set(Calendar.DAY_OF_MONTH, Integer.parseInt(endDate.substring(3, 5)));

                AlarmManager alarmMgr2 = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
                Intent intent2 = new Intent(this, AlertReceiver.class);
                intent2.putExtra("event", assessmentEdit.getAssessment());
                intent2.putExtra("time", "Due");
                PendingIntent pendingIntent2 = PendingIntent.getBroadcast(this, 0, intent2, 0);


                alarmMgr2.set(AlarmManager.RTC_WAKEUP, cal2.getTimeInMillis(), pendingIntent2);

                break;
        }

        return true;
    }

    private void deleteAssessment() {
        DataSource datasource = new DataSource(this);
        datasource.open();
        datasource.delete(assessmentEdit);
        Toast.makeText(this, "Assessment deleted!", Toast.LENGTH_SHORT).show();
        setResult(RESULT_OK);
        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (action.equals(Intent.ACTION_EDIT)) {
            getMenuInflater().inflate(R.menu.menu_term_edit, menu);
        }

        return true;
    }

    public void viewAssessments(View view) {
        Intent getAssessmentScreenIntent = new Intent(EditAssessmentActivity.this, AssessmentsActivity.class);
        getAssessmentScreenIntent.putExtra(DataSource.CONTENT_ITEM_TYPE, assessmentEdit.getId());
        startActivityForResult(getAssessmentScreenIntent, EDITOR_REQUEST_CODE);

    }
}
