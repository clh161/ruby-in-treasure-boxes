import java.util.*
import kotlin.math.max
import kotlin.random.Random

const val BOX_COUNT = 5
const val RUBY_COUNT = 3

fun main() {
    val rubies = getRubyPermutation().toList()
    while (true) {
        val statements = genStatements()
        val validRubies = rubies.filter { validateStatements(it, statements).filter { it }.size == 1 }
        if (validRubies.size != 1) continue
        val validStatementIndex =
            validateStatements(validRubies.first(), statements).mapIndexed { i, isTrue -> if (isTrue) i else null }
                .filterNotNull().first()
        printQuestion(statements, validRubies, validStatementIndex)
        return
    }
}

private fun printQuestion(
    statements: MutableList<Statement>,
    validRubies: List<Set<Int>>,
    validStatementIndex: Int,
    locale: Locale = Locale.ENGLISH
) {
    when (locale) {
        Locale.CHINESE, Locale.TRADITIONAL_CHINESE -> {
            println("海盜發現了${BOX_COUNT}個寶箱，其中只有${RUBY_COUNT}個寶箱有寶石。")
            println("每個寶箱都有一句提示寶石的位置。")
            println("${BOX_COUNT}個提示中只有一個是正確。")
            println("寶石究竟在哪${RUBY_COUNT}個寶箱？")
            println("-------------提示-------------")
            statements.forEachIndexed { i, statement -> println("""提示 ${i + 1}: $statement""") }
            println("-----------------------------")
            println("答案:${validRubies.first().map { it + 1 }}號箱有寶石，提示${validStatementIndex + 1}正確")
        }
        else -> {
            println("Pirates found $BOX_COUNT treasure boxes. $RUBY_COUNT of them ${if (BOX_COUNT > 1) "have" else "has"} a ruby.")
            println("There is a hint on each treasure box.")
            println("Only one of $BOX_COUNT hints is a correct statement.")
            println("Which $RUBY_COUNT treasure boxes have a ruby?")
            println("-----------Hints-----------")
            statements.forEachIndexed { i, statement -> println("Hint ${i + 1}: ${statement.toString(locale)}") }
            println("---------------------------")
            println(
                "Answer: Box No. ${
                    validRubies.first().map { it + 1 }
                } has a ruby. Hint ${validStatementIndex + 1} is correct."
            )
        }
    }
}

fun getRubyPermutation(): MutableSet<Set<Int>> {
    val list = mutableSetOf<Set<Int>>()
    getRubyPermutation(list, mutableSetOf(), 0)
    return list
}

fun getRubyPermutation(existing: MutableSet<Set<Int>>, pre: Set<Int>, i: Int) {
    if (pre.size == RUBY_COUNT) {
        existing.add(pre)
        return
    }
    if (i == BOX_COUNT) return
    getRubyPermutation(existing, pre, i + 1)
    getRubyPermutation(existing, pre.toMutableSet().apply { this.add(i) }, i + 1)
}

fun validateStatements(rubies: Set<Int>, statements: MutableList<Statement>): List<Boolean> {
    return statements.map {
        if (it.statementType == StatementType.HAS) rubies.containsAll(it.targets) else it.targets.intersect(
            rubies
        )
            .isEmpty()
    }
}

fun genStatements(): MutableList<Statement> {
    val statements = mutableListOf<Statement>()
    while (statements.size < BOX_COUNT) {
        val statement = genStatement()
        if (statements.map { it.targets }.contains(statement.targets)) continue
        statements.add(statement)
    }
    return statements
}


fun genStatement(): Statement {
    val statementType = if (Random.nextBoolean()) StatementType.HAS else StatementType.HAS_NOT
    val targetCountMax = if (StatementType.HAS === statementType) RUBY_COUNT else BOX_COUNT - RUBY_COUNT
    val targetCountMin = max(1, targetCountMax - 1)
    val targetCount = Random.nextInt(targetCountMin, targetCountMax + 1)
    val targets = mutableSetOf<Int>()
    while (targets.size < targetCount) {
        val target = Random.nextInt(0, BOX_COUNT)
        if (!targets.contains(target))
            targets.add(target)
    }
    return Statement(statementType, targets)
}

enum class StatementType {
    HAS, HAS_NOT
}
