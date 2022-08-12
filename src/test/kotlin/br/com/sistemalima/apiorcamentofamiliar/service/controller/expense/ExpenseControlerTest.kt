package br.com.sistemalima.apiorcamentofamiliar.service.controller.expense

import br.com.sistemalima.apiorcamentofamiliar.constant.ApiRoutes
import br.com.sistemalima.apiorcamentofamiliar.constant.ProcessingResult
import br.com.sistemalima.apiorcamentofamiliar.dto.ExpenseRequestDTO
import br.com.sistemalima.apiorcamentofamiliar.dto.ExpenseResponseDTO
import br.com.sistemalima.apiorcamentofamiliar.exceptions.BadRequestException
import br.com.sistemalima.apiorcamentofamiliar.exceptions.EntityNotFoundException
import br.com.sistemalima.apiorcamentofamiliar.model.Expense
import br.com.sistemalima.apiorcamentofamiliar.request.Request
import br.com.sistemalima.apiorcamentofamiliar.response.Response
import br.com.sistemalima.apiorcamentofamiliar.service.ExpenseService
import br.com.sistemalima.apiorcamentofamiliar.service.util.ExpenseFixture
import br.com.sistemalima.apiorcamentofamiliar.service.util.ExpenseRequestDTOFixture
import br.com.sistemalima.apiorcamentofamiliar.service.util.ExpenseResponseDTOFixture
import br.com.sistemalima.apiorcamentofamiliar.service.util.ListExpenseFixture
import com.fasterxml.jackson.databind.ObjectMapper
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.Mockito
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.http.MediaType
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.junit.jupiter.SpringExtension
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultMatchers
import org.springframework.web.util.UriComponentsBuilder
import java.time.LocalDate
import kotlin.random.Random

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@AutoConfigureMockMvc
class ExpenseControlerTest {

    @Autowired
    private lateinit var mockMvc: MockMvc

    @Autowired
    private lateinit var objectMapper: ObjectMapper

    @MockBean
    private lateinit var expenseService: ExpenseService

    @Test
    fun `create POST deve retornar o status 201`() {

        val request = ExpenseRequestDTOFixture.build()
        val expenseEntity = request.data.toModel()
        val response = ExpenseResponseDTOFixture.build()

        Mockito.`when`(expenseService.create(expenseEntity)).thenReturn(response)

        mockMvc.perform(
            MockMvcRequestBuilders.post(ApiRoutes.EXPENSE_ROUTER)
                .contentType(MediaType.APPLICATION_JSON_VALUE).content(toJson(request))
        )
            .andExpect(MockMvcResultMatchers.status().isCreated)
            .andExpect(MockMvcResultMatchers.content().json(toJsonResponse(response)))

    }

    @Test
    fun `create POST deve retornar o status 400 quando a mesma descricao da despesa,no mesmo mes ja existir`() {

        val request = Request(
            data = ExpenseRequestDTO(
                description = "feira do mes",
                value = 1000.0,
                date = LocalDate.of(2022, 8, 10)
            )
        )

        val expenseEntity = request.data.toModel()

        Mockito.`when`(expenseService.create(expenseEntity)).thenThrow(BadRequestException(ProcessingResult.BAD_REQUEST_MESSAGE_VALIDATION_EXPENSE))

        mockMvc.perform(
            MockMvcRequestBuilders.post(ApiRoutes.EXPENSE_ROUTER)
                .contentType(MediaType.APPLICATION_JSON_VALUE).content(toJson(request))
        )
            .andExpect(MockMvcResultMatchers.status().isBadRequest)
    }

    @Test
    fun `create POST deve retornar o status 400 quando a descricao estiver vazia`() {

        val request = Request(
            data = ExpenseRequestDTO(
                description = " ",
                value = 1000.0,
                date = LocalDate.of(2022, 8, 10)
            )
        )

        mockMvc.perform(
            MockMvcRequestBuilders.post(ApiRoutes.EXPENSE_ROUTER)
                .contentType(MediaType.APPLICATION_JSON_VALUE).content(toJson(request))
        )
            .andExpect(MockMvcResultMatchers.status().isBadRequest)
    }

    @Test
    fun `create POST deve retornar o status 400, quando a descricao for nula`() {

        val request = Request(
            data = ExpenseRequestDTO(
                description = null,
                value = 1000.0,
                date = LocalDate.of(2022, 8, 10)
            )
        )

        mockMvc.perform(
            MockMvcRequestBuilders.post(ApiRoutes.EXPENSE_ROUTER)
                .contentType(MediaType.APPLICATION_JSON_VALUE).content(toJson(request))
        )
            .andExpect(MockMvcResultMatchers.status().isBadRequest)
    }

    @Test
    fun `create POST deve retornar o status 400, quando o valor for negativo`() {

        val request = Request(
            data = ExpenseRequestDTO(
                description = "Salario",
                value = -100.0,
                date = LocalDate.of(2022, 8, 10)
            )
        )

        mockMvc.perform(
            MockMvcRequestBuilders.post(ApiRoutes.EXPENSE_ROUTER)
                .contentType(MediaType.APPLICATION_JSON_VALUE).content(toJson(request))
        )
            .andExpect(MockMvcResultMatchers.status().isBadRequest)
    }

    @Test
    fun `create POST deve retornar o status 400, quando o valor for nulo`() {

        val request = Request(
            data = ExpenseRequestDTO(
                description = "Salario",
                value = null,
                date = LocalDate.of(2022, 8, 10)
            )
        )

        mockMvc.perform(
            MockMvcRequestBuilders.post(ApiRoutes.EXPENSE_ROUTER)
                .contentType(MediaType.APPLICATION_JSON_VALUE).content(toJson(request))
        )
            .andExpect(MockMvcResultMatchers.status().isBadRequest)
    }

    @Test
    fun `create POST deve retornar o status 400, quando a data for nula`() {

        val request = Request(
            data = ExpenseRequestDTO(
                description = "Salario",
                value = 1000.0,
                date = null
            )
        )

        mockMvc.perform(
            MockMvcRequestBuilders.post(ApiRoutes.EXPENSE_ROUTER)
                .contentType(MediaType.APPLICATION_JSON_VALUE).content(toJson(request))
        )
            .andExpect(MockMvcResultMatchers.status().isBadRequest)
    }

    @Test
    fun `findAll GET deve retornar 200 com uma lista de despesas dto`() {

        val list = ListExpenseFixture.build()
        val dto = list.map { expense -> ExpenseResponseDTO(expense) }
        val response = Response(data = dto)

        Mockito.`when`(expenseService.findAll()).thenReturn(response)

        mockMvc.perform(
            MockMvcRequestBuilders.get(ApiRoutes.EXPENSE_ROUTER)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
        )
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andExpect(MockMvcResultMatchers.content().json(toJsonListResponse(response)))

    }

    @Test
    fun `findAll GET deve retornar 200 com uma lista dto vazia quando nao houver despesas registradas`() {

        val list = listOf<Expense>()
        val dto = list.map { expense -> ExpenseResponseDTO(expense) }
        val response = Response(data = dto)

        Mockito.`when`(expenseService.findAll()).thenReturn(response)

        mockMvc.perform(
            MockMvcRequestBuilders.get(ApiRoutes.EXPENSE_ROUTER)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
        )
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andExpect(MockMvcResultMatchers.content().json(toJsonListResponse(response)))
    }

    @Test
    fun `findById GET deve retornar 200 ao passar um id existente`() {

        val response = ExpenseResponseDTOFixture.build()
        val uri = UriComponentsBuilder.fromUriString(ApiRoutes.EXPENSE_ROUTER + ApiRoutes.PATH_ID)
            .buildAndExpand(response.data.id).toUri()


        Mockito.`when`(expenseService.findById(response.data.id!!)).thenReturn(response)

        mockMvc.perform(
            MockMvcRequestBuilders.get(uri)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
        )
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andExpect(MockMvcResultMatchers.content().json(toJsonResponse(response)))
    }

    @Test
    fun `findById GET deve retornar 404 ao passar um id que nao existe`() {

        val idNotexist = Random.nextInt().toLong()
        val uri =
            UriComponentsBuilder.fromUriString(ApiRoutes.EXPENSE_ROUTER + ApiRoutes.PATH_ID).buildAndExpand(idNotexist)
                .toUri()

        Mockito.`when`(expenseService.findById(idNotexist)).thenThrow(EntityNotFoundException(ProcessingResult.ENTITY_NOT_FOUND_MESSAGE))

        mockMvc.perform(
            MockMvcRequestBuilders.get(uri)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
        )
            .andExpect(MockMvcResultMatchers.status().isNotFound)
    }

    @Test
    fun `delete DELETE deve retornar 204 ao receber um ID de despesa ja cadastrado`() {

        val expenseDb = ExpenseFixture.build()
        val idExist = expenseDb.id
        val uri =
            UriComponentsBuilder.fromUriString(ApiRoutes.EXPENSE_ROUTER + ApiRoutes.PATH_ID).buildAndExpand(idExist)
                .toUri()

        Mockito.doNothing().`when`(expenseService).delete(idExist!!)

        mockMvc.perform(
            MockMvcRequestBuilders.delete(uri)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
        )
            .andExpect(MockMvcResultMatchers.status().isNoContent)

    }

    @Test
    fun `delete DELETE deve retornar 404 ao receber um ID de despesa nao registrado`() {

        val idNotExist = 5000L
        val uri =
            UriComponentsBuilder.fromUriString(ApiRoutes.EXPENSE_ROUTER + ApiRoutes.PATH_ID).buildAndExpand(idNotExist)
                .toUri()

        Mockito.`when`(expenseService.delete(idNotExist)).thenThrow(EntityNotFoundException(ProcessingResult.ENTITY_NOT_FOUND_MESSAGE))

        mockMvc.perform(
            MockMvcRequestBuilders.delete(uri)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
        )
            .andExpect(MockMvcResultMatchers.status().isNotFound)

    }

    @Test
    fun `update PUT deve retornar 200 quando receber um id de despesa cadastrada para atualizar`() {
        val request = ExpenseRequestDTOFixture.build()
        val idExist = 1L
        val expenseEntity = request.data.toModel()

        val response = ExpenseResponseDTOFixture.build()

        val uri =
            UriComponentsBuilder.fromUriString(ApiRoutes.EXPENSE_ROUTER + ApiRoutes.PATH_ID).buildAndExpand(idExist)
                .toUri()

        Mockito.`when`(expenseService.update(expenseEntity, idExist)).thenReturn(response)

        mockMvc.perform(
            MockMvcRequestBuilders.put(uri)
                .contentType(MediaType.APPLICATION_JSON_VALUE).content(toJson(request))
        )
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andExpect(MockMvcResultMatchers.content().json(toJsonResponse(response)))
    }

    @Test
    fun `update PUT deve retornar 404 quando receber um id de despesa que nao existe para atualizar`() {
        val request = ExpenseRequestDTOFixture.build()
        val idNotExist = 5000L
        val expenseEntity = request.data.toModel()


        val uri =
            UriComponentsBuilder.fromUriString(ApiRoutes.EXPENSE_ROUTER + ApiRoutes.PATH_ID).buildAndExpand(idNotExist)
                .toUri()

        Mockito.`when`(expenseService.update(expenseEntity, idNotExist))
            .thenThrow(EntityNotFoundException(ProcessingResult.ENTITY_NOT_FOUND_MESSAGE))

        mockMvc.perform(
            MockMvcRequestBuilders.put(uri)
                .contentType(MediaType.APPLICATION_JSON_VALUE).content(toJson(request))
        )
            .andExpect(MockMvcResultMatchers.status().isNotFound)

    }

    @Test
    fun `update PUT deve retornar o status 400, quando a mesma descricao despesa, dentro do mesmo mes ja existe para ser atualizada`() {
        val request = ExpenseRequestDTOFixture.build()
        val idNotExist = 5000L
        val expenseEntity = request.data.toModel()

        val uri =
            UriComponentsBuilder.fromUriString(ApiRoutes.EXPENSE_ROUTER + ApiRoutes.PATH_ID).buildAndExpand(idNotExist)
                .toUri()

        Mockito.`when`(expenseService.update(expenseEntity, idNotExist))
            .thenThrow(EntityNotFoundException(ProcessingResult.BAD_REQUEST_MESSAGE_VALIDATION_EXPENSE))

        mockMvc.perform(
            MockMvcRequestBuilders.put(uri)
                .contentType(MediaType.APPLICATION_JSON_VALUE).content(toJson(request))
        )
            .andExpect(MockMvcResultMatchers.status().isNotFound)

    }

    private fun toJson(request: Request<ExpenseRequestDTO>): String {
        return objectMapper.writeValueAsString(request)
    }

    private fun toJsonResponse(response: Response<ExpenseResponseDTO>): String {
        return objectMapper.writeValueAsString(response)
    }

    private fun toJsonListResponse(response: Response<List<ExpenseResponseDTO>>): String {
        return objectMapper.writeValueAsString(response)
    }
}