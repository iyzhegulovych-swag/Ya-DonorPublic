plugins {
    // this is necessary to avoid the plugins to be loaded multiple times
    // in each subproject's classloader
    alias(libs.plugins.androidApplication) apply false
    alias(libs.plugins.androidMultiplatformLibrary) apply false
    alias(libs.plugins.composeMultiplatform) apply false
    alias(libs.plugins.composeCompiler) apply false
    alias(libs.plugins.kotlinMultiplatform) apply false
    alias(libs.plugins.spotless)
}

// Форматування всього проєкту з кореня: модулів мало, і тримати однакове
// налаштування у кожному було б зайвим дублюванням.
//
//   ./gradlew spotlessApply   — відформатувати
//   ./gradlew spotlessCheck   — перевірити, нічого не змінюючи
//
// Правила стилю лежать в .editorconfig, звідки їх читає і ktlint, і IDE.
spotless {
    kotlin {
        target("**/src/**/*.kt")
        targetExclude("**/build/**")
        ktlint(libs.versions.ktlint.get())
        trimTrailingWhitespace()
        endWithNewline()
    }
    kotlinGradle {
        target("**/*.gradle.kts")
        targetExclude("**/build/**")
        ktlint(libs.versions.ktlint.get())
    }
}
