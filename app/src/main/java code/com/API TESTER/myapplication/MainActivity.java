package com.example.myapplication;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewManager;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;
import java.util.Scanner;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemSelectedListener,AlertDialogueBox.NoticeDialogueListener {
    private Spinner spinner;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private static final String[] Choices = {"GET", "POST", "PUT","PATCH","DELETE"};
    private String requestMethod;
    private EditText editText1;
    private EditText editText2;
    private final ArrayList<View> layout = new ArrayList<>();
    private final ArrayList<Button> rmvButton = new ArrayList<>();
    private final ArrayList<String> keys = new ArrayList<>();
    private final ArrayList<String> values = new ArrayList<>();
    private HttpURLConnection urlConnection;
    @Override
    public void onClick(View view) {
        AlertDialogueBox popUp = AlertDialogueBox.getInstance();
        popUp.show(getSupportFragmentManager(),"Add Parameter");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        editText1 = findViewById(R.id.key);
        editText2 = findViewById(R.id.value);
        Window window = this.getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.setStatusBarColor(this.getResources().getColor(R.color.green));
        spinner = findViewById(R.id.spinner);
        ArrayAdapter<String>adapter = new ArrayAdapter<>(MainActivity.this,
                android.R.layout.simple_spinner_item, Choices);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);
        tabLayout = findViewById(R.id.tab_layout);
        viewPager = findViewById(R.id.pager);
        tabLayout.setupWithViewPager(viewPager);
        tabLayout.addTab(tabLayout.newTab().setText("Request"));
        tabLayout.addTab(tabLayout.newTab().setText("Response"));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
        final MyAdapter myAdapter = new MyAdapter(getSupportFragmentManager(), tabLayout.getTabCount());
        viewPager.setAdapter(myAdapter);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        switch (i) {
            case 0:
                requestMethod = "GET";
                break;
            case 1:
                requestMethod = "POST";
                break;
            case 2:
                requestMethod = "PUT";
                break;
            case 4:
                requestMethod = "PATCH";
                break;
            default:
                requestMethod = "DELETE";

        }

    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {
            requestMethod = "GET";
    }

    @Override
    public void onDialogPositiveClick(DialogFragment dialog) {
        EditText editText1 = Objects.requireNonNull(dialog.getDialog()).findViewById(R.id.key);
        EditText editText2 = Objects.requireNonNull(dialog.getDialog()).findViewById(R.id.value);
        if((!editText1.getText().toString().isEmpty()&&!editText2.getText().toString().isEmpty())){
               LinearLayout linearLayout = findViewById(R.id.container);
               LinearLayout temporaryLayout = new LinearLayout(this);
               LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,LinearLayout.LayoutParams.WRAP_CONTENT);
               TextView key = new TextView(this);
               lp.setMargins(20,20,200,5);
               key.setPadding(0,20,0,0);
               lp.gravity = Gravity.CENTER_HORIZONTAL;
               key.setLayoutParams(lp);
               temporaryLayout.setGravity(Gravity.CENTER_HORIZONTAL);
               key.setText(editText1.getText().toString());
               TextView value = new TextView(this);
               value.setText(editText2.getText().toString());
               value.setPadding(0,20,0,0);
               value.setLayoutParams(lp);
               Button removeBtn = new Button(this);
               removeBtn.setText("X");
               removeBtn.setBackgroundColor(getResources().getColor(R.color.green));
               removeBtn.setTextColor(Color.WHITE);
                LinearLayout.LayoutParams lp1 = new LinearLayout.LayoutParams(100,100);
                lp1.setMargins(100,0,0,5);
               removeBtn.setLayoutParams(lp1);
               rmvButton.add(removeBtn);
               keys.add(editText1.getText().toString());
               values.add(editText2.getText().toString());
               layout.add(temporaryLayout);
               removeBtn.setOnClickListener((view)->{
                   keys.remove(rmvButton.indexOf(view));
                   values.remove(rmvButton.indexOf(view));
                   linearLayout.removeView(layout.get(rmvButton.indexOf(view)));
                   layout.remove(rmvButton.indexOf(view));
                   rmvButton.remove(view);
               });
               temporaryLayout.addView(key);
               temporaryLayout.addView(value);
               temporaryLayout.addView(removeBtn);
               linearLayout.addView(temporaryLayout);
               dialog.dismiss();
        }
        else{
            if(editText1.getText().toString().isEmpty()) {
                editText1.setHighlightColor(Color.RED);
                editText1.setHint("Please Enter Key");
                editText1.setHintTextColor(Color.RED);
            }
            if(editText2.getText().toString().isEmpty()) {
                editText2.setHighlightColor(Color.RED);
                editText2.setHint("Please Enter Value");
                editText2.setHintTextColor(Color.RED);
            }
        }
    }

    @Override
    public void onDialogNegativeClick(DialogFragment dialog) {
         dialog.dismiss();
    }
    @SuppressLint("SetTextI18n")
    public void sendRequest(View view) {
        EditText urlTextField = findViewById(R.id.url);
        URL url;
        if(!urlTextField.getText().toString().isEmpty()) {
            try {
                if (urlTextField.getText().toString().contains("http")) {
                    url = new URL(urlTextField.getText().toString());
                } else {
                    url = new URL("http://" + urlTextField.getText());
                }
                System.out.println(url);
                urlConnection = (HttpURLConnection) url.openConnection();
                switch (requestMethod) {
                    case "GET":
                        sendHTTPRequest(urlConnection);
                        break;
                    case "POST":
                        urlConnection.setRequestMethod("POST");
                        sendHTTPRequest(urlConnection);
                        break;
                    case "PUT":
                        urlConnection.setRequestMethod("PUT");
                        sendHTTPRequest(urlConnection);
                        break;
                    case "PATCH":
                        urlConnection.setRequestMethod("PATCH");
                        sendHTTPRequest(urlConnection);
                        break;
                    case "DELETE":
                        urlConnection.setRequestMethod("DELETE");
                        sendHTTPRequest(urlConnection);
                        break;
                }


            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        else{
            urlTextField.setHintTextColor(Color.RED);
            urlTextField.setHint("No URL Entered");
        }
    }
    void sendHTTPRequest(HttpURLConnection urlConnection){
        try {
            urlConnection.setRequestProperty("Content-Type","application/json; charset=UTF-8");
            urlConnection.setRequestProperty("Accept","application/json");
            String jsonData = "{";
            for(int i = 0;i<keys.size();i++){
                jsonData+="\"";
                jsonData += keys.get(i);
                jsonData += "\"";
                jsonData += ":";
                jsonData+= "\"";
                jsonData +=values.get(i);
                jsonData+="\"";
                jsonData+=",";
            }
            jsonData = jsonData.substring(0,jsonData.length()-1);
            jsonData+="}";
            ConnectivityManager connectivityManager = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
            if(networkInfo!=null) {
                new SendTask().execute(jsonData);
            }
            else{
              Toast.makeText(this,"No Connection",Toast.LENGTH_LONG).show();
            }
            } finally {
                urlConnection.disconnect();
            }
    }
    @SuppressLint("StaticFieldLeak")
    private class ReceiveTask extends AsyncTask<String,Integer,String>{

        @Override
        protected String doInBackground(String... strings) {
            BufferedReader br = null;
            StringBuilder response = new StringBuilder();
            try {
                br = new BufferedReader(new InputStreamReader(urlConnection.getInputStream(),StandardCharsets.UTF_8));
            String responseLine = null;
            while((responseLine=br.readLine())!=null){
                response.append(responseLine.trim());
            }
            } catch (IOException e) {
                e.printStackTrace();
            }
            return response.toString();
        }
        @Override
        protected void onPostExecute(String result){
            super.onPostExecute(result);
            TextView responseTextView = findViewById(R.id.editTextTextPersonName);
            responseTextView.setText(result);
            viewPager.setCurrentItem(1);
        }
    }
    @SuppressLint("StaticFieldLeak")
    private class SendTask extends AsyncTask<String,Integer,String>{

        @Override
        protected String doInBackground(String... strings) {
            String jsonData = strings[0];
            try{
                if(keys.size()>0){
                    urlConnection.setDoOutput(true);
                    OutputStream os = urlConnection.getOutputStream();
                    byte[] input = jsonData.getBytes(StandardCharsets.UTF_8);
                    os.write(input,0,input.length);
                }
            }
            catch(IOException e){
                System.out.println(e.getMessage());
            }
            return jsonData;
        }
        @Override
        protected void onPostExecute(String result){
            super.onPostExecute(result);
            new ReceiveTask().execute();
        }
    }
}