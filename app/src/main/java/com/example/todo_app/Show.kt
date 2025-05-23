/**
 * 制作実習Ⅱ 第一期個人制作
 * @author 0J01028 中村蒼
 */

package com.example.todo_app

import android.content.Context
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
import androidx.compose.material.icons.filled.Close
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.navigation.NavController
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.ViewModel

// タスク一覧の表示
@Composable
fun ShowTasks(
    tasks: List<Task>,      // 表示するタスクのリスト
    tags: List<Tag>,        // タグリスト
    selectedTasks: List<Int>,        // 選択されているタスクのIDリスト
    isTagSelectionActive: Boolean,      // 選択モード(タグ)か判定
    onTaskToggle: (Int) -> Unit,      // 選択モードの切り替え用
    showState: Boolean,      // 表示するタスクの状態を判定(true:完了、false:未完了)
    nav: NavController
) {
    val filteredTasks = tasks.filter { it.state == showState }

    // 今日のタスク一覧を表示
    filteredTasks.forEach { task ->
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 4.dp)
                .clickable{ nav.navigate("Create_Screen/${task.id}") },  // クリック時の遷移先
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
    selectedTags: List<Int>, // 選択されているタグのIDリスト
    isTaskSelectionActive: Boolean, // 選択モード(タスク)か判定
    onTagToggle: (Int) -> Unit, // 選択モードの切り替え用
    nav: NavController
) {
    // タグ一覧を表示
    tags.forEach { tag ->
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 4.dp)
                .clickable{ nav.navigate("TaskList_Screen/${tag.id}") },  // クリック時の遷移先
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
    selectedTasks: List<Int>,  // 選択中のタスクリスト
    selectedTags: List<Int>,  // 選択中のタグリスト
    showState: Boolean,      // 表示しているタスクの状態を判定(true:完了、false:未完了)
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
                if(selectedTags.isEmpty()) {
                    Button(
                        onClick = onComplete,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFFFFF9C4) // 薄い黄色
                        )
                    ) {
                        Text(
                            text = if (showState == true) "未完了" else "完了",  // showStateがtrueの場合は"未完了"、showStateがfalseの場合は"完了"
                            color = Color(0xFFF57F17) // 濃い黄色
                        )
                    }
                }
            }
        }
    }
}

// タグ作成ウィンドウの表示
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TagDialog(context: Context, viewModel: TaskViewModel, tagDialogOpen: Boolean, tags: List<Tag>, onDismiss: () -> Unit) {
    // タグ名の入力状態を保持する変数
    var title by remember { mutableStateOf("") }

    // ドロップダウンメニューの展開状態を保持する変数
    var expanded by remember { mutableStateOf(false) }

    // 選択された色を保持する変数
    var selectedColor by remember { mutableStateOf<String?>(null) }

    // 全ての欄が入力されているか判定
    val isFormValid = title.isNotBlank() && selectedColor != null

    // 使用中のIDリスト
    val usedIds = tags.map { it.id }.toSet()

    // 色の選択肢リスト
    val colorOptions = listOf("赤", "黄", "青", "オレンジ", "緑", "紫", "ピンク")

    // 色名とカラーコードのリスト
    val colorMap: Map<String, Color> = mapOf(
        "赤" to Color(0xFFFFCDD2),
        "黄" to Color(0xFFFFF9C4),
        "青" to Color(0xFFBBDEFB),
        "オレンジ" to Color(0xFFFFE0B2),
        "緑" to Color(0xFFC8E6C9),
        "紫" to Color(0xFFE1BEE7),
        "ピンク" to Color(0xFFF8BBD0)
    )

    // ダイアログが開いている場合のみ表示
    if (tagDialogOpen) {
        Dialog(onDismissRequest = onDismiss) {
            // ウィンドウの背景と角丸を設定
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                shape = RoundedCornerShape(8.dp)
            ) {
                // レイアウト
                Box {
                    Column(modifier = Modifier.padding(16.dp)) {
                        // タイトル
                        Text(
                            text = "新規タグ作成",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        // タグ名の入力フィールド
                        TextField(
                            value = title,
                            onValueChange = { title = it },
                            placeholder = { Text("タグ名") }, // 未入力時の表示文字
                            colors = TextFieldDefaults.textFieldColors(
                                containerColor = Color(0xFFF5F5F5)
                            )
                        )


                        Spacer(modifier = Modifier.height(16.dp))

                        // カラーコードのボックス
                        ExposedDropdownMenuBox(
                            expanded = expanded,
                            onExpandedChange = { expanded = !expanded } // クリックで展開/非展開を切り替え
                        ) {
                            // 選択中のタグ名を表示する読み取り専用のテキストフィールド
                            TextField(
                                modifier = Modifier
                                    .menuAnchor()
                                    .fillMaxWidth(),
                                value = selectedColor ?: "", // 現在選択中のタグ名を表示
                                onValueChange = {},
                                readOnly = true,
                                label = { Text("色") },
                                trailingIcon = {
                                    ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) // 展開アイコン
                                },
                                colors = TextFieldDefaults.textFieldColors(
                                    containerColor = colorMap[selectedColor] ?: Color(0xFFF5F5F5) // 選択された色を背景に反映
                                )
                            )

                            // ドロップダウンメニューの中身
                            ExposedDropdownMenu(
                                expanded = expanded,
                                onDismissRequest = { expanded = false }
                            ) {
                                // カラーコードをメニューとして表示
                                colorOptions.forEach { colorName ->
                                    DropdownMenuItem(
                                        text = { Text(colorName) },
                                        onClick = {
                                            selectedColor = colorName
                                            expanded = false
                                        },
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .background(colorMap[colorName] ?: Color.White) // タグの背景
                                    )
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        // 作成ボタン
                        Button(
                            onClick = {
                                // カラーコードを取得
                                val color = colorMap[selectedColor] ?: Color.White

                                // 新しいタグを作成
                                val newTag = Tag(
                                    id = generateSequence(0) { it + 1 }.first { it !in usedIds },    // 未使用かつ最小のIDを使用
                                    name = title,   // タグ名
                                    color = color   // カラーコード
                                )

                                // タグを追加
                                viewModel.addTag(context, newTag)

                                // ダイアログを閉じる
                                onDismiss()
                            },
                            enabled = isFormValid,   // タグ名と色が選択されているときのみ有効に設定
                            modifier = Modifier.align(Alignment.End)
                        ) {
                            Text(text = "作成")
                        }

                    }

                    // バツボタン
                    IconButton(
                        onClick = onDismiss,
                        modifier = Modifier
                            .align(Alignment.TopEnd)
                            .padding(8.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "閉じる",
                            tint = Color.Red
                        )
                    }
                }
            }
        }
    }
}

