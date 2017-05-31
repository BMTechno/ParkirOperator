package com.tomcat.parkiroperator.DB;

import android.content.Context;
import android.util.Log;

import com.tomcat.parkiroperator.Object.Parkir;
import com.tomcat.parkiroperator.Object.User;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by albertbrucelee on 26/04/17.
 */

public class DB {

    DBHelper dbH; //DBHelper class buat ke server
    User user;

    private Context context;
    public DB(Context context, User user){
        this.context = context;
        this.user=user;
        dbH = new DBHelper(this.context, user.getUsername(), user.getPassword());

        //new DBCreate(context);
    }


    public Parkir getParkir(){
        int signal=dbH.getParkir();       //ambil data dari server. Nanti data akan disimpan dulu di DBServer. signal buat melihat apakah query sukses atau failed
        if (signal==0){                     //jika sukses
            JSONArray json=dbH.get();        //ambil data di DBServer! Data akan seperti: [{"nama":"Albert"}]

            Parkir parkir = new Parkir();

            try{
                    JSONObject jData = json.getJSONObject(0);   //ambil data object ke 0
                    parkir.setId(jData.getInt("id"));
                    parkir.setName(jData.getString("name"));
                    parkir.setAddress(jData.getString("address"));
                    parkir.setCapacity(jData.getInt("capacity"));
                    parkir.setAvailable(jData.getInt("available"));
            }catch (JSONException e){                       //jika JSON error
                signal=1;
                Log.e("DB JParser", "Error parsing data " + e.toString());    //pesan ke logcat
            }
            return parkir;
        }
        else        //jika tidak sukses ambil data (error);
            return null;
    }
    public boolean setAvailable(Parkir parkir){

        int signal=dbH.setAvailable(parkir.getId(), parkir.getAvailable());
        if (signal==0){
            return true;
        }
        return false;
    }
    public boolean setCapacity(Parkir parkir){
        int signal=dbH.setCapacity(parkir.getId(), parkir.getCapacity());
        if (signal==0){
            return true;
        }
        return false;
    }
    public boolean login(){
        int signal=dbH.login(user.getUsername(),user.getPassword());
        if (signal==0){
            JSONArray json=dbH.get();
            try{
                JSONObject jData = json.getJSONObject(0);
                user.setPassword(jData.getString("password"));
                user.setAuth();
                new DBCreate(context);
            }catch (JSONException e){
                Log.e("Login.class JSON Parser", "Error parsing data " + e.toString());
            }
            return true;
        }
        return false;
    }
    public int auth(){
        return dbH.auth(user.getUsername(),user.getPassword());
    }
}
