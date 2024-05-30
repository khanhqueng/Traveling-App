package com.example.uddd.Activities

import android.Manifest
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.View
import android.widget.ImageButton
import android.widget.Toast
import android.widget.ToggleButton
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.app.ActivityCompat
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.uddd.API.RetrofitClient
import com.example.uddd.API.RetrofitMapbox
import com.example.uddd.Adapters.ResultAdapter
import com.example.uddd.Domains.PopularDomain
import com.example.uddd.Models.PlacesInfo
import com.example.uddd.R
import com.google.gson.Gson
import com.google.gson.JsonElement
import com.mapbox.android.gestures.MoveGestureDetector
import com.mapbox.geojson.Point
import com.mapbox.maps.CameraOptions
import com.mapbox.maps.ImageHolder
import com.mapbox.maps.MapInitOptions
import com.mapbox.maps.MapView
import com.mapbox.maps.Style
import com.mapbox.maps.plugin.LocationPuck2D
import com.mapbox.maps.plugin.annotation.AnnotationConfig
import com.mapbox.maps.plugin.annotation.AnnotationPlugin
import com.mapbox.maps.plugin.annotation.annotations
import com.mapbox.maps.plugin.annotation.generated.OnPointAnnotationClickListener
import com.mapbox.maps.plugin.annotation.generated.PointAnnotation
import com.mapbox.maps.plugin.annotation.generated.PointAnnotationManager
import com.mapbox.maps.plugin.annotation.generated.PointAnnotationOptions
import com.mapbox.maps.plugin.annotation.generated.createPointAnnotationManager
import com.mapbox.maps.plugin.gestures.OnMoveListener
import com.mapbox.maps.plugin.gestures.gestures
import com.mapbox.maps.plugin.locationcomponent.OnIndicatorBearingChangedListener
import com.mapbox.maps.plugin.locationcomponent.OnIndicatorPositionChangedListener
import com.mapbox.maps.plugin.locationcomponent.location
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ResultActivity : AppCompatActivity()
{
    lateinit var resultAdapter: ResultAdapter
    lateinit var recyclerView: RecyclerView
    lateinit var backButton: ImageButton
    lateinit var filters: ArrayList<ToggleButton>

    lateinit var annotationConfig: AnnotationConfig;
    var annotationapi: AnnotationPlugin?=null
    var layIDD="Map_annotation"
    var pointAnnotationManager : PointAnnotationManager?=null
    var markerlist:ArrayList<PointAnnotationOptions> = ArrayList();
    val nameCategoryList = arrayListOf<String>("coffee", "food_and_drink","shopping","hotel","restaurant","restaurant")

    private var searchLocation: String? = null
    private var Map: MapView?= null
    private val activityResultLauncher =
        registerForActivityResult<String, Boolean>(
            ActivityResultContracts.RequestPermission(),
            object : ActivityResultCallback<Boolean> {
                override fun onActivityResult(o: Boolean) {
                    if (o) {
                        Toast.makeText(this@ResultActivity, "Granted", Toast.LENGTH_LONG).show()
                    } else {
                        Toast.makeText(this@ResultActivity, "Denied", Toast.LENGTH_LONG).show()
                    }
                }
            })





    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_result)

        resultInfo
        initResultView()

        backButton = findViewById(R.id.btn_back)
        backButton.setOnClickListener(View.OnClickListener { finish() })
        Toast.makeText(this@ResultActivity,"location: "+ searchLocation,Toast.LENGTH_SHORT).show()
        Map = findViewById(R.id.mapView)

        filters = ArrayList()
        if (ActivityCompat.checkSelfPermission(
                this@ResultActivity,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            activityResultLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
        }
        annotationapi= Map?.annotations
        annotationConfig= AnnotationConfig(
            layerId = layIDD
        )
        pointAnnotationManager = annotationapi?.createPointAnnotationManager(annotationConfig);
        pointAnnotationManager?.addClickListener(OnPointAnnotationClickListener {
                annotation: PointAnnotation ->
            onMarkerItemClick(annotation)
            true
        })
        val bitmap = convertDrawToBitmap(
            AppCompatResources.getDrawable(
                this@ResultActivity,
                R.drawable.baseline_location_on_24
            )
        )

        Map!!.mapboxMap.loadStyle(Style.STANDARD, object :Style.OnStyleLoaded{
            override fun onStyleLoaded(style: Style) {
                val longlat: List<String> = searchLocation!!.split(",")
                val initialCameraOptions = CameraOptions.Builder()
                    .center(Point.fromLngLat(longlat.get(0).toDouble(), longlat.get(1).toDouble()))
                    .pitch(45.0)
                    .zoom(15.5)
                    .bearing(-17.6)
                    .build()

                Map!!.mapboxMap.setCamera(initialCameraOptions)
                val pointAnnotationOption: PointAnnotationOptions =
                    PointAnnotationOptions().withPoint(
                        Point.fromLngLat(longlat.get(0).toDouble(), longlat.get(1).toDouble())
                    ).withIconImage(bitmap)
                pointAnnotationManager?.create(pointAnnotationOption)
            }
        })

        for (i in 0..5) {
            val filterId = getResources().getIdentifier("filter" + (i + 1), "id", packageName)
            val button = findViewById<ToggleButton>(filterId)
            when (nameCategoryList[i]){
                "coffee"-> {
                    Toast.makeText(this@ResultActivity, "Get success", Toast.LENGTH_SHORT).show()
                    button.setOnCheckedChangeListener { buttonView, isChecked ->
                        if (isChecked) {
                            button.setBackgroundDrawable(getDrawable(R.drawable.gradient_green_button))
                            val key = getString(R.string.Mapbox_key)
                            val call =
                                RetrofitMapbox.getInstance().api.GetNearbyCoffee(
                                    key,
                                    "en",
                                    10,
                                    searchLocation
                                )

                            call.enqueue(object : Callback<PlacesInfo?> {
                                override fun onResponse(
                                    call: Call<PlacesInfo?>, response: Response<PlacesInfo?>) {
                                    val info = response.body()
                                    var string: String = ""

                                    for (i in info!!.features.indices) {
                                        val longitude =
                                            info.features[i].properties.coordinates.longitude
                                        val latitude =
                                            info.features[i].properties.coordinates.latitude
                                        string += longitude.toString() + "," + latitude.toString()
                                        val jsonObject= JSONObject();
                                        jsonObject.put("Name: ",info.features[i].properties.name)


                                        val pointAnnotationOptions: PointAnnotationOptions =
                                            PointAnnotationOptions().withPoint(
                                                Point.fromLngLat(longitude, latitude)
                                            )
                                                .withData(Gson().fromJson(jsonObject.toString(),JsonElement::class.java)).withIconImage(bitmap)
                                        markerlist.add(pointAnnotationOptions)


                                    }

                                    pointAnnotationManager?.create(markerlist);


                                }

                                override fun onFailure(call: Call<PlacesInfo?>, t: Throwable) {
                                    Toast.makeText(
                                        this@ResultActivity,
                                        t.message,
                                        Toast.LENGTH_SHORT
                                    )
                                        .show()
                                }
                            })
                        }


                        else
                            button.setBackgroundDrawable(getDrawable(R.drawable.outline_shape))
                    }

                }
                "food_and_drink"-> {
                    button.setOnCheckedChangeListener { buttonView, isChecked ->
                        if (isChecked) {
                            button.setBackgroundDrawable(getDrawable(R.drawable.gradient_green_button))
                            val key = getString(R.string.Mapbox_key)
                            val call =
                                RetrofitMapbox.getInstance().api.GetNearbyFoodAndDrink(
                                    key,
                                    "en",
                                    10,
                                    searchLocation
                                )

                            call.enqueue(object : Callback<PlacesInfo?> {
                                override fun onResponse(
                                    call: Call<PlacesInfo?>, response: Response<PlacesInfo?>) {
                                    val info = response.body()
                                    var string: String = ""

                                    for (i in info!!.features.indices) {
                                        val longitude =
                                            info.features[i].properties.coordinates.longitude
                                        val latitude =
                                            info.features[i].properties.coordinates.latitude
                                        string += longitude.toString() + "," + latitude.toString()
                                        val jsonObject= JSONObject();
                                        jsonObject.put("Name: ",info.features[i].properties.name)

                                        val pointAnnotationOptions: PointAnnotationOptions =
                                            PointAnnotationOptions().withPoint(
                                                Point.fromLngLat(longitude, latitude)
                                            )

                                                .withData(Gson().fromJson(jsonObject.toString(),JsonElement::class.java)).withIconImage(bitmap)
                                        markerlist.add(pointAnnotationOptions)


                                    }
                                    Toast.makeText(this@ResultActivity, "Get Success", Toast.LENGTH_SHORT).show()
                                    pointAnnotationManager?.create(markerlist);


                                }

                                override fun onFailure(call: Call<PlacesInfo?>, t: Throwable) {
                                    Toast.makeText(
                                        this@ResultActivity,
                                        t.message,
                                        Toast.LENGTH_SHORT
                                    )
                                        .show()
                                }
                            })
                        }


                        else
                            button.setBackgroundDrawable(getDrawable(R.drawable.outline_shape))
                    }

                }
                "shopping"-> {
                    button.setOnCheckedChangeListener { buttonView, isChecked ->
                        if (isChecked) {
                            button.setBackgroundDrawable(getDrawable(R.drawable.gradient_green_button))
                            initShoppingResult()
                            val key = getString(R.string.Mapbox_key)
                            val call =
                                RetrofitMapbox.getInstance().api.GetNearbyShop(
                                    key,
                                    "en",
                                    10,
                                    searchLocation
                                )

                            call.enqueue(object : Callback<PlacesInfo?> {
                                override fun onResponse(
                                    call: Call<PlacesInfo?>, response: Response<PlacesInfo?>) {
                                    val info = response.body()
                                    var string: String = ""

                                    for (i in info!!.features.indices) {
                                        val longitude =
                                            info.features[i].properties.coordinates.longitude
                                        val latitude =
                                            info.features[i].properties.coordinates.latitude
                                        string += longitude.toString() + "," + latitude.toString()
                                        val jsonObject= JSONObject();
                                        jsonObject.put("Name: ",info.features[i].properties.name)


                                        val pointAnnotationOptions: PointAnnotationOptions =
                                            PointAnnotationOptions().withPoint(
                                                Point.fromLngLat(longitude, latitude)
                                            )
                                                .withData(Gson().fromJson(jsonObject.toString(),JsonElement::class.java)).withIconImage(bitmap)
                                        markerlist.add(pointAnnotationOptions)


                                    }
                                    Toast.makeText(this@ResultActivity, "Get Success", Toast.LENGTH_SHORT).show()
                                    pointAnnotationManager?.create(markerlist);


                                }

                                override fun onFailure(call: Call<PlacesInfo?>, t: Throwable) {
                                    Toast.makeText(
                                        this@ResultActivity,
                                        t.message,
                                        Toast.LENGTH_SHORT
                                    )
                                        .show()
                                }
                            })
                        }


                        else
                            button.setBackgroundDrawable(getDrawable(R.drawable.outline_shape))
                    }

                }
                "hotel"-> {
                    button.setOnCheckedChangeListener { buttonView, isChecked ->
                        if (isChecked) {
                            button.setBackgroundDrawable(getDrawable(R.drawable.gradient_green_button))
                            val key = getString(R.string.Mapbox_key)
                            val call =
                                RetrofitMapbox.getInstance().api.GetNearbyHotel(
                                    key,
                                    "en",
                                    10,
                                    searchLocation
                                )

                            call.enqueue(object : Callback<PlacesInfo?> {
                                override fun onResponse(
                                    call: Call<PlacesInfo?>, response: Response<PlacesInfo?>) {
                                    val info = response.body()
                                    var string: String = ""

                                    for (i in info!!.features.indices) {
                                        val longitude =
                                            info.features[i].properties.coordinates.longitude
                                        val latitude =
                                            info.features[i].properties.coordinates.latitude
                                        string += longitude.toString() + "," + latitude.toString()
                                        val jsonObject= JSONObject();
                                        jsonObject.put("Name: ",info.features[i].properties.name)


                                        val pointAnnotationOptions: PointAnnotationOptions =
                                            PointAnnotationOptions().withPoint(
                                                Point.fromLngLat(longitude, latitude)
                                            )
                                                .withData(Gson().fromJson(jsonObject.toString(),JsonElement::class.java)).withIconImage(bitmap)
                                        markerlist.add(pointAnnotationOptions)


                                    }
                                    Toast.makeText(this@ResultActivity, "hello", Toast.LENGTH_SHORT).show()
                                    pointAnnotationManager?.create(markerlist);


                                }

                                override fun onFailure(call: Call<PlacesInfo?>, t: Throwable) {
                                    Toast.makeText(
                                        this@ResultActivity,
                                        t.message,
                                        Toast.LENGTH_SHORT
                                    )
                                        .show()
                                }
                            })
                        }


                        else
                            button.setBackgroundDrawable(getDrawable(R.drawable.outline_shape))
                    }

                }
                "restaurant"-> {
                    button.setOnCheckedChangeListener { buttonView, isChecked ->
                        if (isChecked) {
                            button.setBackgroundDrawable(getDrawable(R.drawable.gradient_green_button))
                            val key = getString(R.string.Mapbox_key)
                            val call =
                                RetrofitMapbox.getInstance().api.GetNearbyRestaurant(
                                    key,
                                    "en",
                                    10,
                                    searchLocation
                                )

                            call.enqueue(object : Callback<PlacesInfo?> {
                                override fun onResponse(
                                    call: Call<PlacesInfo?>, response: Response<PlacesInfo?>) {
                                    val info = response.body()
                                    var string: String = ""

                                    for (i in info!!.features.indices) {
                                        val longitude =
                                            info.features[i].properties.coordinates.longitude
                                        val latitude =
                                            info.features[i].properties.coordinates.latitude
                                        string += longitude.toString() + "," + latitude.toString()
                                        val jsonObject= JSONObject();
                                        jsonObject.put("Name: ",info.features[i].properties.name)


                                        val pointAnnotationOptions: PointAnnotationOptions =
                                            PointAnnotationOptions().withPoint(
                                                Point.fromLngLat(longitude, latitude)
                                            )
                                                .withData(Gson().fromJson(jsonObject.toString(),JsonElement::class.java)).withIconImage(bitmap)
                                        markerlist.add(pointAnnotationOptions)


                                    }
                                    Toast.makeText(this@ResultActivity, "hello", Toast.LENGTH_SHORT).show()
                                    pointAnnotationManager?.create(markerlist);


                                }

                                override fun onFailure(call: Call<PlacesInfo?>, t: Throwable) {
                                    Toast.makeText(
                                        this@ResultActivity,
                                        t.message,
                                        Toast.LENGTH_SHORT
                                    )
                                        .show()
                                }
                            })
                        }
                        else
                            button.setBackgroundDrawable(getDrawable(R.drawable.outline_shape))
                    }

                }
            }

            filters.add(button)
        }


    }
    private fun onMarkerItemClick(marker:PointAnnotation){
        var jsonElement: JsonElement?= marker.getData()
        AlertDialog.Builder(this).setTitle("Info place")
            .setMessage(jsonElement.toString())
            .setPositiveButton("Ok"){
                    dialog,whichButton -> dialog.dismiss()

            }.show()
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
    private fun initResultView() {
        recyclerView = findViewById(R.id.view_result)
        recyclerView.setLayoutManager(
            LinearLayoutManager(
                this,
                LinearLayoutManager.VERTICAL,
                false
            )
        )
        val dividerItemDecoration =
            DividerItemDecoration(recyclerView.getContext(), LinearLayoutManager.VERTICAL)
        recyclerView.addItemDecoration(dividerItemDecoration)
    }
    val resultInfo: Unit
        get() {
            var extras: Bundle? = intent.extras;
            searchLocation = extras?.getString("location")
        }
    private fun initShoppingResult() {
        val call =
            RetrofitClient.getInstance().api.vungTauShop
        call.enqueue(object : Callback<ArrayList<PopularDomain>?> {
            override fun onResponse(
                call: Call<ArrayList<PopularDomain>?>,
                response: Response<ArrayList<PopularDomain>?>
            ) {
                if (response.body() != null) {
                    resultAdapter = ResultAdapter(response.body())
                    recyclerView.adapter = resultAdapter
                }

            }

            override fun onFailure(call: Call<ArrayList<PopularDomain>?>, t: Throwable) {
                Toast.makeText(this@ResultActivity, "Fail to connect to server", Toast.LENGTH_SHORT)
                    .show()
            }
        })
    }

    private fun initFoodResult() {
        val call =
            RetrofitClient.getInstance().api.vungTauFood
        call.enqueue(object : Callback<ArrayList<PopularDomain>?> {
            override fun onResponse(
                call: Call<ArrayList<PopularDomain>?>,
                response: Response<ArrayList<PopularDomain>?>
            ) {
                if (response.body() != null) {
                    resultAdapter = ResultAdapter(response.body())
                    recyclerView.adapter = resultAdapter
                }

            }

            override fun onFailure(call: Call<ArrayList<PopularDomain>?>, t: Throwable) {
                Toast.makeText(this@ResultActivity, "Fail to connect to server", Toast.LENGTH_SHORT)
                    .show()
            }
        })
    }


}