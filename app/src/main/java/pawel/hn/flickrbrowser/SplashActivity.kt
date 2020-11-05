package pawel.hn.flickrbrowser

import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.WindowManager
import androidx.annotation.RequiresApi
import kotlinx.android.synthetic.main.activity_splash.*


class SplashActivity : AppCompatActivity() {
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        Log.d("PHN", "onCreate splash called")
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)

        val typeFace = resources.getFont(R.font.good_times_rg)
        tvSplashTitle.typeface = typeFace


        Handler().postDelayed(
            {
                startActivity(Intent(this@SplashActivity, MainActivity::class.java ))
                finish()
            },
            2500)

    }
}