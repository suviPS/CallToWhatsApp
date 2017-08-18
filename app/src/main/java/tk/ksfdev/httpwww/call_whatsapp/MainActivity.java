package tk.ksfdev.httpwww.call_whatsapp;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.CheckBox;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setUpPreferences();
    }




    public void onCheckClicked(View v){
        CheckBox checkBox = (CheckBox) v;
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
        if(checkBox.isChecked()){
            //enable
            sp.edit().putBoolean(MyUtils.PREF_ENABLE_SERVICE, true).commit();
        } else {
            //disable
            sp.edit().putBoolean(MyUtils.PREF_ENABLE_SERVICE, false).commit();
        }
    }


    private void setUpPreferences(){
        CheckBox checkBox = (CheckBox) findViewById(R.id.checkBox01);
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
        if(sp.getBoolean(MyUtils.PREF_ENABLE_SERVICE, false)){
            checkBox.setChecked(true);
        }

        //check permissions
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.WRITE_CONTACTS) != PackageManager.PERMISSION_GRANTED
                    || checkSelfPermission(Manifest.permission.PROCESS_OUTGOING_CALLS) != PackageManager.PERMISSION_GRANTED) {

                //Ask for permission
                requestPermissions(new String[]{Manifest.permission.WRITE_CONTACTS, Manifest.permission.PROCESS_OUTGOING_CALLS}, 1);

            } else {
                //ok
            }

        } else {
            //ok
        }
    }


    //When users responds
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        //code we used in requestPermissions()
        if (requestCode == 1) {

            if (grantResults.length > 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                //ok
            } else {
                //notify user that he's asshole
                new AlertDialog.Builder(this)
                        .setTitle("\tPermissions not granted")
                        .setMessage("One or more required permissions this application needs are missing." + "\n\nPlease go to: \nSettings/Apps/Call -> WhatsApp/Permissions" +
                                "\nand grant all required permissions for this application to work properly.")
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                            }
                        })
                        .show();
            }

        }

    }






    private void gotoEntryActivity(){
        Intent intent = new Intent(this, EntryActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    protected void onStop() {
        super.onStop();
        gotoEntryActivity();
    }
}
