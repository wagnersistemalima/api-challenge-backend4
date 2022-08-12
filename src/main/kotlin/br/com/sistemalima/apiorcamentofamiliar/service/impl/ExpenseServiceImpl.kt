package br.com.sistemalima.apiorcamentofamiliar.service.impl

import br.com.sistemalima.apiorcamentofamiliar.constant.ProcessingResult
import br.com.sistemalima.apiorcamentofamiliar.dto.ExpenseResponseDTO
import br.com.sistemalima.apiorcamentofamiliar.exceptions.BadRequestException
import br.com.sistemalima.apiorcamentofamiliar.exceptions.EntityNotFoundException
import br.com.sistemalima.apiorcamentofamiliar.model.Expense
import br.com.sistemalima.apiorcamentofamiliar.repository.ExpenseRepository
import br.com.sistemalima.apiorcamentofamiliar.response.Response
import br.com.sistemalima.apiorcamentofamiliar.service.ExpenseService
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class ExpenseServiceImpl(
    @Autowired
    private val expenseRepository: ExpenseRepository
) : ExpenseService {

    companion object {
        private val logger = LoggerFactory.getLogger(ExpenseServiceImpl::class.java)
        private const val TAG = "class: ExpenseServiceImpl"
    }

    @Transactional
    override fun create(expenseEntity: Expense): Response<ExpenseResponseDTO> {

        logger.info("$TAG, method: create, ${ProcessingResult.GET_MOVIMENT_REQUEST}")

        val status = expenseRuleValidation(expenseEntity)

        if (!status) {

            logger.error("ERROR $TAG, method: create, message: ${ProcessingResult.BAD_REQUEST_MESSAGE_VALIDATION_EXPENSE} ")

            throw BadRequestException(ProcessingResult.BAD_REQUEST_MESSAGE_VALIDATION_EXPENSE)
        }

        expenseRepository.save(expenseEntity)

        val response = ExpenseResponseDTO(expenseEntity)

        logger.info("$TAG, method: create SUCCESS, ${ProcessingResult.GET_MOVIMENT_REQUEST}")

        return Response(response)
    }

    @Transactional(readOnly = true)
    override fun findAll(): Response<List<ExpenseResponseDTO>> {

        logger.info("$TAG, method: findAll, ${ProcessingResult.GET_MOVIMENT_REQUEST}")

        val list = expenseRepository.findAll()
        val dto = list.map { expense -> ExpenseResponseDTO(expense) }

        logger.info("$TAG, method: findAll SUCCESS, ${ProcessingResult.GET_MOVIMENT_REQUEST}")

        return Response(dto)

    }

    @Transactional(readOnly = true)
    override fun findById(id: Long): Response<ExpenseResponseDTO> {
        logger.info("$TAG, method: findById id: $id, ${ProcessingResult.GET_MOVIMENT_REQUEST}")

        val expenseDb = getExpenseDb(id)

        logger.info("$TAG, method: findById id: $id SUCCESS, ${ProcessingResult.GET_MOVIMENT_REQUEST}")

        return Response(data = ExpenseResponseDTO(expenseDb))
    }

    @Transactional
    override fun delete(id: Long) {
        logger.info("$TAG, method: delete id: $id, ${ProcessingResult.GET_MOVIMENT_REQUEST}")

        val expenseDb = getExpenseDb(id)

        expenseRepository.delete(expenseDb)

        logger.info("$TAG, method: delete SUCCESS id: $id, ${ProcessingResult.GET_MOVIMENT_REQUEST}")
    }

    override fun update(expenseEntity: Expense, id: Long): Response<ExpenseResponseDTO> {

        logger.info("TAG, method: update id: $id, ${ProcessingResult.GET_MOVIMENT_REQUEST}")

        val expenseDb = getExpenseDb(id)

        val status = expenseRuleValidation(expenseEntity)

        if (!status) {

            logger.error("ERROR $TAG, method: create, message: ${ProcessingResult.BAD_REQUEST_MESSAGE_VALIDATION_EXPENSE} ")

            throw BadRequestException(ProcessingResult.BAD_REQUEST_MESSAGE_VALIDATION_EXPENSE)
        }

        val updateExpense = updateExpenseObject(expenseDb, expenseEntity)

        expenseRepository.save(updateExpense)

        logger.info("TAG, method: update id: $id SUCCESS, ${ProcessingResult.GET_MOVIMENT_REQUEST}")

        return Response(data = ExpenseResponseDTO(updateExpense))

    }

    @Transactional(readOnly = true)
    fun expenseRuleValidation(expenseEntity: Expense): Boolean {

        logger.info("$TAG, method: expenseRuleValidation, ${ProcessingResult.GET_MOVIMENT_REQUEST}")

        val obj = expenseRepository.findByDescription(expenseEntity.description)

        if (obj.isEmpty()) {
            return true
        }

        obj.forEach {
            if (it.data.month == expenseEntity.data.month && it.data.year == expenseEntity.data.year) {
                return false
            }
        }
        return true

    }

    @Transactional(readOnly = true)
    fun getExpenseDb(id: Long): Expense {

        logger.info("$TAG, method: getExpenseDb, id: $id, ${ProcessingResult.GET_MOVIMENT_REQUEST}")

        val expenseDb = expenseRepository.findById(id).orElseThrow {
            logger.error("ERROR $TAG, method: findById id: $id, message: ${ProcessingResult.ENTITY_NOT_FOUND_MESSAGE}")
            throw EntityNotFoundException(ProcessingResult.ENTITY_NOT_FOUND_MESSAGE)
        }

        logger.info("$TAG, method: getExpenseDb, id: $id SUCCESS, ${ProcessingResult.GET_MOVIMENT_REQUEST}")

        return expenseDb
    }

    fun updateExpenseObject(expenseDb: Expense, expenseEntity: Expense): Expense {

        logger.info("$TAG, method: updateExpenseObject, id: ${expenseDb.id}, ${ProcessingResult.GET_MOVIMENT_REQUEST}")

        expenseDb.description = expenseEntity.description
        expenseDb.valor = expenseEntity.valor
        expenseDb.data = expenseEntity.data

        return expenseDb
    }

}