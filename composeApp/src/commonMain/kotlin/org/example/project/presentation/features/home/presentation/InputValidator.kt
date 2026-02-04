package org.example.project.presentation.features.home.presentation

object InputValidator {

    private val allowedRegex = Regex("^[a-zA-Z][a-zA-Z0-9_]{2,11}$")

    fun isValidIdOrName(input: String): Boolean {
        return allowedRegex.matches(input)
    }
}