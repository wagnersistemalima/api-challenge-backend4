package br.com.sistemalima.apiorcamentofamiliar.service.advice

import br.com.sistemalima.apiorcamentofamiliar.constant.ProcessingResult
import br.com.sistemalima.apiorcamentofamiliar.controller.advice.ExceptionHandler
import br.com.sistemalima.apiorcamentofamiliar.exceptions.BadRequestException
import br.com.sistemalima.apiorcamentofamiliar.exceptions.EntityNotFoundException
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.junit5.MockKExtension
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.Mockito
import org.springframework.http.HttpInputMessage
import org.springframework.http.HttpStatus
import org.springframework.http.converter.HttpMessageNotReadableException
import org.springframework.mock.web.MockHttpServletRequest

@ExtendWith(MockKExtension::class)
class ExceptionHandllerTest {

    @InjectMockKs
    private lateinit var handller: ExceptionHandler

    private val request = MockHttpServletRequest()

    @Test
    fun `handleBadRequest deve garantir que uma excecao BadRequest de entrada retornara 400`() {

        val response = handller.handleBadRequest(
            BadRequestException(ProcessingResult.BAD_REQUEST_MESSAGE_VALIDATION_REVENUE),
            request
        )

        Assertions.assertEquals(HttpStatus.BAD_REQUEST.value(), response.data.status)
        Assertions.assertEquals(ProcessingResult.BAD_REQUEST_MESSAGE_VALIDATION_REVENUE, response.data.message)
        Assertions.assertEquals(HttpStatus.BAD_REQUEST.name, response.data.error)
    }

    @Test
    fun `handleNotFound deve garantir que uma EntityNotFoundException recebida retornara 404`() {

        val response =
            handller.handleNotFound(EntityNotFoundException(ProcessingResult.ENTITY_NOT_FOUND_MESSAGE), request)

        Assertions.assertEquals(HttpStatus.NOT_FOUND.value(), response.data.status)
        Assertions.assertEquals(ProcessingResult.ENTITY_NOT_FOUND_MESSAGE, response.data.message)
        Assertions.assertEquals(HttpStatus.NOT_FOUND.name, response.data.error)
    }

    @Test
    fun `handleException deve garantir que uma Exception recebida retorne 500`() {
        val response = handller.handleException(Exception(ProcessingResult.INTERNAL_SERVER_ERROR_MESSAGE), request)

        Assertions.assertEquals(HttpStatus.INTERNAL_SERVER_ERROR.value(), response.data.status)
        Assertions.assertEquals(ProcessingResult.INTERNAL_SERVER_ERROR_MESSAGE, response.data.message)
        Assertions.assertEquals(HttpStatus.INTERNAL_SERVER_ERROR.name, response.data.error)

    }

    @Test
    fun `handleHttpMessageNotReadableException deve garantir que uma HttpMessageNotReadableException recebida retorne 502`() {
        val response = handller.handleHttpMessageNotReadableException(
            HttpMessageNotReadableException(
                ProcessingResult.JSON_PARSE_ERROR_MESSAGE,
                Mockito.mock(HttpInputMessage::class.java)
            ), request
        )

        Assertions.assertEquals(HttpStatus.BAD_GATEWAY.value(), response.data.status)
        Assertions.assertEquals(ProcessingResult.JSON_PARSE_ERROR_MESSAGE, response.data.message)
        Assertions.assertEquals(HttpStatus.BAD_GATEWAY.name, response.data.error)

    }
}