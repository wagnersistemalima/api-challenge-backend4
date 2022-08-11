package br.com.sistemalima.apiorcamentofamiliar.service.util

import br.com.sistemalima.apiorcamentofamiliar.model.Revenue

object ListRevenueFixture {
    fun build(): List<Revenue> {
        val revenue1 = RevenueFixture.build()
        val revenue2 = RevenueFixture.build()

        return listOf(revenue1, revenue2)
    }
}