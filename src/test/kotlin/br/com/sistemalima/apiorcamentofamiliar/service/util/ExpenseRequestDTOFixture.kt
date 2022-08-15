package br.com.sistemalima.apiorcamentofamiliar.service.util

import br.com.sistemalima.apiorcamentofamiliar.dto.ExpenseRequestDTO
import br.com.sistemalima.apiorcamentofamiliar.dto.RevenueRequestDTO
import br.com.sistemalima.apiorcamentofamiliar.dto.enum.CategoryStatusEnumDTO
import br.com.sistemalima.apiorcamentofamiliar.request.Request
import java.time.LocalDate

object ExpenseRequestDTOFixture
{
    fun build(): Request<ExpenseRequestDTO> {
        return Request(
            data = ExpenseRequestDTO(
                description = "feira do mes",
                value = 1000.0,
                date = LocalDate.now(),
                category = CategoryStatusEnumDTO.ALIMENTACAO
            )
        )
    }
}