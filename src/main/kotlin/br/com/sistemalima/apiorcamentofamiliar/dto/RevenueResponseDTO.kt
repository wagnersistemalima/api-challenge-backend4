package br.com.sistemalima.apiorcamentofamiliar.dto

import br.com.sistemalima.apiorcamentofamiliar.model.Revenue
import com.fasterxml.jackson.annotation.JsonFormat
import com.fasterxml.jackson.annotation.JsonProperty
import java.time.LocalDate

data class RevenueResponseDTO(

    @JsonProperty("id")
    val id: Long?,

    @JsonProperty("descricao")
    val description: String,

    @JsonProperty("valor")
    val value: Double,

    @JsonProperty("data")
    @field:JsonFormat(pattern = "dd/MM/yyyy", shape = JsonFormat.Shape.STRING)
    val date: LocalDate
) {
    constructor(revenue: Revenue): this (revenue.id, revenue.description, revenue.valor, revenue.date)
}
