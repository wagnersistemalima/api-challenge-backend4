package br.com.sistemalima.apiorcamentofamiliar.service.util

import br.com.sistemalima.apiorcamentofamiliar.dto.ExpenseResponseDTO
import br.com.sistemalima.apiorcamentofamiliar.dto.enum.CategoryStatusEnumDTO
import br.com.sistemalima.apiorcamentofamiliar.response.Response
import java.time.LocalDate

object ExpenseResponseDTOFixture {
    fun build(): Response<ExpenseResponseDTO> {
        return Response(
            data = ExpenseResponseDTO(
                id = 1L,
                description = "feira do mes",
                value = 1000.0,
                date = LocalDate.now(),
                category = CategoryStatusEnumDTO.outras
            )
        )
    }
}