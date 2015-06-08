package teaminfamous.com.friendsend;

import android.location.Location;
import android.os.AsyncTask;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class TrackPackage extends FragmentActivity {
    String sqlurl = "jdbc:postgresql://10.0.2.2/FriendSend?user=postgres&password=barry1";
    private GoogleMap mMap; // Might be null if Google Play services APK is not available.
    public Location markers[];
    int uid = 1; //once we get it ignore it

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_track_package);
        setUpMapIfNeeded();
        //get user id
        //all packages from datatbase
        new GetPackagesQuery().execute();


    }

    @Override
    protected void onResume() {
       super.onResume();
        setUpMapIfNeeded();
    }

    /**
     * Sets up the map if it is possible to do so (i.e., the Google Play services APK is correctly
     * installed) and the map has not already been instantiated.. This will ensure that we only ever
     * call {@link #setUpMap()} once when {@link #mMap} is not null.
     * <p/>
     * If it isn't installed {@link SupportMapFragment} (and
     * {@link com.google.android.gms.maps.MapView MapView}) will show a prompt for the user to
     * install/update the Google Play services APK on their device.
     * <p/>
     * A user can return to this FragmentActivity after following the prompt and correctly
     * installing/updating/enabling the Google Play services. Since the FragmentActivity may not
     * have been completely destroyed during this process (it is likely that it would only be
     * stopped or paused), {@link #onCreate(Bundle)} may not be called again so we should call this
     * method in {@link #onResume()} to guarantee that it will be called.
     */
    private void setUpMapIfNeeded() {
        // Do a null check to confirm that we have not already instantiated the map.
        if (mMap == null) {
            // Try to obtain the map from the SupportMapFragment.
            mMap = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map))
                    .getMap();
            // Check if we were successful in obtaining the map.
            if (mMap != null) {
                setUpMap();
            }
        }
    }

    /**
     * This is where we can add markers or lines, add listeners or move the camera. In this case, we
     * just add a marker near Africa.
     * <p/>
     * This should only be called once and when we are sure that {@link #mMap} is not null.
     */
    private void setUpMap() {
        mMap.addMarker(new MarkerOptions().position(new LatLng(0, 0)).title("Marker"));
    }

    public class GetPackagesQuery extends AsyncTask<Void, Void, String> {

        @Override
        protected String doInBackground(Void... voids) {
            try{
                Class.forName("org.postgresql.Driver");
            } catch (ClassNotFoundException e){
                e.printStackTrace();
            }

            Connection conn;
            try{
                Log.d("JakeDebug", "GetPackagesQuery: ");
                DriverManager.setLoginTimeout(15);
                conn = DriverManager.getConnection(sqlurl);
                Statement st = conn.createStatement();
                Log.d("JakeDebug", "AddPackageQuery: just before query");
                //String query = "INSERT INTO _parcels_ (name, sender, deliv_date, trust_level, description) VALUES('Drugs', 10001, '9/11/2001', 666, 'Not suspicious');";
                String query = "SELECT name, long, lat FROM _parcels_ where id=" + uid; //actual query
                Log.d("JakeDebug", "AddPackageQuery: query = \"" + query + "\"");
                ResultSet rs = st.executeQuery(query);
                Boolean empty = true;
                while(rs.next()){
                    empty = false;
                }
                //no matches
                if(empty){
                    Log.d("JakeDebug", "Login Query: empty = true");
                }
                Log.d("JakeDebug", " just after query");
                st.close();

            } catch (SQLException e) {
                e.printStackTrace();
            }
            return null;

        }
    }//end of location query




}
