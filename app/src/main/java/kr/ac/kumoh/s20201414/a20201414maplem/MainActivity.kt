package kr.ac.kumoh.s20201414.a20201414maplem

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kr.ac.kumoh.s20201414.a20201414maplem.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var model: MapleViewModel
    private val mapleAdapter = MapleAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityMainBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        model = ViewModelProvider(this)[MapleViewModel::class.java]

        binding.list.apply {
            layoutManager = LinearLayoutManager(applicationContext)
            setHasFixedSize(true)
            itemAnimator = DefaultItemAnimator()
            adapter = mapleAdapter
        }

        model.list.observe(this){
            mapleAdapter.notifyItemRangeInserted(0, mapleAdapter.itemCount)
        }

        model.requestMaple()
    }
    inner class MapleAdapter: RecyclerView.Adapter<MapleAdapter.ViewHolder>(){
        inner class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
            val txJobName = itemView.findViewById<TextView>(android.R.id.text1)
            val txJobGroup = itemView.findViewById<TextView>(android.R.id.text2)
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            val view = layoutInflater.inflate(android.R.layout.simple_list_item_2, parent, false)
            return ViewHolder(view)
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            holder.txJobName.text = model.list.value?.get(position)?.job_name.toString()
            holder.txJobGroup.text = model.list.value?.get(position)?.job_group.toString()
        }

        override fun getItemCount() = model.list.value?.size ?: 0
    }
}