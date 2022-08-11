package br.com.sistemalima.apiorcamentofamiliar.constant

class ProcessingResult {

    companion object {
        const val START_PROCCESS = "Inicio do processo request"
        const val GET_MOVIMENT_REQUEST = "Movimentação do processo request"
        const val END_PROCESS = "Fim do processo request"
        const val BAD_REQUEST_MESSAGE_VALIDATION_REVENUE = "cadastro de receitas duplicadas contendo a mesma descrição, dentro do mesmo mês"
        const val ENTITY_NOT_FOUND_MESSAGE = "recurso não encontrado"
    }
}