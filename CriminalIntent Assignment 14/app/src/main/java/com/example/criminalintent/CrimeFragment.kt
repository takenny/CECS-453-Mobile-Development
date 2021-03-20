package com.example.criminalintent

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.RecyclerView
import java.util.*


private const val TAG = "CrimeFragment"
private const val KTARG_CRIME_ID = "crime_id"
private const val KTDIALOG_DATE = "DialogDate"
private const val KTREQUEST_DATE = 0

class CrimeFragment : Fragment(), DatePickerFragment.KTCallbacks {
    private lateinit var KTcrime: Crime
    private lateinit var KTtitleField: EditText
    private lateinit var KTdateButton: Button
    private lateinit var KTsolvedCheckBox: CheckBox
    private val KTcrimeDetailViewModel: KTCrimeDetailViewModel by lazy {
        ViewModelProviders.of(this).get(KTCrimeDetailViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        KTcrime = Crime()
        val KTcrimeId: UUID = arguments?.getSerializable(KTARG_CRIME_ID) as UUID
        KTcrimeDetailViewModel.KTloadCrime(KTcrimeId)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_crime, container, false)
        KTtitleField = view.findViewById(R.id.crime_title) as EditText
        KTdateButton = view.findViewById(R.id.crime_date) as Button
        KTsolvedCheckBox = view.findViewById(R.id.crime_solved) as CheckBox

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        KTcrimeDetailViewModel.KTcrimeLiveData.observe(
            viewLifecycleOwner,
            Observer { crime ->
                crime?.let {
                    this.KTcrime = crime
                    updateUI()
                }
            })
    }


    override fun onStart() {
        super.onStart()
        val titleWatcher = object : TextWatcher {
            override fun beforeTextChanged(
                sequence: CharSequence?,
                start: Int,
                count: Int,
                after: Int
            ) {
                // This space intentionally left blank
            }

            override fun onTextChanged(
                sequence: CharSequence?,
                start: Int,
                before: Int,
                count: Int
            ) {
                KTcrime.title = sequence.toString()
            }

            override fun afterTextChanged(sequence: Editable?) {
                // This one too
            }
        }
        KTtitleField.addTextChangedListener(titleWatcher)

        KTsolvedCheckBox.apply {
            setOnCheckedChangeListener { _, isChecked ->
                KTcrime.isSolved = isChecked
            }
        }

        KTdateButton.setOnClickListener {
            DatePickerFragment.newInstance(KTcrime.date).apply {
                setTargetFragment(this@CrimeFragment, KTREQUEST_DATE)
                show(this@CrimeFragment.requireFragmentManager(), KTDIALOG_DATE)
            }
        }
    }

    override fun onStop() {
        super.onStop()
        KTcrimeDetailViewModel.KTsaveCrime(KTcrime)
    }

    override fun onDateSelected(date: Date) {
        KTcrime.date = date
        updateUI()
    }

    private fun updateUI() {
        KTtitleField.setText(KTcrime.title)
        KTdateButton.text = KTcrime.date.toString()
        KTsolvedCheckBox.apply {
            isChecked = KTcrime.isSolved
            jumpDrawablesToCurrentState()
        }
    }

    companion object {
        fun newInstance(crimeId: UUID): CrimeFragment {
            val KTargs = Bundle().apply {
                putSerializable(KTARG_CRIME_ID, crimeId)
            }
            return CrimeFragment().apply {
                arguments = KTargs
            }
        }
    }

}
