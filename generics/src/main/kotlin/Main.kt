data class User(val id: Int, val name: String, val age: Int)
data class Product(val id: Int, val name: String, val price: Int)
data class Purchase(val userId: Int, val productId: Int, val quantity: Int)


fun <T> findMax(items: List<T>, selector: (T) -> Int): T? {
    if (items.isEmpty()) return null
    return items.maxByOrNull(selector)
}

fun main() {

    val users = listOf(
        User(1, "Артём", 19),
        User(2, "Мария", 22),
        User(3, "Иван", 31),
        User(4, "Ольга", 27),
        User(5, "Денис", 40)
    )

    val products = listOf(
        Product(1, "Клавиатура", 3500),
        Product(2, "Мышь", 1800),
        Product(3, "Наушники", 5200),
        Product(4, "Монитор", 22000),
        Product(5, "Флешка 64ГБ", 1200),
        Product(6, "SSD 1TB", 8900),
        Product(7, "Коврик", 700)
    )

    val purchases = listOf(
        Purchase(userId = 1, productId = 2, quantity = 1),
        Purchase(userId = 1, productId = 5, quantity = 2),
        Purchase(userId = 2, productId = 3, quantity = 1),
        Purchase(userId = 2, productId = 4, quantity = 1),
        Purchase(userId = 3, productId = 1, quantity = 1),
        Purchase(userId = 3, productId = 6, quantity = 1),
        Purchase(userId = 3, productId = 2, quantity = 2),
        Purchase(userId = 4, productId = 5, quantity = 1),
        Purchase(userId = 4, productId = 7, quantity = 3),
        Purchase(userId = 5, productId = 4, quantity = 2),
        Purchase(userId = 5, productId = 6, quantity = 1)
    )


    val productById: Map<Int, Product> = products.associateBy { it.id }


    val ageLimit = 25
    val olderThan = users.filter { it.age > ageLimit }


    val top3Expensive = products
        .sortedByDescending { it.price }
        .take(3)


    val checkUserId = 3
    val checkProductId = 4


    val purchasesByUser: Map<Int, List<Purchase>> = purchases.groupBy { it.userId }

    val didUserBuyProduct = purchasesByUser[checkUserId]
        ?.any { it.productId == checkProductId } ?: false




    val purchasesByProduct: Map<Int, List<Purchase>> = purchases.groupBy { it.productId }



    val spendingByUser: Map<Int, Int> = purchasesByUser.mapValues { (_, userPurchases) ->
        userPurchases.sumOf { p ->
            val price = productById[p.productId]?.price ?: 0
            price * p.quantity
        }
    }


    val topBuyerEntry: Map.Entry<Int, Int>? = spendingByUser.maxByOrNull { it.value }
    val topBuyerUser: User? = topBuyerEntry?.key?.let { id -> users.find { it.id == id } }
    val topBuyerSum: Int = topBuyerEntry?.value ?: 0



    val oldestUser = findMax(users) { it.age }


    println("СПИСОК ВСЕХ ПОЛЬЗОВАТЕЛЕЙ")
    users.forEach { println(it) }

    println("\n ПОЛЬЗОВАТЕЛИ СТАРШЕ $ageLimit ")
    olderThan.forEach { println(it) }

    println("\n ТОП-3 САМЫХ ДОРОГИХ ТОВАРА ")
    top3Expensive.forEach { println(it) }

    println("\n ПРОВЕРКА: покупал ли пользователь $checkUserId товар $checkProductId ")
    println("Ответ: $didUserBuyProduct")

    println("\n СУММА ТРАТ КАЖДОГО ПОЛЬЗОВАТЕЛЯ")
    users.forEach { user ->
        val sum = spendingByUser[user.id] ?: 0
        println("${user.name} (id=${user.id}) потратил(а): $sum")
    }

    println("\n ТОП ПОКУПАТЕЛЬ ")
    if (topBuyerUser != null) {
        println("${topBuyerUser.name} (id=${topBuyerUser.id}) — сумма трат: $topBuyerSum")
    } else {
        println("Нет данных")
    }

    println("\n Самый взрослый пользователь")
    println(oldestUser)
}