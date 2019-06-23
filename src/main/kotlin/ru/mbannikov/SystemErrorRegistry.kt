package ru.mbannikov

/**
 * Реестр возможных ошибок в приложении
 */
object SystemErrorRegistry {
    private val types = hashMapOf<String, SystemError>()

    init {
        register("MINIMUM_PAYMENT_SUM", 100, "Сумма перевода не может быть меньше %s руб.")
        register("INTERNAL", 101, "Не удалось выполнить операцию. Попробуйте позже.")
        register("BLOCKED_OPERATION_BY_LIMIT", 102, "Текущий лимит в размере %s руб. превышен, операция %s не может быть выполнена.")
    }

    /**
     * Добавляет ошибку в реестр
     */
    fun register(error: SystemError) {
        // TODO: check that name is uniq
        types[error.name] = error
    }

    /**
     * Создает и добавляет ошибку в реестр
     *
     * @return созданная ошибка
     */
    fun register(name: String, code: Int, message: String): SystemError {
        return SystemError(
            name = name,
            code = code,
            message = message
        ).also { register(it) }
    }

    /**
     * Удаляет ошибку из реестра
     */
    fun unregister(error: SystemError) {
        types.remove(error.name)
    }

    /**
     * Удаляет ошибку из реестра
     */
    fun unregister(name: String) {
        types.remove(name)
    }

    /**
     * Возвращает ошибку по [name]
     * 
     * @sample SystemErrorRegistry["SOME_ERROR_NAME"]
     */
    operator fun get(name: String): SystemError =
        types[name] ?: throw Exception("Unknown error type: $name") // TODO: change exception class
}
