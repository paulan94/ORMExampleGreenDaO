package com.example.technorides.greendaoexamplelol.db;


import java.text.DateFormat;
import java.util.Date;

import android.app.ListActivity;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;

import com.example.technorides.greendaoexamplelol.R;
import com.example.technorides.greendaoexamplelol.db.DaoMaster.DevOpenHelper;

public class ProgramActivity extends ListActivity {

    private SQLiteDatabase db;

    private EditText editText;

    private DaoMaster daoMaster;
    private DaoSession daoSession;

    private ProgramDao programDao;

    private Cursor cursor;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        DevOpenHelper helper = new DaoMaster.DevOpenHelper(this, "notes-db", null);
        db = helper.getWritableDatabase();
        daoMaster = new DaoMaster(db);
        daoSession = daoMaster.newSession();
        programDao = daoSession.getProgramDao();

        String textColumn = ProgramDao.Properties.Id.columnName;
        String orderBy = textColumn + " COLLATE LOCALIZED ASC";
        cursor = db.query(programDao.getTablename(), programDao.getAllColumns(), null, null, null, null, orderBy);
        String[] from = { textColumn, ProgramDao.Properties.Name.columnName };
        int[] to = { android.R.id.text1, android.R.id.text2 };

        editText = (EditText) findViewById(R.id.editTextNote);
        addUiListeners();
    }

    protected void addUiListeners() {
        editText.setOnEditorActionListener(new OnEditorActionListener() {

            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    addNote();
                    return true;
                }
                return false;
            }
        });

        final View button = findViewById(R.id.buttonAdd);
        button.setEnabled(false);
        editText.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                boolean enable = s.length() != 0;
                button.setEnabled(enable);
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
    }

    public void onMyButtonClick(View view) {
        addNote();
    }

    private void addNote() {
        String nameText = editText.getText().toString();
        editText.setText("");

        final DateFormat df = DateFormat.getDateTimeInstance(DateFormat.MEDIUM, DateFormat.MEDIUM);
        String comment = "Added on " + df.format(new Date());
        Program program = new Program(null, nameText, new Date());
        programDao.insert(program);
        Log.d("DaoExample", "Inserted new note, ID: " + program.getId());

        cursor.requery();
    }

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        programDao.deleteByKey(id);
        Log.d("DaoExample", "Deleted note, ID: " + id);
        cursor.requery();
    }
}