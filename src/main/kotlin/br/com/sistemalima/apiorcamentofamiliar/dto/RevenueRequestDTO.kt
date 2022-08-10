package br.com.sistemalima.apiorcamentofamiliar.dto

import br.com.sistemalima.apiorcamentofamiliar.model.Revenue
import com.fasterxml.jackson.annotation.JsonFormat
import com.fasterxml.jackson.annotation.JsonProperty
import java.time.LocalDate
import javax.validation.constraints.NotBlank
import javax.validation.constraints.NotNull
import javax.validation.constraints.Positive

data class RevenueRequestDTO(

    @field:NotBlank
    @JsonProperty("descricao")
    val description: String?,

    @field:NotNull
    @field:Positive
    @JsonProperty("valor")
    val value: Double?,

    @field:NotNull
    @field:JsonFormat(pattern = "dd/MM/yyyy", shape = JsonFormat.Shape.STRING)
    @JsonProperty("data")
    val date: LocalDate?
) {
    fun toModel(): Revenue {
        return Revenue(
            description = this.description!!,
            valor = this.value!!,
            date = this.date!!
        )
    }
}