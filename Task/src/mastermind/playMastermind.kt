package mastermind

import kotlin.random.Random
import mastermind.Evaluation

data class Evaluation(val rightPosition: Int, val wrongPosition: Int)

val ALPHABET = 'A'..'F'
const val CODE_LENGTH = 4

fun main() {
    val differentLetters = false
    playMastermind(differentLetters)
}

fun playMastermind(
    differentLetters: Boolean,
    secret: String = generateSecret(differentLetters)
) {
    var evaluation: Evaluation

    do {
        print("Your guess: ")
        var guess = readLine()!!

        while (hasErrorsInInput(guess)) {
            println("Incorrect input: $guess. " +
                    "It should consist of $CODE_LENGTH characters from $ALPHABET. " +
                    "Please try again.")
            print("Your guess: ")
            guess = readLine()!!
        }

        evaluation = if (differentLetters) {
            evaluateGuess(secret, guess)
        } else {
            evaluateGuess(secret, guess, true)
        }

        if (evaluation.isComplete()) {
            println("The guess is correct!")
        } else {
            println("Right positions: ${evaluation.rightPosition}; " +
                    "wrong positions: ${evaluation.wrongPosition}.")
        }

    } while (!evaluation.isComplete())
}

fun Evaluation.isComplete(): Boolean = rightPosition == CODE_LENGTH

fun hasErrorsInInput(guess: String): Boolean {
    val possibleLetters = ALPHABET.toSet()
    return guess.length != CODE_LENGTH || guess.any { it !in possibleLetters }
}

fun generateSecret(differentLetters: Boolean): String {
    val chars = ALPHABET.toMutableList()
    return buildString {
        repeat(CODE_LENGTH) {
            val letter = chars.random()
            append(letter)
            if (differentLetters) {
                chars.remove(letter)
            }
        }
    }
}

fun evaluateGuess(secret: String, guess: String, allowRepeats: Boolean = false): Evaluation {
    var rightPosition = 0
    var wrongPosition = 0
    val secretList = secret.toMutableList()
    val guessList = guess.toMutableList()

    for (i in secret.indices) {
        if (secret[i] == guess[i]) {
            rightPosition++
            secretList[i] = ' ' // Mark positions that are already evaluated
            guessList[i] = ' '
        }
    }

    for (i in secret.indices) {
        if (secretList[i] != ' ') {
            val foundIndex = guessList.indexOf(secretList[i])
            if (foundIndex != -1) {
                wrongPosition++
                guessList[foundIndex] = ' ' // Mark positions that are already evaluated
            }
        }
    }

    if (allowRepeats) {
        val countsSecret = secret.groupBy { it }.mapValues { it.value.size }
        val countsGuess = guess.groupBy { it }.mapValues { it.value.size }

        val correctRepeats = countsSecret.entries.sumBy { entry ->
            minOf(entry.value, countsGuess[entry.key] ?: 0)
        }

        val wrongPositionWithRepeats = wrongPosition - correctRepeats
        return Evaluation(rightPosition, wrongPositionWithRepeats)
    }

    return Evaluation(rightPosition, wrongPosition)
}
