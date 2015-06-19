package by.org.cgm.didyoufeelit.fragments;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import by.org.cgm.didyoufeelit.AppCache;
import by.org.cgm.didyoufeelit.R;
import by.org.cgm.didyoufeelit.listeners.OnNavigationListener;
import by.org.cgm.didyoufeelit.utils.StringUtils;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link OnNavigationListener} interface
 * to handle interaction events.
 */
public class PlaceFragment extends Fragment implements View.OnClickListener,
        OnMapReadyCallback {

    private static final String PLACE_KEY = "lat_lng";
    private OnNavigationListener mListener;
    private int position;
    private static LatLng place;
    private SupportMapFragment mapFragment;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Bundle arg = getArguments();
        position = arg.getInt(StringUtils.PAGE_POSITION);
        if (savedInstanceState!=null) {
            double[] latlng = savedInstanceState.getDoubleArray(PLACE_KEY);
            if (latlng != null) place = new LatLng(latlng[0], latlng[1]);
        }
        return inflater.inflate(R.layout.fragment_place, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        view.findViewById(R.id.btnNext).setOnClickListener(this);
        view.findViewById(R.id.btnPrevious).setOnClickListener(this);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnNavigationListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnNavigationListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        if (place !=null) {
            outState.putDoubleArray(
                    PLACE_KEY,
                    new double[]{place.latitude, place.longitude}
            );
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        FragmentManager fm = getChildFragmentManager();
        FragmentTransaction fragmentTransaction = fm.beginTransaction();
        fragmentTransaction.remove(mapFragment);
        fragmentTransaction.commitAllowingStateLoss();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnNext:
                mListener.onNavigatePage(position + 1);
                break;
            case R.id.btnPrevious:
                mListener.onNavigatePage(position - 1);
                break;
        }
    }

    @Override
    public void onMapReady(final GoogleMap googleMap) {
        googleMap.setMyLocationEnabled(true);
        googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        googleMap.setIndoorEnabled(true);
        googleMap.setBuildingsEnabled(true);
        UiSettings settings = googleMap.getUiSettings();
        settings.setCompassEnabled(true);
        if (place!=null)
            googleMap.addMarker(new MarkerOptions().position(place).title("Место события"));
        googleMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                if (place != null) googleMap.clear();
                googleMap.addMarker(
                        new MarkerOptions().position(latLng).title("Место события")
                );
                place = latLng;
            }
        });
    }

    public void setPlaceInData() {
        if (place != null) {
            AppCache.getInstance().getData().place =
                    "  долгота: " + StringUtils.round(place.longitude, 2) + ",\n" +
                    "  широта: " + StringUtils.round(place.latitude, 2);
        }
    }
}
