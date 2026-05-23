package com.example.newsnest.worker

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.domain.usecase.RefreshArticlesUseCase
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject

@HiltWorker
class NewsSyncWorker @AssistedInject constructor(
    @Assisted context: Context,
    @Assisted workerParams: WorkerParameters,
    private val refreshArticlesUseCase: RefreshArticlesUseCase
) : CoroutineWorker(context, workerParams) {

    override suspend fun doWork(): Result {
        return try {
            // Read category passed via inputData, default to "general" if not provided
            val category = inputData.getString(KEY_CATEGORY) ?: "general"
            refreshArticlesUseCase(category)
            Result.success()
        } catch (e: Exception) {
            Result.retry()
        }
    }

    companion object {
        const val WORK_NAME   = "NewsSyncWorker"
        const val KEY_CATEGORY = "key_category"   // key to pass category via WorkRequest
    }
}