package com.example.flipretail.adapters

import android.content.pm.ApplicationInfo
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Switch
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.flipretail.R

class AppListAdapter(private val data: List<String>, val packageManager: PackageManager) :
    RecyclerView.Adapter<AppListAdapter.MyViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.app_view, parent, false)
        return MyViewHolder(view, packageManager)
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.bind(data[position])
    }

    class MyViewHolder(itemView: View, val packageManager: PackageManager) :
        RecyclerView.ViewHolder(itemView) {

        val textView = itemView.findViewById<TextView>(R.id.textView2)
        val switch = itemView.findViewById<Switch>(R.id.switch1)
        val packages: MutableList<ApplicationInfo> =
            packageManager.getInstalledApplications(PackageManager.GET_PERMISSIONS)

        fun bind(data: String) {
            for (i in packages) {
                Log.d("NAMEPAC", i.packageName.toString())
                if (i.packageName.toString() == data) {
                    textView.text = packageManager.getApplicationLabel(
                        packageManager.getApplicationInfo(
                            data,
                            PackageManager.GET_META_DATA
                        )
                    ).toString()

                }
            }


        }
    }

}