fun main() {
    val generator = GameGenerator(6, 3)
    generator.debug(10000)
    val game = generator.generateGame()
    game.print()
}
