/**
 * 制作実習Ⅱ 第一期個人制作
 * @author 0J01028 中村蒼
 */

package com.example.todo_app

import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel

// 作成画面
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Create_Screen(navController: NavController, viewModel: TaskViewModel = viewModel()) {
    // タスク情報とタグ情報の読み込み
    val tasks by viewModel.tasks
    val tags by viewModel.tags

    // タスク名の入力値
    var task_in by remember { mutableStateOf("") }

    // タグの入力値
    var tag_in by remember { mutableStateOf("") }

    // 日付の入力値
    var date_in by remember { mutableStateOf("") }

    // 開始時刻の入力値
    var starttime_in by remember { mutableStateOf("") }

    // 終了時刻の入力値
    var endtime_in by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        // トップバー
        TopBar(navController)

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

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 18.dp) // 左右のスペース
        ){
            // タスク名
            InputField(
                section = "予定",
                value = task_in,
                onValueChange = { task_in = it }
            )

            // タグ名
            InputField(
                section = "タグ",
                value = tag_in,
                onValueChange = { tag_in = it }
            )

            // 日付
            InputField(
                section = "日付",
                value = date_in,
                onValueChange = { date_in = it }
            )

            // 時間
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)   // テキストとフィールドの間のスペース
            ) {
                Spacer(modifier = Modifier.height(16.dp))     // 位置調整スペース

                // 入力項目名の表示
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = "時間",    // 入力項目
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Normal
                    )
                }

                // 入力フィールド
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // 開始時間の入力フィールド
                    TimeInputField(
                        value = starttime_in,
                        onValueChange = { starttime_in = it },
                        modifier = Modifier.weight(1f)
                    )

                    // 区切り文字"～"の表示
                    Text(
                        text = "〜",
                        fontSize = 14.sp,
                        modifier = Modifier.padding(horizontal = 18.dp)
                    )

                    // 終了時間の入力フィールド
                    TimeInputField(
                        value = endtime_in,
                        onValueChange = { endtime_in = it },
                        modifier = Modifier.weight(1f)
                    )
                }
            }

            // 作成ボタン
            Button(
                onClick = {     // タスクの作成処理
                    val newTask = Task(
                        id = tasks.size.toString(),
                        title = task_in,
                        date = date_in,
                        time = "$starttime_in〜$endtime_in",
                        tag = 0 // 仮
                    )

                    // タスクの追加
                    viewModel.addTask(newTask)

                    // 前の画面に遷移
                    navController.popBackStack()
                },
                modifier = Modifier
                    .padding(top = 40.dp)
                    .align(Alignment.End),      // 右端に配置
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF4285F4)
                )
            ) {
                // ボタンの表示文字
                Text(
                    text = "作成",
                    fontSize = 18.sp,
                    color = Color.White,
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 4.dp)
                )
            }
        }

    }

}

// 項目と入力フィールドの表示(時間以外)
@Composable
fun InputField(
    section: String,        // 入力項目
    value: String,      // 入力値
    onValueChange: (String) -> Unit
) {

    Column(
        verticalArrangement = Arrangement.spacedBy(8.dp),   // テキストとフィールドの間のスペース
        modifier = Modifier
            .fillMaxWidth()
    ) {
        Spacer(modifier = Modifier.height(16.dp))     // 位置調整スペース

        // 入力項目名の表示
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = section,     // 入力項目
                fontSize = 14.sp,
                fontWeight = FontWeight.Normal
            )
        }

        // 入力フィールド
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    color = Color(0xFFF5F5F5),
                    shape = RoundedCornerShape(4.dp)        // 枠の角を丸く
                )
                .padding(14.dp)      // 内側にスペース
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                BasicTextField(
                    value = value,      // 入力値
                    onValueChange = onValueChange,
                    modifier = Modifier.fillMaxWidth(),
                    textStyle = TextStyle(
                        fontSize = 16.sp
                    )
                )
            }
        }

    }
}

// 短めの入力フィールドの表示(時間用)
@Composable
fun TimeInputField(
    value: String,      // 入力値
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .background(
                color = Color(0xFFF5F5F5),
                shape = RoundedCornerShape(4.dp)        // 枠の角を丸く
            )
            .padding(14.dp)      // 内側にスペース
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            BasicTextField(
                value = value,      // 入力値
                onValueChange = onValueChange,
                modifier = Modifier.fillMaxWidth(),
                textStyle = TextStyle(
                    fontSize = 16.sp
                )
            )
        }
    }
}


