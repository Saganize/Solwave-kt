package com.saganize.solwave.domain.usecases

interface UseCase<in I, out O> {
    suspend fun execute(input: I): O
}
