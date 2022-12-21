package kr.ac.kumoh.s20201414.a20201414maplem

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.View.OnClickListener
import android.view.ViewGroup
import android.widget.TextView
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.toolbox.NetworkImageView
import kr.ac.kumoh.s20201414.a20201414maplem.databinding.ActivityMainBinding
import org.w3c.dom.Text

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding //viewBinding
    private lateinit var model: MapleViewModel //viewModel
    val mapleAdapter = MapleAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityMainBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        model = ViewModelProvider(this)[MapleViewModel::class.java] //viewModel 초기화

        binding.viewList.apply { //리스트에 적용될 옵션
            layoutManager = LinearLayoutManager(applicationContext)
            setHasFixedSize(true)
            adapter = mapleAdapter
        }

        //list의 변화를 감지
        model.list.observe(this){
            mapleAdapter.notifyItemRangeInserted(0, mapleAdapter.itemCount)
        }

        model.requestMaple()
    }
    inner class MapleAdapter: RecyclerView.Adapter<MapleAdapter.ViewHolder>(){
        inner class ViewHolder(itemView: View)
            : RecyclerView.ViewHolder(itemView), OnClickListener {

            val txJobName: TextView = itemView.findViewById<TextView>(R.id.text1) //직업명
            val txJovGroup: TextView = itemView.findViewById<TextView>(R.id.text2) //직업군
            val niImage: NetworkImageView = itemView.findViewById<NetworkImageView>(R.id.image) //네트워크 이미지

            init { //네트워크 이미지를 사용하기 위한 초기화
                niImage.setDefaultImageResId(android.R.drawable.ic_menu_report_image)
                itemView.setOnClickListener(this)
            }

            override fun onClick(v: View?) { //클릭시
                //intent를 통해 이동할 페이지로 넘길 데이터들
                val intent = Intent(applicationContext, MapleActivity::class.java)
                intent.putExtra(MapleActivity.KEY_JOB_NAME, model.list.value?.get(adapterPosition)?.job_name)
                intent.putExtra(MapleActivity.KEY_JOB_GROUP, model.list.value?.get(adapterPosition)?.job_group)
                intent.putExtra(MapleActivity.KEY_JOB_IMAGE, model.getImageUrl(adapterPosition))
                intent.putExtra(MapleActivity.KEY_JOB_LINE, model.list.value?.get(adapterPosition)?.job_line)
                intent.putExtra(MapleActivity.KEY_JOB_RACE, model.list.value?.get(adapterPosition)?.race)
                intent.putExtra(MapleActivity.KEY_MAIN_WEAPON, model.list.value?.get(adapterPosition)?.main_weapon)
                intent.putExtra(MapleActivity.KEY_MAIN_STAT, model.list.value?.get(adapterPosition)?.main_stat)
                intent.putExtra(MapleActivity.KEY_NAMU, model.list.value?.get(adapterPosition)?.namu)
                startActivity(intent)
            }
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            //직접 만든 Activity, 즉 새로운 layout으로 적용
            val view = layoutInflater.inflate(R.layout.item_job, parent, false)
            return ViewHolder(view)
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            //스크롤 시 새롭게 나올 데이터들
            holder.txJobName.text = model.list.value?.get(position)?.job_name.toString()
            holder.txJovGroup.text = model.list.value?.get(position)?.job_group.toString()
            holder.niImage.setImageUrl(model.getImageUrl(position), model.imageLoader)
        }

        override fun getItemCount() = model.list.value?.size ?: 0
    }

}