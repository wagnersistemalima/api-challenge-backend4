package br.com.sistemalima.apiorcamentofamiliar.service.util

import br.com.sistemalima.apiorcamentofamiliar.dto.ExpenseResponseDTO
import br.com.sistemalima.apiorcamentofamiliar.dto.RevenueResponseDTO
import br.com.sistemalima.apiorcamentofamiliar.dto.enum.CategoryStatusEnumDTO
import br.com.sistemalima.apiorcamentofamiliar.util.Calculadora
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import java.time.LocalDate

class CalculadoraTest {


    @Test
    fun `calculaDespesaDoMes deve calcular o total de despesas por mes`() {

        val dto1 = ExpenseResponseDTO(
            id = 1L,
            description = "gasolina carro",
            value = 20.0,
            date = LocalDate.of(2022, 8, 10),
            category = CategoryStatusEnumDTO.outras

        )

        val dto2 = ExpenseResponseDTO(
            id = 2L,
            description = "cigarro",
            value = 22.0,
            date = LocalDate.of(2022, 8, 10),
            category = CategoryStatusEnumDTO.outras

        )

        val listaDespesasDTO = listOf(dto1, dto2)

        val soma = Calculadora.calculaDespesaDoMes(listaDespesasDTO)


        Assertions.assertEquals(42.0, soma)
    }

    @Test
    fun `calculaReceitaDoMes deve calcular o total de receita por mes`() {

        val dto1 = RevenueResponseDTO(
            id = 1L,
            description = "Salario janeiro",
            value = 1500.0,
            date = LocalDate.of(2022, 8, 10)
        )

        val dto2 = RevenueResponseDTO(
            id = 2L,
            description = "Venda de perfumes",
            value = 255.20,
            date = LocalDate.of(2022, 8, 10)
        )

        val listaReceitaMesDTO = listOf(dto1, dto2)

        val soma = Calculadora.calculaReceitaMes(listaReceitaMesDTO)

        Assertions.assertEquals(1755.20, soma)
    }

    @Test
    fun `calculaSaldoFinalMes deve calcular o saldo final da soma das despesas e receitas`() {

        val somaDespesas = 1000.0

        val somaReceita = 1200.0

        val saldo = Calculadora.calculaSaldoFinalMes(somaReceita, somaDespesas)

        Assertions.assertEquals(200.0, saldo)
    }

    @Test
    fun `calculaGastoNoMesPorCategoria deve calcular o valor total gasto no mes por categoria`() {

        val dto1 = ExpenseResponseDTO(
            id = 5L,
            description = "despesa gasolina",
            value = 35.00,
            date = LocalDate.of(2022,8,5),
            category = CategoryStatusEnumDTO.outras

        )

        val dto2 = ExpenseResponseDTO(
            id = 6L,
            description = "despesa feira mes",
            value = 35.00,
            date = LocalDate.of(2022,8,10),
            category = CategoryStatusEnumDTO.alimentacao

        )

        val dto3 = ExpenseResponseDTO(
            id = 7L,
            description = "despesa dentista",
            value = 35.00,
            date = LocalDate.of(2022,8,17),
            category = CategoryStatusEnumDTO.saude

        )

        val dto4 = ExpenseResponseDTO(
            id = 8L,
            description = "despesa cigarro",
            value = 35.00,
            date = LocalDate.of(2022,8,3),
            category = CategoryStatusEnumDTO.outras

        )

        val listaDespesasDTO = listOf(dto1, dto2, dto3, dto4)

        val dto = Calculadora.calculaGastoNoMesPorCategoria(listaDespesasDTO)

        Assertions.assertEquals(70.0, dto.totalOutras)
        Assertions.assertEquals(35.0, dto.totalSaude)

    }
}