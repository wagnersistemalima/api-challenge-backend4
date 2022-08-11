package br.com.sistemalima.apiorcamentofamiliar.service.util

import br.com.sistemalima.apiorcamentofamiliar.model.Revenue
import java.time.LocalDate
import kotlin.random.Random


object RevenueFixture {

    fun build(): Revenue {
        return Revenue(
            id = Random.nextInt().toLong(),
            description = "descrição receita test",
            valor = 20.0,
            data = LocalDate.now()
        )
    }
}