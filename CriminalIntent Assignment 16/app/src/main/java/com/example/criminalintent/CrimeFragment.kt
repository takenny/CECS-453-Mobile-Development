package com.example.criminalintent

import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.content.pm.ResolveInfo
import android.net.Uri
import android.os.Bundle
import android.provider.ContactsContract
import android.provider.MediaStore
import android.text.Editable
import android.text.TextWatcher
import android.text.format.DateFormat
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.RecyclerView
import java.io.File
import java.util.*


private const val TAG = "CrimeFragment"
private const val KTARG_CRIME_ID = "crime_id"
private const val KTDIALOG_DATE = "DialogDate"
private const val KTREQUEST_DATE = 0
private const val KTREQUEST_CONTACT = 1
private const val KTREQUEST_PHOTO = 2
private const val KTDATE_FORMAT = "EEE, MMM, dd"

class CrimeFragment : Fragment(), DatePickerFragment.KTCallbacks {
    private lateinit var KTcrime: Crime
    private lateinit var KTphotoFile: File
    private lateinit var KTphotoUri: Uri
    private lateinit var KTtitleField: EditText
    private lateinit var KTdateButton: Button
    private lateinit var KTsolvedCheckBox: CheckBox
    private lateinit var KTreportButton: Button
    private lateinit var KTsuspectButton: Button
    private lateinit var KTphotoButton: ImageButton
    private lateinit var KTphotoView: ImageView
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
        KTreportButton = view.findViewById(R.id.crime_report) as Button
        KTsuspectButton = view.findViewById(R.id.crime_suspect) as Button
        KTphotoButton = view.findViewById(R.id.crime_camera) as ImageButton
        KTphotoView = view.findViewById(R.id.crime_photo) as ImageView
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        KTcrimeDetailViewModel.KTcrimeLiveData.observe(
            viewLifecycleOwner,
            Observer { crime ->
                crime?.let {
                    this.KTcrime = crime
                    KTphotoFile = KTcrimeDetailViewModel.KTgetPhotoFile(crime)
                    KTphotoUri = FileProvider.getUriForFile(requireActivity(),
                        "com.bignerdranch.android.criminalintent.fileprovider",
                        KTphotoFile)
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

        KTreportButton.setOnClickListener {
            Intent(Intent.ACTION_SEND).apply {
                type = "text/plain"
                putExtra(Intent.EXTRA_TEXT, getCrimeReport())
                putExtra(
                        Intent.EXTRA_SUBJECT,
                        getString(R.string.crime_report_subject))
            }.also { intent ->
                val KTchooserIntent =
                        Intent.createChooser(intent, getString(R.string.send_report))
                startActivity(KTchooserIntent)
            }
        }

        KTsuspectButton.apply {
            val KTpickContactIntent =
                    Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI)
            setOnClickListener {
                startActivityForResult(KTpickContactIntent, KTREQUEST_CONTACT)
            }
                    //if i remove this code, choose suspect works
            val KTpackageManager: PackageManager = requireActivity().packageManager
            val KTresolvedActivity: ResolveInfo? =
                    KTpackageManager.resolveActivity(KTpickContactIntent,
                            PackageManager.MATCH_DEFAULT_ONLY)
            if (KTresolvedActivity == null) {
                isEnabled = false
            }
        }

        KTphotoButton.apply {
            val KTpackageManager: PackageManager = requireActivity().packageManager
            val KTcaptureImage = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            val KTresolvedActivity: ResolveInfo? =
                KTpackageManager.resolveActivity(KTcaptureImage,
                    PackageManager.MATCH_DEFAULT_ONLY)
            if (KTresolvedActivity == null) {
                isEnabled = false
            }
            setOnClickListener {
                KTcaptureImage.putExtra(MediaStore.EXTRA_OUTPUT, KTphotoUri)
                val KTcameraActivities: List<ResolveInfo> =
                    KTpackageManager.queryIntentActivities(KTcaptureImage,
                        PackageManager.MATCH_DEFAULT_ONLY)
                for (cameraActivity in KTcameraActivities) {
                    requireActivity().grantUriPermission(
                        cameraActivity.activityInfo.packageName,
                        KTphotoUri,
                        Intent.FLAG_GRANT_WRITE_URI_PERMISSION)
                }
                startActivityForResult(KTcaptureImage, KTREQUEST_PHOTO)
            }
        }
    }

    override fun onStop() {
        super.onStop()
        KTcrimeDetailViewModel.KTsaveCrime(KTcrime)
    }

    override fun onDetach() {
        super.onDetach()
        requireActivity().revokeUriPermission(KTphotoUri,
            Intent.FLAG_GRANT_WRITE_URI_PERMISSION)
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
        if (KTcrime.suspect.isNotEmpty()) {
            KTsuspectButton.text = KTcrime.suspect
        }
        updatePhotoView()
    }

    private fun updatePhotoView() {
        if (KTphotoFile.exists()) {
            val KTbitmap = KTgetScaledBitmap(KTphotoFile.path, requireActivity())
            KTphotoView.setImageBitmap(KTbitmap)
        } else {
            KTphotoView.setImageDrawable(null)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        when {
            resultCode != Activity.RESULT_OK -> return
            requestCode == KTREQUEST_CONTACT && data != null -> {
                val KTcontactUri: Uri? = data.data
// Specify which fields you want your query to return values for
                val KTqueryFields = arrayOf(ContactsContract.Contacts.DISPLAY_NAME)
// Perform your query - the contactUri is like a "where" clause here
                val KTcursor = KTcontactUri?.let {
                    requireActivity().contentResolver
                            .query(it, KTqueryFields, null, null, null)
                }
                KTcursor?.use {
// Verify cursor contains at least one result
                    if (it.count == 0) {
                        return
                    }
// Pull out the first column of the first row of data -
// that is your suspect's name
                    it.moveToFirst()
                    val KTsuspect = it.getString(0)
                    KTcrime.suspect = KTsuspect
                    KTcrimeDetailViewModel.KTsaveCrime(KTcrime)
                    KTsuspectButton.text = KTsuspect
                }
            }
            requestCode == KTREQUEST_PHOTO -> {
                requireActivity().revokeUriPermission(KTphotoUri,
                    Intent.FLAG_GRANT_WRITE_URI_PERMISSION)
                updatePhotoView()
            }
        }
    }

    private fun getCrimeReport(): String {
        val KTsolvedString = if (KTcrime.isSolved) {
            getString(R.string.crime_report_solved)
        } else {
            getString(R.string.crime_report_unsolved)
        }
        val KTdateString = DateFormat.format(KTDATE_FORMAT, KTcrime.date).toString()
        var KTsuspect = if (KTcrime.suspect.isBlank()) {
            getString(R.string.crime_report_no_suspect)
        } else {
            getString(R.string.crime_report_suspect, KTcrime.suspect)
        }
        return getString(R.string.crime_report,
                KTcrime.title, KTdateString, KTsolvedString, KTsuspect)
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
