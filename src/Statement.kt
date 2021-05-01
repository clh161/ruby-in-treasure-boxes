import java.util.*

class Statement(val statementType: StatementType, val targets: Set<Int>) {
    fun toString(locale: Locale): String {
        return when (locale) {
            Locale.CHINESE, Locale.TRADITIONAL_CHINESE -> {
                val typeString = if (statementType == StatementType.HAS) "有" else "沒有"
                val targetsString =
                    targets.toList().sorted().map { it + 1 }
                "" + targetsString + "號寶箱" + (if (targets.size > 1) "均" else "") + typeString + "寶石"
            }
            else -> {
                val typeString = if (statementType == StatementType.HAS) "" else " no"
                val targetsString =
                    targets.toList().sorted().map { it + 1 }
                "Box No. " + targetsString + (if (targets.size > 1) " all have" else " has") + typeString + " a ruby."

            }
        }
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Statement

        if (statementType != other.statementType) return false
        if (targets != other.targets) return false

        return true
    }

    override fun hashCode(): Int {
        var result = statementType.hashCode()
        result = 31 * result + targets.hashCode()
        return result
    }
}