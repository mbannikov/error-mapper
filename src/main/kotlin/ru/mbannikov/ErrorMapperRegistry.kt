package ru.mbannikov

/**
 * Реестр преобразователей сообщений об ошибках из сторонних систем.
 */
class ErrorMapperRegistry {
    /** Общие преобразователи для всех микросервисов */
    private val commonMappers = sequenceOf(
        RegexErrorMapper("Минимальная сумма перевода (\\d*) рублей".toRegex(), ErrorType.MINIMUM_PAYMENT_SUM),

        SimpleErrorMapper("Сервер временно недоступен", ErrorType.INTERNAL),

        RegexErrorMapper(
            "Невозможно совершить операцию (.*?). Превышен лимит \\((\\d*) руб\\).".toRegex(),
            ErrorType.BLOCKED_OPERATION_BY_LIMIT
        ) {
            it.reversed()
        }
    )

    /** Преобразователи для текущего микросервиса */
    private val customMappers = mutableSetOf<ErrorMapper>()

    /**
     * Добавляет [mapper] в {@link customMappers}
     */
    fun register(mapper: ErrorMapper) {
        customMappers.add(mapper)
    }

    /**
     * Пытается преобразовать [message] через {@link commonMappers} и {@link customMappers}
     *
     * @return ApplicationError с преобразованным сообщением или ApplicationError с [message] в случае отсутствия подходящего преобразователя
     */
    fun map(message: String): ApplicationError =
        mapOrNull(message) ?: ApplicationError(
            code = ErrorType.UNKNOWN.code,
            type = ErrorType.UNKNOWN,
            message = message
        )

    /**
     * Пытается преобразовать [message] через {@link commonMappers} и {@link customMappers}
     *
     * @return ApplicationError с преобразованным сообщением или null в случае отсутствия подходящего преобразователя
     */
    fun mapOrNull(message: String): ApplicationError? =
        (commonMappers + customMappers.asSequence())
            .map { it.map(message) }
            .find { it != null }
}
