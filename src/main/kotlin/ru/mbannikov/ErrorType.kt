package ru.mbannikov

/**
 * Список общих для всех микросервисов типов ошибок
 *
 * @property code ololo
 * @property message ololo
 */
enum class ErrorType(val code: Int, val message: String) {
    UNKNOWN(-1, "Незвестная ошибка"),

    MINIMUM_PAYMENT_SUM(100, "Сумма перевода не может быть меньше %s руб."),

    INTERNAL(101, "Не удалось выполнить операцию. Попробуйте позже."),

    BLOCKED_OPERATION_BY_LIMIT(102, "Текущий лимит в размере %s руб. превышен, операция %s не может быть выполнена.")
}
