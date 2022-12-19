package kr.ac.kumoh.s20201414.a20201414maplem

import android.annotation.SuppressLint
import android.graphics.Bitmap
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.LruCache
import com.android.volley.toolbox.ImageLoader
import com.android.volley.toolbox.Volley
import kr.ac.kumoh.s20201414.a20201414maplem.databinding.ActivityMapleBinding

class MapleActivity : AppCompatActivity() {
    companion object {
        const val KEY_JOB_NAME = "JobName"
        const val KEY_JOB_GROUP = "JobGroup"
        const val KEY_JOB_IMAGE = "JobImage"
    }
    private lateinit var binding:ActivityMapleBinding
    private lateinit var imageLoader: ImageLoader

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityMapleBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        imageLoader = ImageLoader(Volley.newRequestQueue(this),
            object : ImageLoader.ImageCache {
                private val cache = LruCache<String, Bitmap>(100)
                override fun getBitmap(url: String?): Bitmap? {
                    return cache.get(url)
                }

                override fun putBitmap(url: String?, bitmap: Bitmap?) {
                    cache.put(url, bitmap)
                }
            }
        )
        binding.imageJob.setImageUrl(intent.getStringExtra(KEY_JOB_IMAGE), imageLoader)
        binding.textJobName.text = intent.getStringExtra(KEY_JOB_NAME)
        binding.textJobGroup.text = "직업군 : " + intent.getStringExtra(KEY_JOB_GROUP)
        binding.textJobLine.text = "직업계열 : " + intent.getStringExtra(KEY_JOB_GROUP)
        binding.textRace.text = "종족명 : " + intent.getStringExtra(KEY_JOB_GROUP)
        binding.textMainWeapon.text = "주무기 : " + intent.getStringExtra(KEY_JOB_GROUP)
        binding.textMainStat.text = "주스탯 : " + intent.getStringExtra(KEY_JOB_GROUP)
    }
}