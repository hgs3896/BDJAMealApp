package com.bdjamealapp.position;

import android.content.Context;
import android.location.*;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class Positioning {
    private Context ct;

    private LocationManager locationManager;
    private Criteria criteria;
    private Location location;
    private List<Address> addresses;
    private Geocoder gcK;

    public Positioning(Context ct) {
        this.ct = ct;

        locationManager = (LocationManager) ct
                .getSystemService(Context.LOCATION_SERVICE);
        criteria = new Criteria();
        criteria.setAccuracy(Criteria.ACCURACY_COARSE);// 정확도
        criteria.setPowerRequirement(Criteria.POWER_LOW); // 전원 소비량
        criteria.setAltitudeRequired(false); // 고도 사용여부
        criteria.setBearingRequired(false); //
        criteria.setSpeedRequired(false); // 속도
        criteria.setCostAllowed(true); // 금전적비용

        refreshLocation();
    }

    public void refreshLocation() {
        String provider = locationManager.getBestProvider(criteria, true);
        location = locationManager.getLastKnownLocation(provider);
        gcK = new Geocoder(ct.getApplicationContext(), Locale.KOREA);
        try {
            addresses = gcK.getFromLocation(getLatitude(), getLongitude(), 1);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public double getLatitude() {
        return location.getLatitude(); // 위도
    }

    public double getLongitude() {
        return location.getLongitude(); // 경도
    }

    public String getCountryName() { // 국가
        return addresses.size() > 0 ? addresses.get(0).getCountryName() : "";
    }

    public String getAdminArea() { // XX도
        return addresses.size() > 0 ? addresses.get(0).getAdminArea() : "";
    }

    public String getAddress() { // XX도
        return addresses.size() > 0 ? addresses.get(0).getAddressLine(0) : "";
    }

    public String getLocality() { // XX시
        return addresses.size() > 0 ? addresses.get(0).getLocality() : "";
    }

    public String getThoroughfare() { // XX동
        return addresses.size() > 0 ? addresses.get(0).getThoroughfare() : "";
    }

    public String getFeatureName() { // 번지
        return addresses.size() > 0 ? addresses.get(0).getFeatureName() : "";
    }

}
