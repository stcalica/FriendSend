package teaminfamous.com.friendsend;

import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class makeRequestDetails extends ActionBarActivity {

    private int package_id; // the package id
    private String package_name; // the name of the package to be sent
    private int sender_id; // the user_id of the package sender
    private int receiver_id; // the user_id of the package receiver
    private String date_for_delivery; // the date for the package to be delivered.
    private int package_trust_level; // the level of trust for the package
    private String package_description; // the package description


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_make_request_details);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_make_request_details, menu);
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

    public class AddPackageQuery extends AsyncTask<Void, Void, String> {

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
                Log.d("JakeDebug", "AddPackageQuery: ");
                DriverManager.setLoginTimeout(15);
                conn = DriverManager.getConnection(sqlurl);
                Statement st = conn.createStatement();
                Log.d("JakeDebug", "AddUserQuery: just before query");
                String query = "INSERT INTO _parcels_ (   ) VALUES(" + package_id + ", '" + package_name + "');"; //actual query
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

} // class makeRequestDetails
