package osp.leobert.android.davinci.viewer

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.DialogFragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.flow.*
import osp.leobert.android.davinci.StyleRegistry
import osp.leobert.android.davinci.annotation.PreviewConfigRegistry
import osp.leobert.android.davinci.viewer.widget.StyleItemInteract
import osp.leobert.android.davinci.viewer.widget.StyleVHCreator
import osp.leobert.android.davinci.viewer.widget.StyleVO2
import osp.leobert.android.pandora.Logger
import osp.leobert.android.pandora.Pandora
import osp.leobert.androidkt.pandora.rv.DataSet
import osp.leobert.androidkt.pandora.rv.PandoraRvDataSet
import osp.leobert.androidkt.pandora.ui.RvAdapter

class StylesActivity : AppCompatActivity() {

    private val rvStyles by lazy { findViewById<RecyclerView>(R.id.rv_styles) }

    private val dataSet: PandoraRvDataSet<DataSet.Data> = PandoraRvDataSet(Pandora.real())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_styles)

        Logger.DEBUG = true

        rvStyles.layoutManager = LinearLayoutManager(this)
        val adapter = RvAdapter(dataSet)
        Pandora.bind2RecyclerViewAdapter(dataSet.boxAdapter(), adapter)
        rvStyles.adapter = adapter


        dataSet.registerDVRelation(
            StyleVO2.Impl::class.java,
            StyleVHCreator(object : StyleItemInteract {
                override fun onItemClicked(vo: StyleVO2) {
                    val styleInfo = StyleRegistry.find(vo.name)?.toString() ?: "null"
                    AlertDialog.Builder(this@StylesActivity)
                        .setMessage(styleInfo)
                        .create().show()
                }

            })
        )

        loadAllStyles()
    }

    private fun loadAllStyles() {

        StyleRegistry.allStyleNames().apply {
            Toast.makeText(
                this@StylesActivity,
                "${this.size} Bg-style detected",
                Toast.LENGTH_SHORT
            ).show()
        }.map {
            StyleVO2.Impl(it, PreviewConfigRegistry.find(it))
        }.let {
            dataSet.setData(it)
        }
    }
}