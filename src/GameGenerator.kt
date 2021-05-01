import java.lang.Exception
import kotlin.math.max
import kotlin.random.Random

class GameGenerator(private val boxCount: Int, private val rubyCount: Int) {
    private val rubyPermutation: MutableSet<Set<Int>>

    init {
        rubyPermutation = getRubyPermutation()
    }

    fun generateGame(): Game {
        while (true) {
            val statements = genStatements()
            val game = Game(boxCount, rubyCount, statements)
            if (game.isValid) return game
        }
    }

    fun debug(printingInterval: Int = 100) {
        println("Start generating a game.")
        var totalGameCount = 0
        var validGameCount = 0
        val startTime = System.currentTimeMillis()
        while (true) {
            val statements = genStatements()
            val game = Game(boxCount, rubyCount, statements)
            totalGameCount++
            if (game.isValid)
                validGameCount++
            val duration = System.currentTimeMillis() - startTime
            if (totalGameCount % printingInterval == 0)
                println(
                    listOf(
                        "Total games: $totalGameCount",
                        "Games/sec: ${(totalGameCount.toDouble() / duration) * 1000 * 60}",
                        "Valid Games/sec: ${(validGameCount.toDouble() / duration) * 1000 * 60}",
                        "Valid Games/Total Games: ${(validGameCount.toDouble() / totalGameCount)}",
                    )
                )

        }
    }

    private fun getRubyPermutation(): MutableSet<Set<Int>> {
        val list = mutableSetOf<Set<Int>>()
        getRubyPermutation(list, mutableSetOf(), 0)
        return list
    }

    private fun getRubyPermutation(existing: MutableSet<Set<Int>>, pre: Set<Int>, i: Int) {
        if (pre.size == rubyCount) {
            existing.add(pre)
            return
        }
        if (i == boxCount) return
        getRubyPermutation(existing, pre, i + 1)
        getRubyPermutation(existing, pre.toMutableSet().apply { this.add(i) }, i + 1)
    }

    private fun genStatements(): MutableList<Statement> {
        val statements = mutableListOf<Statement>()
        while (statements.size < boxCount) {
            val statement = genStatement()
            if (statements.map { it.targets }.contains(statement.targets)) continue
            statements.add(statement)
        }
        return statements
    }


    private fun genStatement(): Statement {
        val statementType = if (Random.nextBoolean()) StatementType.HAS else StatementType.HAS_NOT
        val targetCountMax = if (StatementType.HAS === statementType) rubyCount else boxCount - rubyCount
        val targetCountMin = max(1, targetCountMax - 1)
        val targetCount = Random.nextInt(targetCountMin, targetCountMax + 1)
        val targets = mutableSetOf<Int>()
        while (targets.size < targetCount) {
            val target = Random.nextInt(0, boxCount)
            if (!targets.contains(target))
                targets.add(target)
        }
        return Statement(statementType, targets)
    }
}