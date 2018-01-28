package com.crypt.asus.secret;

import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Base64;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Properties;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import javax.mail.Authenticator;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;

import com.crypt.asus.secret.R;


public class MyActivity extends Activity {


    EditText input,key,email;
    Button encrypt,decrypt,send,copy;
    RadioGroup select;
    CheckBox key_select;
    TextView output;
    private ClipboardManager copy_text;
    private ClipData myclip;
    static int value=1;
    private ProgressBar p;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my);

        input=(EditText)findViewById(R.id.input);
        key=(EditText)findViewById(R.id.key);
        email=(EditText)findViewById(R.id.email);
        key_select=(CheckBox)findViewById(R.id.key_option);
        encrypt=(Button)findViewById(R.id.encrypt_button);
        decrypt=(Button)findViewById(R.id.decrypt_button);
        send=(Button)findViewById(R.id.send_button);
        copy=(Button)findViewById(R.id.copy_button);
        select=(RadioGroup)findViewById(R.id.select);
        output=(TextView)findViewById(R.id.output);
        decrypt.setEnabled(false);
        key.setEnabled(false);
        email.setEnabled(false);
        key.setBackgroundColor(Color.TRANSPARENT);
        email.setBackgroundColor(Color.TRANSPARENT);
        copy_text=(ClipboardManager)getSystemService(CLIPBOARD_SERVICE);
        key.setHint("Enter Secret Key");
        p=(ProgressBar)findViewById(R.id.progressBar);

        key.setOnFocusChangeListener(new View.OnFocusChangeListener(){

            @Override
            public void onFocusChange(View view, boolean b) {
                if(b)
                    key.setHint("");
            }
        });
        email.setOnFocusChangeListener(new View.OnFocusChangeListener() {

            @Override
            public void onFocusChange(View view, boolean b) {
                if (b)
                    email.setHint("");
            }
        });







        select.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId)
                {
                    case R.id.encryption:
                        encrypt.setEnabled(true);
                        decrypt.setEnabled(false);
                        input.setText("");
                        output.setText("");
                        email.setText("");
                        send.setEnabled(true);
                        email.setEnabled(false);
                        break;
                    case R.id.decryption:
                        decrypt.setEnabled(true);
                        encrypt.setEnabled(false);
                        input.setText("");
                        output.setText("");
                        send.setEnabled(false);
                        email.setEnabled(false);
                        email.setText("");
                        email.setHint("Enter your email id....");
                       // email.setHintTextColor(Color.parseColor("#ff6a6a6a"));
                        email.setBackgroundColor(Color.TRANSPARENT);
                        break;
                }
            }
        });

       key_select.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               if(key_select.isChecked())
               {
                   key.setEnabled(true);
                   key.setHint("Enter Secret Key");
                   key.setBackgroundResource(R.drawable.b4);
               }
               else
               {
                   key.setEnabled(false);
                   key.setText("");
                   key.setHint("Enter Secret Key");
                   key.setBackgroundColor(Color.TRANSPARENT);
               }
           }
       });

        encrypt.setOnClickListener(encryption);
        decrypt.setOnClickListener(decryption);
        copy.setOnClickListener(copytext);
        send.setOnClickListener(sendmail);

    }

    private static SecretKeySpec secretKey;
    private static byte[] key1;

    public static void setKey(String myKey)
    {
        MessageDigest sha = null;
        try {
            key1 = myKey.getBytes("UTF-8");
            sha = MessageDigest.getInstance("SHA-1");
            key1 = sha.digest(key1);
            key1 = Arrays.copyOf(key1, 16);
            secretKey = new SecretKeySpec(key1, "AES");
        }
        catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }


    public String encryptText_Nokey(String s){

        String secret="chintanmahadik";
        try
        {
            setKey(secret);
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
            cipher.init(Cipher.ENCRYPT_MODE, secretKey);
            return Base64.encodeToString(cipher.doFinal(s.getBytes("UTF-8")),Base64.DEFAULT);
        }
        catch (Exception e)
        {
            System.out.println("Error while encrypting: " + e.toString());
        }
        return null;

    }
    public String encryptText_withkey(String s, String k)
    {
        String secret=k;
        try
        {
            setKey(secret);
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
            cipher.init(Cipher.ENCRYPT_MODE, secretKey);
            return Base64.encodeToString(cipher.doFinal(s.getBytes("UTF-8")),Base64.DEFAULT);
        }
        catch (Exception e)
        {
            System.out.println("Error while encrypting: " + e.toString());
        }
        return null;
    }

    public String decryptText_Nokey(String s){
        String secret="chintanmahadik";
        try
        {
            setKey(secret);
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5PADDING");
            cipher.init(Cipher.DECRYPT_MODE, secretKey);
            byte[] txt=Base64.decode(s,Base64.DEFAULT);

            return new String(cipher.doFinal(txt));
        }
        catch (Exception e)
        {
            System.out.println("Error while decrypting: " + e.toString());
        }
        return null;

    }
    public String decryptText_withkey(String s, String k)
    {
        String secret=k;
        try
        {
            setKey(secret);
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5PADDING");
            cipher.init(Cipher.DECRYPT_MODE, secretKey);
            byte[] txt=Base64.decode(s,Base64.DEFAULT);
            return new String(cipher.doFinal(txt));
        }
        catch (Exception e)
        {
            System.out.println("Error while decrypting: " + e.toString());
            Toast.makeText(getApplicationContext(),"Enter Valid Key for Encryption ",Toast.LENGTH_SHORT).show();
        }
        return null;
    }



    Button.OnClickListener copytext = new View.OnClickListener() {
        @Override
        public void onClick(View view) {


            String out=output.getText().toString();
            myclip=ClipData.newPlainText("text",out);
            copy_text.setPrimaryClip(myclip);

            Toast.makeText(getApplicationContext(),"Text Copied",Toast.LENGTH_SHORT).show();

        }
    };


    Button.OnClickListener encryption = new View.OnClickListener() {
        @Override
        public void onClick(View view) {

            if(!key.isEnabled() && !key_select.isChecked())
            {
                String input_text=input.getText().toString();

                String output_text= null;
                try {
                    output_text = encryptText_Nokey(input_text);
                } catch (Exception e) {
                    e.printStackTrace();
                }

                output.setText(output_text);
                email.setEnabled(true);
                email.setHint("Enter your email id...");
                email.setBackgroundResource(R.drawable.b4);

            }
            else
            {
                String input_text=input.getText().toString();
                String key_text=key.getText().toString();
                if(key_text.isEmpty())
                {
                    Toast.makeText(getApplicationContext(),"Enter Valid Key for Encryption",Toast.LENGTH_SHORT).show();
                }
                else {
                    String output_text = encryptText_withkey(input_text, key_text);
                    output.setText(output_text);
                    email.setEnabled(true);
                    email.setHint("Enter your email id...");
                    email.setBackgroundResource(R.drawable.b4);

                }
            }
        }
    };

    Button.OnClickListener decryption = new View.OnClickListener() {
        @Override
        public void onClick(View view) {

            if(!key.isEnabled() && !key_select.isChecked())
            {
                String input_text=input.getText().toString();

                String output_text= null;
                try {
                    output_text = decryptText_Nokey(input_text);
                } catch (Exception e) {
                    e.printStackTrace();
                }

                output.setText(output_text);

            }
            else
            {
                String input_text=input.getText().toString();
                String key_text=key.getText().toString();
                if(key_text.isEmpty())
                {
                    Toast.makeText(getApplicationContext(),"Enter Valid Key for Decryption",Toast.LENGTH_SHORT).show();
                }
                else {
                    String output_text = decryptText_withkey(input_text, key_text);
                    output.setText(output_text);

                }

            }
        }
    };

    Button.OnClickListener sendmail=new View.OnClickListener() {
        @Override
        public void onClick(View view) {

        try{

            p.setVisibility(View.VISIBLE);
          SendMail s1=new SendMail();
           s1.execute(key.getText().toString(), output.getText().toString(), email.getText().toString());
        }
        catch(Exception e){
            Toast.makeText(getApplicationContext(), "Email Sending Failed", Toast.LENGTH_LONG).show();

        }
        }
    };

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.my, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }




    ///////////////////////////////////////////ASYNC TASK//////////////////////////////////////////

    class SendMail extends AsyncTask<String,Integer,String>
    {

        @Override
        protected String doInBackground(String... strings) {
            final String fromEmail = "10encrypt.decrypt01@gmail.com"; //requires valid gmail id
            final String password = "Cipher@1"; // correct password for gmail id
            final String toEmail = strings[2]; // can be any email id

            System.out.println("TLSEmail Start");
            try {

                Properties props = new Properties();
                props.put("mail.smtp.host", "smtp.gmail.com"); //SMTP Host
                props.put("mail.smtp.port", "587"); //TLS Port
                props.put("mail.smtp.auth", "true"); //enable authentication
                props.put("mail.smtp.starttls.enable", "true"); //enable STARTTLS


                Authenticator auth = new Authenticator() {
                    //override the getPasswordAuthentication method
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(fromEmail, password);
                    }
                };
                Session session = Session.getInstance(props, auth);

                String msgbody = "<html>" +
                        "<body>" +
                        "<table border='2' >" +
                        "<tr>" +
                        "<td>KEY</td>" +
                        "<td>" + strings[0] + "</td>" +
                        "</tr>" +
                        "<tr>" +
                        "<td>Encrypted Text</td>" +
                        "<td>" + strings[1] + "</td>" +
                        "</table>" +
                        "</body></html>";

                EmailUtil.sendEmail(session, toEmail, "Encrypted Text ", msgbody);

            }
            catch(Exception e)
            {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPreExecute() {

            System.out.println("Pre Execute");

            Toast.makeText(MyActivity.this, "Sending Mail", Toast.LENGTH_LONG).show();
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            super.onPreExecute();

        }

        @Override
        protected void onPostExecute(String result) {
            System.out.println("Post Execute");
            p.setVisibility(View.GONE);
            if(value==1) {
                Toast.makeText(MyActivity.this, "Mail Sent", Toast.LENGTH_LONG).show();
                email.setText("");
            }else
            {
                Toast.makeText(MyActivity.this, "Invalid Email Id", Toast.LENGTH_LONG).show();

            }


        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);

        }
    }

}
