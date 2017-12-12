package com.example.android.downloadablefonts

import android.graphics.Typeface
import android.os.Bundle
import android.os.Handler
import android.os.HandlerThread
import android.support.design.widget.TextInputLayout
import android.support.v4.provider.FontRequest
import android.support.v4.provider.FontsContractCompat
import android.support.v4.util.ArraySet
import android.support.v7.app.AppCompatActivity
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Button
import android.widget.CheckBox
import android.widget.ProgressBar
import android.widget.SeekBar
import android.widget.TextView
import android.widget.Toast

import java.util.Arrays

import com.example.android.downloadablefonts.Constants.ITALIC_DEFAULT
import com.example.android.downloadablefonts.Constants.WEIGHT_DEFAULT
import com.example.android.downloadablefonts.Constants.WEIGHT_MAX
import com.example.android.downloadablefonts.Constants.WIDTH_DEFAULT
import com.example.android.downloadablefonts.Constants.WIDTH_MAX

class MainActivity : AppCompatActivity() {

    lateinit private var mHandler: Handler

    lateinit private var mDownloadableFontTextView: TextView
    lateinit private var mRequestDownloadButton: Button

    lateinit private var mFamilyNameSet: ArraySet<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val handlerThread = HandlerThread("fonts")
        handlerThread.start()
        mHandler = Handler(handlerThread.looper)
        mFamilyNameSet = ArraySet<String>()
        mFamilyNameSet.addAll(Arrays.asList(*resources.getStringArray(R.array.family_names)))

        mDownloadableFontTextView = findViewById<TextView>(R.id.textview)
        val adapter = ArrayAdapter(this,
                android.R.layout.simple_dropdown_item_1line,
                resources.getStringArray(R.array.family_names))
        val familyNameInput = findViewById<TextInputLayout>(R.id.auto_complete_family_name_input)
        val autoCompleteFamilyName = findViewById<AutoCompleteTextView>(R.id.auto_complete_family_name)
        autoCompleteFamilyName.setAdapter<ArrayAdapter<String>>(adapter)
        autoCompleteFamilyName.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(charSequence: CharSequence, start: Int, count: Int,
                                           after: Int) {
                // No op
            }

            override fun onTextChanged(charSequence: CharSequence, start: Int, count: Int, after: Int) {
                if (isValidFamilyName(charSequence.toString())) {
                    familyNameInput.isErrorEnabled = false
                    familyNameInput.error = ""
                } else {
                    familyNameInput.isErrorEnabled = true
                    familyNameInput.error = getString(R.string.invalid_family_name)
                }
            }

            override fun afterTextChanged(editable: Editable) {
                // No op
            }
        })

        mRequestDownloadButton = findViewById<Button>(R.id.button_request)
        mRequestDownloadButton.setOnClickListener(View.OnClickListener {
            val familyName = autoCompleteFamilyName.getText().toString()
            if (!isValidFamilyName(familyName)) {
                familyNameInput.isErrorEnabled = true
                familyNameInput.error = getString(R.string.invalid_family_name)
                Toast.makeText(
                        this@MainActivity,
                        R.string.invalid_input,
                        Toast.LENGTH_SHORT).show()
                return@OnClickListener
            }
            requestDownload(familyName)
            mRequestDownloadButton.isEnabled = false
        })
    }

    private fun requestDownload(familyName: String) {
        val queryBuilder = QueryBuilder(familyName,
                width = 2f,
                weight = 100,
                italic = 0f,
                besteffort = true)
        val query = queryBuilder.build()

        Log.d(TAG, "Requesting a font. Query: " + query)
        val request = FontRequest(
                "com.google.android.gms.fonts",
                "com.google.android.gms",
                query,
                R.array.com_google_android_gms_fonts_certs)

        val progressBar = findViewById<ProgressBar>(R.id.progressBar)
        progressBar.visibility = View.VISIBLE

        val callback = object : FontsContractCompat.FontRequestCallback() {
            override fun onTypefaceRetrieved(typeface: Typeface) {
                mDownloadableFontTextView.typeface = typeface
                progressBar.visibility = View.GONE
                mRequestDownloadButton.isEnabled = true
            }

            override fun onTypefaceRequestFailed(reason: Int) {
                Toast.makeText(this@MainActivity,
                        getString(R.string.request_failed, reason), Toast.LENGTH_LONG)
                        .show()
                progressBar.visibility = View.GONE
                mRequestDownloadButton.isEnabled = true
            }
        }
        FontsContractCompat
                .requestFont(this@MainActivity, request, callback, mHandler)
    }

    private fun isValidFamilyName(familyName: String?): Boolean {
        return familyName != null && mFamilyNameSet.contains(familyName)
    }

    companion object {
        private val TAG = "MainActivity"
    }
}

