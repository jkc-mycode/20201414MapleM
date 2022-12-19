package kr.ac.kumoh.s20201414.a20201414maplem

import android.app.Application
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.Volley
import org.json.JSONArray
import org.json.JSONObject


class MapleViewModel(application: Application) : AndroidViewModel(application) {
    data class Maple(var id: Int, var job_name: String, var job_group: String)

    companion object{
        const val QUEUE_TAG = "MapleVolleyRequest"
    }

    private val jobs = ArrayList<Maple>()
    private val _list = MutableLiveData<ArrayList<Maple>>()
    val list: LiveData<ArrayList<Maple>>
        get() = _list

    private var queue: RequestQueue

    init {
        _list.value = jobs
        queue = Volley.newRequestQueue(getApplication())
    }

    fun requestMaple(){
        val url = "https://maple-game-inven-fnmzz.run.goorm.io/job"

        val request = JsonArrayRequest (
            Request.Method.GET,
            url,
            null,
            {
                //Toast.makeText(getApplication(), it.toString(), Toast.LENGTH_SHORT).show()
                jobs.clear()
                parseJson(it)
                _list.value = jobs
            },
            {
                Toast.makeText(getApplication(), it.toString(), Toast.LENGTH_SHORT).show()
            }
        )

        queue.add(request)
    }

    private fun parseJson(items: JSONArray){
        for(i in 0 until items.length()){
            val item: JSONObject = items[i] as JSONObject
            val id = item.getInt("id")
            val job_name = item.getString("job_name")
            val job_group = item.getString("job_group")

            jobs.add(Maple(id, job_name, job_group))
        }
    }

    override fun onCleared() {
        super.onCleared()
        queue.cancelAll(QUEUE_TAG)
    }
}