package maojian.android.walnut;

import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import com.mapbox.mapboxsdk.MapboxAccountManager;
import com.mapbox.mapboxsdk.annotations.Icon;
import com.mapbox.mapboxsdk.annotations.IconFactory;
import com.mapbox.mapboxsdk.annotations.MarkerViewOptions;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback;

public class MapActivity  extends Activity {
    private MapView mapView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        MapboxAccountManager.start(this, "pk.eyJ1IjoibXJqb2UxMjMiLCJhIjoiY2ozMnA4N2xqMDA0MDJxbnE2MmRvOXhhZSJ9.mEJgFmdaUTiMt41ZyJiY2g");

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        //地图
        mapView = (MapView) findViewById(R.id.mapView);
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(MapboxMap mapboxMap) {

                IconFactory iconFactory = IconFactory.getInstance(MapActivity.this);
                Drawable iconDrawable = ContextCompat.getDrawable(MapActivity.this, R.drawable.deviceselected);
                Icon icon = iconFactory.fromDrawable(iconDrawable);

                // The easiest way to add a marker view
                mapboxMap.addMarker(new MarkerViewOptions()
                        .position(new LatLng(-37.821629, 144.978535)));

                // marker view using all the different options available
                mapboxMap.addMarker(new MarkerViewOptions()
                        .position(new LatLng(-37.822829, 144.981842))
                        .icon(icon)
                        .rotation(90)
                        .anchor(0.5f, 0.5f)
                        .alpha(0.5f)
                        .title("Hisense Arena")
                        .snippet("Olympic Blvd, Melbourne VIC 3001, Australia")
                        .infoWindowAnchor(0.5f, 0.5f)
                        .flat(true));
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
//        mapView.on
    }

    @Override
    public void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    public void onStop() {
        super.onStop();
//        mapView.onStop();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }
}
