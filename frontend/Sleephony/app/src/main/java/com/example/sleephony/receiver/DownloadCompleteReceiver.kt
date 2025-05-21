package com.example.sleephony.receiver

import android.app.DownloadManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.database.Cursor

class DownloadCompleteReceiver(
    private val onSuccess: (downloadId: Long) -> Unit,
    private val onFailure: (downloadId: Long, reason: Int) -> Unit
) : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action != DownloadManager.ACTION_DOWNLOAD_COMPLETE) return

        val dm = context.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
        val id = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1L)
        if (id == -1L) return

        // 해당 ID 쿼리
        val query = DownloadManager.Query().setFilterById(id)
        val cursor: Cursor = dm.query(query) ?: return
        cursor.use {
            if (it.moveToFirst()) {
                val status = it.getInt(it.getColumnIndexOrThrow(DownloadManager.COLUMN_STATUS))
                when (status) {
                    DownloadManager.STATUS_SUCCESSFUL -> {
                        onSuccess(id)
                    }
                    DownloadManager.STATUS_FAILED,
                    DownloadManager.STATUS_PAUSED,
                    DownloadManager.STATUS_PENDING,
                    DownloadManager.STATUS_RUNNING -> {
                        val reason = it.getInt(it.getColumnIndexOrThrow(DownloadManager.COLUMN_REASON))
                        onFailure(id, reason)
                    }
                }
            }
        }
    }
}
