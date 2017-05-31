package com.tomcat.parkiroperator;

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
import android.widget.TextView;
import android.widget.Toast;

import com.tomcat.parkiroperator.DB.DB;
import com.tomcat.parkiroperator.Object.Parkir;
import com.tomcat.parkiroperator.Object.User;

public class MainActivity extends AppCompatActivity {

    private DB db;
    private User user;
    private Parkir parkir;

    private EditText inputAvailable;
    private Button btnSetAvailable;
    private Button btnParkirIn;
    private Button btnParkirOut;
    private TextView txtCapacity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(R.string.title_main);

        user = new User(this);
        db = new DB(this,user);
        parkir = db.getParkir();
        setView();

        btnSetAvailable.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setAvailable();
            }
        });
        btnParkirIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                parkirIn();
            }
        });
        btnParkirOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                parkirOut();
            }
        });
    }

    public void setView(){
        inputAvailable = (EditText) findViewById(R.id.inputAvailable);
        btnSetAvailable = (Button) findViewById(R.id.btnSetAvailable);
        btnParkirIn = (Button) findViewById(R.id.btnParkirIn);
        btnParkirOut = (Button) findViewById(R.id.btnParkirOut);
        txtCapacity = (TextView) findViewById(R.id.txtCapacity);

        inputAvailable.setText(String.valueOf(parkir.getAvailable()));
        txtCapacity.setText(String.valueOf(parkir.getCapacity()));
    }
    public void setAvailable(){
        int newAvailable=Integer.parseInt(inputAvailable.getText().toString());
        int lastAvailable = parkir.getAvailable();

        parkir.setAvailable(newAvailable);

        if(!db.setAvailable(parkir)){
            parkir.setAvailable(lastAvailable);
            inputAvailable.setText(String.valueOf(lastAvailable));
            Toast.makeText(getApplicationContext(), getString(R.string.signal1), Toast.LENGTH_SHORT).show();
        }
    }
    public void parkirIn(){
        int newAvailable=Integer.parseInt(inputAvailable.getText().toString())-1;
        int lastAvailable = parkir.getAvailable();

        parkir.setAvailable(newAvailable);

        if(!db.setAvailable(parkir)){
            parkir.setAvailable(lastAvailable);
            inputAvailable.setText(String.valueOf(lastAvailable));
            Toast.makeText(getApplicationContext(), getString(R.string.signal1), Toast.LENGTH_SHORT).show();
        }
        else
            inputAvailable.setText(String.valueOf(newAvailable));
    }
    public void parkirOut(){
        int newAvailable=Integer.parseInt(inputAvailable.getText().toString())+1;
        int lastAvailable = parkir.getAvailable();

        parkir.setAvailable(newAvailable);

        if(!db.setAvailable(parkir)){
            parkir.setAvailable(lastAvailable);
            inputAvailable.setText(String.valueOf(lastAvailable));
            Toast.makeText(getApplicationContext(), getString(R.string.signal1), Toast.LENGTH_SHORT).show();
        }
        else
            inputAvailable.setText(String.valueOf(newAvailable));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Intent i = new Intent(getApplicationContext(), PreferenceActivity.class);
            startActivity(i);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

}
