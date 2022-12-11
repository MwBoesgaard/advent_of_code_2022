fun main() {

    class Monkey(var items: MutableList<Int>, val operation: Pair<String, String>, val test: List<Int>) {
        val monkeyMailbox: MutableList<Pair<Int, Int>> = mutableListOf()
        var totalNumberOfItemsInspected = 0L

        fun inspect() {
            val increaseBy = operation.second.toIntOrNull()
            for (item in items) {
                var inspectedItem = when (operation.first) {
                    "+" -> item + (increaseBy ?: item)
                    "*" -> item * (increaseBy ?: item)
                    else -> error("Unknown item operation")
                }
                inspectedItem /= 3
                testAndPassToMailbox(inspectedItem)
                totalNumberOfItemsInspected++
            }
            items.clear()
        }

        fun testAndPassToMailbox(itemToTest: Int) {
            val denominator = test[0]
            val monkeyToSendIfTrue = test[1]
            val monkeyToSendIfFalse = test[2]

            if (itemToTest % denominator == 0) {
                monkeyMailbox.add(Pair(monkeyToSendIfTrue, itemToTest))
            } else {
                monkeyMailbox.add(Pair(monkeyToSendIfFalse, itemToTest))
            }
        }
    }

    class MonkeyGame(val monkeys: List<Monkey>, val roundsToPlay: Int) {
        fun start() {
            for (round in 1..roundsToPlay) {
                for (monkey in monkeys) {
                    if (monkey.items.size == 0) {
                        continue
                    }
                    monkey.inspect()
                    handleMonkeyMail(monkey)
                }
            }
        }

        private fun handleMonkeyMail(monkey: Monkey) {
            for (mail in monkey.monkeyMailbox) {
                val recipient = mail.first
                val item = mail.second
                monkeys[recipient].items.add(item)
            }
            monkey.monkeyMailbox.clear()
        }
    }

    fun part1(input: String): Long {
        val monkey0 = Monkey(mutableListOf(65, 78), Pair("*", "3"), listOf(5, 2, 3))
        val monkey1 = Monkey(mutableListOf(54, 78, 86, 79, 73, 64, 85, 88), Pair("+", "8"), listOf(11, 4, 7))
        val monkey2 = Monkey(mutableListOf(69, 97, 77, 88, 87), Pair("+", "2"), listOf(2, 5, 3))
        val monkey3 = Monkey(mutableListOf(99), Pair("+", "4"), listOf(13, 1, 5))
        val monkey4 = Monkey(mutableListOf(60, 57, 52), Pair("*", "19"), listOf(7, 7, 6))
        val monkey5 = Monkey(mutableListOf(91, 82, 85, 73, 84, 53), Pair("+", "5"), listOf(3, 4, 1))
        val monkey6 = Monkey(mutableListOf(88, 74, 68, 56), Pair("*", "old"), listOf(17, 0, 2))
        val monkey7 = Monkey(mutableListOf(54, 82, 72, 71, 53, 99, 67), Pair("+", "1"), listOf(19, 6, 0))


        val monkeys = listOf(monkey0, monkey1, monkey2, monkey3, monkey4, monkey5, monkey6, monkey7)

        val game = MonkeyGame(monkeys, 20)
        game.start()
        return game.monkeys
            .map { it.totalNumberOfItemsInspected }
            .sortedDescending()
            .take(2)
            .reduce { first, second -> first * second }
    }

    class Item(value: Int) {
        val remainderByFactor = mutableMapOf<Int, Int>()
        val listOfFactors = listOf(2, 3, 5, 7, 11, 13, 17, 19)

        init {
            decompose(value)
        }

        fun decompose(decomposable: Int) {
            for (factor in listOfFactors) {
                remainderByFactor[factor] = decomposable % factor
            }
        }

        fun add(number: Int) {
            for (factor in listOfFactors) {
                val currentRemainder = remainderByFactor[factor]!!
                remainderByFactor[factor] = (currentRemainder + number) % factor
            }
        }

        fun multiply(number: Int) {
            for (factor in listOfFactors) {
                val currentRemainder = remainderByFactor[factor]!!
                remainderByFactor[factor] = (currentRemainder * number) % factor
            }
        }

        fun double() {
            for (factor in listOfFactors) {
                val currentRemainder = remainderByFactor[factor]!!
                remainderByFactor[factor] = (currentRemainder * currentRemainder) % factor
            }
        }
    }

    class ItemMonkey(var items: MutableList<Item>, val operation: Pair<String, String>, val test: List<Int>) {
        val monkeyMailbox: MutableList<Pair<Int, Item>> = mutableListOf()
        var totalNumberOfItemsInspected = 0L

        fun inspect() {
            val increaseBy = operation.second.toIntOrNull()
            for (item in items) {
                when (operation.first) {
                    "+" -> item.add(increaseBy!!)
                    "*" -> if (increaseBy != null) item.multiply(increaseBy) else item.double()
                    else -> error("Unknown item operation")
                }
                testAndPassToMailbox(item)
                totalNumberOfItemsInspected++
            }
            items.clear()
        }

        fun testAndPassToMailbox(itemToTest: Item) {
            val denominator = test[0]
            val monkeyToSendIfTrue = test[1]
            val monkeyToSendIfFalse = test[2]

            if (itemToTest.remainderByFactor[denominator] == 0) {
                monkeyMailbox.add(Pair(monkeyToSendIfTrue, itemToTest))
            } else {
                monkeyMailbox.add(Pair(monkeyToSendIfFalse, itemToTest))
            }
        }
    }

    class ItemMonkeyGame(val monkeys: List<ItemMonkey>, val roundsToPlay: Int) {
        fun start() {
            for (round in 1..roundsToPlay) {
                for (monkey in monkeys) {
                    if (monkey.items.size == 0) {
                        continue
                    }
                    monkey.inspect()
                    handleMonkeyMail(monkey)
                }
            }
        }

        private fun handleMonkeyMail(monkey: ItemMonkey) {
            for (mail in monkey.monkeyMailbox) {
                val recipient = mail.first
                val item = mail.second
                monkeys[recipient].items.add(item)
            }
            monkey.monkeyMailbox.clear()
        }
    }

    fun part2(input: String): Long {
        val monkey0 = ItemMonkey(mutableListOf(Item(65), Item(78)), Pair("*", "3"), listOf(5, 2, 3))
        val monkey1 = ItemMonkey(
            mutableListOf(Item(54), Item(78), Item(86), Item(79), Item(73), Item(64), Item(85), Item(88)),
            Pair("+", "8"),
            listOf(11, 4, 7)
        )
        val monkey2 =
            ItemMonkey(mutableListOf(Item(69), Item(97), Item(77), Item(88), Item(87)), Pair("+", "2"), listOf(2, 5, 3))
        val monkey3 = ItemMonkey(mutableListOf(Item(99)), Pair("+", "4"), listOf(13, 1, 5))
        val monkey4 = ItemMonkey(mutableListOf(Item(60), Item(57), Item(52)), Pair("*", "19"), listOf(7, 7, 6))
        val monkey5 = ItemMonkey(
            mutableListOf(Item(91), Item(82), Item(85), Item(73), Item(84), Item(53)),
            Pair("+", "5"),
            listOf(3, 4, 1)
        )
        val monkey6 =
            ItemMonkey(mutableListOf(Item(88), Item(74), Item(68), Item(56)), Pair("*", "old"), listOf(17, 0, 2))
        val monkey7 = ItemMonkey(
            mutableListOf(Item(54), Item(82), Item(72), Item(71), Item(53), Item(99), Item(67)),
            Pair("+", "1"),
            listOf(19, 6, 0)
        )


        val monkeys = listOf(monkey0, monkey1, monkey2, monkey3, monkey4, monkey5, monkey6, monkey7)

        val game = ItemMonkeyGame(monkeys, 10_000)
        game.start()
        return game.monkeys
            .map { it.totalNumberOfItemsInspected }
            .sortedDescending()
            .take(2)
            .reduce { first, second -> first * second }
    }

    printSolutionFromInputRaw("Day11", ::part1)
    printSolutionFromInputRaw("Day11", ::part2)
}
