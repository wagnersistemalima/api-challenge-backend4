package br.com.sistemalima.apiorcamentofamiliar.dto

import br.com.sistemalima.apiorcamentofamiliar.model.Expense
import com.fasterxml.jackson.annotation.JsonFormat
import com.fasterxml.jackson.annotation.JsonProperty
import java.time.LocalDate

data class ExpenseResponseDTO(

    @JsonProperty("id")
    val id: Long?,

    @JsonProperty("descricao")
    val description: String,

    @JsonProperty("valor")
    val value: Double,

    @JsonProperty("data")
    @field:JsonFormat(pattern = "dd/MM/yyyy", shape = JsonFormat.Shape.STRING)
    val date: LocalDate,

    @JsonProperty("categoria")
    val category: String?
) {
    constructor(expense: Expense) : this(expense.id, expense.description, expense.valor, expense.data, expense.category)
}
