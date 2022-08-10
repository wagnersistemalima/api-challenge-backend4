package br.com.sistemalima.apiorcamentofamiliar.response

import com.fasterxml.jackson.annotation.JsonProperty

data class Response<T>(

    @JsonProperty("response")
    val data: T
)
