package teaminfamous.com.friendsend;

import android.content.Intent;
import android.location.Location;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class makeRequestDetails extends ActionBarActivity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {
    String sqlurl = "jdbc:postgresql://10.0.2.2/FriendSend?user=postgres&password=Batman4738473";
    GoogleApiClient mGoogleApiClient;
    Location origin;
    //private int package_id; // the package id
    private String package_name; // the name of the package to be sent
    private int sender_id; // the user_id of the package sender
    private String date_for_delivery; // the date for the package to be delivered.
    private int package_trust_level; // the level of trust for the package
    private String package_description; // the package description

    public  EditText pkg_name;
    public EditText pkg_date;
    public  EditText pkg_descrip;
    public EditText pkg_trust;

    public void SubmitRequest(View view){
        package_name  = pkg_name.getText().toString();
        date_for_delivery = pkg_date.getText().toString();
        package_description = pkg_descrip.getText().toString();
        if(pkg_trust.getText().toString().length() == 0) {
            package_trust_level = 0;
        }
        else {
            package_trust_level = Integer.parseInt(pkg_trust.getText().toString());
        }
        new AddPackageQuery().execute();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        setContentView(R.layout.activity_make_request_details);
        pkg_name = (EditText) findViewById(R.id.editName);
        pkg_date = (EditText) findViewById(R.id.editDate);
        pkg_descrip = (EditText) findViewById(R.id.editDescrip);
        pkg_trust = (EditText) findViewById(R.id.editTrust);


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_make_request_details, menu);
        Intent intent = getIntent();
        sender_id = intent.getIntExtra("user_id", 0);
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
    } // onOptionsItemSelected(MenuItem item)

    @Override
    public void onConnected(Bundle bundle) {
       origin = LocationServices.FusedLocationApi.getLastLocation(
                mGoogleApiClient);
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Toast t = Toast.makeText(getApplicationContext(),"Connection Failed To Get Location", Toast.LENGTH_LONG);
        t.show();
    }

    public class AddPackageQuery extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... voids) {
            try{
                Class.forName("org.postgresql.Driver");
            } catch (ClassNotFoundException e){
                e.printStackTrace();
            }

            Connection conn;
            try{
                Log.d("JakeDebug", "AddPackageQuery: ");
                DriverManager.setLoginTimeout(15);
                conn = DriverManager.getConnection(sqlurl);
                Statement st = conn.createStatement();
                Log.d("JakeDebug", "AddPackageQuery: just before query");

                //String query = "INSERT INTO _parcels_ (name, sender, deliv_date, trust_level, description) VALUES('Drugs', 10001, '9/11/2001', 666, 'Not suspicious');";
                String query = "INSERT INTO _parcels_ (name, sender, deliv_date, trust_level, description) VALUES('" + package_name + "', " + sender_id + ", '" +
                        date_for_delivery + "', " + package_trust_level + ", '" + package_description + "');"; //actual query
                Log.d("JakeDebug", "AddPackageQuery: query = \"" + query + "\"");
                st.executeQuery(query);
                Log.d("JakeDebug", "AddUserQuery: just after query");
                st.close();

            } catch (SQLException e) {
                e.printStackTrace();
            }
            return null;

        }
    }//end of add user

} // class makeRequestDetails
