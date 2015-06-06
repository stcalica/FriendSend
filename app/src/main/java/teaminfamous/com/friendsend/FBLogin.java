package teaminfamous.com.friendsend;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookActivity;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.facebook.login.widget.ProfilePictureView;

import android.content.pm.Signature;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;
import android.os.AsyncTask;

public class FBLogin  extends ActionBarActivity{
    public CallbackManager callbackManager;
    LoginButton loginButton;
    JSONObject fbResponse;
    String fb_user_id;
    String fb_name;
    String sqlurl = "jdbc:postgresql://10.0.2.2/FriendSend?user=postgres&password=barry1";
//    String url = "jdbc:postgresql://10.0.2.2/test?user=postgres&password=barry1";

    public void PopulateFriends() {
        GraphRequest request = GraphRequest.newMeRequest(AccessToken.getCurrentAccessToken(), new GraphRequest.GraphJSONObjectCallback() {
            @Override
            public void onCompleted(JSONObject jsonObject, GraphResponse graphResponse) {
                Log.d("JSON OBJECT", jsonObject.toString());
                String uid = "";
                try {
                    uid = jsonObject.getString("id");
                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        });
    }//END OF POPULATE FRIENDS


    public void makePackageRequest(View view){
        Toast toast = Toast.makeText(getApplicationContext(), "Button Clicked!", Toast.LENGTH_LONG);
        toast.show();
        Intent intent = new Intent(FBLogin.this, makeRequestDetails.class);
        intent.putExtra();
        startActivity(intent);
    } //END OF PKG REQUEST INTENT/LISTENER

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());
        FacebookSdk.setIsDebugEnabled(true);
        setContentView(R.layout.activity_fblogin);//look up!
        callbackManager = CallbackManager.Factory.create();
        loginButton = (LoginButton) findViewById(R.id.login_button);
        loginButton.setReadPermissions("email", "user_friends", "public_profile");
        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                Toast toast = Toast.makeText(getApplicationContext(), "Successful Login", Toast.LENGTH_LONG);
                toast.show();
                GraphRequest request = GraphRequest.newMeRequest(AccessToken.getCurrentAccessToken(), new GraphRequest.GraphJSONObjectCallback() {
                    @Override
                    public void onCompleted(JSONObject jsonObject, GraphResponse graphResponse) {
                        Log.d("JSON OBJECT", jsonObject.toString());
                        String uid = "";
                        String name = "";
                        try {
                            uid = jsonObject.getString("id");
                            name = jsonObject.getString("first_name");

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        ProfilePictureView profilePictureView;
                        profilePictureView = (ProfilePictureView) findViewById(R.id.propic);
                        profilePictureView.setProfileId(uid);
                        fb_user_id = uid;
                        fb_name = name;
                        new LoginQuery().execute();
                        //new LocationQuery().execute();
                        //Toast t = Toast.makeText(getApplicationContext(), graphResponse.toString(), Toast.LENGTH_LONG );
                        //t.show();
                        //createUser(uid);
                        //Toast t = Toast.makeText(getApplicationContext(), JSONObject responseJSON = new JSONObject(jsonObject);
                        // graph response just has jsonObject and Status Code connected to itaphResponse.toString(), Toast.LENGTH_LONG);
                        // t.show();
                    }
                });
                request.executeAsync();

            }//END OF ON SUCCESS

            @Override
            public void onCancel() {
                Toast toast = Toast.makeText(getApplicationContext(), "Login Cancelled", Toast.LENGTH_LONG);
                toast.show();
            }

            @Override
            public void onError(FacebookException exception) {
                Toast toast = Toast.makeText(getApplicationContext(), "Error Login", Toast.LENGTH_LONG);
                toast.show();

            }
        }); // end of login stuff



                // Toast toast = Toast.makeText(getApplicationContext(), "End of onCreate", Toast.LENGTH_LONG);
                // toast.show();


            } //END OF ON CREATE
            @Override
            protected void onActivityResult(int requestCode, int resultCode, Intent data) {
                super.onActivityResult(requestCode, resultCode, data);
                callbackManager.onActivityResult(requestCode, resultCode, data);
            }

            @Override
            public boolean onCreateOptionsMenu(Menu menu) {
                // Inflate the menu; this adds items to the action bar if it is present.
                getMenuInflater().inflate(R.menu.menu_fblogin, menu);
                return true;
            }

            @Override
            public boolean onOptionsItemSelected(MenuItem item) {
                // Handle action bar item clicks here. The action bar will
                // automatically handle clicks on the Home/Up button, so long
                // as you specify a parent activity in AndroidManifest.xml.
                int id = item.getItemId();

                //noinspection SimplifiableIfStatement
                if (id == R.id.action_settings) {
                    return true;
                }

                return super.onOptionsItemSelected(item);
            }

            @Override
            protected void onDestroy() {
                LoginManager.getInstance().logOut();
                Toast toast = Toast.makeText(getApplicationContext(), "Facebook Logout", Toast.LENGTH_LONG);
                super.onDestroy();
            }


        public class LoginQuery extends AsyncTask<Void, Void, String> {

            @Override
            protected String doInBackground(Void... voids) {

                String retr = "";
                try{
                    Class.forName("org.postgresql.Driver");

                } catch (ClassNotFoundException e){

                    e.printStackTrace();
                    retr = e.toString();
                }

               // String url = "jdbc:postgresql://10.0.2.2/test?user=postgres&password=barry1";
                // String url = "jdbc:postgresql://10.0.2.2/dbname?user=username&password=pass";
                Connection conn;
                try{
                    DriverManager.setLoginTimeout(15);
                    conn = DriverManager.getConnection(sqlurl);
                    Statement st = conn.createStatement();
                    String query = "SELECT * FROM _users_ where id=" + fb_user_id; //actual query
                    ResultSet rs = st.executeQuery(query);
                    Boolean empty = true;
                    while(rs.next()){
                        retr = rs.getString(1); //put column that you want value from
                        empty = false;
                    }
                    //no matches
                    if(empty){
                        Log.d("JakeDebug", "Login Query: empty = true");
                        retr = null;
                        new AddUserQuery().execute();
                    }

                    rs.close();
                    st.close();


                } catch (SQLException e) {
                    e.printStackTrace();
                    retr = e.toString();
                }
                return retr;

            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
            }
        }//end of login query
    public class AddUserQuery extends AsyncTask<Void, Void, String> {

        @Override
        protected String doInBackground(Void... voids) {
            String retr = "";
            try{
                Class.forName("org.postgresql.Driver");

            } catch (ClassNotFoundException e){

                e.printStackTrace();
                retr = e.toString();
            }

            Connection conn;
            try{
                Log.d("JakeDebug", "AddUserQuery: just inside try");
                DriverManager.setLoginTimeout(15);
                conn = DriverManager.getConnection(sqlurl);
                Statement st = conn.createStatement();
                Log.d("JakeDebug", "AddUserQuery: just before query");
                String query = "INSERT INTO _users_ VALUES(" + fb_user_id + ", '" + fb_name + "');"; //actual query
                ResultSet rs = st.executeQuery(query);
                Log.d("JakeDebug", "AddUserQuery: just after query");
                Boolean empty = true;
                while(rs.next()){
                    Log.d("JakeDebug", "Inside while: " + rs.getString("name"));
                    retr = rs.getString("name"); //column data wanted or amount
                    empty = false;
                }
                //no matches
                if(empty){

                    retr = null;
                }

                rs.close();
                st.close();


            } catch (SQLException e) {
                e.printStackTrace();
                retr = e.toString();
            }
            return retr;

        }
    }//end of add user


    public class ParcelQuery extends AsyncTask<Void, Void, String> {

            @Override
            protected String doInBackground(Void... voids) {
                    String retr = "";
                    try{
                        Class.forName("org.postgresql.Driver");

                    } catch (ClassNotFoundException e){

                        e.printStackTrace();
                        retr = e.toString();
                    }

                    Connection conn;
                    try{
                        DriverManager.setLoginTimeout(15);
                        conn = DriverManager.getConnection(sqlurl);
                        Statement st = conn.createStatement();
                        String query = "SELECT * FROM _user_"; //actual query
                        ResultSet rs = st.executeQuery(query);
                        Boolean empty = true;
                        while(rs.next()){
                            retr = rs.getString("name"); //column data wanted or amount
                            empty = false;
                        }
                        //no matches
                        if(empty){

                            retr = null;
                        }

                        rs.close();
                        st.close();


                    } catch (SQLException e) {
                        e.printStackTrace();
                        retr = e.toString();
                    }
                    return retr;

                }
            }

}//end of activity


