package br.com.sistemalima.apiorcamentofamiliar.repository

import br.com.sistemalima.apiorcamentofamiliar.model.Revenue
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface RevenueRepository: JpaRepository<Revenue, Long> {
    fun findByDescription(description: String): List<Revenue>
}