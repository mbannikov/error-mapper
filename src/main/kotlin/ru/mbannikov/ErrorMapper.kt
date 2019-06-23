package ru.mbannikov

/**
 * Преобразователь сообщений из сторонних систем в SystemError.
 */
interface ErrorMapper {
    /**
     * Пытается преобразовать [message] из сторонней системы в SystemError.
     *
     * @return SystemError в случае успеха, иначе null
     */
    fun map(message: String): SystemError?
}


/**
 * Простой преобразователь, для сообщений без изменяемых частей.
 *
 * @property thirdPartyMessage сообщение из сторонней системы
 * @property error системная ошибка соответсвующая [thirdPartyMessage]
 */
class SimpleErrorMapper(
    private val thirdPartyMessage: String,
    private val error: SystemError
) : ErrorMapper {
    override fun map(message: String): SystemError? {
        if (message != thirdPartyMessage)
            return null

        return SystemError(
            name = error.name,
            code = error.code,
            message = error.message
        )
    }
}


/**
 * Преобразователь для сообщений c изменяемыми частями на основе регулярных выражений.
 * Позволяет вычленять изменяемые части и использовать их в сообщении SystemError.
 *
 * @property regex регулярное выражение для сообщения из сторонней системы
 * @property error системная ошибка соответсвующая [regex]
 * @property handle функция осуществляющая преобразования списка изменяемых частей
 */
class RegexErrorMapper(
    private val regex: Regex,
    private val error: SystemError,
    private val handle: ((list: Collection<String>) -> Collection<String>)? = null
) : ErrorMapper {
    override fun map(message: String): SystemError? =
        regex.find(message)?.let { findResult ->
            val size = findResult.groupValues.size
            val values = findResult.groupValues.subList(1, size).let {
                handle?.invoke(it) ?: it
            }.toTypedArray()

            SystemError(
                name = error.name,
                code = error.code,
                message = error.message.format(*values)
            )
        }
}
