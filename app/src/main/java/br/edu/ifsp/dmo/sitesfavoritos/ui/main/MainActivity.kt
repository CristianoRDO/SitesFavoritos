package br.edu.ifsp.dmo.sitesfavoritos.ui.main

import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import br.edu.ifsp.dmo.sitesfavoritos.R
import br.edu.ifsp.dmo.sitesfavoritos.data.model.Site
import br.edu.ifsp.dmo.sitesfavoritos.databinding.ActivityMainBinding
import br.edu.ifsp.dmo.sitesfavoritos.databinding.SitesDialogBinding
import br.edu.ifsp.dmo.sitesfavoritos.ui.adapter.SiteAdapter
import br.edu.ifsp.dmo.sitesfavoritos.ui.listener.SiteitemClickListener

class MainActivity : AppCompatActivity(), SiteitemClickListener {

    private lateinit var binding: ActivityMainBinding
    private var datasource = ArrayList<Site>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        configListeners()
        configRecyclerView()
    }

    private fun configListeners(){
        binding.buttonAdd.setOnClickListener{handleAddSite()}
    }

    private fun configRecyclerView() {
        val adapter = SiteAdapter(this, datasource, this)
        val layoutManager: RecyclerView.LayoutManager =
            LinearLayoutManager(this)
        binding.recyclerviewSites.layoutManager = layoutManager
        binding.recyclerviewSites.adapter = adapter
    }

    private fun notifyAdapter() {
        val adapter = binding.recyclerviewSites.adapter
        adapter?.notifyDataSetChanged()
    }

    override fun clickSiteItem(position: Int) {
        val site = datasource[position]
        val mIntent = Intent(Intent.ACTION_VIEW)
        mIntent.setData(Uri.parse("http://" + site.url))
        startActivity(mIntent)
    }

    override fun clickHeartSiteItem(position: Int) {
        val site = datasource[position]
        site.favorito = !site.favorito
        notifyAdapter()
    }

    private fun handleAddSite(){
        val tela = layoutInflater.inflate(R.layout.sites_dialog, null)
        val bindingDialog: SitesDialogBinding = SitesDialogBinding.bind(tela)
        val builder = AlertDialog.Builder(this)
            .setView(tela)
            .setTitle(R.string.novo_site)
            .setPositiveButton(R.string.salvar,
                DialogInterface.OnClickListener { dialog, which ->
                    datasource.add(
                        Site(
                            bindingDialog.edittextApelido.text.toString(),
                            bindingDialog.edittextUrl.text.toString()
                        )
                    )
                    notifyAdapter()
                    dialog.dismiss()
                })
            .setNegativeButton(R.string.cancelar,
                DialogInterface.OnClickListener { dialog, which ->
                    dialog.dismiss()
                })
        val dialog = builder.create()
        dialog.show()

    }
}