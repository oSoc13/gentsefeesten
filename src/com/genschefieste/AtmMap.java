package com.genschefieste;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;
import com.google.android.maps.Overlay;
import com.google.android.maps.OverlayItem;
import com.google.android.maps.ItemizedOverlay;

import java.util.ArrayList;
import java.util.List;

public class AtmMap extends MapActivity {

    List<Atm> atms;
    MapView mapView;
    MapController mc;

    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.map);

        atms = new ArrayList<Atm>();
        Atm atm = new Atm();
        atm.setLatitude((float)BaseActivity.latitude + 500);
        atm.setLongitude((float)BaseActivity.longitude + 500);
        atms.add(atm);
        Atm atm1 = new Atm();
        atm1.setLatitude((float)BaseActivity.latitude - 500);
        atm1.setLongitude((float)BaseActivity.longitude - 500);
        atms.add(atm1);

        mapView = (MapView) findViewById(R.id.mapview);
        mapView.setBuiltInZoomControls(true);
        mapView.getOverlays().clear();

        Drawable currentLocationIcon = this.getResources().getDrawable(R.drawable.menu_favorites);
        MyItemizedOverlay itemizedOverlay = new MyItemizedOverlay(currentLocationIcon);
        addOverlayItems(BaseActivity.latitude, BaseActivity.longitude, itemizedOverlay);
        mapView.getOverlays().add(itemizedOverlay);

        for(Atm a : atms)
        {
            Drawable atmIcon = this.getResources().getDrawable(R.drawable.menu_home);
            itemizedOverlay = new MyItemizedOverlay(atmIcon);
            addOverlayItems(a.getLatitude(), a.getLongitude(), itemizedOverlay);

            mapView.getOverlays().add(itemizedOverlay);
        }

        mc = mapView.getController();

        mc.animateTo(new GeoPoint((int) BaseActivity.latitude, (int) BaseActivity.longitude));
        mc.setZoom(17);
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    protected boolean isRouteDisplayed() {
        return false;
    }

    private void addOverlayItems(double lat, double lng, MyItemizedOverlay itemizedOverlay) {
        GeoPoint point = new GeoPoint((int) lat, (int) lng);
        OverlayItem overlayItem = new OverlayItem(point, "", null);
        itemizedOverlay.addOverlayItem(overlayItem);
    }

    private class MyItemizedOverlay extends ItemizedOverlay<OverlayItem> {

        private List<OverlayItem> mOverlays = new ArrayList<OverlayItem>();

        public MyItemizedOverlay(Drawable defaultMarker) {
            super(boundCenterBottom(defaultMarker));
        }

        @Override
        protected OverlayItem createItem(int i) {
            return mOverlays.get(i);
        }

        @Override
        public int size() {
            return mOverlays.size();
        }

        public void addOverlayItem(OverlayItem overlayItem) {
            mOverlays.add(overlayItem);
            populate();
        }
    }
}
