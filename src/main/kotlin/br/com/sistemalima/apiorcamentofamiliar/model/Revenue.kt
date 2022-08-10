package br.com.sistemalima.apiorcamentofamiliar.model

import java.time.LocalDate
import javax.persistence.*

@Entity
@Table(name = "tb_receita")
data class Revenue(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    val description: String,

    val valor: Double,

    val date: LocalDate

)
