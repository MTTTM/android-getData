package com.example.http1;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.facebook.drawee.backends.pipeline.Fresco;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.net.ssl.HttpsURLConnection;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{
    private Button mButton;
    private TextView mTextView;
    private  Button mParseDataBtn;
    private String result;
    List<LessionResult.Lesson> lessonlist=new ArrayList<>();
     private MyAdapter mAdapter = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Fresco.initialize(this);
        findViews();
        setListeners();
       initList();
    }
    private  void  initList(){
        mTextView.setText("");
        Context mContext= MainActivity.this;

        ListView list_dom = (ListView) findViewById(R.id.list_one);
        mAdapter = new MyAdapter(lessonlist, mContext);
        list_dom.setAdapter(mAdapter);
    }
    public  void  requestDataByGet(){
        mTextView.setText("请求中....");
        try {
            URL url=new URL("http://www.imooc.com/api/teacher?type=2&page=1");
            HttpURLConnection connection=(HttpURLConnection) url.openConnection();
            connection.setConnectTimeout(30*1000);
            connection.setRequestMethod("GET");
            connection.setRequestProperty("Content-Type","application/json");
            connection.setRequestProperty("Charset","UTF-8");
            connection.setRequestProperty("Accept-Charset","UTF-8");
            connection.connect();//发起连接
            Log.d("TAG", "GET====???");
            int responseCode=connection.getResponseCode();
            String reponseMessage=connection.getResponseMessage();

            if (responseCode==HttpsURLConnection.HTTP_OK){
                InputStream inputStream=connection.getInputStream();
                result=streamToString(inputStream);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        String utf8Str=decodeUnicode(result);
                        mTextView.setText(utf8Str);
                    }
                });
                if(lessonlist.size()>0){
                    lessonlist.clear();
                    mAdapter.notifyDataSetChanged();//更新数据
                }

            }

            else{
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mTextView.setText("请求失败");
                    }
                });

            }
        }catch (MalformedURLException e){
            Log.d("formed", "run:fdsdssf");
            e.printStackTrace();
        }catch (IOException e){
            Log.d("formed", "run:???");
            e.printStackTrace();
        }
    }
    @Override
    public void onClick(View view) {
        Log.d("TAG", ""+view.getId());
        switch (view.getId()){
            case R.id.get:
                Log.d("TAG", "GET====--22");
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        requestDataByGet();


                    }
                }).start();



                break;
            case R.id.parse:
                handleJSONData(result);
                break;
        }
    }
    private  void  findViews(){
        mButton=findViewById(R.id.get);
        mParseDataBtn=findViewById(R.id.parse);
        mTextView=findViewById(R.id.txt);



    }
    private  void setListeners(){
    mButton.setOnClickListener(this);
    mParseDataBtn.setOnClickListener(this);
    }
    private void handleJSONData(String result){
        mTextView.setText("");
        try{
            LessionResult lessonResult=new LessionResult();
            lessonlist.clear();//不能直接从新赋值新的列表对象，否则无法使用notifyDataSetChanged

            JSONObject jsonObject=new JSONObject(result);
            int status=jsonObject.getInt("status");
            JSONArray lessons=jsonObject.getJSONArray("data");
            if(lessons!=null&&lessons.length()>0){
                for (int index=0;index<lessons.length();index++){
                    JSONObject lesson=(JSONObject) lessons.get(index);
                    int id=lesson.getInt("id");
                    int learner=lesson.getInt("learner");
                    String name=lesson.getString("name");
                    String picSmall=lesson.getString("picSmall");
                    String picBig=lesson.getString("picBig");
                    String desc=lesson.getString("description");
                    LessionResult.Lesson lessonItem=new LessionResult.Lesson();
                    lessonItem.setId(id);
                    lessonItem.setName(name);
                    lessonItem.setPicSmall(picSmall);
                    lessonItem.setPicBig(picBig);
                    lessonItem.setDescription(desc);
                    lessonItem.setLearner(learner);
                    lessonlist.add(lessonItem);

                }
                lessonResult.setLessons(lessonlist);
                mAdapter.notifyDataSetChanged();//更新数据
//                Log.d("TAG", "toStr"+lessonResult.toString());
            }
        }catch (JSONException E){
            E.printStackTrace();
        }

    }
    private String streamToString(InputStream is) throws IOException
    {
        final char[] buffer = new char[0x10000];
        final StringBuilder out = new StringBuilder();
        final InputStreamReader in = new InputStreamReader(is);
        int read;
        do {
            read = in.read(buffer);
            if (read>0) {
                out.append(buffer, 0, read);
            }
        } while (read>=0);
        return out.toString();
    }
    /**
     * 将unicode字符转utf-8
     *
     * @param theString
     * @return
     */
    public  String decodeUnicode(String theString) {
        char aChar;
        int len = theString.length();
        StringBuffer outBuffer = new StringBuffer(len);
        for (int x = 0; x < len; ) {
            aChar = theString.charAt(x++);
            if (aChar == '\\') {
                aChar = theString.charAt(x++);
                if (aChar == 'u') {
                    // Read the xxxx
                    int value = 0;
                    for (int i = 0; i < 4; i++) {
                        aChar = theString.charAt(x++);
                        switch (aChar) {
                            case '0':
                            case '1':
                            case '2':
                            case '3':
                            case '4':
                            case '5':
                            case '6':
                            case '7':
                            case '8':
                            case '9':
                                value = (value << 4) + aChar - '0';
                                break;
                            case 'a':
                            case 'b':
                            case 'c':
                            case 'd':
                            case 'e':
                            case 'f':
                                value = (value << 4) + 10 + aChar - 'a';
                                break;
                            case 'A':
                            case 'B':
                            case 'C':
                            case 'D':
                            case 'E':
                            case 'F':
                                value = (value << 4) + 10 + aChar - 'A';
                                break;
                            default:
                                throw new IllegalArgumentException(
                                        "Malformed   \\uxxxx   encoding.");
                        }

                    }
                    outBuffer.append((char) value);
                } else {
                    if (aChar == 't')
                        aChar = '\t';
                    else if (aChar == 'r')
                        aChar = '\r';
                    else if (aChar == 'n')
                        aChar = '\n';
                    else if (aChar == 'f')
                        aChar = '\f';
                    outBuffer.append(aChar);
                }
            } else
                outBuffer.append(aChar);
        }
        return outBuffer.toString();
    }
}
