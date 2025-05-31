/**
 * 制作実習Ⅱ 第一期個人制作
 * @author 0J01028 中村蒼
 */

package com.example.todo_app

import android.content.Context
import android.os.Build
import android.util.Log
import android.view.View
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import java.util.Calendar

// 作成画面・編集画面
@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Create_Screen(navController: NavController, viewModel: TaskViewModel = viewModel(), id: Int? = null) {
    // タスク情報とタグ情報の読み込み
    val tasks by viewModel.tasks
    val tags by viewModel.tags

    // タスクidからタスク情報を抜き取る
    val Edit_task = tasks.find { it.id == id }

    // タスク名の入力値
    var task_in by remember { mutableStateOf(Edit_task?.title ?: "") }

    // タグの入力値
    var tag_in by remember { mutableStateOf(Edit_task?.tag) }

    // 日付の入力値
    var date_in by remember { mutableStateOf(Edit_task?.date ?: "") }

    // 開始と終了時刻の入力値
    val timeParts = Edit_task?.time?.split("〜") ?: listOf("", "")
    var starttime_in by remember { mutableStateOf(timeParts.getOrNull(0) ?: "")}
    var endtime_in by remember { mutableStateOf(timeParts.getOrNull(1) ?: "") }

    // 選択されたタスクのIDリスト
    var selectedTasks by remember { mutableStateOf(listOf<Int>()) }

    // 選択されたタグのIDリスト
    var selectedTags by remember { mutableStateOf(listOf<Int>()) }

    // タグ選択用ドロップダウンの展開状態を管理
    var expanded by remember { mutableStateOf(false) }

    // 使用中のIDリスト
    val usedIds = tasks.map { it.id }.toSet()

    // 現在のコンテキストを取得
    val context = LocalContext.current

    // 更新
    viewModel.loadFromStorage(context)

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

            Spacer(modifier = Modifier.height(16.dp))     // 位置調整スペース

            // タグ名
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = "タグ名",     // 入力項目
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Normal
                )
            }
            // タグ名のボックス
            ExposedDropdownMenuBox(
                expanded = expanded,
                onExpandedChange = { expanded = !expanded } // クリックで展開/非展開を切り替え
            ) {
                // 選択中のタグ名を表示する読み取り専用のテキストフィールド
                TextField(
                    value = tags.find { it.id == tag_in }?.name ?: "", // 現在選択中のタグ名を表示
                    onValueChange = {},
                    readOnly = true,
                    trailingIcon = {
                        ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) // 展開アイコン
                    },
                    modifier = Modifier
                        .menuAnchor() // ドロップダウンの位置をこのフィールドに合わせる
                        .fillMaxWidth(),
                    colors = TextFieldDefaults.textFieldColors(
                        containerColor = Color(0xFFF5F5F5) // 背景色
                    )
                )

                // ドロップダウンメニューの中身
                ExposedDropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false } // 外側をクリックしたら閉じる
                ) {
                    // タグ一覧をメニューとして表示
                    tags.forEach { tag ->
                        DropdownMenuItem(
                            text = { Text(tag.name) }, // タグ名を表示
                            onClick = {
                                tag_in = tag.id // 選択されたタグのIDを格納
                                expanded = false // メニューを閉じる
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(tag.color) // タグの背景
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))     // 位置調整スペース

            // 日付
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = "日付",     // 入力項目
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Normal
                )
            }
            // 日付
            DateBox(
                date = date_in,
                onDateSelected = { date_in = it },
                context = context
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
                onClick = {
                    // 現在のタスクを削除
                    if(id != null){
                        selectedTasks = listOf(id)

                        //タスクやタグの削除処理
                        viewModel.deleteItems(context, selectedTasks, selectedTags)
                    }

                    // タスクの作成処理
                    val newTask = Task(
                        id = generateSequence(0) { it + 1 }.first { it !in usedIds },    // 未使用かつ最小のIDを使用
                        title = task_in,
                        date = date_in,
                        time = "$starttime_in〜$endtime_in",
                        tag = tag_in ?: 0,
                        state = false
                    )

                    // タスクの追加
                    viewModel.addTask(context, newTask)
                    Log.d("AddTask", "AddTask: $newTask")
                    Log.d("NowTask", "NowTask: $tasks")

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
                    text = if (id != null) "変更" else "作成",  // idがある場合は"変更"、idがない場合は"作成"
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

// 年月日の入力ボックス
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun DateBox(
    date: String,   // 現在選択されている日付（文字列形式）
    onDateSelected: (String) -> Unit,
    context: Context
) {
    // 今日の日付を取得（DatePickerDialog の初期値として使用）
    val calendar = Calendar.getInstance()
    val year = calendar.get(Calendar.YEAR)
    val month = calendar.get(Calendar.MONTH)
    val day = calendar.get(Calendar.DAY_OF_MONTH)

    // 日付選択ダイアログ
    val datePickerDialog = remember {
        android.app.DatePickerDialog(
            context,
            { _, selectedYear, selectedMonth, selectedDay ->
                // 日付が選択されたときの処理（YYYY/MM/DD形式)
                val selectedDate =
                    "%04d/%02d/%02d".format(selectedYear, selectedMonth + 1, selectedDay)
                onDateSelected(selectedDate)
            },
            year, month, day // 初期表示の日付（今日）
        )
    }

    // 日付表示用のボックス
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color(0xFFF5F5F5), shape = RoundedCornerShape(4.dp)) // 背景色と角丸
            .clickable { datePickerDialog.show() } // クリックでカレンダーを表示
            .padding(16.dp) // 内側の余白
    ) {
        // 横並びで日付テキストとアイコンを配置（今回はテキストのみ）
        Row(
            verticalAlignment = Alignment.CenterVertically, // 垂直方向の中央揃え
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = date, // 日付
                color = Color.Black
            )
        }
    }
}


