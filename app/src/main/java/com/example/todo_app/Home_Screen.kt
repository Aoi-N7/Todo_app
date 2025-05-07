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
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.window.Dialog
import androidx.compose.animation.AnimatedVisibility


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Home_Screen(navController: NavController){
    // タスク作成ボタンのクリック判定
    var CreateTask by remember { mutableStateOf(false) }

    // タグ作成ボタンのクリック判定
    var CreateTag by remember { mutableStateOf(false) }

    // メイン画面のレイアウト
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF5F5F5)) // 背景色を薄いグレーに設定
    ) {
        TopBar(navController)

        // スクロール可能なコンテンツ
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

            // 現在の予定
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


            }
        }
    }
}