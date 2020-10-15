package com.example.wastedfoodteam.detailproduct;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.example.wastedfoodteam.DirectionParser;
import com.example.wastedfoodteam.R;
import com.example.wastedfoodteam.global.Variable;
import com.example.wastedfoodteam.source.model.Product;
import com.example.wastedfoodteam.source.model.Seller;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class FragmentDetailProduct extends Fragment implements OnMapReadyCallback {
    private GoogleMap mMap;
    private Product product;
    private Seller seller;
    ImageView ivProduct;
    TextView tvName, tvQuantity;
    private final static int MY_PERMISSIONS_REQUEST = 32;

    public FragmentDetailProduct() {
    }

    public FragmentDetailProduct(Product product) {
        this.product = product;

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        String apikey = getString(R.string.maps_api_key);
        View view = inflater.inflate(R.layout.fragment_product_detail, container, false);
        ivProduct = view.findViewById(R.id.ivProductFPD);
        tvName = view.findViewById(R.id.tvProductNameFPD);
        tvQuantity = view.findViewById(R.id.tvQuantityFPD);


        tvName.setText(product.getName());
        tvQuantity.setText("Quantity: " + product.getRemain_quantity() + "/" + product.getOriginal_quantity());
        Picasso.get().load(product.getImage().isEmpty() ? Variable.noImageUrl : product.getImage()).into(ivProduct);

        LatLng here = new LatLng(21.013255, 105.5248756);
        LatLng end = new LatLng(21.0092414, 105.528148);
        float[] distanceB = new float[1];
        Location.distanceBetween(here.latitude, here.longitude, end.latitude, end.longitude, distanceB);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) this.getChildFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        return view;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.getUiSettings().setZoomControlsEnabled(true);
        mMap.getUiSettings().setMyLocationButtonEnabled(true);


        // Add a marker in fptUniversity and move the camera
        LatLng fptUniversity = new LatLng(21.013255, 105.5248756);
        mMap.addMarker(new MarkerOptions().position(fptUniversity).title("Bạn ở đây"));
        mMap.addMarker(new MarkerOptions().position(new LatLng(Variable.seller.getLatitude(), Variable.seller.getLongitude())).title(Variable.seller.getName()));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(fptUniversity, 16f));
        new TaskDirectionRequest().execute(buildRequestUrl(fptUniversity,new LatLng(Variable.seller.getLatitude(), Variable.seller.getLongitude())));
    }

    private void requestPermission(String permission) {
        if (ContextCompat.checkSelfPermission(getActivity().getApplicationContext(), permission) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(),
                    new String[]{permission},
                    MY_PERMISSIONS_REQUEST);
        }
    }

    private String buildRequestUrl(LatLng origin, LatLng destination) {
        String strOrigin = "origin=" + origin.latitude + "," + origin.longitude;
        String strDestination = "destination=" + destination.latitude + "," + destination.longitude;
        String sensor = "sensor=false";
        String mode = "mode=driving";

        String param = strOrigin + "&" + strDestination + "&" + sensor + "&" + mode;
        String output = "json";
        String APIKEY = getResources().getString(R.string.google_direct_api);

        String url = "https://maps.googleapis.com/maps/api/directions/" + output + "?" + param + "&key="+APIKEY;
        Log.d("TAG", url);
        return url;
    }


    /**
     * Request direction from Google Direction API
     *
     * @param requestedUrl see {@link #buildRequestUrl(LatLng, LatLng)}
     * @return JSON data routes/direction
     */
    private String requestDirection(String requestedUrl) {
        String responseString = "";
        InputStream inputStream = null;
        HttpURLConnection httpURLConnection = null;
        try {
            URL url = new URL(requestedUrl);
            httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.connect();

            inputStream = httpURLConnection.getInputStream();
            InputStreamReader reader = new InputStreamReader(inputStream);
            BufferedReader bufferedReader = new BufferedReader(reader);

            StringBuffer stringBuffer = new StringBuffer();
            String line = "";
            while ((line = bufferedReader.readLine()) != null) {
                stringBuffer.append(line);
            }
            responseString = stringBuffer.toString();
            bufferedReader.close();
            reader.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        httpURLConnection.disconnect();
        return responseString;
    }


    public class TaskDirectionRequest extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... strings) {
            String responseString = "";
            try {
                responseString = requestDirection(strings[0]);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return responseString;
        }

        @Override
        protected void onPostExecute(String responseString) {
            super.onPostExecute(responseString);
            //Json object parsing
            TaskParseDirection parseResult = new TaskParseDirection();
            parseResult.execute(responseString);
        }
    }

    public class TaskParseDirection extends AsyncTask<String, Void, List<List<HashMap<String, String>>>> {
        @Override
        protected List<List<HashMap<String, String>>> doInBackground(String... jsonString) {
            List<List<HashMap<String, String>>> routes = null;
            JSONObject jsonObject = null;

            try {
                jsonObject = new JSONObject(jsonString[0]);
                DirectionParser parser = new DirectionParser();
                routes = parser.parse(jsonObject);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return routes;
        }

        @Override
        protected void onPostExecute(List<List<HashMap<String, String>>> lists) {
            super.onPostExecute(lists);
            ArrayList points = null;
            PolylineOptions polylineOptions = null;

            for (List<HashMap<String, String>> path : lists) {
                points = new ArrayList();
                polylineOptions = new PolylineOptions();

                for (HashMap<String, String> point : path) {
                    double lat = Double.parseDouble(point.get("lat"));
                    double lon = Double.parseDouble(point.get("lng"));

                    points.add(new LatLng(lat, lon));
                }
                polylineOptions.addAll(points);
                polylineOptions.width(15f);
                polylineOptions.color(Color.BLUE);
                polylineOptions.geodesic(true);
            }
            if (polylineOptions != null) {
                mMap.addPolyline(polylineOptions);
            } else {
                Toast.makeText(getActivity().getApplicationContext(), "Direction not found", Toast.LENGTH_LONG).show();
            }
        }
    }
}
