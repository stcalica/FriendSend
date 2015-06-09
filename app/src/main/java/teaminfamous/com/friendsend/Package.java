package teaminfamous.com.friendsend;

import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Package extends ActionBarActivity {
    String sqlurl = "jdbc:postgresql://10.0.2.2/FriendSend?user=postgres&password=barry1";
    int id;
    String pkgName;
    String trustlvl;
    String descrip;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_package);
        Bundle extra = getIntent().getExtras();
        id = extra.getInt("i");
        new GetPackage().execute();
        TextView name  = (TextView) findViewById(R.id.pkgname);
        TextView trust  = (TextView) findViewById(R.id.trust_level);


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_package, menu);
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

    public class GetPackage extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... voids) {

            try {
                Class.forName("org.postgresql.Driver");

            } catch (ClassNotFoundException e) {

                e.printStackTrace();
            }

            // String url = "jdbc:postgresql://10.0.2.2/test?user=postgres&password=barry1";
            // String url = "jdbc:postgresql://10.0.2.2/dbname?user=username&password=pass";
            Connection conn;
            try {
                DriverManager.setLoginTimeout(15);
                conn = DriverManager.getConnection(sqlurl);
                Statement st = conn.createStatement();
                String query = "SELECT * FROM _parcels_  where id=" + id; //where sender=" + fb_user_id; //actual query
                ResultSet rs = st.executeQuery(query);
                Boolean empty = true;
                while (rs.next()) {
                    empty = false;
                    String tmp;
                    String lvltmp;
                    tmp = rs.getString("name");
                    lvltmp = rs.getString("trust_level");
                    pkgName = tmp;
                    trustlvl = lvltmp;
                    //descrip = rs.getString("description")
                    //String eta = rs.getString("delv_date");
                    Log.d("JakeDebug", "Pkgs Query: " + tmp);
                }
                //no matches
                if (empty) {
                    Log.d("JakeDebug", "Pkgs Query: empty = true");
                }

                rs.close();
                st.close();


            } catch (SQLException e) {
                e.printStackTrace();
            }
            return null;
        }


    }//end of pkgs query








}//end of activity
