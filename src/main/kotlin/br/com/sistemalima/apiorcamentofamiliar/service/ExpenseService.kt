package br.com.sistemalima.apiorcamentofamiliar.service

import br.com.sistemalima.apiorcamentofamiliar.dto.ExpenseResponseDTO
import br.com.sistemalima.apiorcamentofamiliar.model.Expense
import br.com.sistemalima.apiorcamentofamiliar.response.Response

interface ExpenseService {
    fun create(expenseEntity: Expense): Response<ExpenseResponseDTO>

    fun findAll(): Response<List<ExpenseResponseDTO>>

    fun findById(id: Long): Response<ExpenseResponseDTO>

    fun delete(id: Long)

    fun update(expenseEntity: Expense, id: Long): Response<ExpenseResponseDTO>

}