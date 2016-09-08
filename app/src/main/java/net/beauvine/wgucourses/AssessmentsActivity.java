package net.beauvine.wgucourses;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.List;

import static net.beauvine.wgucourses.R.id.listView;

public class AssessmentsActivity extends AppCompatActivity {

    private static final int EDITOR_REQUEST_CODE = 1001;
    private DataSource datasource;
    private List<Assessment> assessments;
    private ArrayAdapter<Assessment> adapter;
    private ListView lv;
    private Course courseEdit;
    private long courseId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_assessments);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Intent intent = getIntent();

        courseId = intent.getLongExtra(DataSource.CONTENT_ITEM_TYPE, -1L);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                long id = -1;
                Intent getAssessmentScreenIntent = new Intent(AssessmentsActivity.this, EditAssessmentActivity.class);
                getAssessmentScreenIntent.putExtra(DataSource.CONTENT_ITEM_TYPE, id);
                getAssessmentScreenIntent.putExtra("course", courseId);
                startActivityForResult(getAssessmentScreenIntent, EDITOR_REQUEST_CODE);
            }
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        datasource = new DataSource(this);
        datasource.open();



        courseEdit = datasource.getCourseById(courseId);
        setTitle("Assessments for " + courseEdit.getCourse());

        lv = (ListView) findViewById(listView);

        refreshDisplay();
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(AssessmentsActivity.this, EditAssessmentActivity.class);
                Assessment assessment = (Assessment) parent.getItemAtPosition(position);
                intent.putExtra(DataSource.CONTENT_ITEM_TYPE, assessment.getId());
                intent.putExtra("course", courseId);
                startActivityForResult(intent, EDITOR_REQUEST_CODE);
            }
        });



    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                Intent intent = new Intent();
                intent.putExtra(DataSource.CONTENT_ITEM_TYPE, courseEdit.getId());
                intent.putExtra("term", courseEdit.getTermId());
                setResult(RESULT_OK, intent);
                finish();
                return(true);
        }

        return(super.onOptionsItemSelected(item));
    }

    private void refreshDisplay() {
        assessments = datasource.findAllAssessments(courseId);
        adapter = new ArrayAdapter<Assessment>(this,
                android.R.layout.simple_expandable_list_item_1,
                android.R.id.text1, assessments);

        lv.setAdapter(adapter);


    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        refreshDisplay();
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent();
        intent.putExtra(DataSource.CONTENT_ITEM_TYPE, courseEdit.getId());
        intent.putExtra("term", courseEdit.getTermId());
        setResult(RESULT_OK, intent);
        finish();
    }
}
