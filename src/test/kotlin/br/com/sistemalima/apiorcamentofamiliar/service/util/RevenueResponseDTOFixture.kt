package br.com.sistemalima.apiorcamentofamiliar.service.util

import br.com.sistemalima.apiorcamentofamiliar.dto.RevenueResponseDTO
import br.com.sistemalima.apiorcamentofamiliar.response.Response
import java.time.LocalDate

object RevenueResponseDTOFixture {

    fun build(): Response<RevenueResponseDTO> {
        return Response(
            data = RevenueResponseDTO(
                id = 1L,
                description = "Salario",
                value = 1000.0,
                date = LocalDate.now()
            )
        )
    }
}