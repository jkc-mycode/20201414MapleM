package kr.ac.kumoh.s20201414.a20201414maplem

import android.app.Application
import android.graphics.Bitmap
import android.util.LruCache
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.toolbox.ImageLoader
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.Volley
import org.json.JSONArray
import org.json.JSONObject


class MapleViewModel(application: Application) : AndroidViewModel(application) {
    //서버 데이터 클래스 (어떤 데이터를 사용할지 타입과 변수명 결정)
    data class Maple(var id: Int, var job_name: String, var job_group: String,
                     var image: String, var job_line: String, var race: String,
                     var main_weapon: String, var main_stat: String, var namu: String)

    companion object {
        const val QUEUE_TAG = "MapleVolleyRequest"
        const val SERVER_URL = "https://maple-game-inven-fnmzz.run.goorm.io" //서버 url
    }

    private val jobs = ArrayList<Maple>()
    private val _list = MutableLiveData<ArrayList<Maple>>()
    val list: LiveData<ArrayList<Maple>>
        get() = _list

    private var queue: RequestQueue
    val imageLoader: ImageLoader

    init {
        _list.value = jobs
        queue = Volley.newRequestQueue(getApplication()) //인터넷과 통신하기 위해서 사용
        imageLoader = ImageLoader( //이미지 데이터 요청
            queue,
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
    }
    //데이터베이스에서 가져온 이미지 url를 사용
    fun getImageUrl(i: Int): String = jobs[i].image

    fun requestMaple() {
        val request = JsonArrayRequest(
            Request.Method.GET,
            "$SERVER_URL/job",
            null,
            {
                //Volley 성공시
                jobs.clear() //리스트 초기화
                parseJson(it) //데이터 파싱
                _list.value = jobs //파싱된 데이터를 리스트에 저장
            },
            {
                //Volley 에러시
                Toast.makeText(getApplication(), it.toString(),Toast.LENGTH_SHORT).show()
            }
        )
        queue.cancelAll(QUEUE_TAG)
        queue.add(request)
    }

    //데이터베이스에서 가져온 JsonArray를 반복문을 통해서 파싱
    private fun parseJson(items: JSONArray){
        for(i in 0 until items.length()){
            val item: JSONObject = items[i] as JSONObject
            val id = item.getInt("id")
            val job_name = item.getString("job_name") //직업명
            val job_group = item.getString("job_group") //직업군
            val image = item.getString("img") //이미지
            val job_line = item.getString("job_line") //직업계열
            val race = item.getString("race") //종족
            val main_weapon = item.getString("main_weapon") //주무기
            val main_stat = item.getString("main_stat") //주스탯
            val namu = item.getString("namu") //나무위키
            
            //데이터 클래스에 나와있는 형식대로 데이터 추가
            jobs.add(Maple(id, job_name, job_group, image, job_line, race, main_weapon, main_stat, namu))
        }
    }

    override fun onCleared() {
        super.onCleared()
        queue.cancelAll(QUEUE_TAG)
    }
}