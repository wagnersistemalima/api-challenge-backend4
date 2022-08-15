package br.com.sistemalima.apiorcamentofamiliar.service.service.impl.summary

import br.com.sistemalima.apiorcamentofamiliar.response.Response
import br.com.sistemalima.apiorcamentofamiliar.service.ExpenseService
import br.com.sistemalima.apiorcamentofamiliar.service.RevenueService
import br.com.sistemalima.apiorcamentofamiliar.service.impl.SummaryServiceImpl
import br.com.sistemalima.apiorcamentofamiliar.service.util.ListExpenseResponseDTOFixture
import br.com.sistemalima.apiorcamentofamiliar.service.util.ListRevenueResponseDTOFixture
import io.mockk.every
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith

@ExtendWith(MockKExtension::class)
class SummaryServiceImplTest {

    @InjectMockKs
    private lateinit var summaryServiceImpl: SummaryServiceImpl

    @MockK
    private lateinit var expenseService: ExpenseService

    @MockK
    private lateinit var revenueService: RevenueService

    @Test
    fun `deve gerar um dto contendo o relatorio de despesas e receita do mes`() {

        val ano = 2022
        val mes = 8

        val listExpenseDb = ListExpenseResponseDTOFixture.build()
        val responseExpense = Response(listExpenseDb)

        val listRevenueDb = ListRevenueResponseDTOFixture.build()
        val responseRevenue = Response(listRevenueDb)

        every { expenseService.findByYearMonth(ano, mes) } returns responseExpense

        every { revenueService.findByYearMonth(ano, mes) } returns responseRevenue

        val relatorio = summaryServiceImpl.findBySummaryYearMonth(ano, mes)

        Assertions.assertEquals(1000.0, relatorio.data.totalReceitaMes)
        Assertions.assertEquals(140.0, relatorio.data.totalDespesaMes)
        Assertions.assertEquals(70.0, relatorio.data.totalDespesaPorCategoria.totalOutras)
        Assertions.assertEquals(35.0, relatorio.data.totalDespesaPorCategoria.totalSaude)
        Assertions.assertEquals(35.0, relatorio.data.totalDespesaPorCategoria.totalAlimentacao)
    }
}