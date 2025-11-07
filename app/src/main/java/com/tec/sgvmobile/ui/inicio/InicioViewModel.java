package com.tec.sgvmobile.ui.inicio;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

public class InicioViewModel extends AndroidViewModel {
    //private MutableLiveData<MapaActual> mapa;

    public InicioViewModel(@NonNull Application application) {
        super(application);
    }
//    public LiveData<MapaActual> getMapa(){
//        if (mapa == null){
//            mapa = new MutableLiveData<>();
//        }
//        return mapa;
//    }
//
//    public void obtenerMapa(){
//        MapaActual mapaActual = new MapaActual();
//        mapa.setValue(mapaActual);
//    }
//
//    public class MapaActual implements OnMapReadyCallback {
//        LatLng SANLUIS = new LatLng(-33.280576, -66.332482);
//        LatLng ULP = new LatLng(-33.150720, -66.306864);
//
//        @Override
//        public void onMapReady(@NonNull GoogleMap googleMap) {
//            googleMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
//            googleMap.addMarker(new MarkerOptions().position(SANLUIS).title("San Luis"));
//            googleMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
//            googleMap.addMarker(new MarkerOptions().position(ULP).title("Universidad de La Punta"));
//
//            CameraPosition cp = new CameraPosition.Builder().target(ULP).zoom(19).bearing(45).tilt(70).build();
//            CameraUpdate cameraUpdate = CameraUpdateFactory.newCameraPosition(cp);
//            googleMap.animateCamera(cameraUpdate);
//        }
//    }

}