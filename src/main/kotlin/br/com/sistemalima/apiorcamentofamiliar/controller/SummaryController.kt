package br.com.sistemalima.apiorcamentofamiliar.controller

import br.com.sistemalima.apiorcamentofamiliar.constant.ApiRoutes
import br.com.sistemalima.apiorcamentofamiliar.constant.ProcessingResult
import br.com.sistemalima.apiorcamentofamiliar.dto.SummaryResponseDTO
import br.com.sistemalima.apiorcamentofamiliar.response.Response
import br.com.sistemalima.apiorcamentofamiliar.service.SummaryService
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping(path = [ApiRoutes.SUMMARY_ROUTER], produces = [MediaType.APPLICATION_JSON_VALUE])
class SummaryController(
    @Autowired
    private val summaryService: SummaryService
) {

    companion object {
        private const val TAG = "class: SummaryController"
        private val logger = LoggerFactory.getLogger(SummaryController::class.java)
    }

    @GetMapping(path = [ApiRoutes.PATH_YEAR_MONTH])
    fun findBySummaryYearMonth(@PathVariable ano: Int, @PathVariable mes: Int): ResponseEntity<Response<SummaryResponseDTO>> {

        logger.info("$TAG, method: findBySummaryYearMonth, ano: $ano, mes: $mes, ${ProcessingResult.START_PROCCESS}")

        val response = summaryService.findBySummaryYearMonth(ano, mes)

        return ResponseEntity.ok().body(response)

    }
}