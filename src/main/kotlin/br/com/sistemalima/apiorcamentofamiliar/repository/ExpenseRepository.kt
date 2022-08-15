package br.com.sistemalima.apiorcamentofamiliar.repository

import br.com.sistemalima.apiorcamentofamiliar.model.Expense
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface ExpenseRepository: JpaRepository<Expense, Long> {

    fun findByDescription(description: String): List<Expense>

}