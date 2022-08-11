package br.com.sistemalima.apiorcamentofamiliar.model

import java.time.LocalDate
import javax.persistence.*
import javax.validation.constraints.NotBlank
import javax.validation.constraints.NotNull
import javax.validation.constraints.Size

@Entity
@Table(name = "tb_receita")
data class Revenue(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    @field:NotBlank
    @field:NotNull
    @field:Size(max = 100)
    val description: String,

    @field:NotNull
    val valor: Double,

    @field:NotNull
    val data: LocalDate


)
