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
import androidx.compose.material3.*
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp


// ホーム画面
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TaskList_Screen(navController: NavController) {
    // 選択されたタスクのIDリスト
    var selectedTasks by remember { mutableStateOf(listOf<String>()) }

    // 選択されたタグのIDリスト
    var selectedTags by remember { mutableStateOf(listOf<String>()) }

    // チェックボックスでの選択
    val isSelectionActive = selectedTasks.isNotEmpty() || selectedTags.isNotEmpty()
    val isTaskSelectionActive = selectedTasks.isNotEmpty()
    val isTagSelectionActive = selectedTags.isNotEmpty()

    // 仮タスク
    var tasks by remember {
        mutableStateOf(
            listOf(
                Task(
                    id = "0",
                    title = "タスク名",
                    date = "1月1日",
                    time = "10:21",
                    tag = 1
                ),
                Task(
                    id = "1",
                    title = "誕生日プレゼント決め",
                    date = "4月1日",
                    time = "12:00",
                    tag = 2
                )
            )
        )
    }

    // 仮タグ
    var tags by remember {
        mutableStateOf(
            listOf(
                Tag(id = "0", name = "仕事", color = Color(0xFFE3F2FD)), // 薄い青色
                Tag(id = "1", name = "プライベート", color = Color(0xFFE8F5E9)), // 薄い緑色
                Tag(id = "2", name = "自治会", color = Color(0xFFF8BBD0)) // 薄いピンク色
            )
        )
    }

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
                        "タグ名",
                        color = Color.Black,
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center
                    )
                }
            }

            // 下半分: タスク一覧
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color(0xFFFDEFF2))
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(start = 15.dp, end = 15.dp)        // 左右にスペース
                ) {
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
                        }
                    )
                }
            }
        }
    }

}
