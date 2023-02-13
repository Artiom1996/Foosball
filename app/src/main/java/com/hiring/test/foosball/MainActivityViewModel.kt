package com.hiring.test.foosball

import com.hiring.test.foosball.base.BaseViewModel
import com.hiring.test.foosball.data.model.GameResult
import com.hiring.test.foosball.data.model.Player
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.Completable
import io.reactivex.Single
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class MainActivityViewModel @Inject constructor() : BaseViewModel() {

    private val gameResults = mutableListOf<GameResult>()

    /**
    TODO
    Add sharedRepository to the constructor of the ViewModel.
    Use Room database to store the data and manipulate
    the data of the database instead of saving values in memory.
    Also use separate viewModels for each fragment after this change
    instead of using sharedViewModel.
     **/

    private val players = mutableListOf<Player>()

    fun requestGameResults(): Single<MutableList<GameResult>> {
        //TODO receive data from the repository instead of returning the data from memory
        return Single.just(
            gameResults
        ).doOnError {
            Timber.e(it)
        }
    }

    fun addGameResults(gameResult: GameResult): Completable {
        //TODO insert data to the Room database instead of adding to the collection in memory
        return Completable.create { gameResultEmitter ->
            gameResults.add(gameResult)
            updatePlayersInfo(gameResult, true)
            gameResultEmitter.onComplete()
        }.doOnError {
            Timber.e(it)
        }
    }

    fun removeGameResults(position: Int): Completable {
        //TODO remove data from the Room database instead of removing it from the collection in memory
        return Completable.create { gameResultEmitter ->
            updatePlayersInfo(gameResults[position], false)
            gameResults.removeAt(position)
            gameResultEmitter.onComplete()
        }.doOnError {
            Timber.e(it)
        }
    }

    private fun updatePlayersInfo(gameResult: GameResult, isAdded: Boolean) {
        updateFirstPlayerInfo(gameResult, isAdded)
        updateSecondPlayerInfo(gameResult, isAdded)
    }

    private fun updateFirstPlayerInfo(gameResult: GameResult, isAdded: Boolean) {
        var firstPlayer =
            players.find { it.userName == gameResult.firstUserGameData.userName }
        if (firstPlayer != null) {
            var firstPlayerIndex = -1
            players.forEachIndexed { index, player ->
                if (player == firstPlayer) {
                    firstPlayerIndex = index
                }
            }
            if (firstPlayerIndex != -1) {
                if (isAdded) {
                    val isWinner =
                        gameResult.firstUserGameData.userScore > gameResult.secondUserGameData.userScore
                    players[firstPlayerIndex] =
                        Player(
                            firstPlayer.userName, firstPlayer.games + 1, if (isWinner) {
                                firstPlayer.wins + 1
                            } else {
                                firstPlayer.wins
                            }
                        )
                } else {
                    players.removeAt(firstPlayerIndex)
                }
            }
        } else {
            val isWinner =
                gameResult.firstUserGameData.userScore > gameResult.secondUserGameData.userScore
            firstPlayer = Player(
                userName = gameResult.firstUserGameData.userName,
                games = 1,
                wins = if (isWinner) {
                    1
                } else 0
            )
            players.add(firstPlayer)
        }
    }

    private fun updateSecondPlayerInfo(gameResult: GameResult, isAdded: Boolean) {
        // TODO use userId for manipulating with user's data instead of username, save or remove data to/from Room database
        var secondPlayer =
            players.find { it.userName == gameResult.secondUserGameData.userName }
        if (secondPlayer != null) {
            var secondPlayerIndex = -1
            players.forEachIndexed { index, player ->
                if (player == secondPlayer) {
                    secondPlayerIndex = index
                }
            }
            if (secondPlayerIndex != -1) {
                if (isAdded) {
                    val isWinner =
                        gameResult.firstUserGameData.userScore < gameResult.secondUserGameData.userScore
                    players[secondPlayerIndex] =
                        Player(
                            secondPlayer.userName, secondPlayer.games + 1, if (isWinner) {
                                secondPlayer.wins + 1
                            } else {
                                secondPlayer.wins
                            }
                        )
                } else {
                    players.removeAt(secondPlayerIndex)
                }
            }
        } else {
            val isWinner =
                gameResult.firstUserGameData.userScore < gameResult.secondUserGameData.userScore
            secondPlayer = Player(
                userName = gameResult.secondUserGameData.userName,
                games = 1,
                wins = if (isWinner) {
                    1
                } else 0
            )
            players.add(secondPlayer)
        }
    }

    fun requestTopPlayers(sortType: SortType): Single<List<Player>> {
        // TODO receive the data from Room Database
        return Single.just(
            when (sortType) {
                SortType.NUMBER_OF_GAMES -> {
                    players.sortedByDescending { it.games }
                }
                SortType.WINS -> {
                    players.sortedByDescending { it.wins }
                }
            }
        )
    }

    enum class SortType {
        WINS, NUMBER_OF_GAMES
    }
}