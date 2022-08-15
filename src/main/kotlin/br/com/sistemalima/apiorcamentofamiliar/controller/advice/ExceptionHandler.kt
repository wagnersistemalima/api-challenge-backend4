package br.com.sistemalima.apiorcamentofamiliar.controller.advice

import br.com.sistemalima.apiorcamentofamiliar.constant.ProcessingResult
import br.com.sistemalima.apiorcamentofamiliar.dto.ErrorView
import br.com.sistemalima.apiorcamentofamiliar.exceptions.BadRequestException
import br.com.sistemalima.apiorcamentofamiliar.exceptions.EntityNotFoundException
import br.com.sistemalima.apiorcamentofamiliar.response.Response
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.http.converter.HttpMessageNotReadableException
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestControllerAdvice
import javax.servlet.http.HttpServletRequest


@RestControllerAdvice
class ExceptionHandler {

    companion object {
        private const val TAG = "class: ExceptionHandler"
        private val logger = LoggerFactory.getLogger(this::class.java)
    }

    @ExceptionHandler(BadRequestException::class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    fun handleBadRequest(exception: BadRequestException, request: HttpServletRequest): Response<ErrorView> {

        logger.info(
            "ERROR: $TAG, method: handleBadRequest, " +
                    "status: ${HttpStatus.BAD_REQUEST.value()}, " +
                    "error: ${HttpStatus.BAD_REQUEST.name} " +
                    "message: ${exception.message}"
        )

        return Response(
            data = ErrorView(
                status = HttpStatus.BAD_REQUEST.value(),
                error = HttpStatus.BAD_REQUEST.name,
                message = exception.message ?: ProcessingResult.BAD_REQUEST_EXCEPTION_MESSAGE,
                path = request.servletPath
            )
        )
    }

    @ExceptionHandler(MethodArgumentNotValidException::class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    fun handleValidationError(
        exception: MethodArgumentNotValidException,
        request: HttpServletRequest
    ): Response<ErrorView> {

        logger.info(
            "ERROR: $TAG, method: handleValidationError, " +
                    "status: ${HttpStatus.BAD_REQUEST.value()}, " +
                    "error: ${HttpStatus.BAD_REQUEST.name} " +
                    "message: ${exception.message}"
        )

        val errorMessage = HashMap<String, String?>()
        exception.bindingResult.fieldErrors.forEach { error -> errorMessage[error.field] = error.defaultMessage }
        return Response(
            data = ErrorView(
                status = HttpStatus.BAD_REQUEST.value(),
                error = HttpStatus.BAD_REQUEST.name,
                message = errorMessage.toString(),
                path = request.servletPath
            )
        )
    }

    @ExceptionHandler(EntityNotFoundException::class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    fun handleNotFound(exception: EntityNotFoundException, request: HttpServletRequest): Response<ErrorView> {

        logger.info(
            "ERROR: $TAG, method: handleNotFound, " +
                    "status: ${HttpStatus.NOT_FOUND.value()}, " +
                    "error: ${HttpStatus.NOT_FOUND.name} " +
                    "message: ${exception.message}"
        )

        return Response(
            data = ErrorView(
                status = HttpStatus.NOT_FOUND.value(),
                error = HttpStatus.NOT_FOUND.name,
                message = exception.message ?: ProcessingResult.ENTITY_NOT_FOUND_MESSAGE,
                path = request.servletPath
            )
        )
    }

    @ExceptionHandler(HttpMessageNotReadableException::class)
    @ResponseStatus(HttpStatus.BAD_GATEWAY)
    fun handleHttpMessageNotReadableException(
        exception: HttpMessageNotReadableException,
        request: HttpServletRequest
    ): Response<ErrorView> {

        logger.info(
            "ERROR: $TAG, method: handleHttpMessageNotReadableException, " +
                    "status: ${HttpStatus.BAD_GATEWAY.value()}, " +
                    "error: ${HttpStatus.BAD_GATEWAY.name} " +
                    "message: ${exception.message}"
        )

        return Response(
            data = ErrorView(
                status = HttpStatus.BAD_GATEWAY.value(),
                error = HttpStatus.BAD_GATEWAY.name,
                message = ProcessingResult.JSON_PARSE_ERROR_MESSAGE,
                path = request.servletPath
            )
        )
    }

    @ExceptionHandler(Exception::class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    fun handleException(exception: Exception, request: HttpServletRequest): Response<ErrorView> {

        logger.info(
            "ERROR: $TAG, method: handleException, " +
                    "status: ${HttpStatus.INTERNAL_SERVER_ERROR.value()}, " +
                    "error: ${HttpStatus.INTERNAL_SERVER_ERROR.name} " +
                    "message: ${exception.message}"
        )

        return Response(
            data = ErrorView(
                status = HttpStatus.INTERNAL_SERVER_ERROR.value(),
                error = HttpStatus.INTERNAL_SERVER_ERROR.name,
                message = ProcessingResult.INTERNAL_SERVER_ERROR_MESSAGE,
                path = request.servletPath
            )
        )
    }

}