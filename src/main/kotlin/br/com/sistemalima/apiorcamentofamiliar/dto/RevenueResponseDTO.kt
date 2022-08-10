package br.com.sistemalima.apiorcamentofamiliar.dto

import br.com.sistemalima.apiorcamentofamiliar.model.Revenue
import java.time.LocalDate

data class RevenueResponseDTO(
    val id: Long?,
    val description: String,
    val valor: Double,
    val date: LocalDate
) {
    constructor(revenue: Revenue): this (revenue.id, revenue.description, revenue.valor, revenue.date)
}
