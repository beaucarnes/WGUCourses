package net.beauvine.wgucourses;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.List;

import static net.beauvine.wgucourses.R.id.listView;

public class TermsActivity extends AppCompatActivity {

    private static final int EDITOR_REQUEST_CODE = 1001;
    private DataSource datasource;
    private List<Term> terms;
    private ArrayAdapter<Term> adapter;
    private ListView lv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_terms);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                long id = -1;
                Intent getTermScreenIntent = new Intent(TermsActivity.this, EditTermActivity.class);
                getTermScreenIntent.putExtra(DataSource.CONTENT_ITEM_TYPE, id);
                startActivityForResult(getTermScreenIntent, EDITOR_REQUEST_CODE);
            }
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        datasource = new DataSource(this);
        datasource.open();




        lv = (ListView) findViewById(listView);

        refreshDisplay();
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(TermsActivity.this, EditTermActivity.class);
                Term term = (Term) parent.getItemAtPosition(position);
                intent.putExtra(DataSource.CONTENT_ITEM_TYPE, term.getId());
                startActivityForResult(intent, EDITOR_REQUEST_CODE);
            }
        });



    }

    private void refreshDisplay() {
        terms = datasource.findAll();
        adapter = new ArrayAdapter<Term>(this,
                android.R.layout.simple_expandable_list_item_1,
                android.R.id.text1, terms);

        lv.setAdapter(adapter);


    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    refreshDisplay();
    }
}
