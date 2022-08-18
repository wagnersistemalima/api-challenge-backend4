package br.com.sistemalima.apiorcamentofamiliar.service.controller.summary

import br.com.sistemalima.apiorcamentofamiliar.constant.ApiRoutes
import br.com.sistemalima.apiorcamentofamiliar.dto.SummaryResponseDTO
import br.com.sistemalima.apiorcamentofamiliar.response.Response
import br.com.sistemalima.apiorcamentofamiliar.service.SummaryService
import br.com.sistemalima.apiorcamentofamiliar.service.util.SummaryResponseDTOFixture
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

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
class SummaryControllerTest {

    @Autowired
    private lateinit var mockMvc: MockMvc

    @MockBean
    private lateinit var summaryService: SummaryService

    @Autowired
    private lateinit var objectMapper: ObjectMapper

    @Test
    fun `deve retornar 200 com um relatorio da receita e despesa por mes e categoria`() {

        val ano = 2022
        val mes = 8
        val response = SummaryResponseDTOFixture.build()

        val uri = UriComponentsBuilder.fromUriString(ApiRoutes.SUMMARY_ROUTER + ApiRoutes.PATH_YEAR_MONTH)
            .buildAndExpand(ano, mes).toUri()

        Mockito.`when`(summaryService.findBySummaryYearMonth(ano, mes)).thenReturn(response)

        mockMvc.perform(MockMvcRequestBuilders.get(uri).contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andExpect(MockMvcResultMatchers.content().json(toJson(response)))
    }

    private fun toJson(response: Response<SummaryResponseDTO>): String {
        return objectMapper.writeValueAsString(response)
    }

}