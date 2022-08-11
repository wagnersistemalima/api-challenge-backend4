package br.com.sistemalima.apiorcamentofamiliar.service.util

import br.com.sistemalima.apiorcamentofamiliar.dto.RevenueRequestDTO
import br.com.sistemalima.apiorcamentofamiliar.request.Request
import java.time.LocalDate

object RevenueRequestDTOFixture
{

    fun build(): Request<RevenueRequestDTO> {
        return Request(
            data = RevenueRequestDTO(
                description = "Salario",
                value = 1000.0,
                date = LocalDate.now()
            )
        )
    }
}