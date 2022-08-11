package br.com.sistemalima.apiorcamentofamiliar.service.controller

import br.com.sistemalima.apiorcamentofamiliar.constant.ApiRoutes
import br.com.sistemalima.apiorcamentofamiliar.dto.RevenueRequestDTO
import br.com.sistemalima.apiorcamentofamiliar.dto.RevenueResponseDTO
import br.com.sistemalima.apiorcamentofamiliar.exceptions.EntityNotFoundException
import br.com.sistemalima.apiorcamentofamiliar.model.Revenue
import br.com.sistemalima.apiorcamentofamiliar.request.Request
import br.com.sistemalima.apiorcamentofamiliar.response.Response
import br.com.sistemalima.apiorcamentofamiliar.service.RevenueService
import br.com.sistemalima.apiorcamentofamiliar.service.util.ListRevenueFixture
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
    fun `create POST should return status 201`() {

        val request = RevenueRequestDTOFixture.build()
        val revenueEntity = request.data.toModel()
        val response = RevenueResponseDTOFixture.build()

        Mockito.`when`(revenueService.create(revenueEntity)).thenReturn(response)

        mockMvc.perform(MockMvcRequestBuilders.post(ApiRoutes.REVENUE_ROUTER)
            .contentType(MediaType.APPLICATION_JSON_VALUE).content(toJson(request)))
            .andExpect(MockMvcResultMatchers.status().isCreated)
            .andExpect(MockMvcResultMatchers.content().json(toJsonResponse(response)))

    }

    @Test
    fun `create POST should return status 400, when description is not empty`() {

        val request = Request(
            data = RevenueRequestDTO(
                description = " ",
                value = 1000.0,
                date = LocalDate.of(2022, 8, 10)
            )
        )

        mockMvc.perform(MockMvcRequestBuilders.post(ApiRoutes.REVENUE_ROUTER)
            .contentType(MediaType.APPLICATION_JSON_VALUE).content(toJson(request)))
            .andExpect(MockMvcResultMatchers.status().isBadRequest)
    }

    @Test
    fun `create POST should return status 400, when the description is null`() {

        val request = Request(
            data = RevenueRequestDTO(
                description = null,
                value = 1000.0,
                date = LocalDate.of(2022, 8, 10)
            )
        )

        mockMvc.perform(MockMvcRequestBuilders.post(ApiRoutes.REVENUE_ROUTER)
            .contentType(MediaType.APPLICATION_JSON_VALUE).content(toJson(request)))
            .andExpect(MockMvcResultMatchers.status().isBadRequest)
    }

    @Test
    fun `create POST should return status 400, when the value is negative`() {

        val request = Request(
            data = RevenueRequestDTO(
                description = "Salario",
                value = -100.0,
                date = LocalDate.of(2022, 8, 10)
            )
        )

        mockMvc.perform(MockMvcRequestBuilders.post(ApiRoutes.REVENUE_ROUTER)
            .contentType(MediaType.APPLICATION_JSON_VALUE).content(toJson(request)))
            .andExpect(MockMvcResultMatchers.status().isBadRequest)
    }

    @Test
    fun `create POST should return status 400, when value is null`() {

        val request = Request(
            data = RevenueRequestDTO(
                description = "Salario",
                value = null,
                date = LocalDate.of(2022, 8, 10)
            )
        )

        mockMvc.perform(MockMvcRequestBuilders.post(ApiRoutes.REVENUE_ROUTER)
            .contentType(MediaType.APPLICATION_JSON_VALUE).content(toJson(request)))
            .andExpect(MockMvcResultMatchers.status().isBadRequest)
    }

    @Test
    fun `create POST should return status 400, when date is null`() {

        val request = Request(
            data = RevenueRequestDTO(
                description = "Salario",
                value = 1000.0,
                date = null
            )
        )

        mockMvc.perform(MockMvcRequestBuilders.post(ApiRoutes.REVENUE_ROUTER)
            .contentType(MediaType.APPLICATION_JSON_VALUE).content(toJson(request)))
            .andExpect(MockMvcResultMatchers.status().isBadRequest)
    }

    @Test
    fun `findAll GET should return 200 with a list of dto recipes`() {

        val list = ListRevenueFixture.build()
        val dto = list.map { revenue -> RevenueResponseDTO(revenue) }
        val response = Response(data = dto)

        Mockito.`when`(revenueService.findAll()).thenReturn(response)

        mockMvc.perform(MockMvcRequestBuilders.get(ApiRoutes.REVENUE_ROUTER)
            .contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andExpect(MockMvcResultMatchers.content().json(toJsonListResponse(response)))

    }

    @Test
    fun `findAll GET hould return 200 with an empty dto list when there are no registered recipes`() {

        val list = listOf<Revenue>()
        val dto = list.map { revenue -> RevenueResponseDTO(revenue) }
        val response = Response(data = dto)

        Mockito.`when`(revenueService.findAll()).thenReturn(response)

        mockMvc.perform(MockMvcRequestBuilders.get(ApiRoutes.REVENUE_ROUTER)
            .contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andExpect(MockMvcResultMatchers.content().json(toJsonListResponse(response)))
    }

    @Test
    fun `findById GET should return 200 when passing an existing id`() {

        val response = RevenueResponseDTOFixture.build()
        val uri = UriComponentsBuilder.fromUriString(ApiRoutes.REVENUE_ROUTER + ApiRoutes.PATH_ID).buildAndExpand(response.data.id).toUri()


        Mockito.`when`(revenueService.findById(response.data.id!!)).thenReturn(response)

        mockMvc.perform(MockMvcRequestBuilders.get(uri)
            .contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andExpect(MockMvcResultMatchers.content().json(toJsonResponse(response)))
    }

    @Test
    fun `findById GET should return 404 when passing an id that does not exist`() {

        val idNotexist = 5000L
        val uri = UriComponentsBuilder.fromUriString(ApiRoutes.REVENUE_ROUTER + ApiRoutes.PATH_ID).buildAndExpand(idNotexist).toUri()

        Mockito.`when`(revenueService.findById(idNotexist)).thenThrow(EntityNotFoundException("Entity not found"))

        mockMvc.perform(MockMvcRequestBuilders.get(uri)
            .contentType(MediaType.APPLICATION_JSON_VALUE))
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