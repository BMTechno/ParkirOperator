package com.tomcat.parkiroperator;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.tomcat.parkiroperator.DB.DB;
import com.tomcat.parkiroperator.Object.Parkir;
import com.tomcat.parkiroperator.Object.User;

import static com.tomcat.parkiroperator.R.id.btnSetAvailable;
import static com.tomcat.parkiroperator.R.id.inputAvailable;

public class PreferenceActivity extends AppCompatActivity {

    private DB db;
    private User user;
    private Parkir parkir;

    private EditText inputCapacity;
    private Button btnSetCapacity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preference);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(R.string.title_preference);

        Button btnLogout = (Button) findViewById(R.id.btnLogout);
        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                logout();
            }
        });

        user = new User(this);
        db = new DB(this,user);
        parkir = db.getParkir();
        setView();

        btnSetCapacity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setCapacity();
            }
        });
    }

    public void setView(){
        inputCapacity = (EditText) findViewById(R.id.inputCapacity);
        btnSetCapacity = (Button) findViewById(R.id.btnSetCapacity);

        inputCapacity.setText(String.valueOf(parkir.getCapacity()));
    }
    public void setCapacity(){
        int newAvailable=Integer.parseInt(inputCapacity.getText().toString());
        int lastAvailable = parkir.getAvailable();

        parkir.setAvailable(newAvailable);

        if(!db.setAvailable(parkir)){
            parkir.setAvailable(lastAvailable);
            inputCapacity.setText(String.valueOf(lastAvailable));
            Toast.makeText(getApplicationContext(), getString(R.string.signal1), Toast.LENGTH_SHORT).show();
        }
    }
    public void logout(){
        if(!user.deleteAuth())
            return;

        Intent i = new Intent(getApplicationContext(), LoginActivity.class);
        startActivity(i);
        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
