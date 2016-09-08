package net.beauvine.wgucourses;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.List;

import static net.beauvine.wgucourses.R.id.listView;

public class CoursesActivity extends AppCompatActivity {

    private static final int EDITOR_REQUEST_CODE = 1001;
    private DataSource datasource;
    private List<Course> courses;
    private ArrayAdapter<Course> adapter;
    private ListView lv;
    private TextView term, termStart, termEnd;
    private String termFilter;
    private String oldTerm;
    private long termId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_courses);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Intent intent = getIntent();

        termId = intent.getLongExtra(DataSource.CONTENT_ITEM_TYPE, -1L);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                long id = -1;
                Intent intent = new Intent(CoursesActivity.this, EditCourseActivity.class);
                intent.putExtra(DataSource.CONTENT_ITEM_TYPE, id);
                intent.putExtra("term", termId);
                startActivityForResult(intent, EDITOR_REQUEST_CODE);
            }
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        datasource = new DataSource(this);
        datasource.open();

        Log.i("logging", "courses: ID is  " + termId );


        String action = Intent.ACTION_EDIT;
        Term termEdit = datasource.getTermById(termId);
            setTitle("Courses for " + termEdit.getTerm());

            datasource.close();


        refreshDisplay();



    }

    private void refreshDisplay() {
        datasource.open();
        courses = datasource.findAllCourses(termId);
        adapter = new ArrayAdapter<Course>(this,
                android.R.layout.simple_expandable_list_item_1,
                android.R.id.text1, courses);
        lv = (ListView) findViewById(listView);
        lv.setAdapter(adapter);

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(CoursesActivity.this, EditCourseActivity.class);
                Course course = (Course) parent.getItemAtPosition(position);
                intent.putExtra(DataSource.CONTENT_ITEM_TYPE, course.getId());
                intent.putExtra("term", termId);
                startActivityForResult(intent, EDITOR_REQUEST_CODE);
            }
        });
        datasource.close();
    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        refreshDisplay();
    }

}
