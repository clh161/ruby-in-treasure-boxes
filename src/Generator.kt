import kotlin.math.max
import kotlin.random.Random

class Generator(val boxCount: Int, val rubyCount: Int) {
    private val rubyPermutation: MutableSet<Set<Int>>

    init {
        rubyPermutation = getRubyPermutation()
    }

    fun generateStatements(): Game {
        while (true) {
            val statements = genStatements()
            val validRubies = rubyPermutation.filter { validateStatements(it, statements).filter { it }.size == 1 }
            if (validRubies.size != 1) continue
            val validStatementIndex =
                validateStatements(validRubies.first(), statements).mapIndexed { i, isTrue -> if (isTrue) i else null }
                    .filterNotNull().first()
            return Game(boxCount, rubyCount, statements, validRubies, validStatementIndex)
        }
    }

    fun validateStatements(rubies: Set<Int>, statements: MutableList<Statement>): List<Boolean> {
        return statements.map {
            if (it.statementType == StatementType.HAS) rubies.containsAll(it.targets) else it.targets.intersect(
                rubies
            )
                .isEmpty()
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

    fun genStatements(): MutableList<Statement> {
        val statements = mutableListOf<Statement>()
        while (statements.size < boxCount) {
            val statement = genStatement()
            if (statements.map { it.targets }.contains(statement.targets)) continue
            statements.add(statement)
        }
        return statements
    }


    fun genStatement(): Statement {
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