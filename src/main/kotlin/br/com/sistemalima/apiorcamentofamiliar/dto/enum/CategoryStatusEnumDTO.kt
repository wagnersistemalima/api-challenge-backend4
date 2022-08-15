package br.com.sistemalima.apiorcamentofamiliar.dto.enum

enum class CategoryStatusEnumDTO {

    ALIMENTACAO {
        override fun deveValidarStatusEnum(): String {
            return CategoryStatusEnumDTO.alimentacao
        }
    },
    SAUDE {
        override fun deveValidarStatusEnum(): String {
            return CategoryStatusEnumDTO.saude
        }
    },
    MORADIA {
        override fun deveValidarStatusEnum(): String {
            return CategoryStatusEnumDTO.moradia
        }
    },
    TRANSPORTE {
        override fun deveValidarStatusEnum(): String {
            return CategoryStatusEnumDTO.transporte
        }
    },
    EDUCACAO {
        override fun deveValidarStatusEnum(): String {
            return CategoryStatusEnumDTO.educacao
        }
    },
    LAZER {
        override fun deveValidarStatusEnum(): String {
            return CategoryStatusEnumDTO.lazer
        }
    },
    IMPREVISTOS {
        override fun deveValidarStatusEnum(): String {
            return CategoryStatusEnumDTO.imprevistos
        }
    },
    OUTRAS {
        override fun deveValidarStatusEnum(): String {
            return CategoryStatusEnumDTO.outras
        }
    };

    abstract fun deveValidarStatusEnum(): String

    companion object {
        val alimentacao = "alimentação"
        val saude = "saúde"
        val moradia = "moradia"
        val transporte = "transporte"
        val educacao = "educação"
        val lazer = "lazer"
        val imprevistos = "imprevistos"
        val outras = "outras"
    }

}