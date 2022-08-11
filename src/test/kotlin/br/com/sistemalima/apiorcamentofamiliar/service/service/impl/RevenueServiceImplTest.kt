package br.com.sistemalima.apiorcamentofamiliar.service.service.impl

import br.com.sistemalima.apiorcamentofamiliar.constant.ProcessingResult
import br.com.sistemalima.apiorcamentofamiliar.exceptions.BadRequestException
import br.com.sistemalima.apiorcamentofamiliar.exceptions.EntityNotFoundException
import br.com.sistemalima.apiorcamentofamiliar.model.Revenue
import br.com.sistemalima.apiorcamentofamiliar.repository.RevenueRepository
import br.com.sistemalima.apiorcamentofamiliar.service.impl.RevenueServiceImpl
import br.com.sistemalima.apiorcamentofamiliar.service.util.ListRevenueFixture
import br.com.sistemalima.apiorcamentofamiliar.service.util.RevenueFixture
import com.nhaarman.mockitokotlin2.any
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
class RevenueServiceImplTest {

    @InjectMockKs
    private lateinit var revenueServiceImpl: RevenueServiceImpl

    @MockK
    private lateinit var revenueRepository: RevenueRepository

    @Test
    fun `create POST deve salvar uma receita`() {

        val revenueEntity = Revenue(
            id = null,
            description = "descrição receita test",
            valor = 1000.0,
            data = LocalDate.of(2022, 12, 5)
        )

        every { revenueRepository.findByDescription(revenueEntity.description) } returns listOf()

        every { revenueRepository.save(revenueEntity) } returns revenueEntity

        Assertions.assertDoesNotThrow { revenueServiceImpl.create(revenueEntity) }

        verify(exactly = 1) { revenueRepository.findByDescription(revenueEntity.description) }
        verify(exactly = 1) { revenueRepository.save(revenueEntity) }

    }

    @Test
    fun `create POST nao deve permitir o registro de uma receita duplicada contendo a mesma descricao dentro do mes, lancar BadRequestException`() {

        val revenueEntity = Revenue(
            id = null,
            description = "descrição receita test",
            valor = 1000.0,
            data = LocalDate.of(2022, 8, 5)
        )
        val revenue = Revenue(
            id = 1L,
            description = "descrição receita test",
            valor = 1000.0,
            data = LocalDate.of(2022, 8, 31)
        )

        every { revenueRepository.findByDescription(revenueEntity.description) } returns listOf(revenue)


        val error = assertThrows<BadRequestException> { revenueServiceImpl.create(revenueEntity) }

        assertThat(error.message).isEqualTo(ProcessingResult.BAD_REQUEST_MESSAGE_VALIDATION_REVENUE)

        verify(exactly = 1) { revenueRepository.findByDescription(revenueEntity.description) }
        verify(exactly = 0) { revenueRepository.save(revenueEntity) }

    }

    @Test
    fun `revenueRuleValidation deve retornar true ao receber uma receita contendo descricao e ano igual a receita cadastrada, mas o mes diferente`() {

        val revenueEntity = Revenue(
            id = null,
            description = "descrição receita test",
            valor = 1000.0,
            data = LocalDate.of(2022, 8, 5)
        )
        val revenueDb = Revenue(
            id = 1L,
            description = "descrição receita test",
            valor = 1000.0,
            data = LocalDate.of(2022, 9, 5)
        )

        every { revenueRepository.findByDescription(revenueEntity.description) } returns listOf(revenueDb)

        val status = revenueServiceImpl.revenueRuleValidation(revenueEntity)

        Assertions.assertEquals(true, status)

        verify(exactly = 1) { revenueRepository.findByDescription(revenueEntity.description) }
    }

    @Test
    fun `revenueRuleValidation deve retornar true ao receber uma receita contendo descricao e data do mes igual a receita cadastrada, mas ano diferente`() {

        val revenueEntity = Revenue(
            id = null,
            description = "descrição receita test",
            valor = 1000.0,
            data = LocalDate.of(2022, 8, 5)
        )
        val revenueDb = Revenue(
            id = 1L,
            description = "descrição receita test",
            valor = 1000.0,
            data = LocalDate.of(2021, 8, 5)
        )

        every { revenueRepository.findByDescription(revenueEntity.description) } returns listOf(revenueDb)

        val status = revenueServiceImpl.revenueRuleValidation(revenueEntity)

        Assertions.assertEquals(true, status)

        verify(exactly = 1) { revenueRepository.findByDescription(revenueEntity.description) }
    }

    @Test
    fun `revenueRuleValidation deve retornar false ao receber uma receita contendo descricao e ano e mes iguais a receita cadastrada`() {

        val revenueEntity = Revenue(
            id = null,
            description = "descrição receita test",
            valor = 1000.0,
            data = LocalDate.of(2022, 8, 5)
        )
        val revenueDb = Revenue(
            id = 1L,
            description = "descrição receita test",
            valor = 1000.0,
            data = LocalDate.of(2022, 8, 31)
        )

        every { revenueRepository.findByDescription(revenueEntity.description) } returns listOf(revenueDb)

        val status = revenueServiceImpl.revenueRuleValidation(revenueEntity)

        Assertions.assertEquals(false, status)

        verify(exactly = 1) { revenueRepository.findByDescription(revenueEntity.description) }
    }

    @Test
    fun `findAll deve retornar uma lista de dto contendo as receitas`() {
        val list = ListRevenueFixture.build()

        every { revenueRepository.findAll() } returns list

        val dto = revenueServiceImpl.findAll()

        verify(exactly = 1) { revenueRepository.findAll() }
        Assertions.assertEquals(2, dto.data.size)
    }

    @Test
    fun `findAll devera devolver uma lista dto vazia quando nao existirem receitas registadas`() {
        val list = listOf<Revenue>()

        every { revenueRepository.findAll() } returns list

        val dto = revenueServiceImpl.findAll()

        verify(exactly = 1) { revenueRepository.findAll() }
        Assertions.assertEquals(0, dto.data.size)
    }

    @Test
    fun `delete deve deletar um ID de receita registrado`() {
        val revenueDb = RevenueFixture.build()
        val idExist = revenueDb.id

        every { revenueRepository.findById(idExist!!) } returns Optional.of(revenueDb)
        every { revenueRepository.delete(revenueDb) } returns any()

        Assertions.assertDoesNotThrow { revenueServiceImpl.delete(idExist!!) }

        verify(exactly = 1) { revenueRepository.findById(idExist!!) }
        verify(exactly = 1) { revenueRepository.delete(revenueDb) }
    }

    @Test
    fun `delete deve lancar a excecao EntityNotFound ao tentar excluir um ID de receita inexistente`() {
        val idNotExist = 5000L

        every { revenueRepository.findById(idNotExist) } returns Optional.empty()

        Assertions.assertThrows(EntityNotFoundException::class.java) { revenueServiceImpl.delete(idNotExist) }

        verify(exactly = 1) { revenueRepository.findById(idNotExist) }
        verify(exactly = 0) { revenueRepository.delete(any()) }
    }

    @Test
    fun `update deve atualizar uma receita quando receber um id da receita existente`() {
        val idExist = 1L
        val revenueEntity = Revenue(
            id = null,
            description = "salario janeiro",
            valor = 1000.0,
            data = LocalDate.of(2022, 8, 11)
        )

        val revenueDb = Revenue(
            id = 10L,
            description = "venda do carro",
            valor = 5000.0,
            data = LocalDate.of(2022, 5, 9)
        )

        every { revenueRepository.findById(idExist) } returns Optional.of(revenueDb)
        every { revenueRepository.findByDescription(revenueEntity.description) } returns listOf()

        val updateRevenue = revenueServiceImpl.updateRevenueObject(revenueDb, revenueEntity)

        every { revenueRepository.save(updateRevenue) } returns updateRevenue

        Assertions.assertDoesNotThrow { revenueServiceImpl.update(revenueEntity, idExist) }

        verify(exactly = 1) { revenueRepository.findById(idExist) }
        verify(exactly = 1) { revenueRepository.findByDescription(revenueEntity.description) }
        verify(exactly = 1) { revenueRepository.save(updateRevenue) }

        Assertions.assertEquals("salario janeiro", revenueDb.description)

    }

    @Test
    fun `update nao deve permitir a atualizacao do registro de uma receita duplicada contendo a mesma descricao dentro do mes, lancar BadRequestException`() {
        val idExist = 1L
        val revenueEntity = Revenue(
            id = null,
            description = "salario janeiro",
            valor = 1000.0,
            data = LocalDate.of(2022, 8, 11)
        )

        val revenueDb = Revenue(
            id = 10L,
            description = "salario janeiro",
            valor = 5000.0,
            data = LocalDate.of(2022, 8, 9)
        )

        every { revenueRepository.findById(idExist) } returns Optional.of(revenueDb)
        every { revenueRepository.findByDescription(revenueEntity.description) } returns listOf(revenueDb)


        Assertions.assertThrows(BadRequestException::class.java) { revenueServiceImpl.update(revenueEntity, idExist) }

        verify(exactly = 1) { revenueRepository.findById(idExist) }
        verify(exactly = 1) { revenueRepository.findByDescription(revenueEntity.description) }

    }

    @Test
    fun `update deve lancar EntityNotfoundException quando nao encontar o id da receita cadastrada`() {
        val idNotExist = 5000L
        val revenueEntity = Revenue(
            id = null,
            description = "salario janeiro",
            valor = 1000.0,
            data = LocalDate.of(2022, 8, 11)
        )

        every { revenueRepository.findById(idNotExist) } returns Optional.empty()

        Assertions.assertThrows(EntityNotFoundException::class.java) {
            revenueServiceImpl.update(
                revenueEntity,
                idNotExist
            )
        }

        verify(exactly = 1) { revenueRepository.findById(idNotExist) }

    }

}