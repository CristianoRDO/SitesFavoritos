package br.edu.ifsp.dmo.sitesfavoritos.ui.main

import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import br.edu.ifsp.dmo.sitesfavoritos.R
import br.edu.ifsp.dmo.sitesfavoritos.databinding.ActivityMainBinding
import br.edu.ifsp.dmo.sitesfavoritos.databinding.SitesDialogBinding
import br.edu.ifsp.dmo.sitesfavoritos.ui.adapter.SiteAdapter
import br.edu.ifsp.dmo.sitesfavoritos.ui.listener.SiteItemClickListener

class MainActivity : AppCompatActivity(), SiteItemClickListener {

    private lateinit var binding: ActivityMainBinding
    private lateinit var viewModel: MainViewModel
    private lateinit var adapter: SiteAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = ViewModelProvider(this ).get(MainViewModel::class.java)

        configListeners()
        configRecyclerView()
        configObservers()
    }

    private fun configListeners(){
        binding.buttonAdd.setOnClickListener{ handleAddSite() }
    }

    private fun configObservers(){
        viewModel.deletedSite.observe(this, Observer {
            if(it){
                Toast.makeText(this, getString(R.string.site_deleted_success), Toast.LENGTH_SHORT).show()
            }
        })

        viewModel.insertedSite.observe(this, Observer {
            if(it){
                Toast.makeText(this, getString(R.string.site_inserted_success), Toast.LENGTH_SHORT).show()
            }
        })

        viewModel.favoritedSite.observe(this, Observer {
            if(it){
                Toast.makeText(this, getString(R.string.state_updated_successfully), Toast.LENGTH_SHORT).show()
            }
        })

        viewModel.sites.observe(this, Observer { adapter.updateDataset(it) })
    }

    private fun configRecyclerView() {
        adapter = SiteAdapter(this, mutableListOf(), this)
        val layoutManager: RecyclerView.LayoutManager = LinearLayoutManager(this)
        binding.recyclerviewSites.layoutManager = layoutManager
        binding.recyclerviewSites.adapter = adapter
    }

    override fun clickSiteItem(position: Int) {
        val site = viewModel.getSite(position)
        val mIntent = Intent(Intent.ACTION_VIEW)
        mIntent.setData(Uri.parse("http://" + site.url))
        startActivity(mIntent)
    }

    override fun clickHeartSiteItem(position: Int) {
        viewModel.favoriteSite(position)
    }

    override fun clickDeleteSiteItem(position: Int) {
        viewModel.deleteSite(position)
    }

    private fun handleAddSite(){
        val tela = layoutInflater.inflate(R.layout.sites_dialog, null)
        val bindingDialog: SitesDialogBinding = SitesDialogBinding.bind(tela)
        val builder = AlertDialog.Builder(this)
            .setView(tela)
            .setTitle(R.string.novo_site)
            .setPositiveButton(R.string.salvar,
                DialogInterface.OnClickListener { dialog, which ->
                    viewModel.insertSite(
                            bindingDialog.edittextApelido.text.toString(),
                            bindingDialog.edittextUrl.text.toString()
                    )
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