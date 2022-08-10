package br.com.sistemalima.apiorcamentofamiliar.service

import br.com.sistemalima.apiorcamentofamiliar.constant.ProcessingResult
import br.com.sistemalima.apiorcamentofamiliar.dto.RevenueResponseDTO
import br.com.sistemalima.apiorcamentofamiliar.exceptions.BadRequestException
import br.com.sistemalima.apiorcamentofamiliar.exceptions.EntityNotFoundException
import br.com.sistemalima.apiorcamentofamiliar.model.Revenue
import br.com.sistemalima.apiorcamentofamiliar.repository.RevenueRepository
import br.com.sistemalima.apiorcamentofamiliar.response.Response
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDate

@Service
class RevenueService(
    @Autowired
    private val revenueRepository: RevenueRepository
) {

    companion object {
        private val logger = LoggerFactory.getLogger(RevenueService::class.java)
        private const val TAG = "class: ReceitaService"
    }

    @Transactional
    fun create(revenueEntity: Revenue): Response<RevenueResponseDTO> {

        logger.info("$TAG, method: create, ${ProcessingResult.GET_MOVIMENT_REQUEST}")

        val status = revenueRuleValidation(revenueEntity.description, revenueEntity.date)

        if (!status) {
            throw BadRequestException(ProcessingResult.BAD_REQUEST_MESSAGE)
        }


        revenueRepository.save(revenueEntity)

        val response = RevenueResponseDTO(revenueEntity)

        logger.info("$TAG, method: create SUCCESS, ${ProcessingResult.GET_MOVIMENT_REQUEST}")

        return Response(response)
    }

    @Transactional(readOnly = true)
    fun revenueRuleValidation(description: String, date: LocalDate): Boolean {

        logger.info("$TAG, method: validatesRegistrationRecipe, ${ProcessingResult.GET_MOVIMENT_REQUEST}")

        val obj = revenueRepository.findByDescription(description)

        if (obj.isEmpty()) {
            return true
        }

        obj.forEach {
            if (it.date.month == date.month && it.date.year == date.year ) {
                return false
            }
        }
        return true

    }

    @Transactional(readOnly = true)
    fun findAll(): Response<List<RevenueResponseDTO>> {

        logger.info("$TAG, method: findAll, ${ProcessingResult.GET_MOVIMENT_REQUEST}")

        val list = revenueRepository.findAll()
        val dto = list.map { revenue -> RevenueResponseDTO(revenue) }

        logger.info("$TAG, method: findAll SUCCESS, ${ProcessingResult.GET_MOVIMENT_REQUEST}")

        return Response(dto)

    }

    @Transactional(readOnly = true)
    fun findById(id: Long): Response<RevenueResponseDTO> {
        logger.info("$TAG, method: findById id: $id, ${ProcessingResult.GET_MOVIMENT_REQUEST}")

        val revenueDb = revenueRepository.findById(id).orElseThrow {
            logger.error("ERROR $TAG, method: findById id: $id, message: ${ProcessingResult.ENTITY_NOT_FOUND_MESSAGE}")
            throw EntityNotFoundException(ProcessingResult.ENTITY_NOT_FOUND_MESSAGE)
        }

        logger.info("$TAG, method: findById id: $id SUCCESS, ${ProcessingResult.GET_MOVIMENT_REQUEST}")

        return Response(data = RevenueResponseDTO(revenueDb))
    }

}