package br.com.sistemalima.apiorcamentofamiliar.controller

import br.com.sistemalima.apiorcamentofamiliar.constant.ApiRoutes
import br.com.sistemalima.apiorcamentofamiliar.constant.ProcessingResult
import br.com.sistemalima.apiorcamentofamiliar.dto.RevenueRequestDTO
import br.com.sistemalima.apiorcamentofamiliar.dto.RevenueResponseDTO
import br.com.sistemalima.apiorcamentofamiliar.request.Request
import br.com.sistemalima.apiorcamentofamiliar.response.Response
import br.com.sistemalima.apiorcamentofamiliar.service.RevenueService
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.springframework.web.util.UriComponentsBuilder
import javax.validation.Valid

@RestController
@RequestMapping(path = [ApiRoutes.REVENUE_ROUTER], produces = [MediaType.APPLICATION_JSON_VALUE])
class RevenueController(
    @Autowired
    private val revenueService: RevenueService
) {

    companion object {
        private val logger = LoggerFactory.getLogger(RevenueController::class.java)
        private const val TAG = "class: RevenueController"
    }

    @PostMapping
    fun create(
        @RequestBody @Valid request: Request<RevenueRequestDTO>,
        uriBuilder: UriComponentsBuilder
    ): ResponseEntity<Response<RevenueResponseDTO>> {

        logger.info("$TAG, method: create [POST], ${ProcessingResult.START_PROCCESS}")

        val revenueEntity = request.data.toModel()

        val response = revenueService.create(revenueEntity)

        val uri = uriBuilder.path("/receitas/${response.data.id}").build().toUri()

        logger.info("$TAG, method: create [POST] SUCCESS [POST], ${ProcessingResult.END_PROCESS}")

        return ResponseEntity.created(uri).body(response)
    }

    @GetMapping
    fun findAll(): ResponseEntity<Response<List<RevenueResponseDTO>>> {

        logger.info("$TAG, method: findAll [GET], ${ProcessingResult.START_PROCCESS}")

        val response = revenueService.findAll()

        logger.info("$TAG, method: findAll [GET] SUCCESS, ${ProcessingResult.END_PROCESS}")
        return ResponseEntity.ok().body(response)
    }

    @GetMapping(path = [ApiRoutes.PATH_ID])
    fun findById(@PathVariable id: Long): ResponseEntity<Response<RevenueResponseDTO>> {

        logger.info("$TAG, method: findById [GET] id: $id, ${ProcessingResult.START_PROCCESS}")

        val response = revenueService.findById(id)

        logger.info("$TAG, method: findById [GET] id: $id SUCCESS, ${ProcessingResult.END_PROCESS}")
        return ResponseEntity.ok().body(response)
    }

    @DeleteMapping(path = [ApiRoutes.PATH_ID])
    fun delete(@PathVariable id: Long): ResponseEntity<Unit> {

        logger.info("$TAG, method: delete [DELETE] id: $id, ${ProcessingResult.START_PROCCESS}")

        revenueService.delete(id)

        logger.info("$TAG, method: delete [DELETE] id: $id SUCCESS, ${ProcessingResult.END_PROCESS}")

        return ResponseEntity.noContent().build()
    }

    @PutMapping(path = [ApiRoutes.PATH_ID])
    fun update(
        @PathVariable id: Long,
        @RequestBody request: Request<RevenueRequestDTO>
    ): ResponseEntity<Response<RevenueResponseDTO>> {
        logger.info("$TAG, method: update [PUT] id: $id, ${ProcessingResult.START_PROCCESS}")

        val revenueEntity = request.data.toModel()

        val response = revenueService.update(revenueEntity, id)

        logger.info("$TAG, method: update [PUT] id: $id SUCCESS, ${ProcessingResult.END_PROCESS}")

        return ResponseEntity.ok().body(response)
    }

}