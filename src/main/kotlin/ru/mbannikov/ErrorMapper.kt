package ru.mbannikov

/**
 * Преобразователь сообщений из сторонних систем в ApplicationError.
 */
interface ErrorMapper {
    /**
     * Пытается преобразовать [message] из сторонней системы в ApplicationError.
     *
     * @return ApplicationError в случае успеха, иначе null
     */
    fun map(message: String): ApplicationError?
}


/**
 * Простой преобразователь, для сообщений без изменяемых частей.
 *
 * @property thirdPartyMessage сообщение из сторонней системы
 * @property errorType тип ошибки соответсвующий [thirdPartyMessage]
 */
class SimpleErrorMapper(
    private val thirdPartyMessage: String,
    private val errorType: ErrorType
) : ErrorMapper {
    override fun map(message: String): ApplicationError? {
        if (message != thirdPartyMessage)
            return null

        return ApplicationError(
            code = errorType.code,
            type = errorType,
            message = errorType.message
        )
    }
}


/**
 * Преобразователь для сообщений c изменяемыми частями на основе регулярных выражений.
 * Позволяет вычленять изменяемые части и использовать их в сообщении ApplicationError.
 *
 * @property regex регулярное выражение для сообщения из сторонней системы
 * @property errorType тип ошибки соответсвующий [regex]
 * @property handle функция осуществляющая преобразования списка изменяемых частей
 */
class RegexErrorMapper(
    private val regex: Regex,
    private val errorType: ErrorType,
    private val handle: ((list: Collection<String>) -> Collection<String>)? = null
) : ErrorMapper {
    override fun map(message: String): ApplicationError? =
        regex.find(message)?.let { findResult ->
            val size = findResult.groupValues.size
            val values = findResult.groupValues.subList(1, size).let {
                handle?.invoke(it) ?: it
            }.toTypedArray()

            ApplicationError(
                code = errorType.code,
                type = errorType,
                message = errorType.message.format(*values)
            )
        }
}
