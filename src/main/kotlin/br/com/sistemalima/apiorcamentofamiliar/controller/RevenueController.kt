package br.com.sistemalima.apiorcamentofamiliar.controller

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
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.servlet.support.ServletUriComponentsBuilder
import org.springframework.web.util.UriComponentsBuilder
import javax.validation.Valid

@RestController
@RequestMapping(path = ["/receitas"], produces = [MediaType.APPLICATION_JSON_VALUE])
class RevenueController(
    @Autowired
    private val revenueService: RevenueService
) {

    companion object {
        private val logger = LoggerFactory.getLogger(RevenueController::class.java)
        private const val TAG = "class: RevenueController"
    }

    @PostMapping
    fun create(@RequestBody @Valid request: Request<RevenueRequestDTO>, uriBuilder: UriComponentsBuilder): ResponseEntity<Response<RevenueResponseDTO>> {

        logger.info("$TAG, method: create [POST], ${ProcessingResult.START_PROCCESS}")

        val revenueEntity = request.data.toModel()

        val response = revenueService.create(revenueEntity)

        val uri = uriBuilder.path("/receitas/${response.data.id}").build().toUri()

        logger.info("$TAG, method: create [POST], ${ProcessingResult.END_PROCESS}")

        return ResponseEntity.created(uri).body(response)
    }
}