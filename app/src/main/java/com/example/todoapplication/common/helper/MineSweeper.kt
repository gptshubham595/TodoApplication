package com.example.todoapplication.common.helper

import kotlin.random.Random

class MineSweeper(private val rows: Int, private val cols: Int, private val mines: Int) {

    private val board: Array<Array<Cell>> = Array(rows) { Array(cols) { Cell() } }

    fun placeMines() {
        repeat(mines) {
            var row: Int
            var col: Int
            do {
                row = Random.nextInt(rows)
                col = Random.nextInt(cols)
            } while (board[row][col].isMine)
            board[row][col].isMine = true
        }
    }

    fun calculateNeighborMines() {
        for (i in 0 until rows) {
            for (j in 0 until cols) {
                if (!board[i][j].isMine) {
                    board[i][j].neighborMines = countNeighborMines(i, j)
                }
            }
        }
    }

    private fun countNeighborMines(row: Int, col: Int): Int {
        var count = 0
        for (i in -1..1) {
            for (j in -1..1) {
                val newRow = row + i
                val newCol = col + j
                if (newRow in 0 until rows && newCol in 0 until cols && board[newRow][newCol].isMine) {
                    count++
                }
            }
        }
        return count
    }

    fun printBoard() {
        for (i in 0 until rows) {
            for (j in 0 until cols) {
                val cell = board[i][j]
                print(
                    if (cell.isUncovered) {
                        if (cell.isMine) "X" else if (cell.neighborMines > 0) cell.neighborMines.toString() else "."
                    } else {
                        if (cell.isFlagged) "F" else "."
                    }
                )
                print(" ")
            }
            println()
        }
        println()
    }

    fun play(row: Int, col: Int) {
        val cell = board[row][col]
        if (cell.isMine) {
            println("Game Over! You hit a mine.")
        } else {
            cell.isUncovered = true
            if (cell.neighborMines == 0) {
                uncoverEmptyCells(row, col)
            }
            printBoard()
        }
    }

    private fun uncoverEmptyCells(row: Int, col: Int) {
        for (i in -1..1) {
            for (j in -1..1) {
                val newRow = row + i
                val newCol = col + j
                if (newRow in 0 until rows && newCol in 0 until cols) {
                    val cell = board[newRow][newCol]
                    if (!cell.isMine && !cell.isUncovered) {
                        cell.isUncovered = true
                        if (cell.neighborMines == 0) {
                            uncoverEmptyCells(newRow, newCol)
                        }
                    }
                }
            }
        }
    }

    fun toggleFlag(row: Int, col: Int) {
        board[row][col].isFlagged = !board[row][col].isFlagged
        printBoard()
    }

    private data class Cell(
        var isMine: Boolean = false,
        var isUncovered: Boolean = false,
        var isFlagged: Boolean = false,
        var neighborMines: Int = 0
    )
}

fun main() {
    val minesweeper = MineSweeper(rows = 5, cols = 5, mines = 5)
    minesweeper.placeMines()
    minesweeper.calculateNeighborMines()
    minesweeper.printBoard()

    minesweeper.play(1, 1)
    minesweeper.play(2, 2)
    minesweeper.play(3, 3)
    minesweeper.toggleFlag(0, 0)
    minesweeper.play(0, 0)
}
