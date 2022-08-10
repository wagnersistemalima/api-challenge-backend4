package br.com.sistemalima.apiorcamentofamiliar.controller.advice

import br.com.sistemalima.apiorcamentofamiliar.dto.ErrorView
import br.com.sistemalima.apiorcamentofamiliar.exceptions.BadRequestException
import br.com.sistemalima.apiorcamentofamiliar.exceptions.EntityNotFoundException
import br.com.sistemalima.apiorcamentofamiliar.response.Response
import org.springframework.http.HttpStatus
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import javax.servlet.http.HttpServletRequest

@RestControllerAdvice
class ExceptionHandler {

    @ExceptionHandler(BadRequestException::class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    fun handleBadRequest(exception: BadRequestException, request: HttpServletRequest): Response<ErrorView> {
        return Response(
            data = ErrorView(
                status = HttpStatus.BAD_REQUEST.value(),
                error = HttpStatus.BAD_REQUEST.name,
                message = exception.message ?: "Validation request",
                path = request.servletPath
            )
        )
    }

    @ExceptionHandler(MethodArgumentNotValidException::class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    fun handleValidationError(exception: MethodArgumentNotValidException, request: HttpServletRequest): Response<ErrorView> {
        val errorMessage = HashMap<String, String?>()
        exception.bindingResult.fieldErrors.forEach{ error -> errorMessage[error.field] = error.defaultMessage }
        return Response(
            data = ErrorView(
            status = HttpStatus.BAD_REQUEST.value(),
            error = HttpStatus.BAD_REQUEST.name,
            message = errorMessage.toString(),
            path = request.servletPath
        ))
    }

    @ExceptionHandler(EntityNotFoundException::class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    fun handleNotFound(exception: EntityNotFoundException, request: HttpServletRequest): Response<ErrorView> {
        return Response(
            data = ErrorView(
                status = HttpStatus.NOT_FOUND.value(),
                error = HttpStatus.NOT_FOUND.name,
                message = exception.message ?: "Entity not found",
                path = request.servletPath
            )
        )
    }
}