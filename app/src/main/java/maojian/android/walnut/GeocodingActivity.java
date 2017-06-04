//package maojian.android.walnut;
//import android.os.Bundle;
//import android.support.v7.app.AppCompatActivity;
//import android.view.inputmethod.InputMethodManager;
//
//import com.mapbox.mapboxsdk.Mapbox;
//import com.mapbox.mapboxsdk.annotations.MarkerOptions;
//import com.mapbox.mapboxsdk.camera.CameraPosition;
//import com.mapbox.mapboxsdk.camera.CameraUpdateFactory;
//import com.mapbox.mapboxsdk.geometry.LatLng;
//import com.mapbox.mapboxsdk.maps.MapView;
//import com.mapbox.mapboxsdk.maps.MapboxMap;
//import com.mapbox.mapboxsdk.maps.OnMapReadyCallback;
//import com.mapbox.mapboxsdk.maps.widgets.MyLocationView;
//import com.mapbox.services.android.ui.geocoder.GeocoderAutoCompleteView;
//import com.mapbox.services.api.geocoding.v5.GeocodingCriteria;
//import com.mapbox.services.api.geocoding.v5.models.CarmenFeature;
//import com.mapbox.services.commons.models.Position;
//
//
//public class GeocodingActivity extends AppCompatActivity {
//
//    private MapView mapView;
//    private MapboxMap map;
//    MyLocationView myLocationView;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_geocoding);
//
//        // Set up the MapView
//        mapView.getMapAsync(new OnMapReadyCallback() {
//            @Override
//            public void onMapReady(MapboxMap mapboxMap) {
//
//                map = mapboxMap;
//
//
//                // Set the origin waypoint to the devices location
//                Position origin = Position.fromCoordinates(mapboxMap.getMyLocation().getLongitude(), mapboxMap.getMyLocation().getLatitude());
//
//                // Set the destination waypoint to the location point long clicked by the user
//                final Position destination = updateMap(feature.getLongitude(), feature.getLatitude());
//
//                mapboxMap.addMarker(new MarkerOptions()
//                        .position(new LatLng(origin.getLatitude(), origin.getLongitude()))
//                        .title("Origin")
//                        .snippet("Alhambra"));
//                mapboxMap.addMarker(new MarkerOptions()
//                        .position(new LatLng(latitude, destination.getLongitude()))
//                        .title("Destination")
//                        .snippet("Plaza del Triunfo"));
//
//                // Get route from API
//                try {
//                    getRoute(origin, destination);
//                } catch (ServicesException e) {
//                    e.printStackTrace();
//                }
//            }
//        });
//
//        AndroidGeocoder geocoder = new AndroidGeocoder(this, Locale.getDefault());
//        geocoder.setAccessToken(MAPBOX_ACCESS_TOKEN);
//        addresses = geocoder.getFromLocation(
//                LocationBean.result.location.getLatitude(),
//                LocationBean.result.location.getLongitude(),
//                1);
//
//        // Set up autocomplete widget
//        GeocoderAutoCompleteView autocomplete = (GeocoderAutoCompleteView) findViewById(R.id.query);
//        autocomplete.setAccessToken("pk.eyJ1IjoiYmV1dHJveCIsImEiOiJjaW5ybzlwYnQwMGlqdnhtMno3cmtwNTlqIn0.y16mZzmertL4-eEfQNGeqQ");
//        autocomplete.setType(GeocodingCriteria.TYPE_POI);
//        autocomplete.setOnFeatureListener(new GeocoderAutoCompleteView.OnFeatureListener() {
//            @Override
//            public void OnFeatureClick(GeocodingFeature feature) {
//                Position position = feature.asPosition();
//                updateMap(position.getLatitude(), position.getLongitude());
//            }
//        });
//    }
//
//
//    private void getRoute(Position origin, Position destination) throws ServicesException {
//
//        MapboxDirections client = new MapboxDirections.Builder()
//                .setOrigin(origin)
//                .setDestination(destination)
//                .setProfile(DirectionsCriteria.PROFILE_CYCLING)
//                .setAccessToken("pk.eyJ1IjoiYmV1dHJveCIsImEiOiJjaW5ybzlwYnQwMGlqdnhtMno3cmtwNTlqIn0.y16mZzmertL4-eEfQNGeqQ")
//                .build();
//
//        client.enqueueCall(new Callback<DirectionsResponse>() {
//            @Override
//            public void onResponse(Call<DirectionsResponse> call, Response<DirectionsResponse> response) {
//                // You can get the generic HTTP info about the response
//                Log.d(TAG, "Response code: " + response.code());
//                if (response.body() == null) {
//                    Log.e(TAG, "No routes found, make sure you set the right user and access token.");
//                    return;
//                }
//
//                // Display some info about the route
//                currentRoute = response.body().getRoutes().get(0);
//                Log.d(TAG, "Distance: " + currentRoute.getDistance());
//                Toast.makeText(MainActivity.this, "Route is " +  currentRoute.getDistance() + " meters long.", Toast.LENGTH_SHORT).show();
//
//                // Draw the route on the map
//                drawRoute(currentRoute);
//            }
//
//            @Override
//            public void onFailure(Call<DirectionsResponse> call, Throwable t) {
//                Log.e(TAG, "Error: " + t.getMessage());
//                Toast.makeText(MainActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
//            }
//        });
//    }
//
//    private void drawRoute(DirectionsRoute route) {
//        // Convert LineString coordinates into LatLng[]
//        LineString lineString = LineString.fromPolyline(route.getGeometry(), Constants.OSRM_PRECISION_V5);
//        List<Position> coordinates = lineString.getCoordinates();
//        LatLng[] points = new LatLng[coordinates.size()];
//        for (int i = 0; i < coordinates.size(); i++) {
//            points[i] = new LatLng(
//                    coordinates.get(i).getLatitude(),
//                    coordinates.get(i).getLongitude());
//        }
//
//        // Draw Points on MapView
//        map.addPolyline(new PolylineOptions()
//                .add(points)
//                .color(Color.parseColor("#009688"))
//                .width(5));
//
//    }
//
//    private void myLocation() {
//
//        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
//            // TODO: Consider calling
//            //    ActivityCompat#requestPermissions
//            // here to request the missing permissions, and then overriding
//            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
//            //                                          int[] grantResults)
//            // to handle the case where the user grants the permission. See the documentation
//            // for ActivityCompat#requestPermissions for more details.
//            return;
//        }
//
//        mapView.setMyLocationEnabled(true);
//        mapView.setMyLocationTrackingMode(MyLocationTracking.TRACKING_FOLLOW);
//        mapView.getMyLocation();
//    }