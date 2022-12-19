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
    data class Maple(var id: Int, var job_name: String, var job_group: String, var image: String)

    companion object {
        const val QUEUE_TAG = "MapleVolleyRequest"

        const val SERVER_URL = "https://maple-game-inven-fnmzz.run.goorm.io"
    }

    private val jobs = ArrayList<Maple>()
    private val _list = MutableLiveData<ArrayList<Maple>>()
    val list: LiveData<ArrayList<Maple>>
        get() = _list

    private var queue: RequestQueue
    val imageLoader: ImageLoader

    init {
        _list.value = jobs
        queue = Volley.newRequestQueue(getApplication())
        imageLoader = ImageLoader(
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
    //fun getImageUrl(i: Int): String = "$SERVER_URL/image/" + URLEncoder.encode(jobs[i].image, "utf-8")
    //fun getImageUrl(i: Int): String = "https://picsum.photos/300/300?random=" + URLEncoder.encode(jobs[i].id.toString(), "utf-8")
    fun getImageUrl(i: Int): String = jobs[i].image

    fun requestMaple() {
        //val url = "https://maple-game-inven-fnmzz.run.goorm.io/job"
        val request = JsonArrayRequest(
            Request.Method.GET,
            "$SERVER_URL/job",
            null,
            {
                //Toast.makeText(getApplication(), it.toString(),Toast.LENGTH_SHORT).show()
                jobs.clear()
                parseJson(it)
                _list.value = jobs
            },
            {
                Toast.makeText(getApplication(), it.toString(),Toast.LENGTH_SHORT).show()
            }
        )
        queue.cancelAll(QUEUE_TAG)
        queue.add(request)
    }

    private fun parseJson(items: JSONArray){
        for(i in 0 until items.length()){
            val item: JSONObject = items[i] as JSONObject
            val id = item.getInt("id")
            val job_name = item.getString("job_name")
            val job_group = item.getString("job_group")
            val image = item.getString("img")

            jobs.add(Maple(id, job_name, job_group, image))
        }
    }

    override fun onCleared() {
        super.onCleared()
        queue.cancelAll(QUEUE_TAG)
    }
}