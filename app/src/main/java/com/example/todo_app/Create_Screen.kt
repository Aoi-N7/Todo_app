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


// 作成,編集画面
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Create_Screen(navController: NavController) {
    // タスク名の入力値
    var task_in by remember { mutableStateOf("") }

    // タグの入力値
    var tag_in by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        // トップバー
        TopBar(navController)

        // 戻るボタン
        IconButton(
            onClick = { navController.popBackStack() },
            modifier = Modifier
                .padding(start = 8.dp, top = 8.dp)
        ) {
            Icon(
                imageVector = Icons.Default.ArrowBack,
                contentDescription = "戻る",
                tint = Color.Gray
            )
        }

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

    }
}

// 項目と入力フィールドの表示
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
            .padding(horizontal = 18.dp) // 左右のスペース
    ) {
        // テキストの表示
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



