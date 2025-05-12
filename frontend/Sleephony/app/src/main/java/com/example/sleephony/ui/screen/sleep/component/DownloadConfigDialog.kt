package com.example.sleephony.ui.screen.sleep.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.Alignment

@Composable
fun DownloadConfirmDialog(
    allowMobile: Boolean,
    onAllowChange: (Boolean) -> Unit,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("모바일 네트워크로 다운로드하시겠습니까?") },
        text = {
            Column  {
                Text("Wi-Fi 연결이 아닙니다. 데이터가 사용됩니다.")
                Row (
                    modifier = Modifier.padding(top = 8.dp),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Checkbox(
                        checked = allowMobile,
                        onCheckedChange = onAllowChange
                    )
                    Spacer(Modifier.width(4.dp))
                    Text("항상 허용")
                }
            }
        },
        confirmButton = {
            TextButton (onClick = onConfirm) { Text("확인") }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("취소") }
        }
    )
}
