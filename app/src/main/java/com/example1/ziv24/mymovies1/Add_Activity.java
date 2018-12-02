package com.example1.ziv24.mymovies1;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.webkit.URLUtil;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.Toast;

import com.example1.ziv24.mymovies1.db.Constants;
import com.example1.ziv24.mymovies1.db.DBHandler;
import com.squareup.picasso.Picasso;

import net.yslibrary.android.keyboardvisibilityevent.util.UIUtil;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Locale;

public class Add_Activity extends AppCompatActivity {

    EditText editText_title;
    EditText editText_overview;
    EditText editText_url;
    RatingBar ratingBar;
    ImageView imageView;
    String path;
    ImageButton btn_url;
    static final int REQUEST_IMAGE_CAPTURE = 1;
    DBHandler handler = new DBHandler(this);
    String title;
    String overview;
    String poster_path;
    float vote_average;
    int flag;
    int id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        loadLocale();
        setContentView(R.layout.activity_add);


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        editText_title = (EditText) findViewById(R.id.et_title_add);
        editText_overview = (EditText) findViewById(R.id.et_overview_add);
        editText_url = (EditText) findViewById(R.id.tv_url);
        ratingBar = (RatingBar) findViewById(R.id.ratingBar_add);
        ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                vote_average = rating;
            }
        });
        imageView = (ImageView) findViewById(R.id.imageView);
        btn_url = (ImageButton) findViewById(R.id.btn_url);
        btn_url.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (URLUtil.isValidUrl(editText_url.getText().toString())) {
                    Picasso.with(Add_Activity.this).load(editText_url.getText().toString()).resize(320, 380).into(imageView);
                } else {
                    try {


                        File f = new File(editText_url.getText().toString());
                        Bitmap bm = BitmapFactory.decodeFile(f.getAbsolutePath());
                        imageView.setImageBitmap(bm);
                    } catch (Exception e) {

                    }
                }
            }
        });


        final Intent intent = getIntent();
        id = intent.getIntExtra("id", -1);
        try {
            title = intent.getStringExtra("title");
            flag = intent.getIntExtra("flag", -1);
            overview = intent.getStringExtra("overview");
            poster_path = intent.getStringExtra("poster_path");
            vote_average = intent.getFloatExtra("vote_average", 0);
        } catch (Exception e) {

        }


        if (id != -1) {
            closeEdit();
            Movies movie = new Movies(title, overview, vote_average, poster_path, id);
            editText_title.setText(movie.getOriginal_title());
            editText_overview.setText(movie.getOverview());
            editText_url.setText(movie.getPoster());
            ratingBar.setRating(vote_average);
            if (URLUtil.isValidUrl(poster_path)) {
                Picasso.with(this).load(poster_path).resize(320, 380).into(imageView);
            } else {
                try {


                    File f = new File(poster_path);
                    Bitmap bm = BitmapFactory.decodeFile(f.getAbsolutePath());
                    imageView.setImageBitmap(bm);
                } catch (Exception e) {

                }
            }


        }
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                while (editText_title.getText().toString() == null) {
                    Toast.makeText(Add_Activity.this, "For take picture enter title", Toast.LENGTH_LONG).show();

                }
                dispatchTakePictureIntent();
            }
        });

        FloatingActionButton fab_edit = (FloatingActionButton) findViewById(R.id.fab_edit);
        fab_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (editText_title.isFocusable()) {
                    UIUtil.hideKeyboard(Add_Activity.this);
                    closeEdit();
                } else {
                    openEdit();
                    UIUtil.showKeyboard(Add_Activity.this, editText_title);
                }
            }
        });
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                title = editText_title.getText().toString();
                overview = editText_overview.getText().toString();
                poster_path = editText_url.getText().toString();
                vote_average = ratingBar.getRating();
                if (flag == -1) {
                    handler.addMovie(title, overview, vote_average, poster_path);
                    Intent intent = new Intent(Add_Activity.this, MainActivity.class);
                    startActivity(intent);
                } else {
                    handler.updateNote(new Movies(title, overview, vote_average, poster_path, id));
                    Intent intent = new Intent(Add_Activity.this, MainActivity.class);
                    startActivity(intent);
                }
            }
        });
    }


    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
    }

    private boolean createImageFile() throws IOException {
        String imgName = editText_title.getText().toString();

        // Create an image file name

        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        path = storageDir + "/" + imgName + ".jpg";
        FileOutputStream out = null;
        try {
            out = new FileOutputStream(path);

            imageView.buildDrawingCache();
            Bitmap bmp = imageView.getDrawingCache();

            bmp.compress(Bitmap.CompressFormat.PNG, 100, out); // bmp is your Bitmap instance
            // PNG is a lossless format, the compression factor (100) is ignored
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (out != null) {
                    out.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
                return false;
            }
        }

        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");

            imageView = (ImageView) findViewById(R.id.imageView);
            imageView.setImageBitmap(imageBitmap);

            try {
                if (createImageFile())
                    editText_url.setText(path);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    public void loadLocale() {
        SharedPreferences prefs = getSharedPreferences("Settings", Activity.MODE_PRIVATE);
        String language = prefs.getString("My_Lang", "");
        Locale locale = new Locale(language);
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        getBaseContext().getResources().updateConfiguration(config, getBaseContext().getResources().getDisplayMetrics());

    }

    public void closeEdit() {
        editText_overview.setFocusable(false);
        editText_url.setFocusable(false);
        ratingBar.setIsIndicator(true);
        imageView.setClickable(false);
        editText_title.setFocusable(false);

        editText_overview.setFocusableInTouchMode(false);
        editText_url.setFocusableInTouchMode(false);
        editText_title.setFocusableInTouchMode(false);
    }

    public void openEdit() {
        editText_overview.setFocusable(true);
        editText_url.setFocusable(true);
        editText_title.setFocusableInTouchMode(true);
        editText_overview.setFocusableInTouchMode(true);
        editText_url.setFocusableInTouchMode(true);
        ratingBar.setIsIndicator(false);
        imageView.setClickable(true);
        editText_title.setFocusable(true);

    }
}

