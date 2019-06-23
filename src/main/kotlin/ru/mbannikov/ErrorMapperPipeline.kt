package ru.mbannikov

/**
 * Конвейер преобразователей сообщений об ошибках из сторонних систем в SystemError.
 */
object ErrorMapperPipeline {
    /** Общие преобразователи для всех микросервисов */
    private val commonMappers = sequenceOf(
        RegexErrorMapper("Минимальная сумма перевода (\\d*) рублей".toRegex(), SystemErrorRegistry["MINIMUM_PAYMENT_SUM"]),

        SimpleErrorMapper("Сервер временно недоступен", SystemErrorRegistry["INTERNAL"]),

        RegexErrorMapper(
            "Невозможно совершить операцию (.*?). Превышен лимит \\((\\d*) руб\\).".toRegex(), SystemErrorRegistry["BLOCKED_OPERATION_BY_LIMIT"]
        ) { it.reversed() }
    )

    /** Преобразователи для текущего микросервиса */
    private val customMappers = mutableSetOf<ErrorMapper>()

    /**
     * Добавляет [mapper] в {@link customMappers}
     */
    fun add(mapper: ErrorMapper) {
        customMappers.add(mapper)
    }

    /**
     * Пытается преобразовать [message] через {@link commonMappers} и {@link customMappers}
     *
     * @return SystemError с преобразованным сообщением или SystemError с [message] в случае отсутствия подходящего преобразователя
     */
    fun map(message: String): SystemError =
        mapOrNull(message) ?: SystemError(
            code = -1,
            name = "UNKNOWN",
            message = message
        )

    /**
     * Пытается преобразовать [message] через {@link commonMappers} и {@link customMappers}
     *
     * @return SystemError с преобразованным сообщением или null в случае отсутствия подходящего преобразователя
     */
    fun mapOrNull(message: String): SystemError? =
        (customMappers.asSequence() + commonMappers)
            .map { it.map(message) }
            .find { it != null }
}
