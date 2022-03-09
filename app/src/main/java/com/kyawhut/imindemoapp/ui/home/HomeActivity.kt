package com.kyawhut.imindemoapp.ui.home

import android.graphics.BitmapFactory
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.kyawhut.imindemoapp.R
import com.kyawhut.imindemoapp.databinding.ActivityHomeBinding
import com.kyawhut.imindemoapp.utils.printer.Printer
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

/**
 * @author kyawhtut
 * @date 2/14/22
 */
@AndroidEntryPoint
class HomeActivity : AppCompatActivity(), View.OnClickListener {

    @Inject
    lateinit var printer: Printer

    private lateinit var homeBinding: ActivityHomeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        homeBinding = DataBindingUtil.setContentView(this, R.layout.activity_home)
        setLog("Log...")
        homeBinding.onClickListener = this
        homeBinding.executePendingBindings()

        printer.setOnSuccessListener {
            setLog(it.ifEmpty { "Print Success." })
        }

        printer.setOnFailListener {
            setLog(it.localizedMessage ?: "")
        }
    }

    private fun setLog(log: String) {
        homeBinding.printerLog = with(homeBinding.printerLog) {
            if (this == null) ""
            else this + "\n"
        } + log
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.btnInitPrinter -> {
                setLog("Printer initialize.")
                printer.init()
            }
            R.id.btnPrinterStatus -> {
                setLog("Printer Status => ${printer.status}")
            }
            R.id.btnPrintText -> {
                setLog("Printing text {${homeBinding.edtPrint.text.toString()}}")
                printer.printAndFeedPaper(100)
                printer.print(homeBinding.edtPrint.text.toString())
                printer.printAndFeedPaper(100)
            }
            R.id.btnPrintImage -> {
                setLog("Printing image {dd_black_white_print_logo.png}")
                printer.print(
                    BitmapFactory.decodeResource(
                        resources,
                        R.drawable.dd_black_white_print_logo
                    )
                )
                printer.printAndFeedPaper(100)
            }
        }
    }
}
