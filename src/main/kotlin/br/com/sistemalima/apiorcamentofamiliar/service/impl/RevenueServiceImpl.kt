package br.com.sistemalima.apiorcamentofamiliar.service.impl

import br.com.sistemalima.apiorcamentofamiliar.constant.ProcessingResult
import br.com.sistemalima.apiorcamentofamiliar.dto.RevenueResponseDTO
import br.com.sistemalima.apiorcamentofamiliar.exceptions.BadRequestException
import br.com.sistemalima.apiorcamentofamiliar.exceptions.EntityNotFoundException
import br.com.sistemalima.apiorcamentofamiliar.model.Revenue
import br.com.sistemalima.apiorcamentofamiliar.repository.RevenueRepository
import br.com.sistemalima.apiorcamentofamiliar.response.Response
import br.com.sistemalima.apiorcamentofamiliar.service.RevenueService
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class RevenueServiceImpl(
    @Autowired
    private val revenueRepository: RevenueRepository
) : RevenueService {

    companion object {
        private val logger = LoggerFactory.getLogger(RevenueServiceImpl::class.java)
        private const val TAG = "class: RevenueServiceImpl"
    }

    @Transactional
    override fun create(revenueEntity: Revenue): Response<RevenueResponseDTO> {

        logger.info("$TAG, method: create, ${ProcessingResult.GET_MOVIMENT_REQUEST}")

        val status = revenueRuleValidation(revenueEntity)

        if (!status) {

            logger.error("ERROR $TAG, method: create, message: ${ProcessingResult.BAD_REQUEST_MESSAGE_VALIDATION_REVENUE} ")

            throw BadRequestException(ProcessingResult.BAD_REQUEST_MESSAGE_VALIDATION_REVENUE)
        }


        revenueRepository.save(revenueEntity)

        val response = RevenueResponseDTO(revenueEntity)

        logger.info("$TAG, method: create SUCCESS, ${ProcessingResult.GET_MOVIMENT_REQUEST}")

        return Response(response)
    }

    @Transactional(readOnly = true)
    override fun findAll(description: String?): Response<List<RevenueResponseDTO>> {

        logger.info("$TAG, method: findAll, ${ProcessingResult.GET_MOVIMENT_REQUEST}")

        val revenues = description?.let {
            revenueRepository.findByDescription(description)
        } ?: revenueRepository.findAll()

        val dto = revenues.map { revenue -> RevenueResponseDTO(revenue) }

        logger.info("$TAG, method: findAll SUCCESS, ${ProcessingResult.GET_MOVIMENT_REQUEST}")

        return Response(dto)

    }

    @Transactional(readOnly = true)
    override fun findById(id: Long): Response<RevenueResponseDTO> {
        logger.info("$TAG, method: findById id: $id, ${ProcessingResult.GET_MOVIMENT_REQUEST}")

        val revenueDb = getRevenueDb(id)

        logger.info("$TAG, method: findById id: $id SUCCESS, ${ProcessingResult.GET_MOVIMENT_REQUEST}")

        return Response(data = RevenueResponseDTO(revenueDb))
    }

    @Transactional
    override fun delete(id: Long) {
        logger.info("$TAG, method: delete id: $id, ${ProcessingResult.GET_MOVIMENT_REQUEST}")

        val revenueDb = getRevenueDb(id)

        revenueRepository.delete(revenueDb)

        logger.info("$TAG, method: delete SUCCESS id: $id, ${ProcessingResult.GET_MOVIMENT_REQUEST}")
    }

    override fun update(revenueEntity: Revenue, id: Long): Response<RevenueResponseDTO> {

        logger.info("TAG, method: update id: $id, ${ProcessingResult.GET_MOVIMENT_REQUEST}")

        val revenueDb = getRevenueDb(id)

        val status = revenueRuleValidation(revenueEntity)

        if (!status) {

            logger.error("ERROR $TAG, method: create, message: ${ProcessingResult.BAD_REQUEST_MESSAGE_VALIDATION_REVENUE} ")

            throw BadRequestException(ProcessingResult.BAD_REQUEST_MESSAGE_VALIDATION_REVENUE)
        }

        val updateRevenue = updateRevenueObject(revenueDb, revenueEntity)

        revenueRepository.save(updateRevenue)

        logger.info("TAG, method: update id: $id SUCCESS, ${ProcessingResult.GET_MOVIMENT_REQUEST}")

        return Response(data = RevenueResponseDTO(updateRevenue))

    }

    @Transactional(readOnly = true)
    override fun findByYearMonth(ano: Int, mes: Int): Response<List<RevenueResponseDTO>> {

        logger.info("$TAG, method: findByYearMonth, ${ProcessingResult.GET_MOVIMENT_REQUEST}")

        val statusDateMes = dateValidMes(mes)
        val statusDateAno = dateValidAno(ano)

        if (!statusDateMes || !statusDateAno) {
            val listaVazia = listOf<RevenueResponseDTO>()
            return Response(listaVazia)
        }

        val list = revenueRepository.findAll()

        val listExpense = filterRevenueAnoMes(list, ano, mes)

        val response = listExpense.map { expense -> RevenueResponseDTO(expense) }

        logger.info("$TAG, method: findByYearMonth SUCCESS, ${ProcessingResult.GET_MOVIMENT_REQUEST}")

        return Response(data = response)
    }

    @Transactional(readOnly = true)
    fun revenueRuleValidation(revenueEntity: Revenue): Boolean {

        logger.info("$TAG, method: validatesRegistrationRecipe, ${ProcessingResult.GET_MOVIMENT_REQUEST}")

        val obj = revenueRepository.findByDescription(revenueEntity.description)

        if (obj.isEmpty()) {
            return true
        }

        obj.forEach {
            if (it.data.month == revenueEntity.data.month && it.data.year == revenueEntity.data.year) {
                return false
            }
        }
        return true

    }

    @Transactional(readOnly = true)
    fun getRevenueDb(id: Long): Revenue {

        logger.info("$TAG, method: getRevenueDb, id: $id, ${ProcessingResult.GET_MOVIMENT_REQUEST}")

        val revenueDb = revenueRepository.findById(id).orElseThrow {
            logger.error("ERROR $TAG, method: findById id: $id, message: ${ProcessingResult.ENTITY_NOT_FOUND_MESSAGE}")
            throw EntityNotFoundException(ProcessingResult.ENTITY_NOT_FOUND_MESSAGE)
        }

        logger.info("$TAG, method: getRevenueDb, id: $id SUCCESS, ${ProcessingResult.GET_MOVIMENT_REQUEST}")

        return revenueDb
    }

    fun updateRevenueObject(revenueDb: Revenue, revenueEntity: Revenue): Revenue {

        logger.info("$TAG, method: updateRevenueObject, id: ${revenueDb.id}, ${ProcessingResult.GET_MOVIMENT_REQUEST}")

        revenueDb.description = revenueEntity.description
        revenueDb.valor = revenueEntity.valor
        revenueDb.data = revenueEntity.data

        return revenueDb
    }

    fun dateValidMes(mes: Int): Boolean {
        logger.info("$TAG, method: dateValidMes, ${ProcessingResult.GET_MOVIMENT_REQUEST}")
        return !(mes > 12 || mes <= 0)
    }

    fun dateValidAno(ano: Int): Boolean {
        logger.info("$TAG, method: dateValidAno, ${ProcessingResult.GET_MOVIMENT_REQUEST}")
        return ano <= 2100
    }

    fun filterRevenueAnoMes(list: MutableList<Revenue>, ano: Int, mes: Int): List<Revenue> {
        logger.info("$TAG, method: filterRevenueAnoMes, ${ProcessingResult.GET_MOVIMENT_REQUEST}")
        val obj: List<Revenue> = list.filter { it.data.year == ano && it.data.month.value == mes  }
        return obj
    }

}