package net.beauvine.wgucourses;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.ShareActionProvider;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class EditNoteActivity extends AppCompatActivity {

    private static final int REQUEST_IMAGE_CAPTURE = 1;
    private  String action;
    private EditText noteText;
    private ImageView image;
    private Note noteEdit;
    private Button buttonView;
    private boolean imgCapFlag = false;
    private boolean imgVisible = false;


    private long id = 1L;
    private long courseId;
    private static final int EDITOR_REQUEST_CODE = 1001;
    private DataSource datasource;
    private Intent mShareIntent;

    // Storage Permissions
    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_note);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);



        noteText = (EditText) findViewById(R.id.noteText);
        Button buttonNew = (Button) findViewById(R.id.buttonNew);
        buttonView = (Button) findViewById(R.id.buttonView);
        image = (ImageView) findViewById(R.id.imageView);
        image.setVisibility(View.INVISIBLE);


        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Intent intent = getIntent();
        id = intent.getLongExtra(DataSource.CONTENT_ITEM_TYPE, -1L);
        courseId = intent.getLongExtra("course", -1L);


        datasource = new DataSource(this);
        datasource.open();
        Course courseEdit = datasource.getCourseById(courseId);

        if (id == -1) {
            action = Intent.ACTION_INSERT;
            setTitle("New Note for " +  courseEdit.getCourse());
            buttonView.setAlpha(.3f);
            buttonView.setClickable(false);
            buttonNew.setAlpha(.3f);
            buttonNew.setClickable(false);

        } else {
            setTitle("Note for " +  courseEdit.getCourse());
            DataSource datasource = new DataSource(this);
            datasource.open();
            action = Intent.ACTION_EDIT;
            noteEdit = datasource.getNoteById(id);
            noteText.setText(noteEdit.getNote());
            Log.i("logging", "Initializing! Image Path: " + noteEdit.getImgPath());
            Log.i("logging", "Initializing! note: " + noteEdit.getNote());
            mShareIntent = new Intent();
            mShareIntent.setAction(Intent.ACTION_SEND);
            mShareIntent.setType("text/plain");
            mShareIntent.putExtra(Intent.EXTRA_TEXT, noteEdit.getNote());

            datasource.close();

        }
    }

    private void finishEditing() {
        String newNote = noteText.getText().toString().trim();

        switch (action) {
            case  Intent.ACTION_INSERT:
                if (newNote.length() == 0) {
                    setResult(RESULT_CANCELED);
                } else {
                    DataSource datasource = new DataSource(this);
                    datasource.open();
                    Note note = new Note();
                    note.setNote(newNote);
                    note.setCourseId(courseId);
                    datasource.create(note);
                    setResult(RESULT_OK);
                    datasource.close();
                }
                break;
            case Intent.ACTION_EDIT:

                    DataSource datasource = new DataSource(this);
                    datasource.open();
                    Note note = new Note();
                    note.setNote(newNote);
                    note.setId(id);
                    note.setCourseId(courseId);
                    note.setImgPath(noteEdit.getImgPath());
                    datasource.update(note);
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

            case R.id.action_delete:
                deleteNote();
                break;

        }

        return true;
    }

    private void deleteNote() {
        DataSource datasource = new DataSource(this);
        datasource.open();
        datasource.delete(noteEdit);
        Toast.makeText(this, "Note deleted!", Toast.LENGTH_SHORT).show();
        setResult(RESULT_OK);
        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

            getMenuInflater().inflate(R.menu.menu_note_edit, menu);
        // Locate MenuItem with ShareActionProvider
        // Find the MenuItem that we know has the ShareActionProvider
        // Locate MenuItem with ShareActionProvider

        MenuItem item = menu.findItem(R.id.menu_share);
        // Fetch and store ShareActionProvider
        ShareActionProvider mShareActionProvider = (ShareActionProvider) MenuItemCompat.getActionProvider(item);

        // Connect the dots: give the ShareActionProvider its Share Intent

            mShareActionProvider.setShareIntent(mShareIntent);





        return true;
    }

    public void viewNotes(View view) {
        Intent getNoteScreenIntent = new Intent(EditNoteActivity.this, NotesActivity.class);
        getNoteScreenIntent.putExtra(DataSource.CONTENT_ITEM_TYPE, noteEdit.getId());
        startActivityForResult(getNoteScreenIntent, EDITOR_REQUEST_CODE);

    }

    private void takePicture() {
        Log.i("logging", "Taking Picture! " + noteEdit.getImgPath());

        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, REQUEST_IMAGE_CAPTURE);
    }


    public void buttonNewClicked(View view) {
        verifyStoragePermissions(EditNoteActivity.this);
        takePicture();
    }

    public void buttonViewClicked(View view) {
        if (imgVisible) {
            image.setVisibility(View.INVISIBLE);
            imgVisible = false;
            buttonView.setText("View Picture");
        } else {
            Log.i("logging", "Viewing picture. Path  is  " + noteEdit.getImgPath());
            BitmapFactory.Options options = new BitmapFactory.Options();
//            options.inSampleSize = 4;

            Bitmap bitmap = BitmapFactory.decodeFile(noteEdit.getImgPath(), options);
            image.setImageBitmap(bitmap);
            image.setVisibility(View.VISIBLE);
            imgVisible = true;
            buttonView.setText("Hide Picture");

        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap photo = (Bitmap) extras.get("data");
            FileOutputStream out = null;
            try {
                File file = new File(noteEdit.getImgPath());
                out = new FileOutputStream(file.getAbsoluteFile());
                photo.compress(Bitmap.CompressFormat.JPEG, 85, out);
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                try {
                    if (out != null) {
                        out.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

    }

    private static void verifyStoragePermissions(Activity activity) {
        // Check if we have write permission
        int permission = ActivityCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE);

        if (permission != PackageManager.PERMISSION_GRANTED) {
            // We don't have permission so prompt the user
            ActivityCompat.requestPermissions(
                    activity,
                    PERMISSIONS_STORAGE,
                    REQUEST_EXTERNAL_STORAGE
            );
        }
    }
}