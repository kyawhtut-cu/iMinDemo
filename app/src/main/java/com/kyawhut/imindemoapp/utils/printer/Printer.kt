package com.kyawhut.imindemoapp.utils.printer

import android.graphics.Bitmap
import android.view.View
import android.widget.TextView
import com.imin.library.SystemPropManager

/**
 * @author kyawhtut
 * @date 2/14/22
 */
interface Printer {

    companion object {
        //        private val TAG = PrinterImpl::class.java.name
        private val SUPPORTED_GROUP_ONE = listOf(
            "D1p-601",
            "D1p-602",
            "D1p-603",
            "D1p-604",
            "D1w-701",
            "D1w-702",
            "D1w-703",
            "D1w-704",
            "D4-501",
            "D4-502",
            "D4-503",
            "D4-504",
            "D4-505",
            "M2-Max",
            "D1",
            "S1-701",
            "S1-702"
        )
        val isPrinterSupport: Boolean
            get() = SUPPORTED_GROUP_ONE.contains(SystemPropManager.getModel())
    }

    val isConnected: Boolean

    val status: Int

    fun init()

    fun printAndLineFeed()

    fun printAndFeedPaper(height: Int)

    fun partialCut()

    fun print(text: String)

    fun print(textView: TextView)

    fun print(text: String, type: Int)

    fun print(textView: TextView, type: Int)

    fun print(bitmap: Bitmap, convertToBlackAndWhite: Boolean = false)

    fun print(view: View, convertToBlackAndWhite: Boolean = false)

    fun print(vararg bitmap: Bitmap, convertToBlackAndWhite: Boolean = false)

    fun print(vararg view: View, convertToBlackAndWhite: Boolean = false)

    fun setOnSuccessListener(callback: (String) -> Unit)

    fun setOnFailListener(callback: (Exception) -> Unit)

    fun onResume()
}
