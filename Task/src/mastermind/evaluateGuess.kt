package mastermind

data class Evaluation(val rightPosition: Int, val wrongPosition: Int)

fun evaluateGuess(secret: String, guess: String): Evaluation {
   TODO()
    var rightPosition = 0
    var wrongPosition = 0
    val secretList = secret.toMutableList()
    val guessList = guess.toMutableList()

    // Evaluasi posisi yang benar
    for (i in secret.indices) {
        if (secret[i] == guess[i]) {
            rightPosition++
            secretList[i] = ' ' // Tandai posisi yang sudah dievaluasi
            guessList[i] = ' '
        }
    }

    // Evaluasi posisi yang salah
    for (i in secret.indices) {
        if (secretList[i] != ' ') {
            val foundIndex = guessList.indexOf(secret[i])
            if (foundIndex != -1) {
                wrongPosition++
                guessList[foundIndex] = ' ' // Tandai posisi yang sudah dievaluasi
            }
        }
    }

    return Evaluation(rightPosition, wrongPosition)
}
