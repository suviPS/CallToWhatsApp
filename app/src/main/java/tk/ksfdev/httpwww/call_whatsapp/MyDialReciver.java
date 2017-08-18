package tk.ksfdev.httpwww.call_whatsapp;

import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.provider.ContactsContract;
import android.util.Log;
import android.widget.Toast;



public class MyDialReciver extends BroadcastReceiver {

    private Context mContext;

    @Override
    public void onReceive(Context arg0, Intent arg1) {
        mContext = arg0;

        //check in SharedPreferences if feature is Active
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(mContext);
        boolean enabled = sp.getBoolean(MyUtils.PREF_ENABLE_SERVICE, false);
        if(!enabled){
            //Receiver disabled
            return;
        }


        // Log call info
//        debugOut("arg0: " + arg0.toString());
//        debugOut("arg1: " + arg1.toString());
//        debugOut("isOrderedBroadcast = " + isOrderedBroadcast());
//        String num = arg1.getStringExtra(Intent.EXTRA_PHONE_NUMBER);
//        debugOut("Phone: " + num );
        //Notify user
//        Toast.makeText(arg0, "Call to number" + num + " Intercepted :)", Toast.LENGTH_SHORT).show();


        //get contact name from number
        String contactNumber = arg1.getStringExtra(Intent.EXTRA_PHONE_NUMBER);
        String contactName = "";

        Uri uri = ContactsContract.CommonDataKinds.Phone.CONTENT_URI;
        String[] projection    = new String[] {ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME,
                ContactsContract.CommonDataKinds.Phone.NUMBER};
        Cursor people = mContext.getContentResolver().query(uri, projection, null, null, null);

        int indexName = people.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME);
        int indexNumber = people.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER);

        boolean working = true;
        while (people.moveToNext() && working){
            String tempName = people.getString(indexName);
            String number = people.getString(indexNumber);
            if(number.equals(contactNumber)){
                contactName = tempName;
                working = false;
            }
        }


        //get ID of contact connected to WhatsApp and make call
        ContentResolver resolver = mContext.getContentResolver();
        Cursor cursor;
        cursor = resolver.query(
                ContactsContract.Data.CONTENT_URI,
                null, null, null,
                ContactsContract.Contacts.DISPLAY_NAME);

        working = true;
        while (cursor.moveToNext() && working) {
            long contactId = cursor.getLong(cursor.getColumnIndex(ContactsContract.Data._ID));
            String displayName = cursor.getString(cursor.getColumnIndex(ContactsContract.Data.DISPLAY_NAME));
            String mimeType = cursor.getString(cursor.getColumnIndex(ContactsContract.Data.MIMETYPE));

            //Log.d("TAG+++", displayName + " | " + mimeType);

            if(displayName.equals(contactName) && mimeType.equals("vnd.android.cursor.item/vnd.com.whatsapp.voip.call")) {
                //found it
                working = false;
                try {
                    //make WhatsApp call
                    Intent intent = new Intent();
                    intent.setAction(Intent.ACTION_VIEW);

                    //contactID goes at the end of /data/99999
                    intent.setDataAndType(Uri.parse("content://com.android.contacts/data/" + contactId),
                            "vnd.android.cursor.item/vnd.com.whatsapp.voip.call");
                    intent.setPackage("com.whatsapp");
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    mContext.startActivity(intent);


                    //disable call on main dialer
                    setResultData(null);

                } catch (Exception e){
                    e.printStackTrace();
                    Toast.makeText(mContext, "Error: "+ e.getMessage(), Toast.LENGTH_SHORT).show();
                }

            }
        }

        Log.d("TAG+++", "MyDialReciever done :D");

    }

    private static void debugOut(String str) {
        Log.i("TAG+++ DialerReceiver", str);
    }

}