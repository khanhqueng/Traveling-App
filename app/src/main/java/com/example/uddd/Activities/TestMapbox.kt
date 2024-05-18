package com.example.uddd.Activities

import android.Manifest
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.app.ActivityCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.uddd.API.RetrofitMapbox
import com.example.uddd.Models.PlacesInfo
import com.example.uddd.R
import com.mapbox.android.gestures.MoveGestureDetector
import com.mapbox.geojson.Point
import com.mapbox.maps.CameraOptions
import com.mapbox.maps.ImageHolder
import com.mapbox.maps.MapView
import com.mapbox.maps.Style
import com.mapbox.maps.plugin.LocationPuck2D
import com.mapbox.maps.plugin.annotation.AnnotationConfig
import com.mapbox.maps.plugin.annotation.AnnotationPlugin
import com.mapbox.maps.plugin.annotation.annotations
import com.mapbox.maps.plugin.annotation.generated.PointAnnotationManager
import com.mapbox.maps.plugin.annotation.generated.PointAnnotationOptions
import com.mapbox.maps.plugin.annotation.generated.createPointAnnotationManager
import com.mapbox.maps.plugin.gestures.OnMoveListener
import com.mapbox.maps.plugin.gestures.gestures
import com.mapbox.maps.plugin.locationcomponent.OnIndicatorBearingChangedListener
import com.mapbox.maps.plugin.locationcomponent.OnIndicatorPositionChangedListener
import com.mapbox.maps.plugin.locationcomponent.location
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class TestMapbox : AppCompatActivity() {
    lateinit var btn: Button
    private var Map: MapView?= null
    lateinit var annotationConfig: AnnotationConfig;
    var annotationapi: AnnotationPlugin?=null
    var layIDD="Map_annotation"
    var pointAnnotationManager : PointAnnotationManager?=null
    var markerlist:ArrayList<PointAnnotationOptions> = ArrayList();
    private val activityResultLauncher =
        registerForActivityResult<String, Boolean>(
            ActivityResultContracts.RequestPermission(),
            object : ActivityResultCallback<Boolean> {
                override fun onActivityResult(o: Boolean) {
                    if (o) {
                        Toast.makeText(this@TestMapbox, "Granted", Toast.LENGTH_LONG).show()
                    } else {
                        Toast.makeText(this@TestMapbox, "Denied", Toast.LENGTH_LONG).show()
                    }
                }
            })
    private val onIndicatorBearingChangedListener = OnIndicatorBearingChangedListener { v ->
        Map!!.mapboxMap.setCamera(
            CameraOptions.Builder().bearing(v).build()
        )
    }
    private val onIndicatorPositionChangedListener = OnIndicatorPositionChangedListener { point ->
        Map!!.mapboxMap.setCamera(CameraOptions.Builder().center(point).build())
        Map!!.gestures.focalPoint = Map!!.mapboxMap.pixelForCoordinate(point)
    }
    private val onMoveListener: OnMoveListener = object : OnMoveListener {
        override fun onMoveBegin(moveGestureDetector: MoveGestureDetector) {
            Map!!.location.removeOnIndicatorBearingChangedListener(onIndicatorBearingChangedListener)
            Map!!.location.removeOnIndicatorPositionChangedListener(
                onIndicatorPositionChangedListener
            )
            Map!!.gestures.removeOnMoveListener(this)
        }

        override fun onMove(moveGestureDetector: MoveGestureDetector): Boolean {
            return false
        }

        override fun onMoveEnd(moveGestureDetector: MoveGestureDetector) {}
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.enableEdgeToEdge()
        setContentView(R.layout.activity_test_mapbox)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v: View, insets: WindowInsetsCompat ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        btn = findViewById(R.id.getPlaces)
        Map = findViewById(R.id.mapView)
        btn.setOnClickListener(View.OnClickListener {
            val key = getString(R.string.Mapbox_key)
            val call =
                RetrofitMapbox.getInstance().api.GetNearby(key, "en", 10, "106.7004,10.7757")
            call.enqueue(object : Callback<PlacesInfo?> {
                override fun onResponse(call: Call<PlacesInfo?>, response: Response<PlacesInfo?>) {
                    val info = response.body()
                    var string: String =""
                    annotationapi= Map?.annotations
                    annotationConfig= AnnotationConfig(
                        layerId = layIDD
                    )
                    pointAnnotationManager = annotationapi?.createPointAnnotationManager(annotationConfig);
                    for (i in info!!.features.indices) {
                        val longitude = info.features[i].properties.coordinates.longitude
                        val latitude = info.features[i].properties.coordinates.latitude
                        string +=longitude.toString() + ","+latitude.toString()


                        val bitmap = convertDrawToBitmap(AppCompatResources.getDrawable(this@TestMapbox,R.drawable.baseline_location_on_24))
                        val pointAnnotationOptions:PointAnnotationOptions=PointAnnotationOptions().withPoint(
                            Point.fromLngLat(longitude, latitude)).withIconImage(bitmap)
                        markerlist.add(pointAnnotationOptions)







                    }
                    pointAnnotationManager?.create(markerlist);

                }

                override fun onFailure(call: Call<PlacesInfo?>, t: Throwable) {
                    Toast.makeText(this@TestMapbox, t.message, Toast.LENGTH_SHORT).show()
                }
            })
        })
        if (ActivityCompat.checkSelfPermission(
                this@TestMapbox,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            activityResultLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
        }
        Map!!.mapboxMap.loadStyle(Style.STANDARD, object :Style.OnStyleLoaded{
            override fun onStyleLoaded(style: Style) {
                Map!!.mapboxMap.setCamera(CameraOptions.Builder().zoom(15.0).build())
                val locationComponentPlugin = Map!!.location
                locationComponentPlugin.enabled = true
                val locationPuck2D = LocationPuck2D()
                locationPuck2D.bearingImage = ImageHolder.from(R.drawable.baseline_location_on_24)
                locationComponentPlugin.locationPuck = locationPuck2D
                locationComponentPlugin.addOnIndicatorBearingChangedListener(
                    onIndicatorBearingChangedListener
                )
                locationComponentPlugin.addOnIndicatorPositionChangedListener(
                    onIndicatorPositionChangedListener
                )
                Map!!.gestures.addOnMoveListener(onMoveListener)
            }
        })
    }
    private fun convertDrawToBitmap(sourceDrawable :Drawable?): Bitmap{

        return if(sourceDrawable is BitmapDrawable){
            sourceDrawable.bitmap
        }else{
            val constaState= sourceDrawable?.constantState
            val drawable = constaState?.newDrawable()!!.mutate()
            val bitmap: Bitmap = Bitmap.createBitmap(
                drawable.intrinsicWidth, drawable.intrinsicHeight,
                Bitmap.Config.ARGB_8888
            )
            val canvas = Canvas(bitmap)
            drawable.setBounds(0, 0, canvas.width, canvas.height)
            drawable.draw(canvas)
            bitmap
        }
    }
}