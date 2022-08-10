package br.com.sistemalima.apiorcamentofamiliar.service.advice

import br.com.sistemalima.apiorcamentofamiliar.controller.advice.ExceptionHandler
import br.com.sistemalima.apiorcamentofamiliar.exceptions.BadRequestException
import br.com.sistemalima.apiorcamentofamiliar.exceptions.EntityNotFoundException
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.junit5.MockKExtension
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.http.HttpStatus
import org.springframework.mock.web.MockHttpServletRequest

@ExtendWith(MockKExtension::class)
class ExceptionHandllerTest {

    @InjectMockKs
    private lateinit var handller: ExceptionHandler

    private  val request = MockHttpServletRequest()

    companion object {
        private const val testMessage = "test message"
    }

    @Test
    fun `should ensure that an incoming BadRequestexception will return 400`() {

        val response = handller.handleBadRequest(BadRequestException(testMessage), request)

        Assertions.assertEquals(HttpStatus.BAD_REQUEST.value(), response.data.status)
        Assertions.assertEquals(testMessage, response.data.message)
        Assertions.assertEquals(HttpStatus.BAD_REQUEST.name, response.data.error)
    }

    @Test
    fun `must ensure that an incoming EntityNotFoundException will return 404`() {

        val response = handller.handleNotFound(EntityNotFoundException(testMessage), request)

        Assertions.assertEquals(HttpStatus.NOT_FOUND.value(), response.data.status)
        Assertions.assertEquals(testMessage, response.data.message)
        Assertions.assertEquals(HttpStatus.NOT_FOUND.name, response.data.error)
    }
}