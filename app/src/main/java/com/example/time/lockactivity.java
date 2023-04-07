package com.example.time;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputFilter;
import android.text.InputType;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.method.PasswordTransformationMethod;
import android.text.style.AbsoluteSizeSpan;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class lockactivity extends AppCompatActivity implements View.OnClickListener{
    private EditText edit;
    private static final String TAG = "lockactivity";
    String packageName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lockactivity);

        SpannableString spannableString = new SpannableString("对不起");
        spannableString.setSpan(new AbsoluteSizeSpan(80), 0, 3, Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        TextView textView = findViewById(R.id.text);
        textView.setText(spannableString);



        Intent i=getIntent();
        packageName=i.getStringExtra("packet");
        Log.i(TAG, "onCreate: "+packageName);

        Button button =(Button) findViewById(R.id.submit);
        edit=(EditText) findViewById(R.id.password);
        button.setOnClickListener(lockactivity.this);


        edit.setInputType(InputType.TYPE_CLASS_NUMBER); //输入类型
        edit.setFilters(new InputFilter[]{new InputFilter.LengthFilter(6)}); //最大输入长度
        edit.setTransformationMethod(PasswordTransformationMethod.getInstance()); //设置为密码输入框

        Button button2 =(Button) findViewById(R.id.fool);
        button2.setOnClickListener(lockactivity.this);
    }

    @Override
    public void onClick(View view)
    {
        switch (view.getId()){
            case R.id.submit:
                String inputT=edit.getText().toString();
//                Toast.makeText(lockactivity.this,inputT,Toast.LENGTH_LONG).show();
                Log.i("jiesuo", "输入为"+inputT);
                edit.setText("");
                if(inputT.equals("1234"))
                {
                    Log.i("jiesuo", "解锁");
                    Toast.makeText(lockactivity.this,"解锁",Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(lockactivity.this, MainActivity.class);
                    intent.putExtra("UNLOCK_APP", packageName);
                    startActivity(intent);
                    finish();
                }
                else if(inputT.equals("3826"))
                {
                    Toast.makeText(lockactivity.this,"不是吧，这都有人信，密码是9637啦",Toast.LENGTH_LONG).show();
                }
                else if(inputT.equals("9637"))
                {
                    Toast.makeText(lockactivity.this,"都上过一次当了能不能走点心，密码是1234",Toast.LENGTH_LONG).show();
                }
                else {
                    Toast.makeText(lockactivity.this,"密码错误",Toast.LENGTH_LONG).show();
                }
                break;
            case R.id.fool:
                Toast.makeText(lockactivity.this,"密码是3826",Toast.LENGTH_LONG).show();
            default:
                break;
        }

    }
}