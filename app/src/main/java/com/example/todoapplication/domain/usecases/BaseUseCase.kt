package com.example.todoapplication.domain.usecases

import com.example.todoapplication.common.Utils
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

abstract class BaseUseCase<in Params, out Type> where Type : Any? {

    abstract suspend fun run(params: Params): Utils.Either<Exception, Type>

    operator fun invoke(scope: CoroutineScope, params: Params, onSuccess: (Type) -> Unit, onFailure: (Exception) -> Unit) {
        val job = scope.async { run(params) }
        scope.launch {
            when (val result = job.await()) {
                is Utils.Either.Error -> onFailure(result.exception)
                is Utils.Either.Success -> onSuccess(result.data)
            }
        }
            // may or may-not optimal
            .invokeOnCompletion {
                job.cancel()
            }
    }
}
