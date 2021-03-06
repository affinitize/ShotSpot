package com.codepath.photospot.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.codepath.photospot.R;
import com.codepath.photospot.activities.PhotoDetailActivity;
import com.codepath.photospot.adapters.MapWindowAdapter;
import com.codepath.photospot.daos.FlickrPhoto;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.parceler.Parcels;

import java.util.HashMap;
import java.util.List;

public class PhotoMapFragment extends Fragment {

    private SupportMapFragment mapFragment;
    private GoogleMap map;

    private HashMap<Marker, FlickrPhoto> hashMap;

    // newInstance constructor for creating fragment with arguments
    public static PhotoMapFragment newInstance() {
        return new PhotoMapFragment();
    }

    // Store instance variables based on arguments passed
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        hashMap = new HashMap<>();
    }

    // Inflate the view for the fragment based on layout XML
    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_photo_map, container, false);

        mapFragment = ((SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map));
        if (mapFragment != null) {
            mapFragment.getMapAsync(new OnMapReadyCallback() {
                @Override
                public void onMapReady(GoogleMap map) {
                    loadMap(map);
                    map.setInfoWindowAdapter(new MapWindowAdapter(inflater, getActivity()));
                }
            });
        } else {
            Toast.makeText(getActivity(), "Error - Map Fragment was null!!", Toast.LENGTH_SHORT).show();
        }

        return view;
    }

    protected void loadMap(GoogleMap googleMap) {
        map = googleMap;
        if (map != null) {
            map.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
                @Override
                public void onInfoWindowClick(Marker marker) {
                    Intent i = new Intent(getActivity(), PhotoDetailActivity.class);
                    FlickrPhoto photo = (FlickrPhoto) marker.getTag();
                    i.putExtra("photo", Parcels.wrap(photo));
                    startActivity(i);
                }
            });
        } else {
            Toast.makeText(getActivity(), "Error - Map was null!!", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Move camera to location at specified latitude/longitude
     * @param latitude
     * @param longitude
     */
    public void moveCamera(double latitude, double longitude) {
        LatLng latLng = new LatLng(latitude, longitude);
        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLng(latLng);
        map.animateCamera(cameraUpdate);
    }

    public void newList(List<FlickrPhoto> photos) {
        map.clear();
        for (FlickrPhoto photo: photos) {
            newPhoto(photo);
        }
    }

    private void newPhoto(FlickrPhoto photo) {
        BitmapDescriptor defaultMarker =
                BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN);

        LatLng listingPosition = new LatLng(photo.getLatitude(), photo.getLongitude());

        Marker mapMarker = map.addMarker(new MarkerOptions()
                .position(listingPosition)
                .title(photo.getTitle())
                .icon(defaultMarker));

        mapMarker.setTag(photo);

        hashMap.put(mapMarker, photo);
    }
}
