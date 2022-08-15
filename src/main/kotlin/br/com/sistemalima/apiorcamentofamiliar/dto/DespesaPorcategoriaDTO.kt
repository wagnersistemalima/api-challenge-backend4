package br.com.sistemalima.apiorcamentofamiliar.dto

data class DespesaPorcategoriaDTO(

    val totalOutras: Double,
    val totalAlimentacao: Double,
    val totalSaude: Double,
    val totalMoradia: Double,
    val totalTransporte: Double,
    val totalEducacao: Double,
    val totalLazer: Double,
    val totalImprevisto: Double
)
