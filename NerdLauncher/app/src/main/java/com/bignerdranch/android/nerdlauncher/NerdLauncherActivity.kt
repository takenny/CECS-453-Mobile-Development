package com.bignerdranch.android.nerdlauncher

import android.content.Intent
import android.content.pm.ResolveInfo
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

private const val TAG = "NerdLauncherActivity"

class NerdLauncherActivity : AppCompatActivity() {
    private lateinit var KTrecyclerView: RecyclerView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_nerd_launcher)

        KTrecyclerView = findViewById(R.id.app_recycler_view)
        KTrecyclerView.layoutManager = LinearLayoutManager(this)

        setupAdapter()
    }

    private fun setupAdapter() {
        val KTstartupIntent = Intent(Intent.ACTION_MAIN).apply {
            addCategory(Intent.CATEGORY_LAUNCHER)
        }
        val KTactivities = packageManager.queryIntentActivities(KTstartupIntent, 0)
        KTactivities.sortWith(Comparator { a, b ->
            String.CASE_INSENSITIVE_ORDER.compare(
                a.loadLabel(packageManager).toString(),
                b.loadLabel(packageManager).toString()
            )
        })
        Log.i(TAG, "Found ${KTactivities.size} activities")
        KTrecyclerView.adapter = ActivityAdapter(KTactivities)
    }

    private class ActivityHolder(itemView: View) :
        RecyclerView.ViewHolder(itemView),
        View.OnClickListener {
        private val KTnameTextView = itemView as TextView
        private lateinit var KTresolveInfo: ResolveInfo
        init {
            KTnameTextView.setOnClickListener(this)
        }
        fun bindActivity(resolveInfo: ResolveInfo) {
            this.KTresolveInfo = resolveInfo
            val KTpackageManager = itemView.context.packageManager
            val KTappName = resolveInfo.loadLabel(KTpackageManager).toString()
            KTnameTextView.text = KTappName
        }
        override fun onClick(view: View) {
            val KTactivityInfo = KTresolveInfo.activityInfo
            val KTintent = Intent(Intent.ACTION_MAIN).apply {
                setClassName(KTactivityInfo.applicationInfo.packageName,
                    KTactivityInfo.name)
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            }
            val KTcontext = view.context
            KTcontext.startActivity(KTintent)
        }
    }

    private class ActivityAdapter(val activities: List<ResolveInfo>) :
        RecyclerView.Adapter<ActivityHolder>() {
        override fun onCreateViewHolder(container: ViewGroup, viewType: Int):
                ActivityHolder {
            val KTlayoutInflater = LayoutInflater.from(container.context)
            val KTview = KTlayoutInflater
                .inflate(android.R.layout.simple_list_item_1, container, false)
            return ActivityHolder(KTview)
        }
        override fun onBindViewHolder(holder: ActivityHolder, position: Int) {
            val KTresolveInfo = activities[position]
            holder.bindActivity(KTresolveInfo)
        }
        override fun getItemCount(): Int {
            return activities.size
        }
    }


}