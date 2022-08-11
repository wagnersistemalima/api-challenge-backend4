package br.com.sistemalima.apiorcamentofamiliar.service.util

import br.com.sistemalima.apiorcamentofamiliar.model.Revenue
import java.time.LocalDate


object RevenueEntityFixture {

    fun build(): Revenue {
        return Revenue(
            id = null,
            description = "descrição receita test",
            valor = 20.0,
            data = LocalDate.now()
        )
    }
}