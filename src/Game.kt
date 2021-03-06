import java.util.*

class Game(
    private val boxCount: Int,
    private val rubyCount: Int,
    private val statements: MutableList<Statement>,
) {

    val isValid: Boolean
    private val validRuby: Set<Int>?
    private val validStatementIndex: Int?

    init {
        val answers = getAnswers()
        isValid = answers.size == 1
        if (isValid) {
            validRuby = answers.first()
            validStatementIndex =
                statements.mapIndexed { i, statement -> if (statement.validate(validRuby)) i else null }.filterNotNull()
                    .first()
        } else {
            validRuby = null
            validStatementIndex = null
        }
    }

    private fun getAnswers(maxCount: Int = 2): MutableSet<Set<Int>> {
        val list = mutableSetOf<Set<Int>>()
        getAnswers(list, mutableSetOf(), 0, maxCount)
        return list
    }

    private fun getAnswers(answers: MutableSet<Set<Int>>, rubyPositions: Set<Int>, n: Int, maxCount: Int) {
        if (answers.size == maxCount) return
        if (n == boxCount) return
        if (rubyPositions.size == rubyCount) {
            val isValid = statements.map { statement -> statement.validate(rubyPositions) }.filter { it }.size == 1
            if (isValid) {
                answers.add(rubyPositions)
            }
            return
        }
        getAnswers(answers, rubyPositions, n + 1, maxCount)
        val newRubyPositions = rubyPositions.toMutableSet()
        newRubyPositions.add(n)
        getAnswers(answers, newRubyPositions, n + 1, maxCount)
    }


    fun print(
        locale: Locale = Locale.ENGLISH
    ) {
        when (locale) {
            Locale.CHINESE, Locale.TRADITIONAL_CHINESE -> {
                println("海盜發現了${boxCount}個寶箱，其中只有${rubyCount}個寶箱有寶石。")
                println("每個寶箱都有一句提示寶石的位置。")
                println("${boxCount}個提示中只有一個是正確。")
                println("寶石究竟在哪${rubyCount}個寶箱？")
                println("-------------提示-------------")
                statements.forEachIndexed { i, statement -> println("""提示 ${i + 1}: $statement""") }
                println("-----------------------------")
                println("答案:${validRuby?.map { it + 1 }}號箱有寶石，提示${(validStatementIndex ?: 0) + 1}正確")
            }
            else -> {
                println("Pirates found $boxCount treasure boxes. $rubyCount of them ${if (boxCount > 1) "have" else "has"} a ruby.")
                println("There is a hint on each treasure box.")
                println("Only one of $boxCount hints is a correct statement.")
                println("Which $rubyCount treasure boxes have a ruby?")
                println("-----------Hints-----------")
                statements.forEachIndexed { i, statement -> println("Hint ${i + 1}: ${statement.toString(locale)}") }
                println("---------------------------")
                println(
                    "Answer: Box No. ${
                        validRuby?.map { it + 1 }
                    } has a ruby. Hint ${(validStatementIndex ?: 0) + 1} is correct."
                )
            }
        }
    }

}
