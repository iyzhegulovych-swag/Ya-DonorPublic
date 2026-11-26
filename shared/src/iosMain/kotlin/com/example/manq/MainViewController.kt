package com.example.manq

import androidx.compose.ui.window.ComposeUIViewController

/**
 * Точка входу для Swift: ContentView.swift кличе її як
 * MainViewControllerKt.MainViewController(). Ім'я з великої літери —
 * вимога цього контракту, тому правило ktlint вимкнене.
 */
@Suppress("ktlint:standard:function-naming")
fun MainViewController() = ComposeUIViewController { App() }
