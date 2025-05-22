/**
 * 制作実習Ⅱ 第一期個人制作
 * @author 0J01028 中村蒼
 */

package com.example.todo_app

import androidx.compose.runtime.Composable
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.foundation.layout.Column
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.border
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material3.*
import androidx.compose.foundation.clickable
import androidx.compose.material.icons.filled.Check
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.navigation.NavController

// タスク一覧の表示
@Composable
fun ShowTasks(
    tasks: List<Task>,      // 表示するタスクのリスト
    tags: List<Tag>,        // タグリスト
    selectedTasks: List<String>,        // 選択されているタスクのIDリスト
    isTagSelectionActive: Boolean,      // 選択モード(タグ)か判定
    onTaskToggle: (String) -> Unit,      // 選択モードの切り替え用
    nav: NavController
) {
    // 今日のタスク一覧を表示
    tasks.forEach { task ->
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 4.dp)
                .clickable{ nav.navigate("Create_Screen") },  // クリック時の遷移先
            verticalAlignment = Alignment.Top       // 上に配置
        ) {
            // チェックボックス（タグ選択時は無効化）
            Box(
                modifier = Modifier
                    .size(24.dp)
                    .border(
                        width = 1.dp,
                        color = if (isTagSelectionActive) Color.Gray.copy(alpha = 0.5f) else Color(0xFF2196F3),
                        shape = RoundedCornerShape(2.dp)        // 枠の角を丸く
                    )
                    .background(
                        if (isTagSelectionActive) Color.Gray.copy(alpha = 0.1f) else Color.Transparent,
                        RoundedCornerShape(2.dp)
                    )
                    .clickable(enabled = !isTagSelectionActive) {
                        // タスク選択モードに切り替え
                        onTaskToggle(task.id)
                    },
                contentAlignment = Alignment.Center     // 中央に配置
            ) {
                // 選択時のチェックアイコンの表示
                if (selectedTasks.contains(task.id)) {
                    Icon(
                        imageVector = Icons.Default.Check,
                        contentDescription = "選択済み",
                        tint = Color(0xFF2196F3),
                        modifier = Modifier.size(16.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.width(8.dp))

            // タスクの内容を表示
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(8.dp)),        // 枠の角を丸く
                color = tags.getOrNull(task.tag)?.color ?: Color.White      // 背景の設定(タグの色)
            ) {
                Column(modifier = Modifier.padding(12.dp)) {
                    // タスク名
                    Text(text = task.title, fontWeight = FontWeight.Medium)

                    // タグ名(ある場合)
                    tags.getOrNull(task.tag)?.let {
                        Text(text = it.name, fontSize = 14.sp, color = Color.Gray)
                    }

                    // 日付(ある場合)
                    task.date?.let {
                        Text(
                            text = it,
                            fontSize = 14.sp,
                            modifier = Modifier.fillMaxWidth(),
                            textAlign = TextAlign.End       // 右下に配置
                        )
                    }
                }
            }
        }
    }
}

// タグ一覧の表示
@Composable
fun ShowTags(
    tags: List<Tag>, // 表示するタグのリスト
    selectedTags: List<String>, // 選択されているタグのIDリスト
    isTaskSelectionActive: Boolean, // 選択モード(タスク)か判定
    onTagToggle: (String) -> Unit, // 選択モードの切り替え用
    nav: NavController
) {
    // タグ一覧を表示
    tags.forEach { tag ->
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 4.dp)
                .clickable{ nav.navigate("TaskList_Screen") },  // クリック時の遷移先
            verticalAlignment = Alignment.Top       // 上に配置
        ) {
            // チェックボックス（タグ選択時は無効化）
            Box(
                modifier = Modifier
                    .size(24.dp)
                    .border(
                        width = 1.dp,
                        color = if (isTaskSelectionActive) Color.Gray.copy(alpha = 0.5f) else Color(0xFF2196F3),
                        shape = RoundedCornerShape(2.dp)        // 枠の角を丸く
                    )
                    .background(
                        if (isTaskSelectionActive) Color.Gray.copy(alpha = 0.1f) else Color.Transparent,
                        RoundedCornerShape(2.dp)
                    )
                    .clickable(enabled = !isTaskSelectionActive) {
                        // タグ選択モードに切り替え
                        onTagToggle(tag.id)
                    },
                contentAlignment = Alignment.Center     // 中央に配置
            ) {
                // 選択時のチェックアイコンの表示
                if (selectedTags.contains(tag.id)) {
                    Icon(
                        imageVector = Icons.Default.Check,
                        contentDescription = "選択済み",
                        tint = Color(0xFF2196F3),
                        modifier = Modifier.size(16.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.width(8.dp))

            // タグの内容を表示
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(8.dp)),        // 枠の角を丸く
                color = tag.color      // 背景の設定(タグの色)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(12.dp),
                    contentAlignment = Alignment.Center     // 中央に配置
                ) {
                    // タグ名
                    Text(
                        text = tag.name,
                        fontWeight = FontWeight.Medium
                    )
                }
            }
        }
    }
}

//完了削除の選択バー
@Composable
fun ActionButtons(
    isSelectionActive: Boolean,  // 選択モードか判定
    selectedTasks: List<String>,  // 選択中のタスクリスト
    selectedTags: List<String>,  // 選択中のタグリスト
    onDelete: () -> Unit,  // 削除処理
    onComplete: () -> Unit  // 完了処理
) {
    // タスクかタグを選択中のみ表示
    AnimatedVisibility(visible = isSelectionActive) {
        // 枠の配置
        Surface(
            modifier = Modifier.fillMaxWidth(),
            color = Color(0xFFE0E0E0) // 灰色
        ) {
            // ボタンの配置
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                // 削除ボタン
                Button(
                    onClick = onDelete,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFFFFCDD2) // 薄い赤色
                    )
                ) {
                    Text(
                        text = "削除",
                        color = Color(0xFFB71C1C) // 濃い赤色
                    )
                }

                // 完了ボタン(未完了への変更も可能にする予定)
                Button(
                    onClick = onComplete,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFFFFF9C4) // 薄い黄色
                    )
                ) {
                    Text(
                        text = "完了",
                        color = Color(0xFFF57F17) // 濃い黄色
                    )
                }
            }
        }
    }
}
