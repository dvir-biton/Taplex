import Action.Companion.toTaps

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

val lettersTap = mapOf(
    "•" to 'א',
    "••" to 'ב',
    "•••" to 'ג',
    "••••" to 'ד',
    "|" to 'ה',
    "•|" to 'ו',
    "••|" to 'ז',
    "•••|" to 'ח',
    "••••|" to 'ט',
    "||" to 'י',
    "•||" to 'כ',
    "••||" to 'ל',
    "•••||" to 'מ',
    "••••||" to 'נ',
    "|||" to 'ס',
    "•|||" to 'ע',
    "••|||" to 'פ',
    "•••|||" to 'צ',
    "••••|||" to 'ק',
    "||||" to 'ר',
    "•||||" to 'ש',
    "••||||" to 'ת'
)

val endings = mapOf(
    'כ' to 'ך',
    'מ' to 'ם',
    'נ' to 'ן',
    'פ' to 'ף',
    'צ' to 'ץ'
)

fun main() {
    val word = "אתה מבין את זה"

    println(word)
    println(word.toTaps())
}

sealed class Action(val value: Int, val text: Char) {
    data object Tap: Action(1, '•')
    data object Long: Action(5, '|')
    data object Space: Action(0, '/')
    data object Skip: Action(0, ' ')

    companion object {
        fun translateActions(actions: MutableList<Action>): String {
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

        fun tapsToActions(input: String): MutableList<Action> {
            val actions = mutableListOf<Action>()
            input.forEach { char ->
                when (char) {
                    '•' -> actions.add(Tap)
                    '|' -> actions.add(Long)
                    ' ' -> actions.add(Space)
                    '-' -> actions.add(Skip)
                }
            }
            return actions
        }

        fun translateTaps(input: String): String {
            return translateActions(tapsToActions(input))
        }

        fun String.toTaps(): String {
            val tapsByLetter = lettersTap.entries.associate { (key, value) -> value to key }

            val tapsStringBuilder = StringBuilder()
            this.forEach { char ->
                if (char == ' ') {
                    tapsStringBuilder.insert(0, "/ ")
                } else {
                    val tapSequence = tapsByLetter[char]
                    if (tapSequence != null) {
                        if (tapsStringBuilder.isNotEmpty())
                            tapsStringBuilder.insert(0, " ")
                        tapsStringBuilder.insert(0, tapSequence)
                    }
                }
            }
            return tapsStringBuilder.toString()
        }


        private fun convertValueToLetter(value: Int, isEndOfWord: Boolean): Char {
            val letter = letters[value] ?: ' '
            return if (isEndOfWord) endings[letter] ?: letter else letter
        }
    }
}
