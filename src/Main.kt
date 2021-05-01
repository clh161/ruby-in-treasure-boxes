import java.util.*

fun main() {
    val generator = Generator(5, 3)
    val generateStatements = generator.generateStatements()
    generateStatements.print()
}

enum class StatementType {
    HAS, HAS_NOT
}
