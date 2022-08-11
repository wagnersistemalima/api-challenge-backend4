package br.com.sistemalima.apiorcamentofamiliar.service

import br.com.sistemalima.apiorcamentofamiliar.dto.RevenueResponseDTO
import br.com.sistemalima.apiorcamentofamiliar.model.Revenue
import br.com.sistemalima.apiorcamentofamiliar.response.Response

interface RevenueService {
    fun create(revenueEntity: Revenue): Response<RevenueResponseDTO>

    fun findAll(): Response<List<RevenueResponseDTO>>

    fun findById(id: Long): Response<RevenueResponseDTO>

    fun delete(id: Long)

    fun update(revenueEntity: Revenue, id: Long): Response<RevenueResponseDTO>

}