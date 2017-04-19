package com.kgerlilng.crosswordcheater;

import android.content.res.Resources;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.io.File;
import java.io.InputStream;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MainActivity extends AppCompatActivity {


    TextView textView;
    EditText editText;
    Button button;
    Button button2;
    String searchList;
    String error = "Wörterbuch nicht gefunden";

    private class CwCheaterTask extends AsyncTask<String, Void, String> {

        StringBuffer searchResult = new StringBuffer();

        protected String createPatternString(String query) {
            String regex = "[A-Z]";
            char QuMark = '?';
            String ESign = "\\n";
            StringBuffer str = new StringBuffer();

            for (int i=0; i < query.length(); i++) {
                if (query.charAt(i) == QuMark)
                    str.append(regex);
                else
                    str.append(Character.toUpperCase(query.charAt(i)));

            }
            str.append(ESign);

            return str.toString();
        }

        @Override
        protected String doInBackground(String... notNeeded) {
            String search = createPatternString(String.valueOf(editText.getText()));
            Pattern pattern = Pattern.compile(search);
            Matcher matcher = pattern.matcher(searchList);
            while (matcher.find()) {
                searchResult.append(searchList.substring(matcher.start(), matcher.end() ));
                searchResult.append("\n");
            }
            return searchResult.toString();
        }

        @Override
        protected void onPostExecute (String result) {
            if (result.isEmpty()) {
                textView.setText("Kein Ergebnis gefunden");
            } else {
                textView.setText(result);
            }

        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        try {
            Resources res = getResources();
            InputStream in = res.openRawResource(R.raw.dictionary);

            byte[] b = new byte[in.available()];
            in.read(b);
            searchList = new String(b);
        } catch (Exception e) {
            e.printStackTrace();
        }



        textView = (TextView) findViewById(R.id.textView);
        textView.setMovementMethod(new ScrollingMovementMethod());
        editText = (EditText) findViewById(R.id.editText);
        button = (Button) findViewById(R.id.button);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CwCheaterTask task = new CwCheaterTask();
                task.execute(new String[] {});
            }
        });

        button2 = (Button) findViewById(R.id.button2);

        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editText.getText().clear();
                textView.setText("");
            }
        });


    }
}
