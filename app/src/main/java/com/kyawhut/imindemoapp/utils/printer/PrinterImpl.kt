package com.kyawhut.imindemoapp.utils.printer

import android.content.Context
import android.graphics.Bitmap
import android.view.View
import android.widget.ScrollView
import android.widget.TextView
import androidx.core.widget.NestedScrollView
import com.imin.printerlib.Callback
import com.imin.printerlib.IminPrintUtils
import com.kyawhut.imindemoapp.utils.printer.PrintUtils.toBitmap
import com.kyawhut.imindemoapp.utils.printer.PrintUtils.toBlackAndWhite
import timber.log.Timber

/**
 * @author kyawhtut
 * @date 2/14/22
 */

class PrinterImpl constructor(private val context: Context) : Printer {

    private val iMinPrintUtils: IminPrintUtils by lazy {
        IminPrintUtils.getInstance(context)
    }

    private var onSuccess: ((String) -> Unit)? = null
    private var onFail: ((Exception) -> Unit)? = null
    private val printConnectionType: IminPrintUtils.PrintConnectType
        get() = if (iMinPrintUtils.isSPIPrint) IminPrintUtils.PrintConnectType.SPI
        else IminPrintUtils.PrintConnectType.USB

    private var isAlreadyConnected: Boolean = false

    override fun getStatus(callback: (Int) -> Unit) {
        if (!isAlreadyConnected) {
            callback(-1)
            return
        }
        if (iMinPrintUtils.isSPIPrint) {
            iMinPrintUtils.getPrinterStatus(printConnectionType) {
                callback(it)
            }
        } else callback(iMinPrintUtils.getPrinterStatus(printConnectionType))
    }

    override fun init() {
        if (!Printer.isPrinterSupport) {
            onFail?.invoke(Exception("This device is not support printer."))
            return
        }
        if (isAlreadyConnected) {
            onSuccess?.invoke("Printer already connected.\nPrinter connection Type => $printConnectionType")
            return
        }
        if (!iMinPrintUtils.isSPIPrint) iMinPrintUtils.resetDevice()
        iMinPrintUtils.initPrinter(printConnectionType)

        isAlreadyConnected = iMinPrintUtils.isSPIPrint

        getStatus {
            if (it == 0) {
                isAlreadyConnected = true
                onSuccess?.invoke("Printer initialize success.\nPrinter connection Type => $printConnectionType")
            } else {
                onFail?.invoke(Exception("Printer error. Error code => $it"))
            }
        }

    }

    override fun printAndLineFeed() {
        iMinPrintUtils.printAndLineFeed()
    }

    override fun printAndFeedPaper(height: Int) {
        iMinPrintUtils.printAndFeedPaper(height)
    }

    override fun partialCut() {
        iMinPrintUtils.partialCut()
    }

    override fun print(text: String) {
        printer {
            iMinPrintUtils.printText(text)
        }
    }

    override fun print(textView: TextView) {
        print(textView.text.toString())
    }

    override fun print(text: String, type: Int) {
        printer {
            iMinPrintUtils.printText(text, type)
        }
    }

    override fun print(textView: TextView, type: Int) {
        print(textView.text.toString(), type)
    }

    override fun print(bitmap: Bitmap, convertToBlackAndWhite: Boolean) {
        printer {
            val greyImage = if (convertToBlackAndWhite) bitmap.toBlackAndWhite() else bitmap
            iMinPrintUtils.printSingleBitmap(greyImage)
        }
    }

    override fun print(view: View, convertToBlackAndWhite: Boolean) {
        printer {
            print(
                view.toBitmap(
                    getViewWidth(view),
                    getViewHeight(view)
                ),
                convertToBlackAndWhite
            )
        }
    }

    override fun print(vararg bitmap: Bitmap, convertToBlackAndWhite: Boolean) {
        printer {
            (if (convertToBlackAndWhite) bitmap.map {
                it.toBlackAndWhite()
            } else bitmap.toList()).also {
                iMinPrintUtils.printMultiBitmap(it)
            }
        }
    }

    override fun print(vararg view: View, convertToBlackAndWhite: Boolean) {
        printer {
            print(
                convertToBlackAndWhite = convertToBlackAndWhite,
                bitmap = view.map {
                    it.toBitmap(
                        getViewWidth(it),
                        getViewHeight(it)
                    )
                }.toTypedArray()
            )
        }
    }

    override fun setOnSuccessListener(callback: (String) -> Unit) {
        this.onSuccess = callback
    }

    override fun setOnFailListener(callback: (Exception) -> Unit) {
        this.onFail = callback
    }

    override fun onResume() {
        init()
    }

    private fun printer(message: String = "", callback: () -> Unit) {
        try {
            callback.invoke()
            onSuccess?.invoke(message)
            iMinPrintUtils.getPrinterStatus(IminPrintUtils.PrintConnectType.SPI, object : Callback {
                override fun callback(p0: Int) {
                    Timber.d("IMinPrinter Status => %s", p0)
                }
            })
        } catch (e: Exception) {
            e.printStackTrace()
            onFail?.invoke(e)
        }
    }

    private fun getViewWidth(view: View): Int {
        return when (view) {
            is NestedScrollView -> {
                view.getChildAt(0).width
            }
            is ScrollView -> {
                view.getChildAt(0).width
            }
            else -> view.width
        }
    }

    private fun getViewHeight(view: View): Int {
        return when (view) {
            is NestedScrollView -> {
                view.getChildAt(0).height
            }
            is ScrollView -> {
                view.getChildAt(0).height
            }
            else -> view.height
        }
    }
}
