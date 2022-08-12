package br.com.sistemalima.apiorcamentofamiliar.service.util

import br.com.sistemalima.apiorcamentofamiliar.model.Expense
import br.com.sistemalima.apiorcamentofamiliar.model.Revenue

object ListExpenseFixture {
    fun build(): List<Expense> {
        val expense1 = ExpenseFixture.build()
        val expense2 = ExpenseFixture.build()

        return listOf(expense1, expense2)
    }
}