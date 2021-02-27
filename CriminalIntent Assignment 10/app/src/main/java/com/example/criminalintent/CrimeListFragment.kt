package com.example.criminalintent

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

private const val TAG = "CrimeListFragment"

class KTCrimeListFragment : Fragment() {
    private lateinit var KTcrimeRecyclerView: RecyclerView
    private var KTadapter: KTCrimeAdapter? = KTCrimeAdapter(emptyList())
    private val KTcrimeListViewModel: KTCrimeListViewModel by lazy {
        ViewModelProviders.of(this).get(KTCrimeListViewModel::class.java)
    }

    companion object {
        fun newInstance(): KTCrimeListFragment {
            return KTCrimeListFragment()
        }
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_crime_list, container, false)
        KTcrimeRecyclerView =
            view.findViewById(R.id.crime_recycler_view) as RecyclerView
        KTcrimeRecyclerView.layoutManager = LinearLayoutManager(context)
        KTcrimeRecyclerView.adapter = KTadapter

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        KTcrimeListViewModel.KTcrimeListLiveData.observe(
            viewLifecycleOwner,
            Observer { crimes ->
                crimes?.let {
                    Log.i(TAG, "Got crimes ${crimes.size}")
                    updateUI(crimes)
                }
            })
    }


    private fun updateUI(crimes: List<Crime>) {
        KTadapter = KTCrimeAdapter(crimes)
        KTcrimeRecyclerView.adapter = KTadapter
    }
    private inner class KTCrimeHolder(view: View)
        : RecyclerView.ViewHolder(view), View.OnClickListener  {

        private lateinit var KTcrime: Crime

        val KTtitleTextView: TextView = itemView.findViewById(R.id.crime_title)
        val KTdateTextView: TextView = itemView.findViewById(R.id.crime_date)
        private val KTsolvedImageView: ImageView = itemView.findViewById(R.id.crime_solved)

        init {
            itemView.setOnClickListener(this)
        }

        fun bind(crime: Crime) {
            this.KTcrime = crime
            KTtitleTextView.text = this.KTcrime.title
            KTdateTextView.text = this.KTcrime.date.toString()
            KTsolvedImageView.visibility = if (crime.isSolved) {
                View.VISIBLE
            } else {
                View.GONE
            }
        }

        override fun onClick(v: View) {
            Toast.makeText(context, "${KTcrime.title} pressed!", Toast.LENGTH_SHORT)
                .show()
        }

    }

    private inner class KTCrimeAdapter(var crimes: List<Crime>)
        : RecyclerView.Adapter<KTCrimeHolder>() {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int)
                : KTCrimeHolder {
            val KTview = layoutInflater.inflate(R.layout.list_item_crime, parent, false)
            return KTCrimeHolder(KTview)
        }

        override fun onBindViewHolder(holder: KTCrimeHolder, position: Int) {
            val KTcrime = crimes[position]
            holder.bind(KTcrime)
            }
        override fun getItemCount() = crimes.size
        }
}
