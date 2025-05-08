/**
 * 制作実習Ⅱ 第一期個人制作
 * @author 0J01028 中村蒼
 */

package com.example.todo_app

// タグデータ
data class Tag(
    val id: String,     // id
    val name: String,    // タグ名
    val color: androidx.compose.ui.graphics.Color
)