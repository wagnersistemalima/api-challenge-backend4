package br.com.sistemalima.apiorcamentofamiliar.util

import br.com.sistemalima.apiorcamentofamiliar.constant.ProcessingResult
import br.com.sistemalima.apiorcamentofamiliar.dto.DespesaPorcategoriaDTO
import br.com.sistemalima.apiorcamentofamiliar.dto.ExpenseResponseDTO
import br.com.sistemalima.apiorcamentofamiliar.dto.RevenueResponseDTO
import br.com.sistemalima.apiorcamentofamiliar.dto.enum.CategoryStatusEnumDTO
import org.slf4j.LoggerFactory

class Calculadora {

    companion object {

        private const val TAG = "class: Calculadora"
        private val logger = LoggerFactory.getLogger(Calculadora::class.java)

        fun calculaDespesaDoMes(listaDespesasDTO: List<ExpenseResponseDTO>): Double {

            logger.info("$TAG, method: calculaDespesaDoMes, ${ProcessingResult.GET_MOVIMENT_REQUEST}")

            var soma = 0.0

            listaDespesasDTO.forEach { it -> soma += it.value }
            return soma
        }

        fun calculaReceitaMes(listaReceitasDTO: List<RevenueResponseDTO>): Double {

            logger.info("$TAG, method: calculaReceitaMes, ${ProcessingResult.GET_MOVIMENT_REQUEST}")

            var soma = 0.0

            listaReceitasDTO.forEach { it -> soma += it.value }
            return soma
        }

        fun calculaSaldoFinalMes(somaReceita: Double, somaDespesa: Double): Double {

            logger.info("$TAG, method: calculaSaldoFinalMes, ${ProcessingResult.GET_MOVIMENT_REQUEST}")

            return somaReceita - somaDespesa

        }

        fun calculaGastoNoMesPorCategoria(listaDespesasDTO: List<ExpenseResponseDTO>): DespesaPorcategoriaDTO {

            logger.info("$TAG, method: calculaGastoNoMesPorCategoria, ${ProcessingResult.GET_MOVIMENT_REQUEST}")

            var somaOutras = 0.0
            listaDespesasDTO.filter { it.category == CategoryStatusEnumDTO.outras }.forEach { somaOutras += it.value }

            var somaAlimentacao = 0.0
            listaDespesasDTO.filter { it.category == CategoryStatusEnumDTO.alimentacao }
                .forEach { somaAlimentacao += it.value }

            var somaSaude = 0.0
            listaDespesasDTO.filter { it.category == CategoryStatusEnumDTO.saude }.forEach { somaSaude += it.value }

            var somaMoradia = 0.0
            listaDespesasDTO.filter { it.category == CategoryStatusEnumDTO.moradia }.forEach { somaMoradia += it.value }

            var somaTransporte = 0.0
            listaDespesasDTO.filter { it.category == CategoryStatusEnumDTO.transporte }
                .forEach { somaTransporte += it.value }

            var somaEducacao = 0.0
            listaDespesasDTO.filter { it.category == CategoryStatusEnumDTO.educacao }.forEach { somaEducacao += it.value }
            var somaLazer = 0.0
            listaDespesasDTO.filter { it.category == CategoryStatusEnumDTO.lazer }.forEach { somaLazer += it.value }

            var somaImprevistos = 0.0
            listaDespesasDTO.filter { it.category == CategoryStatusEnumDTO.imprevistos }
                .forEach { somaImprevistos += it.value }


            return DespesaPorcategoriaDTO(
                totalOutras = somaOutras,
                totalAlimentacao = somaAlimentacao,
                totalSaude = somaSaude,
                totalMoradia = somaMoradia,
                totalTransporte = somaTransporte,
                totalEducacao = somaEducacao,
                totalLazer = somaLazer,
                totalImprevisto = somaImprevistos
            )

        }
    }
}