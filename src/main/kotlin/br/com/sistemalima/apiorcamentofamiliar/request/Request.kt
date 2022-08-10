package br.com.sistemalima.apiorcamentofamiliar.request

import com.fasterxml.jackson.annotation.JsonProperty

data class Request<T>(

    @JsonProperty("request")
    val data: T
)