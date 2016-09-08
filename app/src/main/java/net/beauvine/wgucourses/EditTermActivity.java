package net.beauvine.wgucourses;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class EditTermActivity extends AppCompatActivity {

    private  String action;
    private EditText term, termStart, termEnd;
    private Term termEdit;
    private boolean editing = true;
    private long id;
    private static final int EDITOR_REQUEST_CODE = 1001;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_term);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        term = (EditText) findViewById(R.id.term);
        termStart = (EditText) findViewById(R.id.termStart);
        termEnd = (EditText) findViewById(R.id.termEnd);
        Button coursesButton = (Button) findViewById(R.id.assessments);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Intent intent = getIntent();
        id = intent.getLongExtra(DataSource.CONTENT_ITEM_TYPE, -1L);
        Log.i("logging", "edit term: ID is  " + id );

        if (id == -1) {
            action = Intent.ACTION_INSERT;
            setTitle("Add New Term");
            term.setEnabled(true);
            termStart.setEnabled(true);
            termEnd.setEnabled(true);
            coursesButton.setAlpha(.3f);
            coursesButton.setClickable(false);

        } else {
            DataSource datasource = new DataSource(this);
            datasource.open();
            action = Intent.ACTION_EDIT;
            termEdit = datasource.getTermById(id);
            term.setText(termEdit.getTerm());
            termStart.setText(termEdit.getTermStart());
            termEnd.setText(termEdit.getTermEnd());
            term.requestFocus();
            datasource.close();
            term.setEnabled(false);
            termStart.setEnabled(false);
            termEnd.setEnabled(false);
        }
    }

    private void finishEditing() {
        String newTerm = term.getText().toString().trim();
        String newTermStart = termStart.getText().toString().trim();
        String newTermEnd = termEnd.getText().toString().trim();

        switch (action) {
            case  Intent.ACTION_INSERT:
                if (newTerm.length() == 0) {
                    setResult(RESULT_CANCELED);
                } else {
                    DataSource datasource = new DataSource(this);
                    datasource.open();
                    Term term = new Term();
                    term.setTerm(newTerm);
                    term.setTermStart(newTermStart);
                    term.setTermEnd(newTermEnd);
                    datasource.create(term);
                    setResult(RESULT_OK);
                    datasource.close();
                }
                break;
            case Intent.ACTION_EDIT:
                if (newTerm.length() == 0) {
                    deleteTerm();
                } else {
                    DataSource datasource = new DataSource(this);
                    datasource.open();
                    Term term = new Term();
                    term.setTerm(newTerm);
                    term.setTermStart(newTermStart);
                    term.setTermEnd(newTermEnd);
                    term.setId(id);
                    datasource.update(term);
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
                    term.setEnabled(true);
                    termStart.setEnabled(true);
                    termEnd.setEnabled(true);
                    editing = false;
                } else {
                    String newTerm = term.getText().toString().trim();
                    String newTermStart = termStart.getText().toString().trim();
                    String newTermEnd = termEnd.getText().toString().trim();
                    DataSource datasource = new DataSource(this);
                    datasource.open();
                    Term tempTerm = new Term();
                    tempTerm.setTerm(newTerm);
                    tempTerm.setTermStart(newTermStart);
                    tempTerm.setTermEnd(newTermEnd);
                    tempTerm.setId(id);
                    datasource.update(tempTerm);
                    setResult(RESULT_OK);
                    datasource.close();
                    term.setEnabled(false);
                    termStart.setEnabled(false);
                    termEnd.setEnabled(false);
                    editing = true;
                }

                break;
            case R.id.action_delete:
                deleteTerm();
                break;
        }

        return true;
    }

    private void deleteTerm() {
        DataSource datasource = new DataSource(this);
        datasource.open();
        datasource.delete(termEdit);
        Toast.makeText(this, "Term deleted!", Toast.LENGTH_SHORT).show();
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

    public void viewCourses(View view) {
        Intent getTermScreenIntent = new Intent(EditTermActivity.this, CoursesActivity.class);
        getTermScreenIntent.putExtra(DataSource.CONTENT_ITEM_TYPE, id);
        startActivityForResult(getTermScreenIntent, EDITOR_REQUEST_CODE);

    }
}
