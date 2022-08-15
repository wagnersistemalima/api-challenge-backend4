package br.com.sistemalima.apiorcamentofamiliar.service.controller.revenue

import br.com.sistemalima.apiorcamentofamiliar.constant.ApiRoutes
import br.com.sistemalima.apiorcamentofamiliar.constant.ProcessingResult
import br.com.sistemalima.apiorcamentofamiliar.dto.RevenueRequestDTO
import br.com.sistemalima.apiorcamentofamiliar.dto.RevenueResponseDTO
import br.com.sistemalima.apiorcamentofamiliar.exceptions.BadRequestException
import br.com.sistemalima.apiorcamentofamiliar.exceptions.EntityNotFoundException
import br.com.sistemalima.apiorcamentofamiliar.model.Revenue
import br.com.sistemalima.apiorcamentofamiliar.request.Request
import br.com.sistemalima.apiorcamentofamiliar.response.Response
import br.com.sistemalima.apiorcamentofamiliar.service.RevenueService
import br.com.sistemalima.apiorcamentofamiliar.service.util.ListRevenueFixture
import br.com.sistemalima.apiorcamentofamiliar.service.util.RevenueFixture
import br.com.sistemalima.apiorcamentofamiliar.service.util.RevenueRequestDTOFixture
import br.com.sistemalima.apiorcamentofamiliar.service.util.RevenueResponseDTOFixture
import com.fasterxml.jackson.databind.ObjectMapper
import org.junit.jupiter.api.Test
import org.mockito.Mockito
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.http.MediaType
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultMatchers
import org.springframework.web.util.UriComponentsBuilder
import java.time.LocalDate

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@AutoConfigureMockMvc
class RevenueControlerTest {


    @Autowired
    private lateinit var mockMvc: MockMvc

    @Autowired
    private lateinit var objectMapper: ObjectMapper

    @MockBean
    private lateinit var revenueService: RevenueService


    @Test
    fun `create POST deve retornar o status 201`() {

        val request = RevenueRequestDTOFixture.build()
        val revenueEntity = request.data.toModel()
        val response = RevenueResponseDTOFixture.build()

        Mockito.`when`(revenueService.create(revenueEntity)).thenReturn(response)

        mockMvc.perform(
            MockMvcRequestBuilders.post(ApiRoutes.REVENUE_ROUTER)
                .contentType(MediaType.APPLICATION_JSON_VALUE).content(toJson(request))
        )
            .andExpect(MockMvcResultMatchers.status().isCreated)
            .andExpect(MockMvcResultMatchers.content().json(toJsonResponse(response)))

    }

    @Test
    fun `create POST deve retornar o status 400 quando a mesma descricao da receita,no mesmo mes ja existir`() {

        val request = Request(
            data = RevenueRequestDTO(
                description = "Salario",
                value = 1000.0,
                date = LocalDate.of(2022, 8, 10)
            )
        )

        val revenueEntity = request.data.toModel()

        Mockito.`when`(revenueService.create(revenueEntity)).thenThrow(BadRequestException(ProcessingResult.BAD_REQUEST_MESSAGE_VALIDATION_REVENUE))

        mockMvc.perform(
            MockMvcRequestBuilders.post(ApiRoutes.REVENUE_ROUTER)
                .contentType(MediaType.APPLICATION_JSON_VALUE).content(toJson(request))
        )
            .andExpect(MockMvcResultMatchers.status().isBadRequest)
    }

    @Test
    fun `create POST deve retornar o status 400 quando a descricao estiver vazia`() {

        val request = Request(
            data = RevenueRequestDTO(
                description = " ",
                value = 1000.0,
                date = LocalDate.of(2022, 8, 10)
            )
        )

        mockMvc.perform(
            MockMvcRequestBuilders.post(ApiRoutes.REVENUE_ROUTER)
                .contentType(MediaType.APPLICATION_JSON_VALUE).content(toJson(request))
        )
            .andExpect(MockMvcResultMatchers.status().isBadRequest)
    }

    @Test
    fun `create POST deve retornar o status 400, quando a descricao for nula`() {

        val request = Request(
            data = RevenueRequestDTO(
                description = null,
                value = 1000.0,
                date = LocalDate.of(2022, 8, 10)
            )
        )

        mockMvc.perform(
            MockMvcRequestBuilders.post(ApiRoutes.REVENUE_ROUTER)
                .contentType(MediaType.APPLICATION_JSON_VALUE).content(toJson(request))
        )
            .andExpect(MockMvcResultMatchers.status().isBadRequest)
    }

    @Test
    fun `create POST deve retornar o status 400, quando o valor for negativo`() {

        val request = Request(
            data = RevenueRequestDTO(
                description = "Salario",
                value = -100.0,
                date = LocalDate.of(2022, 8, 10)
            )
        )

        mockMvc.perform(
            MockMvcRequestBuilders.post(ApiRoutes.REVENUE_ROUTER)
                .contentType(MediaType.APPLICATION_JSON_VALUE).content(toJson(request))
        )
            .andExpect(MockMvcResultMatchers.status().isBadRequest)
    }

    @Test
    fun `create POST deve retornar o status 400, quando o valor for nulo`() {

        val request = Request(
            data = RevenueRequestDTO(
                description = "Salario",
                value = null,
                date = LocalDate.of(2022, 8, 10)
            )
        )

        mockMvc.perform(
            MockMvcRequestBuilders.post(ApiRoutes.REVENUE_ROUTER)
                .contentType(MediaType.APPLICATION_JSON_VALUE).content(toJson(request))
        )
            .andExpect(MockMvcResultMatchers.status().isBadRequest)
    }

    @Test
    fun `create POST deve retornar o status 400, quando a data for nula`() {

        val request = Request(
            data = RevenueRequestDTO(
                description = "Salario",
                value = 1000.0,
                date = null
            )
        )

        mockMvc.perform(
            MockMvcRequestBuilders.post(ApiRoutes.REVENUE_ROUTER)
                .contentType(MediaType.APPLICATION_JSON_VALUE).content(toJson(request))
        )
            .andExpect(MockMvcResultMatchers.status().isBadRequest)
    }

    @Test
    fun `findAll GET deve retornar 200 com uma lista de receitas dto`() {
        val description: String? = null
        val list = ListRevenueFixture.build()
        val dto = list.map { revenue -> RevenueResponseDTO(revenue) }
        val response = Response(data = dto)

        Mockito.`when`(revenueService.findAll(description)).thenReturn(response)

        mockMvc.perform(
            MockMvcRequestBuilders.get(ApiRoutes.REVENUE_ROUTER)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
        )
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andExpect(MockMvcResultMatchers.content().json(toJsonListResponse(response)))

    }

    @Test
    fun `findAll GET deve retornar 200 com uma lista dto vazia quando nao houver receitas registradas`() {

        val description: String? = null
        val list = listOf<Revenue>()
        val dto = list.map { revenue -> RevenueResponseDTO(revenue) }
        val response = Response(data = dto)

        Mockito.`when`(revenueService.findAll(description)).thenReturn(response)

        mockMvc.perform(
            MockMvcRequestBuilders.get(ApiRoutes.REVENUE_ROUTER)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
        )
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andExpect(MockMvcResultMatchers.content().json(toJsonListResponse(response)))
    }

    @Test
    fun `findAll GET deve retornar 200 com um dto receita quando passar a descricao`() {

        val description = "descrição receita test"
        val list = listOf<Revenue>(RevenueFixture.build())
        val dto = list.map { revenue -> RevenueResponseDTO(revenue) }
        val response = Response(data = dto)

        Mockito.`when`(revenueService.findAll(description)).thenReturn(response)

        mockMvc.perform(
            MockMvcRequestBuilders.get(ApiRoutes.REVENUE_ROUTER + "?description=descrição receita test")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
        )
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andExpect(MockMvcResultMatchers.content().json(toJsonListResponse(response)))
    }

    @Test
    fun `findById GET deve retornar 200 ao passar um id existente`() {

        val response = RevenueResponseDTOFixture.build()
        val uri = UriComponentsBuilder.fromUriString(ApiRoutes.REVENUE_ROUTER + ApiRoutes.PATH_ID)
            .buildAndExpand(response.data.id).toUri()


        Mockito.`when`(revenueService.findById(response.data.id!!)).thenReturn(response)

        mockMvc.perform(
            MockMvcRequestBuilders.get(uri)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
        )
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andExpect(MockMvcResultMatchers.content().json(toJsonResponse(response)))
    }

    @Test
    fun `findById GET deve retornar 404 ao passar um id que nao existe`() {

        val idNotexist = 5000L
        val uri =
            UriComponentsBuilder.fromUriString(ApiRoutes.REVENUE_ROUTER + ApiRoutes.PATH_ID).buildAndExpand(idNotexist)
                .toUri()

        Mockito.`when`(revenueService.findById(idNotexist)).thenThrow(EntityNotFoundException(ProcessingResult.ENTITY_NOT_FOUND_MESSAGE))

        mockMvc.perform(
            MockMvcRequestBuilders.get(uri)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
        )
            .andExpect(MockMvcResultMatchers.status().isNotFound)
    }

    @Test
    fun `delete DELETE deve retornar 204 ao receber um ID de receita ja cadastrado`() {

        val revenueDb = RevenueFixture.build()
        val idExist = revenueDb.id
        val uri =
            UriComponentsBuilder.fromUriString(ApiRoutes.REVENUE_ROUTER + ApiRoutes.PATH_ID).buildAndExpand(idExist)
                .toUri()

        Mockito.doNothing().`when`(revenueService).delete(idExist!!)

        mockMvc.perform(
            MockMvcRequestBuilders.delete(uri)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
        )
            .andExpect(MockMvcResultMatchers.status().isNoContent)

    }

    @Test
    fun `delete DELETE deve retornar 404 ao receber um ID de receita nao registrado`() {

        val idNotExist = 5000L
        val uri =
            UriComponentsBuilder.fromUriString(ApiRoutes.REVENUE_ROUTER + ApiRoutes.PATH_ID).buildAndExpand(idNotExist)
                .toUri()

        Mockito.`when`(revenueService.delete(idNotExist)).thenThrow(EntityNotFoundException(ProcessingResult.ENTITY_NOT_FOUND_MESSAGE))

        mockMvc.perform(
            MockMvcRequestBuilders.delete(uri)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
        )
            .andExpect(MockMvcResultMatchers.status().isNotFound)

    }

    @Test
    fun `update PUT deve retornar 200 quando receber um id de receita cadastrada para atualizar`() {
        val request = RevenueRequestDTOFixture.build()
        val idExist = 1L
        val revenueEntity = request.data.toModel()

        val response = RevenueResponseDTOFixture.build()

        val uri =
            UriComponentsBuilder.fromUriString(ApiRoutes.REVENUE_ROUTER + ApiRoutes.PATH_ID).buildAndExpand(idExist)
                .toUri()

        Mockito.`when`(revenueService.update(revenueEntity, idExist)).thenReturn(response)

        mockMvc.perform(
            MockMvcRequestBuilders.put(uri)
                .contentType(MediaType.APPLICATION_JSON_VALUE).content(toJson(request))
        )
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andExpect(MockMvcResultMatchers.content().json(toJsonResponse(response)))
    }

    @Test
    fun `update PUT deve retornar 404 quando receber um id de receita que nao existe para atualizar`() {
        val request = RevenueRequestDTOFixture.build()
        val idNotExist = 5000L
        val revenueEntity = request.data.toModel()


        val uri =
            UriComponentsBuilder.fromUriString(ApiRoutes.REVENUE_ROUTER + ApiRoutes.PATH_ID).buildAndExpand(idNotExist)
                .toUri()

        Mockito.`when`(revenueService.update(revenueEntity, idNotExist))
            .thenThrow(EntityNotFoundException(ProcessingResult.ENTITY_NOT_FOUND_MESSAGE))

        mockMvc.perform(
            MockMvcRequestBuilders.put(uri)
                .contentType(MediaType.APPLICATION_JSON_VALUE).content(toJson(request))
        )
            .andExpect(MockMvcResultMatchers.status().isNotFound)

    }

    @Test
    fun `update PUT deve retornar o status 400, quando a mesma descricao receita, dentro do mesmo mes ja existe para ser atualizada`() {
        val request = RevenueRequestDTOFixture.build()
        val idNotExist = 5000L
        val revenueEntity = request.data.toModel()

        val uri =
            UriComponentsBuilder.fromUriString(ApiRoutes.REVENUE_ROUTER + ApiRoutes.PATH_ID).buildAndExpand(idNotExist)
                .toUri()

        Mockito.`when`(revenueService.update(revenueEntity, idNotExist))
            .thenThrow(EntityNotFoundException(ProcessingResult.BAD_REQUEST_MESSAGE_VALIDATION_REVENUE))

        mockMvc.perform(
            MockMvcRequestBuilders.put(uri)
                .contentType(MediaType.APPLICATION_JSON_VALUE).content(toJson(request))
        )
            .andExpect(MockMvcResultMatchers.status().isNotFound)

    }

    private fun toJson(request: Request<RevenueRequestDTO>): String {
        return objectMapper.writeValueAsString(request)
    }

    private fun toJsonResponse(response: Response<RevenueResponseDTO>): String {
        return objectMapper.writeValueAsString(response)
    }

    private fun toJsonListResponse(response: Response<List<RevenueResponseDTO>>): String {
        return objectMapper.writeValueAsString(response)
    }
}