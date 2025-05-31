/**
 * 制作実習Ⅱ 第一期個人制作
 * @author 0J01028 中村蒼
 */

package com.example.todo_app

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import androidx.compose.foundation.Image
import androidx.compose.material3.Text
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel


// タスク一覧画面
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TaskList_Screen(navController: NavController, viewModel: TaskViewModel, id: Int? = null) {
    // タスク情報とタグ情報の読み込み
    val tasks by viewModel.tasks
    val tags by viewModel.tags

    // idと一致するタスクを取得
    val print = tasks.filter { it.tag == id }

    // 選択されたタスクのIDリスト
    var selectedTasks by remember { mutableStateOf(listOf<Int>()) }

    // 選択されたタグのIDリスト
    var selectedTags by remember { mutableStateOf(listOf<Int>()) }

    // チェックボックスでの選択
    val isSelectionActive = selectedTasks.isNotEmpty() || selectedTags.isNotEmpty()
    val isTaskSelectionActive = selectedTasks.isNotEmpty()
    val isTagSelectionActive = selectedTags.isNotEmpty()

    // 表示するタスクの状態(true:完了、false:未完了)
    var showState by remember { mutableStateOf(false) }

    // id に対応するタグ名を取得
    val currentTagName = tags.find { it.id == id }?.name ?: "タグなし"

    // 現在のコンテキストを取得
    val context = LocalContext.current

    // 更新
    viewModel.loadFromStorage(context)

    // 画面レイアウト
    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            // トップバーの表示
            TopBar(navController)

            // 上半分: ヘッダーとタイトル
            Box(
                modifier = Modifier
                    .height(200.dp)  // 高さを短く設定
                    .fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color(0xFFF5F5F5)),  // 上半分の背景
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    // タグ名
                    Text(
                        text = currentTagName,
                        color = Color.Black,
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center
                    )
                }

                // 右上に「完了タスク」とスイッチを配置
                Column(
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(16.dp),
                    horizontalAlignment = Alignment.End
                ) {
                    Text(
                        text = if (showState == false) "未完了タスク" else "完了タスク",  // showStateがtrueの場合は"未完了"、showStateがfalseの場合は"完了",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Medium,
                        color = Color.Gray
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    Switch(
                        checked = showState,
                        onCheckedChange = { showState = it },
                        modifier = Modifier.scale(0.50f)    // スイッチの大きさ
                    )
                }

                // 左上に戻るボタン
                Column(
                    modifier = Modifier
                        .align(Alignment.TopStart),
                    horizontalAlignment = Alignment.End
                ) {
                    // 戻るボタン
                    IconButton(
                        onClick = { // 前の画面に遷移
                            navController.popBackStack()
                        },
                        modifier = Modifier
                            .padding(start = 8.dp, top = 8.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "戻る",
                            tint = Color.Gray
                        )
                    }
                }
            }

            // 下半分: タスク一覧
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color(0xFFFDEFF2))
            ) {
                // スクロール可能
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(start = 15.dp, end = 15.dp)        // 左右にスペース
                ) {
                    item{
                        // タスクリストの表示
                        ShowTasks(
                            tasks = print,
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
                            showState = showState,
                            nav = navController
                        )
                    }
                }
            }
        }

        // アクションボタン（選択時のみ表示）
        ActionButtons(
            isSelectionActive = isSelectionActive,
            selectedTasks = selectedTasks,
            selectedTags = selectedTags,
            showState = showState,
            onDelete = {
                //タスクやタグの削除処理
                viewModel.deleteItems(context, selectedTasks, selectedTags)

                // 選択中のタスクやタグのリセット
                selectedTasks = emptyList()
                selectedTags = emptyList()
            },
            onComplete = {
                if (selectedTasks.isNotEmpty()) {
                    // 選択されたタスクの状態を反転させて newTasks に格納
                    val newTasks = tasks.filter { selectedTasks.contains(it.id) }
                        .map { task ->
                            task.copy(state = !task.state)
                        }

                    // タスクの状態を反転してないタスクの削除処理
                    viewModel.deleteItems(context, selectedTasks, selectedTags)

                    // タスクの状態を反転させたタスクを代わりに追加
                    viewModel.addTasks(context, newTasks)

                    // 選択中のタスクやタグのリセット
                    selectedTasks = emptyList()
                    selectedTags = emptyList()
                }
            },
            modifier = Modifier
        )
    }
}
