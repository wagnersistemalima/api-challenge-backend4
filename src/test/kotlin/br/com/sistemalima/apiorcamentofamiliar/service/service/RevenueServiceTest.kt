package br.com.sistemalima.apiorcamentofamiliar.service.service

import br.com.sistemalima.apiorcamentofamiliar.constant.ProcessingResult
import br.com.sistemalima.apiorcamentofamiliar.exceptions.BadRequestException
import br.com.sistemalima.apiorcamentofamiliar.repository.RevenueRepository
import br.com.sistemalima.apiorcamentofamiliar.service.RevenueService
import br.com.sistemalima.apiorcamentofamiliar.service.util.RevenueEntityFixture
import br.com.sistemalima.apiorcamentofamiliar.service.util.RevenueFixture
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
import java.util.*

@ExtendWith(MockKExtension::class)
class RevenueServiceTest {

    @InjectMockKs
    private lateinit var revenueService: RevenueService

    @MockK
    private lateinit var revenueRepository: RevenueRepository

    private val description = "descrição receita test"

    @Test
    fun `must save a recipe`() {

        val revenueEntity = RevenueEntityFixture.build()
        every { revenueRepository.findByDescription(description) } returns Optional.empty()

        every { revenueRepository.save(revenueEntity) } returns revenueEntity

        Assertions.assertDoesNotThrow { revenueService.create(revenueEntity) }

        verify(exactly = 1) {revenueRepository.findByDescription(description)}
        verify(exactly = 1) {revenueRepository.save(revenueEntity)}

    }

    @Test
    fun `should not allow registering a duplicate recipe containing the same description within the month, throwing BadRequestException`() {

        val revenueEntity = RevenueEntityFixture.build()
        val revenue = RevenueFixture.build()
        every { revenueRepository.findByDescription(description) } returns Optional.of(revenue)


        val error = assertThrows<BadRequestException> { revenueService.create(revenueEntity)  }

        assertThat(error.message).isEqualTo(ProcessingResult.BAD_REQUEST_MESSAGE)

        verify(exactly = 1) {revenueRepository.findByDescription(description)}
        verify(exactly = 0) {revenueRepository.save(revenueEntity)}

    }
}