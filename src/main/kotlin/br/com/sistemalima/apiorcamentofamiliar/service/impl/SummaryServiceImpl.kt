package br.com.sistemalima.apiorcamentofamiliar.service.impl

import br.com.sistemalima.apiorcamentofamiliar.constant.ProcessingResult
import br.com.sistemalima.apiorcamentofamiliar.dto.SummaryResponseDTO
import br.com.sistemalima.apiorcamentofamiliar.response.Response
import br.com.sistemalima.apiorcamentofamiliar.service.ExpenseService
import br.com.sistemalima.apiorcamentofamiliar.service.RevenueService
import br.com.sistemalima.apiorcamentofamiliar.service.SummaryService
import br.com.sistemalima.apiorcamentofamiliar.util.Calculadora
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class SummaryServiceImpl(
    @Autowired
    private val expenseService: ExpenseService,

    @Autowired
    private val revenueService: RevenueService

): SummaryService {

    companion object {
        private const val TAG = "class: SummaryServiceImpl"
        private val logger = LoggerFactory.getLogger(SummaryServiceImpl::class.java)
    }

    override fun findBySummaryYearMonth(ano: Int, mes: Int): Response<SummaryResponseDTO> {

        logger.info("$TAG, method: findBySummaryYearMonth, ${ProcessingResult.START_PROCCESS}")

        val responseExpense = expenseService.findByYearMonth(ano, mes)
        val listResponseExpenseDTO = responseExpense.data

        val responseRevenue = revenueService.findByYearMonth(ano, mes)
        val listResponseRevenueDTO = responseRevenue.data

        val somaDespesaMes = Calculadora.calculaDespesaDoMes(listResponseExpenseDTO)
        val somaReceitaMes = Calculadora.calculaReceitaMes(listResponseRevenueDTO)

        val saldo = Calculadora.calculaSaldoFinalMes(somaReceitaMes, somaDespesaMes)

        val despesaPorcategoriaDTO = Calculadora.calculaGastoNoMesPorCategoria(listResponseExpenseDTO)

        val responseDTO = SummaryResponseDTO(
            totalReceitaMes = somaReceitaMes,
            totalDespesaMes = somaDespesaMes,
            saldoFinalMes = saldo,
            totalDespesaPorCategoria = despesaPorcategoriaDTO
        )

        return Response(data = responseDTO)

    }


}