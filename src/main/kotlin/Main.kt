val letters = mapOf(
    1 to 'א',
    2 to 'ב',
    3 to 'ג',
    4 to 'ד',
    5 to 'ה',
    6 to 'ו',
    7 to 'ז',
    8 to 'ח',
    9 to 'ט',
    10 to 'י',
    11 to 'כ',
    12 to 'ל',
    13 to 'מ',
    14 to 'נ',
    15 to 'ס',
    16 to 'ע',
    17 to 'פ',
    18 to 'צ',
    19 to 'ק',
    20 to 'ר',
    21 to 'ש',
    22 to 'ת'
)

val endings = mapOf(
    'כ' to 'ך',
    'מ' to 'ם',
    'נ' to 'ן',
    'פ' to 'ף',
    'צ' to 'ץ'
)

fun main() {
    val word: MutableList<Action> = mutableListOf(Action.Long, Action.Long, Action.Long, Action.Long, Action.Tap)

    println(word)
    println(Action.translate(word))
}

sealed class Action(val value: Int, val text: Char) {
    data object Tap: Action(1, '*')
    data object Long: Action(5, '|')
    data object Space: Action(0, ' ')
    data object Skip: Action(0, '-')

    companion object {
        fun translate(actions: MutableList<Action>): String {
            val result = StringBuilder()
            var currentLetterValue = 0
            var prevAction: Action? = null

            actions.forEachIndexed { index, action ->
                when (action) {
                    is Tap, is Long -> currentLetterValue += action.value
                    is Space, is Skip -> {
                        if (prevAction !is Space && prevAction !is Skip) {
                            result.append(
                                convertValueToLetter(
                                    currentLetterValue,
                                    isEndOfWord = action is Space || index+1 == actions.size
                                )
                            )
                            currentLetterValue = 0
                        }
                        if (action is Space) result.append(' ')
                    }
                }
                prevAction = action
            }

            if (currentLetterValue > 0)
                result.append(convertValueToLetter(currentLetterValue, true))
            return result.toString()
        }

        private fun convertValueToLetter(value: Int, isEndOfWord: Boolean): Char {
            val letter = letters[value] ?: ' '
            return if (isEndOfWord) endings[letter] ?: letter else letter
        }
    }
}
