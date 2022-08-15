package br.com.sistemalima.apiorcamentofamiliar.service.util

import br.com.sistemalima.apiorcamentofamiliar.dto.RevenueResponseDTO
import br.com.sistemalima.apiorcamentofamiliar.model.Revenue
import java.time.LocalDate

object ListRevenueResponseDTOFixture {

    fun build(): List<RevenueResponseDTO> {

        val revenue1 = RevenueResponseDTO(
            id = 10L,
            description = "salario",
            value = 1000.0,
            date = LocalDate.of(2022, 8, 5)

        )

        return listOf(revenue1)
    }
}