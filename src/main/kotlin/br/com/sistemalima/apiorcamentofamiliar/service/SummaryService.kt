package br.com.sistemalima.apiorcamentofamiliar.service

import br.com.sistemalima.apiorcamentofamiliar.dto.SummaryResponseDTO
import br.com.sistemalima.apiorcamentofamiliar.response.Response

interface SummaryService {

    fun findBySummaryYearMonth(ano: Int, mes: Int): Response<SummaryResponseDTO>
}