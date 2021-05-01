fun main() {
    val generator = GameGenerator(5, 3)
    val game = generator.generateGame()
    game.print()
}
