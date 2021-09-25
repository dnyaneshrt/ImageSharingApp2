package com.tech.shareimageapp

import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.os.Bundle
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.Toast
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import org.json.JSONObject

class MainActivity : AppCompatActivity() {

    var currentImageurl: String? = null
    var btn_next: Button? = null
    var imageView: ImageView? = null
    var progressBar: ProgressBar? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        btn_next = findViewById(R.id.btn_next)
        imageView = findViewById(R.id.imageView)
        progressBar = findViewById(R.id.progressBar)

        loadImage()

        findViewById<FloatingActionButton>(R.id.btn_share).setOnClickListener { view ->
//here will write the code to share image url
            var intent = Intent(Intent.ACTION_SEND)
            intent.type = "text/plain"
            intent.putExtra(
                Intent.EXTRA_TEXT,
                "hey i got this image from this awesome application,please check it $currentImageurl"
            )
            Intent.createChooser(intent, "share this image using..")
            startActivity(intent)


        }

        btn_next?.setOnClickListener {
            loadImage()

        }

    }

    //code to load the image(nw call using volley api)
    private fun loadImage() {
        progressBar?.visibility = View.VISIBLE
// 1. Instantiate the RequestQueue.
        val queue = Volley.newRequestQueue(this)
        val url = "https://meme-api.herokuapp.com/gimme"

// Request a JsonObject response from the provided URL.
        val jsonObjectRequest = JsonObjectRequest(
            Request.Method.GET, url, null,
            Response.Listener<JSONObject> { response ->
                currentImageurl = response.getString("url")

                Glide.with(this).load(currentImageurl).listener(object : RequestListener<Drawable> {
                    override fun onLoadFailed(
                        e: GlideException?,
                        model: Any?,
                        target: Target<Drawable>?,
                        isFirstResource: Boolean
                    ): Boolean {
                        imageView?.setBackgroundColor(Color.GRAY)
                        progressBar?.visibility = View.GONE
                        return false
                    }

                    override fun onResourceReady(
                        resource: Drawable?,
                        model: Any?,
                        target: Target<Drawable>?,
                        dataSource: DataSource?,
                        isFirstResource: Boolean
                    ): Boolean {
                        progressBar?.visibility = View.GONE
                        return false
                    }


                }).into(imageView!!)

            },
            Response.ErrorListener {
                Toast.makeText(this, "unable to fetch the images", Toast.LENGTH_LONG).show()
            })

// Add the request to the RequestQueue.
        queue.add(jsonObjectRequest)

    }


}