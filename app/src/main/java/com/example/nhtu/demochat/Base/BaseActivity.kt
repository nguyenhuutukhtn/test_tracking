package com.example.nhtu.demochat.Base

import android.support.v7.app.AppCompatActivity
import android.widget.Toast

open class BaseActivity : AppCompatActivity() {
    protected fun showMessage(message: String) {
        runOnUiThread { if (this@BaseActivity != null) {
            Toast.makeText(this@BaseActivity, message, Toast.LENGTH_LONG).show()
        } }

    }
}