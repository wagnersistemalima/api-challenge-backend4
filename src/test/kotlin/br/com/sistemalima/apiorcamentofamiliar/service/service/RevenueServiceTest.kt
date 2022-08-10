package br.com.sistemalima.apiorcamentofamiliar.service.service

import br.com.sistemalima.apiorcamentofamiliar.constant.ProcessingResult
import br.com.sistemalima.apiorcamentofamiliar.exceptions.BadRequestException
import br.com.sistemalima.apiorcamentofamiliar.model.Revenue
import br.com.sistemalima.apiorcamentofamiliar.repository.RevenueRepository
import br.com.sistemalima.apiorcamentofamiliar.service.RevenueService
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
class RevenueServiceTest {

    @InjectMockKs
    private lateinit var revenueService: RevenueService

    @MockK
    private lateinit var revenueRepository: RevenueRepository

    @Test
    fun `must save a recipe`() {

        val revenueEntity = Revenue(
            id = null,
            description = "descrição receita test",
            valor = 1000.0,
            date = LocalDate.of(2022, 8, 5)
        )

        every { revenueRepository.findByDescription(revenueEntity.description) } returns Optional.empty()

        every { revenueRepository.save(revenueEntity) } returns revenueEntity

        Assertions.assertDoesNotThrow { revenueService.create(revenueEntity) }

        verify(exactly = 1) {revenueRepository.findByDescription(revenueEntity.description)}
        verify(exactly = 1) {revenueRepository.save(revenueEntity)}

    }

    @Test
    fun `should not allow registering a duplicate recipe containing the same description within the month, throwing BadRequestException`() {

        val revenueEntity = Revenue(
            id = null,
            description = "descrição receita test",
            valor = 1000.0,
            date = LocalDate.of(2022, 8, 5)
        )
        val revenue = Revenue(
            id = 1L,
            description = "descrição receita test",
            valor = 1000.0,
            date = LocalDate.of(2022, 8, 31)
        )

        every { revenueRepository.findByDescription(revenueEntity.description) } returns Optional.of(revenue)


        val error = assertThrows<BadRequestException> { revenueService.create(revenueEntity)  }

        assertThat(error.message).isEqualTo(ProcessingResult.BAD_REQUEST_MESSAGE)

        verify(exactly = 1) {revenueRepository.findByDescription(revenueEntity.description)}

    }

    @Test
    fun `must validate duplicate revenue rule by description within the month and return status true`() {

        val revenueEntity = Revenue(
            id = null,
            description = "descrição receita test",
            valor = 1000.0,
            date = LocalDate.of(2022, 8, 5)
        )
        val revenueDb = Revenue(
            id = 1L,
            description = "descrição receita test",
            valor = 1000.0,
            date = LocalDate.of(2022, 9, 5)
        )

        every { revenueRepository.findByDescription(revenueEntity.description) } returns Optional.of(revenueDb)

        val status = revenueService.revenueRuleValidation(revenueEntity.description, revenueEntity.date)

        Assertions.assertEquals(true, status)

        verify(exactly = 1) {revenueRepository.findByDescription(revenueEntity.description)}
    }

    @Test
    fun `must validate duplicate revenue rule by description within the month and return status false`() {

        val revenueEntity = Revenue(
            id = null,
            description = "descrição receita test",
            valor = 1000.0,
            date = LocalDate.of(2022, 8, 5)
        )
        val revenue = Revenue(
            id = 1L,
            description = "descrição receita test",
            valor = 1000.0,
            date = LocalDate.of(2022, 8, 31)
        )

        every { revenueRepository.findByDescription(revenueEntity.description) } returns Optional.of(revenue)

        val status = revenueService.revenueRuleValidation(revenueEntity.description, revenueEntity.date)

        Assertions.assertEquals(false, status)

        verify(exactly = 1) {revenueRepository.findByDescription(revenueEntity.description)}
        verify(exactly = 0) {revenueRepository.save(revenueEntity)}
    }
}