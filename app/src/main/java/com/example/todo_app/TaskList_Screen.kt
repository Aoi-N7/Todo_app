package com.example.todo_app

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import androidx.compose.foundation.Image
import androidx.compose.material3.Text
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
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
                    .background(Color(0xFFFDEFF2)) // 背景色を薄いグレーに設定
            ) {
                Column {

                }
            }
        }
    }

}
