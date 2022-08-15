package br.com.sistemalima.apiorcamentofamiliar.service.util

import br.com.sistemalima.apiorcamentofamiliar.dto.DespesaPorcategoriaDTO
import br.com.sistemalima.apiorcamentofamiliar.dto.SummaryResponseDTO
import br.com.sistemalima.apiorcamentofamiliar.response.Response

object SummaryResponseDTOFixture {

    fun build(): Response<SummaryResponseDTO> {
        val relatorio = SummaryResponseDTO(
            totalReceitaMes = 1000.0,
            totalDespesaMes = 800.0,
            saldoFinalMes = 200.0,
            totalDespesaPorCategoria = DespesaPorcategoriaDTO(
                totalOutras = 100.0,
                totalAlimentacao = 100.0,
                totalSaude = 100.0,
                totalMoradia = 100.0,
                totalTransporte = 100.0,
                totalEducacao = 100.0,
                totalLazer = 100.0,
                totalImprevisto = 100.0
            )
        )
        return Response(data = relatorio)
    }
}