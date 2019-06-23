package ru.mbannikov

/**
 * Описание ошибки приложения.
 * Может быть использовано в качестве DTO ответа во внешние системы или для обработки ошибок внутри системы.
 *
 * @property name уникальный текстовый инеднтификатор ошибки, может быть использован для switch/when операторов
 * @property code уникальный числовой инеднтификатор ошибки, может быть использован для switch/when операторов
 * @property message пользовательское описание ошибки
 */
data class SystemError(
    val name: String,
    val code: Int,
    val message: String
)
