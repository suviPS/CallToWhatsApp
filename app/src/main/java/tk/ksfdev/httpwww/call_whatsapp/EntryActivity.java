package tk.ksfdev.httpwww.call_whatsapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

public class EntryActivity extends AppCompatActivity {

    private EditText editTextPass;

    private char[] myP = {'s', 't', 'r', 'o', 'n', 'g'};
    private StringBuilder sb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_entry);

        editTextPass = (EditText) findViewById(R.id.editTextPassword);

        //add pass to StringBuilder
        sb = new StringBuilder();
        for(char c: myP)
            sb.append(c);


        editTextPass.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                //check if we have a match
                if(charSequence.toString().equals(sb.toString())){
                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                    startActivity(intent);
                    finish();
                }

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

    }
}
