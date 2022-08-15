package br.com.sistemalima.apiorcamentofamiliar.service.util

import br.com.sistemalima.apiorcamentofamiliar.dto.ExpenseResponseDTO
import br.com.sistemalima.apiorcamentofamiliar.dto.enum.CategoryStatusEnumDTO
import java.time.LocalDate

object ListExpenseResponseDTOFixture {

    fun build(): List<ExpenseResponseDTO> {
        val dto1 = ExpenseResponseDTO(
            id = 5L,
            description = "despesa gasolina",
            value = 35.00,
            date = LocalDate.of(2022, 8, 5),
            category = CategoryStatusEnumDTO.outras

        )

        val dto2 = ExpenseResponseDTO(
            id = 6L,
            description = "despesa feira mes",
            value = 35.00,
            date = LocalDate.of(2022, 8, 10),
            category = CategoryStatusEnumDTO.alimentacao

        )

        val dto3 = ExpenseResponseDTO(
            id = 7L,
            description = "despesa dentista",
            value = 35.00,
            date = LocalDate.of(2022, 8, 17),
            category = CategoryStatusEnumDTO.saude

        )

        val dto4 = ExpenseResponseDTO(
            id = 8L,
            description = "despesa cigarro",
            value = 35.00,
            date = LocalDate.of(2022, 8, 3),
            category = CategoryStatusEnumDTO.outras

        )

        return listOf(dto1, dto2, dto3, dto4)
    }
}