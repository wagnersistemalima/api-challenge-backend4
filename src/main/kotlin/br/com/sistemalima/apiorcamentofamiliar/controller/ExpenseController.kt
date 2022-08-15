package br.com.sistemalima.apiorcamentofamiliar.controller

import br.com.sistemalima.apiorcamentofamiliar.constant.ApiRoutes
import br.com.sistemalima.apiorcamentofamiliar.constant.ProcessingResult
import br.com.sistemalima.apiorcamentofamiliar.dto.ExpenseRequestDTO
import br.com.sistemalima.apiorcamentofamiliar.dto.ExpenseResponseDTO
import br.com.sistemalima.apiorcamentofamiliar.request.Request
import br.com.sistemalima.apiorcamentofamiliar.response.Response
import br.com.sistemalima.apiorcamentofamiliar.service.ExpenseService
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.springframework.web.util.UriComponentsBuilder
import javax.validation.Valid

@RestController
@RequestMapping(path = [ApiRoutes.EXPENSE_ROUTER], produces = [MediaType.APPLICATION_JSON_VALUE])
class ExpenseController(
    @Autowired
    private val expenseService: ExpenseService
) {

    companion object {
        private val logger = LoggerFactory.getLogger(ExpenseController::class.java)
        private const val TAG = "class: ExpenseController"
    }

    @PostMapping
    fun create(
        @RequestBody @Valid request: Request<ExpenseRequestDTO>,
        uriBuilder: UriComponentsBuilder
    ): ResponseEntity<Response<ExpenseResponseDTO>> {

        logger.info("$TAG, method: create [POST], ${ProcessingResult.START_PROCCESS}")

        val expenseEntity = request.data.toModel()

        val response = expenseService.create(expenseEntity)

        val uri = uriBuilder.path("${ApiRoutes.EXPENSE_ROUTER}/${response.data.id}").build().toUri()

        logger.info("$TAG, method: create [POST] SUCCESS [POST], ${ProcessingResult.END_PROCESS}")

        return ResponseEntity.created(uri).body(response)
    }

    @GetMapping
    fun findAll(@RequestParam description: String?): ResponseEntity<Response<List<ExpenseResponseDTO>>> {

        logger.info("$TAG, method: findAll [GET], ${ProcessingResult.START_PROCCESS}")

        val response = expenseService.findAll(description)

        logger.info("$TAG, method: findAll [GET] SUCCESS, ${ProcessingResult.END_PROCESS}")
        return ResponseEntity.ok().body(response)
    }

    @GetMapping(path = [ApiRoutes.PATH_ID])
    fun findById(@PathVariable id: Long): ResponseEntity<Response<ExpenseResponseDTO>> {

        logger.info("$TAG, method: findById [GET] id: $id, ${ProcessingResult.START_PROCCESS}")

        val response = expenseService.findById(id)

        logger.info("$TAG, method: findById [GET] id: $id SUCCESS, ${ProcessingResult.END_PROCESS}")
        return ResponseEntity.ok().body(response)
    }

    @DeleteMapping(path = [ApiRoutes.PATH_ID])
    fun delete(@PathVariable id: Long): ResponseEntity<Unit> {

        logger.info("$TAG, method: delete [DELETE] id: $id, ${ProcessingResult.START_PROCCESS}")

        expenseService.delete(id)

        logger.info("$TAG, method: delete [DELETE] id: $id SUCCESS, ${ProcessingResult.END_PROCESS}")

        return ResponseEntity.noContent().build()
    }

    @PutMapping(path = [ApiRoutes.PATH_ID])
    fun update(
        @PathVariable id: Long,
        @RequestBody request: Request<ExpenseRequestDTO>
    ): ResponseEntity<Response<ExpenseResponseDTO>> {

        logger.info("$TAG, method: update [PUT] id: $id, ${ProcessingResult.START_PROCCESS}")

        val expenseEntity = request.data.toModel()

        val response = expenseService.update(expenseEntity, id)

        logger.info("$TAG, method: update [PUT] id: $id SUCCESS, ${ProcessingResult.END_PROCESS}")

        return ResponseEntity.ok().body(response)
    }

    @GetMapping(path = [ApiRoutes.PATH_YEAR_MONTH])
    fun findByYearMonth(@PathVariable ano: Int, @PathVariable  mes: Int): ResponseEntity<Response<List<ExpenseResponseDTO>>> {
        logger.info("$TAG, method: findByYearMonth, ano: $ano, mes: $mes, ${ProcessingResult.START_PROCCESS}")

        val response = expenseService.findByYearMonth(ano, mes)

        logger.info("$TAG, method: findByYearMonth, ano: $ano, mes: $mes SUCCESS, ${ProcessingResult.END_PROCESS}")

        return ResponseEntity.ok().body(response)
    }
}