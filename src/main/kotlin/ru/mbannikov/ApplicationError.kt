package ru.mbannikov

/**
 * Описывает ошибку приложения.
 * Может быть использовано в качестве DTO ответа во внешние системы или для обработки ошибок внутри системы.
 *
 * @property code уникальный числовой инеднтификатор ошибки, может быть использован для switch/when операторов
 * @property type уникальный текстовый инеднтификатор ошибки, может быть использован для switch/when операторов
 * @property message сообщение описывающее ошибку
 */
data class ApplicationError(
    val code: Int,
    val type: ErrorType,
    val message: String
)
