package br.edu.ifsp.dmo.sitesfavoritos.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import br.edu.ifsp.dmo.sitesfavoritos.data.dao.SiteDao
import br.edu.ifsp.dmo.sitesfavoritos.data.model.Site

class MainViewModel: ViewModel() {
    val dao = SiteDao

    private val _sites = MutableLiveData<List<Site>>()
    val sites: LiveData<List<Site>> = _sites

    private val _insertedSite = MutableLiveData<Boolean>()
    val insertedSite: LiveData<Boolean> = _insertedSite

    private val _deletedSite = MutableLiveData<Boolean>()
    val deletedSite: LiveData<Boolean> = _deletedSite

    private val _favoritedSite = MutableLiveData<Boolean>()
    val favoritedSite: LiveData<Boolean> = _favoritedSite

    init{
        mock()
        load()
    }

    private fun mock() {
        dao.add(Site("Github", "github.com/CristianoRDO"))
        dao.add(Site("Github", "github.com/CristianoRDO"))
    }

    fun load() {
        _sites.value = dao.getAll()
    }

    fun insertSite(apelido: String, url: String){
        val site = Site(apelido, url)
        dao.add(site)

        _insertedSite.value = true
        load()
    }

    fun getSite(position: Int): Site{
        return dao.getSite(position)
    }

    fun favoriteSite(position: Int){
        val site = dao.getSite(position)

        if(site != null){
            site.favorito = !site.favorito
            _favoritedSite.value = true
            load()
        }
    }

    fun deleteSite(position: Int){
        dao.delete(position)
        _deletedSite.value = true
        load()
    }
}