package br.com.sistemalima.apiorcamentofamiliar.request

import com.fasterxml.jackson.annotation.JsonProperty
import javax.validation.Valid
import javax.validation.constraints.NotNull

data class Request<T>(

    @JsonProperty("request")
    @field:NotNull
    @field:Valid
    val data: T
)