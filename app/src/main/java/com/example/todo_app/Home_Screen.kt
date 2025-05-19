/**
 * 制作実習Ⅱ 第一期個人制作
 * @author 0J01028 中村蒼
 */

package com.example.todo_app

import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.border
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.foundation.clickable
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.lifecycle.viewmodel.compose.viewModel


// ホーム画面
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Home_Screen(navController: NavController, viewModel: TaskViewModel = viewModel()){
    // 選択されたタスクのIDリスト
    var selectedTasks by remember { mutableStateOf(listOf<String>()) }

    // 選択されたタグのIDリスト
    var selectedTags by remember { mutableStateOf(listOf<String>()) }

    // タスク作成ボタンのクリック判定
    var CreateTask by remember { mutableStateOf(false) }

    // タグ作成ボタンのクリック判定
    var CreateTag by remember { mutableStateOf(false) }

    // タグダイアログの表示状態
    var tagDialogOpen by remember { mutableStateOf(false) }

    // チェックボックスでの選択
    val isSelectionActive = selectedTasks.isNotEmpty() || selectedTags.isNotEmpty()
    val isTaskSelectionActive = selectedTasks.isNotEmpty()
    val isTagSelectionActive = selectedTags.isNotEmpty()

    // タスク情報とタグ情報の読み込み
    val tasks by viewModel.tasks
    val tags by viewModel.tags

    // メイン画面のレイアウト
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF5F5F5)) // 背景色を薄いグレーに設定
    ) {
        // トップバーの表示
        TopBar(navController)

        // スクロール可能
        LazyColumn(
            modifier = Modifier
                .weight(1f)
                .padding(16.dp)
        ) {
            // 検索バー
            item {
                // 検索入力フィールド
                OutlinedTextField(
                    value = "",
                    // 入力時の処理
                    onValueChange = {},
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 16.dp)
                        .border(
                            width = 1.dp,
                            color = Color.Gray.copy(alpha = 0.5f),
                            shape = RoundedCornerShape(8.dp)
                        ),
                    placeholder = { Text("タスクの検索") },       // 入力される前に表示する値
                    leadingIcon = {
                        // 検索アイコンの表示
                        Icon(
                            imageVector = Icons.Default.Search,
                            contentDescription = "検索",
                            tint = Color.Gray
                        )
                    },
                    colors = TextFieldDefaults.outlinedTextFieldColors(
                        focusedBorderColor = Color.Transparent,
                        unfocusedBorderColor = Color.Transparent
                    )
                )
            }

            // 現在のタスク
            item {
                // 現在の予定ヘッダー
                Surface(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(8.dp))
                        .background(Color(0xFFEEEEEE))
                        .clickable { CreateTask = true }
                        .padding(8.dp),
                    color = Color(0xFFEEEEEE)
                ) {
                    // ヘッダーの内容
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "現在の予定",
                            fontSize = 18.sp
                        )
                        // プラスアイコン
                        IconButton(
                            onClick = { CreateTask = true }
                        ) {
                            Icon(
                                imageVector = Icons.Default.Add,
                                contentDescription = "追加",
                                tint = Color(0xFFFF9800) // オレンジ色
                            )
                        }
                    }
                }

                // 区切り線
                Divider(
                    modifier = Modifier.padding(vertical = 8.dp),
                    color = Color.Gray.copy(alpha = 0.3f)
                )

                // タスクリストの表示
                ShowTasks(
                    tasks = tasks,
                    tags = tags,
                    selectedTasks = selectedTasks,
                    isTagSelectionActive = isTagSelectionActive,
                    onTaskToggle = { taskId ->
                        selectedTasks = if (selectedTasks.contains(taskId)) {
                            selectedTasks.filter { it != taskId }
                        } else {
                            selectedTasks + taskId
                        }
                    },
                    nav = navController
                )


            }

            // タグ一覧セクション
            item {
                Spacer(modifier = Modifier.height(16.dp))

                // タグ一覧ヘッダー（クリック可能な灰色の枠）
                Surface(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(8.dp))
                        .background(Color(0xFFEEEEEE))
                        .clickable { tagDialogOpen = true }
                        .padding(8.dp),
                    color = Color(0xFFEEEEEE)
                ) {
                    // ヘッダーの内容（タイトルとプラスボタン）
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "タグ一覧",
                            fontSize = 18.sp
                        )
                        // プラスボタン
                        IconButton(
                            onClick = { tagDialogOpen = true }
                        ) {
                            Icon(
                                imageVector = Icons.Default.Add,
                                contentDescription = "追加",
                                tint = Color(0xFFFF9800) // オレンジ色
                            )
                        }
                    }
                }

                // 区切り線
                Divider(
                    modifier = Modifier.padding(vertical = 8.dp),
                    color = Color.Gray.copy(alpha = 0.3f)
                )

                // タグリスト
                ShowTags(
                    tags = tags,
                    selectedTags = selectedTags,
                    isTaskSelectionActive = isTaskSelectionActive,
                    onTagToggle = { tagId ->
                        selectedTags = if (selectedTags.contains(tagId)) {
                            selectedTags.filter { it != tagId }
                        } else {
                            selectedTags + tagId
                        }
                    },
                    nav = navController
                )

            }
        }

        // アクションボタン（選択時のみ表示）UI作成後に取り組み予定
    }
}
