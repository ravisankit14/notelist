package com.note.views;

import android.app.FragmentTransaction;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.note.Constants;
import com.note.R;

public class AddActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);

        long id = getIntent().getLongExtra(Constants._ID,0);
        boolean edit = getIntent().getBooleanExtra("feomAdd",false);

        if(edit) {
            AddNoteFragment viewPageFragment = (AddNoteFragment) getSupportFragmentManager()
                    .findFragmentById(R.id.fragmentAdd);
            if (viewPageFragment != null) {
                FragmentTransaction ft = getFragmentManager().beginTransaction();
                ft.addToBackStack(null);
                viewPageFragment.getEditData(id);
            }
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
