package br.com.sistemalima.apiorcamentofamiliar.service

import br.com.sistemalima.apiorcamentofamiliar.constant.ProcessingResult
import br.com.sistemalima.apiorcamentofamiliar.dto.RevenueResponseDTO
import br.com.sistemalima.apiorcamentofamiliar.exceptions.BadRequestException
import br.com.sistemalima.apiorcamentofamiliar.model.Revenue
import br.com.sistemalima.apiorcamentofamiliar.repository.RevenueRepository
import br.com.sistemalima.apiorcamentofamiliar.response.Response
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

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

        val validRecipe = validatesRegistrationRecipe(revenueEntity)

        revenueRepository.save(validRecipe)

        val response = RevenueResponseDTO(validRecipe)

        logger.info("$TAG, method: create SUCCESS, ${ProcessingResult.GET_MOVIMENT_REQUEST}")

        return Response(response)
    }

    @Transactional(readOnly = true)
    private fun validatesRegistrationRecipe(revenueEntity: Revenue): Revenue {

        logger.info("$TAG, method: validatesRegistrationRecipe, ${ProcessingResult.GET_MOVIMENT_REQUEST}")

        val date = revenueEntity.date
        val description = revenueEntity.description
        val dateRequestMonth = date.toString()[5].toString() + date.toString()[6].toString()

        val obj = revenueRepository.findByDescription(description)

        if (obj.isEmpty) {
            return revenueEntity
        }

        val entityMonthDate = obj.get().date.toString()[5].toString() + obj.get().date.toString()[6]

        if (obj.isPresent && dateRequestMonth == entityMonthDate) {
            logger.error("ERROR: $TAG, method: create, message: ${ProcessingResult.BAD_REQUEST_MESSAGE}")
            throw BadRequestException(ProcessingResult.BAD_REQUEST_MESSAGE)
        }

        logger.info("$TAG, method: validatesRegistrationRecipe SUCCESS, ${ProcessingResult.GET_MOVIMENT_REQUEST}")

        return revenueEntity

    }


}