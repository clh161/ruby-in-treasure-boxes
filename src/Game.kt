import java.util.*

class Game(
    val boxCount: Int,
    val rubyCount: Int,
    val statements: MutableList<Statement>,
    val validRubies: List<Set<Int>>,
    val validStatementIndex: Int
) {


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
                println("答案:${validRubies.first().map { it + 1 }}號箱有寶石，提示${validStatementIndex + 1}正確")
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
                        validRubies.first().map { it + 1 }
                    } has a ruby. Hint ${validStatementIndex + 1} is correct."
                )
            }
        }
    }

}
