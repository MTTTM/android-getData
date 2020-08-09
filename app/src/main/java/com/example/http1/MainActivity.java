package com.example.http1;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{
    private Button mButton;
    private TextView mTextView;
    private  Button mParseDataBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViews();
        setListeners();
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
                        Log.d("TAG", "GET====--33");
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
                                final String result=streamToString(inputStream);
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        String utf8Str=decodeUnicode(result);
                                        mTextView.setText(utf8Str);
                                    }
                                });
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
                }).start();



                break;
            case R.id.parse:
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
