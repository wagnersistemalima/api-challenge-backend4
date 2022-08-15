package br.com.sistemalima.apiorcamentofamiliar.dto

import br.com.sistemalima.apiorcamentofamiliar.dto.enum.CategoryStatusEnumDTO
import br.com.sistemalima.apiorcamentofamiliar.model.Expense
import com.fasterxml.jackson.annotation.JsonFormat
import com.fasterxml.jackson.annotation.JsonProperty
import org.springframework.validation.annotation.Validated
import java.time.LocalDate
import java.util.*
import javax.validation.Valid
import javax.validation.constraints.NotBlank
import javax.validation.constraints.NotNull
import javax.validation.constraints.Positive
import javax.validation.constraints.Size

@Validated
data class ExpenseRequestDTO(

    @field:NotBlank
    @field:Size(max = 100)
    @JsonProperty("descricao")
    val description: String?,

    @field:NotNull
    @field:Positive
    @JsonProperty("valor")
    val value: Double?,

    @field:NotNull
    @field:JsonFormat(pattern = "dd/MM/yyyy", shape = JsonFormat.Shape.STRING)
    @JsonProperty("data")
    val date: LocalDate?,

    @JsonProperty("categoria")
    @field:Valid
    var category: CategoryStatusEnumDTO? = null

) {
    fun toModel(): Expense {

        if (category == null) {
            category = CategoryStatusEnumDTO.OUTRAS
        }

        return Expense(
            description = this.description!!.lowercase(Locale.getDefault()),
            valor = this.value!!,
            data = this.date!!,
            category = category!!.deveValidarStatusEnum()
        )
    }
}