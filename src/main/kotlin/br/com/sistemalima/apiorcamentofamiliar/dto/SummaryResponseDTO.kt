package br.com.sistemalima.apiorcamentofamiliar.dto

data class SummaryResponseDTO(
    val totalReceitaMes: Double,
    val totalDespesaMes: Double,
    val saldoFinalMes: Double,
    val totalDespesaPorCategoria: DespesaPorcategoriaDTO
)