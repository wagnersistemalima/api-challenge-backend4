package br.com.sistemalima.apiorcamentofamiliar.service.util

import br.com.sistemalima.apiorcamentofamiliar.dto.enum.CategoryStatusEnumDTO
import br.com.sistemalima.apiorcamentofamiliar.model.Expense
import java.time.LocalDate

object ListExpenseDbFixture {

    fun build(): List<Expense> {

        val dto1 = Expense(
            id = 5L,
            description = "despesa gasolina",
            valor = 35.00,
            data = LocalDate.of(2022, 8, 5),
            category = CategoryStatusEnumDTO.outras

        )

        val dto2 = Expense(
            id = 6L,
            description = "despesa feira mes",
            valor = 35.00,
            data = LocalDate.of(2022, 8, 10),
            category = CategoryStatusEnumDTO.alimentacao

        )

        val dto3 = Expense(
            id = 7L,
            description = "despesa dentista",
            valor = 35.00,
            data = LocalDate.of(2022, 8, 17),
            category = CategoryStatusEnumDTO.saude

        )

        val dto4 = Expense(
            id = 8L,
            description = "despesa cigarro",
            valor = 35.00,
            data = LocalDate.of(2022, 8, 3),
            category = CategoryStatusEnumDTO.outras

        )

        return listOf(dto1, dto2, dto3, dto4)
    }
}