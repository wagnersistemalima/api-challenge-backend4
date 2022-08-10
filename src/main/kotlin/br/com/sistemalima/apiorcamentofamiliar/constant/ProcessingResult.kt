package br.com.sistemalima.apiorcamentofamiliar.constant

class ProcessingResult {

    companion object {
        const val START_PROCCESS = "Inicio do processo request"
        const val GET_MOVIMENT_REQUEST = "Movimentação do processo request"
        const val END_PROCESS = "Fim do processo request"
        const val BAD_REQUEST_MESSAGE = "receita contendo descrição neste mes já cadastrada"
    }
}