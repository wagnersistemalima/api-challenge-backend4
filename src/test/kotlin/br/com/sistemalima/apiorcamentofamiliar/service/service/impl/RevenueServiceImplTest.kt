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
    fun `create POST must save a recipe`() {

        val revenueEntity = Revenue(
            id = null,
            description = "descrição receita test",
            valor = 1000.0,
            data = LocalDate.of(2022, 12, 5)
        )

        every { revenueRepository.findByDescription(revenueEntity.description) } returns listOf()

        every { revenueRepository.save(revenueEntity) } returns revenueEntity

        Assertions.assertDoesNotThrow { revenueServiceImpl.create(revenueEntity) }

        verify(exactly = 1) {revenueRepository.findByDescription(revenueEntity.description)}
        verify(exactly = 1) {revenueRepository.save(revenueEntity)}

    }

    @Test
    fun `create POST should not allow registering a duplicate recipe containing the same description within the month, throwing BadRequestException`() {

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


        val error = assertThrows<BadRequestException> { revenueServiceImpl.create(revenueEntity)  }

        assertThat(error.message).isEqualTo(ProcessingResult.BAD_REQUEST_MESSAGE)

        verify(exactly = 1) {revenueRepository.findByDescription(revenueEntity.description)}
        verify(exactly = 0) {revenueRepository.save(revenueEntity)}

    }

    @Test
    fun `revenueRuleValidation must return true when receiving a recipe containing a description and year equal to the registered recipe but the month is different`() {

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

        val status = revenueServiceImpl.revenueRuleValidation(revenueEntity.description, revenueEntity.data)

        Assertions.assertEquals(true, status)

        verify(exactly = 1) {revenueRepository.findByDescription(revenueEntity.description)}
    }

    @Test
    fun `revenueRuleValidation must return true when receiving a recipe containing a description and date of the month equal to the registered recipe but different year`() {

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

        val status = revenueServiceImpl.revenueRuleValidation(revenueEntity.description, revenueEntity.data)

        Assertions.assertEquals(true, status)

        verify(exactly = 1) {revenueRepository.findByDescription(revenueEntity.description)}
    }

    @Test
    fun `revenueRuleValidation should return false when receiving a recipe containing description and year and month equal to the recipe but different day`() {

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

        val status = revenueServiceImpl.revenueRuleValidation(revenueEntity.description, revenueEntity.data)

        Assertions.assertEquals(false, status)

        verify(exactly = 1) {revenueRepository.findByDescription(revenueEntity.description)}
    }

    @Test
    fun `findAll should return a list of dto containing the recipes`() {
        val list = ListRevenueFixture.build()

        every { revenueRepository.findAll() } returns list

        val dto = revenueServiceImpl.findAll()

        verify(exactly = 1) {revenueRepository.findAll()}
        Assertions.assertEquals(2, dto.data.size)
    }

    @Test
    fun `findAll should return an empty dto list when there are no registered recipes`() {
        val list = listOf<Revenue>()

        every { revenueRepository.findAll() } returns list

        val dto = revenueServiceImpl.findAll()

        verify(exactly = 1) {revenueRepository.findAll()}
        Assertions.assertEquals(0, dto.data.size)
    }

    @Test
    fun `deve deletar um id de receita cadastrada`() {
        val revenueDb = RevenueFixture.build()
        val idExist = revenueDb.id

        every { revenueRepository.findById(idExist!!) } returns Optional.of(revenueDb)
        every { revenueRepository.delete(revenueDb) } returns any()

        Assertions.assertDoesNotThrow { revenueServiceImpl.delete(idExist!!) }

        verify(exactly = 1) {revenueRepository.findById(idExist!!)}
        verify(exactly = 1) {revenueRepository.delete(revenueDb)}
    }

    @Test
    fun `deve lancar exception EntityNotFound quando tentar deletar um id receita inexistente`() {
        val idNotExist = 5000L

        every { revenueRepository.findById(idNotExist) } returns Optional.empty()

        Assertions.assertThrows(EntityNotFoundException::class.java) {revenueServiceImpl.delete(idNotExist)}

        verify(exactly = 1) {revenueRepository.findById(idNotExist)}
        verify(exactly = 0) {revenueRepository.delete(any())}
    }

}