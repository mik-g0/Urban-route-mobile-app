package com.example.myapplication2.ui.screens


import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.rememberCameraPositionState
import com.example.myapplication2.ui.models.MainViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
@Composable
fun CityMapScreen(vm: MainViewModel = viewModel()) {
    val cityLatLng = vm.selectedCityLatLng ?: return

    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(cityLatLng, 12f)
    }

    GoogleMap(
        modifier = Modifier.fillMaxSize(),
        cameraPositionState = cameraPositionState
    ) {
        Marker(
            state = MarkerState(position = cityLatLng),
            title = vm.selectedCity ?: "Город"
        )
    }
}

