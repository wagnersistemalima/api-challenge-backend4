package br.com.sistemalima.apiorcamentofamiliar.service.enum

import br.com.sistemalima.apiorcamentofamiliar.dto.enum.CategoryStatusEnumDTO
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

class CategoryStatusEnumDTOTest {

    @Test
    fun `deve validar o status do enum a partir da descricao ALIMENTACAO`() {

        val categoryEnum = CategoryStatusEnumDTO.ALIMENTACAO

        val name = categoryEnum.deveValidarStatusEnum()

        Assertions.assertEquals(CategoryStatusEnumDTO.alimentacao, name)
    }

    @Test
    fun `deve validar o status do enum a partir da descricao SAUDE`() {

        val categoryEnum = CategoryStatusEnumDTO.SAUDE

        val name = categoryEnum.deveValidarStatusEnum()

        Assertions.assertEquals(CategoryStatusEnumDTO.saude, name)
    }

    @Test
    fun `deve validar o status do enum a partir da descricao MORADIA`() {

        val categoryEnum = CategoryStatusEnumDTO.MORADIA

        val name = categoryEnum.deveValidarStatusEnum()

        Assertions.assertEquals(CategoryStatusEnumDTO.moradia, name)
    }

    @Test
    fun `deve validar o status do enum a partir da descricao TRANSPORTE`() {

        val categoryEnum = CategoryStatusEnumDTO.TRANSPORTE

        val name = categoryEnum.deveValidarStatusEnum()

        Assertions.assertEquals(CategoryStatusEnumDTO.transporte, name)
    }

    @Test
    fun `deve validar o status do enum a partir da descricao EDUCACAO`() {

        val categoryEnum = CategoryStatusEnumDTO.EDUCACAO

        val name = categoryEnum.deveValidarStatusEnum()

        Assertions.assertEquals(CategoryStatusEnumDTO.educacao, name)
    }

    @Test
    fun `deve validar o status do enum a partir da descricao LAZER`() {

        val categoryEnum = CategoryStatusEnumDTO.LAZER

        val name = categoryEnum.deveValidarStatusEnum()

        Assertions.assertEquals(CategoryStatusEnumDTO.lazer, name)
    }

    @Test
    fun `deve validar o status do enum a partir da descricao IMPREVISTOS`() {

        val categoryEnum = CategoryStatusEnumDTO.IMPREVISTOS

        val name = categoryEnum.deveValidarStatusEnum()

        Assertions.assertEquals(CategoryStatusEnumDTO.imprevistos, name)
    }

    @Test
    fun `deve validar o status do enum a partir da descricao OUTRAS`() {

        val categoryEnum = CategoryStatusEnumDTO.OUTRAS

        val name = categoryEnum.deveValidarStatusEnum()

        Assertions.assertEquals(CategoryStatusEnumDTO.outras, name)
    }
}