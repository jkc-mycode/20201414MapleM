package kr.ac.kumoh.s20201414.a20201414maplem

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.LruCache
import com.android.volley.toolbox.ImageLoader
import com.android.volley.toolbox.Volley
import kr.ac.kumoh.s20201414.a20201414maplem.databinding.ActivityMapleBinding

class MapleActivity : AppCompatActivity() {
    companion object {
        const val KEY_JOB_NAME = "JobName" //직업명
        const val KEY_JOB_GROUP = "JobGroup" //직업군
        const val KEY_JOB_IMAGE = "JobImage" //캐릭터 이미지
        const val KEY_JOB_LINE = "JobLine" //직업계열
        const val KEY_JOB_RACE = "JobRace" //종족
        const val KEY_MAIN_WEAPON = "MainWeapon" //주무기
        const val KEY_MAIN_STAT = "MainStat" //주스탯
        const val KEY_NAMU = "Namu" //나무위키 주소
    }
    private lateinit var binding:ActivityMapleBinding
    private lateinit var imageLoader: ImageLoader

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityMapleBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        //이미지 데이터 요청하기
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
        //xml에 있는 각 항목에 데이터를 집어 넣음 (intent로 받아온 데이터를)
        binding.imageJob.setImageUrl(intent.getStringExtra(KEY_JOB_IMAGE), imageLoader)
        binding.textJobName.text = intent.getStringExtra(KEY_JOB_NAME)
        binding.textJobGroup.text = "직업군 : " + intent.getStringExtra(KEY_JOB_GROUP)
        binding.textJobLine.text = "직업계열 : " + intent.getStringExtra(KEY_JOB_LINE)
        binding.textRace.text = "종족명 : " + intent.getStringExtra(KEY_JOB_RACE)
        binding.textMainWeapon.text = "주무기 : " + intent.getStringExtra(KEY_MAIN_WEAPON)
        binding.textMainStat.text = "주스탯 : " + intent.getStringExtra(KEY_MAIN_STAT)
        //클릭 시 intent 처리
        binding.textNamu.setOnClickListener{
            val uri = Uri.parse(intent.getStringExtra(KEY_NAMU))
            val intent = Intent(Intent.ACTION_VIEW, uri)
            startActivity(intent)
        }
    }
}