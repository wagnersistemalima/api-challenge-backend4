package br.com.sistemalima.apiorcamentofamiliar.service.service.impl.expense

import br.com.sistemalima.apiorcamentofamiliar.constant.ProcessingResult
import br.com.sistemalima.apiorcamentofamiliar.dto.ExpenseResponseDTO
import br.com.sistemalima.apiorcamentofamiliar.exceptions.BadRequestException
import br.com.sistemalima.apiorcamentofamiliar.exceptions.EntityNotFoundException
import br.com.sistemalima.apiorcamentofamiliar.model.Expense
import br.com.sistemalima.apiorcamentofamiliar.repository.ExpenseRepository
import br.com.sistemalima.apiorcamentofamiliar.service.impl.ExpenseServiceImpl
import br.com.sistemalima.apiorcamentofamiliar.service.util.ExpenseFixture
import br.com.sistemalima.apiorcamentofamiliar.service.util.ListExpenseFixture
import io.mockk.every
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import io.mockk.verify
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.api.extension.ExtendWith
import java.time.LocalDate
import java.util.*

@ExtendWith(MockKExtension::class)
class ExpenseServiceImplTest {

    @InjectMockKs
    private lateinit var expenseServiceImpl: ExpenseServiceImpl

    @MockK
    private lateinit var expenseRepository: ExpenseRepository

    @Test
    fun `create POST deve salvar uma despesa`() {

        val expenseEntity = Expense(
            id = null,
            description = "descrição despesa test",
            valor = 1000.0,
            data = LocalDate.of(2022, 12, 5)
        )

        every { expenseRepository.findByDescription(expenseEntity.description) } returns listOf()

        every { expenseRepository.save(expenseEntity) } returns expenseEntity

        Assertions.assertDoesNotThrow { expenseServiceImpl.create(expenseEntity) }

        verify(exactly = 1) { expenseRepository.findByDescription(expenseEntity.description) }
        verify(exactly = 1) { expenseRepository.save(expenseEntity) }

    }

    @Test
    fun `create POST nao deve permitir o registro de uma despesa duplicada contendo a mesma descricao dentro do mes, lancar BadRequestException`() {

        val expenseEntity = Expense(
            id = null,
            description = "descrição despesa test",
            valor = 1000.0,
            data = LocalDate.of(2022, 8, 5)
        )
        val expense = Expense(
            id = 1L,
            description = "descrição despesa test",
            valor = 1000.0,
            data = LocalDate.of(2022, 8, 31)
        )

        every { expenseRepository.findByDescription(expenseEntity.description) } returns listOf(expense)


        val error = assertThrows<BadRequestException> { expenseServiceImpl.create(expenseEntity) }

        assertThat(error.message).isEqualTo(ProcessingResult.BAD_REQUEST_MESSAGE_VALIDATION_EXPENSE)

        verify(exactly = 1) { expenseRepository.findByDescription(expenseEntity.description) }
        verify(exactly = 0) { expenseRepository.save(expenseEntity) }

    }

    @Test
    fun `expenseRuleValidation deve retornar true ao receber uma despesa contendo descricao e ano igual a despesa cadastrada, mas o mes diferente`() {

        val expenseEntity = Expense(
            id = null,
            description = "descrição despesa test",
            valor = 1000.0,
            data = LocalDate.of(2022, 8, 5)
        )
        val expenseDb = Expense(
            id = 1L,
            description = "descrição despesa test",
            valor = 1000.0,
            data = LocalDate.of(2022, 9, 5)
        )

        every { expenseRepository.findByDescription(expenseEntity.description) } returns listOf(expenseDb)

        val status = expenseServiceImpl.expenseRuleValidation(expenseEntity)

        Assertions.assertEquals(true, status)

        verify(exactly = 1) { expenseRepository.findByDescription(expenseEntity.description) }
    }

    @Test
    fun `expenseRuleValidation deve retornar true ao receber uma despesa contendo descricao e data do mes igual a despesa cadastrada, mas ano diferente`() {

        val expenseEntity = Expense(
            id = null,
            description = "descrição despesa test",
            valor = 1000.0,
            data = LocalDate.of(2022, 8, 5)
        )
        val expenseDb = Expense(
            id = 1L,
            description = "descrição despesa test",
            valor = 1000.0,
            data = LocalDate.of(2021, 8, 5)
        )

        every { expenseRepository.findByDescription(expenseEntity.description) } returns listOf(expenseDb)

        val status = expenseServiceImpl.expenseRuleValidation(expenseEntity)

        Assertions.assertEquals(true, status)

        verify(exactly = 1) { expenseRepository.findByDescription(expenseEntity.description) }
    }

    @Test
    fun `expenseRuleValidation deve retornar false ao receber uma despesa contendo descricao e ano e mes iguais a despesa cadastrada`() {

        val expenseEntity = Expense(
            id = null,
            description = "descrição despesa test",
            valor = 1000.0,
            data = LocalDate.of(2022, 8, 5)
        )
        val expenseDb = Expense(
            id = 1L,
            description = "descrição despesa test",
            valor = 1000.0,
            data = LocalDate.of(2022, 8, 31)
        )

        every { expenseRepository.findByDescription(expenseEntity.description) } returns listOf(expenseDb)

        val status = expenseServiceImpl.expenseRuleValidation(expenseEntity)

        Assertions.assertEquals(false, status)

        verify(exactly = 1) { expenseRepository.findByDescription(expenseEntity.description) }
    }

    @Test
    fun `findAll deve retornar uma lista de dto contendo as despesas`() {
        val list = ListExpenseFixture.build()
        val description: String? = null
        every { expenseRepository.findAll() } returns list

        val dto = expenseServiceImpl.findAll(description)

        verify(exactly = 1) { expenseRepository.findAll() }
        Assertions.assertEquals(2, dto.data.size)
    }

    @Test
    fun `findAll devera devolver uma lista dto vazia quando nao existirem despesas registadas`() {
        val list = listOf<Expense>()
        val description: String? = null
        every { expenseRepository.findAll() } returns list

        val dto = expenseServiceImpl.findAll(description)

        verify(exactly = 1) { expenseRepository.findAll() }
        Assertions.assertEquals(0, dto.data.size)
    }

    @Test
    fun `findAll devera devolver uma lista dto com despesas registadas quando receber a descricao de despesa existente`() {
        val list = listOf<Expense>(ExpenseFixture.build())
        val description = "descrição despesa test"
        every { expenseRepository.findByDescription(description) } returns list

        val dto = expenseServiceImpl.findAll(description)

        verify(exactly = 1) { expenseRepository.findByDescription(description) }
        Assertions.assertEquals(1, dto.data.size)
    }

    @Test
    fun `delete deve deletar um ID de despesa registrado`() {
        val expenseDb = ExpenseFixture.build()
        val idExist = expenseDb.id

        every { expenseRepository.findById(idExist!!) } returns Optional.of(expenseDb)
        every { expenseRepository.delete(expenseDb) } returns Unit

        Assertions.assertDoesNotThrow { expenseServiceImpl.delete(idExist!!) }

        verify(exactly = 1) { expenseRepository.findById(idExist!!) }
        verify(exactly = 1) { expenseRepository.delete(expenseDb) }
    }

    @Test
    fun `delete deve lancar a excecao EntityNotFound ao tentar excluir um ID de despesa inexistente`() {
        val idNotExist = 5000L

        every { expenseRepository.findById(idNotExist) } returns Optional.empty()

        Assertions.assertThrows(EntityNotFoundException::class.java) { expenseServiceImpl.delete(idNotExist) }

        verify(exactly = 1) { expenseRepository.findById(idNotExist) }
        verify(exactly = 0) { expenseRepository.delete(any()) }
    }

    @Test
    fun `update deve atualizar uma despesa quando receber um id da despesa existente`() {
        val idExist = 1L
        val expenseEntity = Expense(
            id = null,
            description = "feira janeiro",
            valor = 1000.0,
            data = LocalDate.of(2022, 8, 11)
        )

        val expenseDb = Expense(
            id = 10L,
            description = "gasolina carro",
            valor = 5000.0,
            data = LocalDate.of(2022, 5, 9)
        )

        every { expenseRepository.findById(idExist) } returns Optional.of(expenseDb)
        every { expenseRepository.findByDescription(expenseEntity.description) } returns listOf()

        val updateExpense = expenseServiceImpl.updateExpenseObject(expenseDb, expenseEntity)

        every { expenseRepository.save(updateExpense) } returns updateExpense

        Assertions.assertDoesNotThrow { expenseServiceImpl.update(expenseEntity, idExist) }

        verify(exactly = 1) { expenseRepository.findById(idExist) }
        verify(exactly = 1) { expenseRepository.findByDescription(expenseEntity.description) }
        verify(exactly = 1) { expenseRepository.save(updateExpense) }

        Assertions.assertEquals("feira janeiro", expenseDb.description)

    }

    @Test
    fun `update nao deve permitir a atualizacao do registro de uma despesa duplicada contendo a mesma descricao dentro do mes, lancar BadRequestException`() {
        val idExist = 1L
        val expenseEntity = Expense(
            id = null,
            description = "feira janeiro",
            valor = 1000.0,
            data = LocalDate.of(2022, 8, 11)
        )

        val expenseDb = Expense(
            id = 10L,
            description = "feira janeiro",
            valor = 5000.0,
            data = LocalDate.of(2022, 8, 9)
        )

        every { expenseRepository.findById(idExist) } returns Optional.of(expenseDb)
        every { expenseRepository.findByDescription(expenseEntity.description) } returns listOf(expenseDb)


        Assertions.assertThrows(BadRequestException::class.java) { expenseServiceImpl.update(expenseEntity, idExist) }

        verify(exactly = 1) { expenseRepository.findById(idExist) }
        verify(exactly = 1) { expenseRepository.findByDescription(expenseEntity.description) }

    }

    @Test
    fun `update deve lancar EntityNotfoundException quando nao encontar o id da despesa cadastrada`() {
        val idNotExist = 5000L
        val expenseEntity = Expense(
            id = null,
            description = "feira janeiro",
            valor = 1000.0,
            data = LocalDate.of(2022, 8, 11)
        )

        every { expenseRepository.findById(idNotExist) } returns Optional.empty()

        Assertions.assertThrows(EntityNotFoundException::class.java) {
            expenseServiceImpl.update(
                expenseEntity,
                idNotExist
            )
        }

        verify(exactly = 1) { expenseRepository.findById(idNotExist) }

    }

    @Test
    fun `findByYearMonth deve retornar uma lista de dto contendo despesas por mes quando ano e mes for valido`() {
        val ano: Int = 2022
        val mes: Int = 8

        val expense1 = Expense(
            id = 10L,
            description = "concerto carro",
            valor = 200.0,
            data = LocalDate.of(2022, 8, 25)
        )
        val expense2 = Expense(
            id = 10L,
            description = "reforma casa",
            valor = 500.0,
            data = LocalDate.of(2022, 8, 5)
        )

        val list = mutableListOf<Expense>(expense1, expense2)

        every { expenseRepository.findAll() } returns list

        val response = expenseServiceImpl.findByYearMonth(ano, mes)

        verify (exactly = 1) {expenseRepository.findAll()}

        Assertions.assertEquals(2, response.data.size)

    }

    @Test
    fun `findByYearMonth deve retornar uma lista de dto vazia, quando o ano e o mes for invalido e nao chamar o findAll`() {
        val ano: Int = 2101
        val mes: Int = 13

        every { expenseRepository.findAll() } returns listOf()

        val response = expenseServiceImpl.findByYearMonth(ano, mes)

        verify (exactly = 0) {expenseRepository.findAll()}

        Assertions.assertEquals(0, response.data.size)

    }

}