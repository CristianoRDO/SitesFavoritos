package br.edu.ifsp.dmo.sitesfavoritos.data.dao

import br.edu.ifsp.dmo.sitesfavoritos.data.model.Site

object SiteDao {

    private var sites: MutableList<Site> = mutableListOf()

    fun add(site: Site) {
        sites.add(site)
    }

    fun getAll() = sites

    fun getSite(position: Int) = sites.get(position)

    fun delete(position: Int) = sites.removeAt(position)
}