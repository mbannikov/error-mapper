package ru.mbannikov

fun map(message: String) {
    println("До:")
    println("\t$message")
    println("После:")
    println("\t${ErrorMapperPipeline.map(message)}\n")
}

fun main() {
    listOf(
        "Минимальная сумма перевода 10 рублей",
        "Сервер временно недоступен",
        "Невозможно совершить операцию 15778864-М. Превышен лимит (1000 руб)."
    ).forEach(::map)
}
