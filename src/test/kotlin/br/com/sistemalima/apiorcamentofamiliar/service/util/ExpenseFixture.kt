package br.com.sistemalima.apiorcamentofamiliar.service.util

import br.com.sistemalima.apiorcamentofamiliar.model.Expense
import br.com.sistemalima.apiorcamentofamiliar.model.Revenue
import java.time.LocalDate
import kotlin.random.Random


object ExpenseFixture {
    fun build(): Expense {
        return Expense(
            id = Random.nextInt().toLong(),
            description = "descrição despesa test",
            valor = 20.0,
            data = LocalDate.now()
        )
    }
}