package com.example.todo_app

// タスクデータ
data class Task(
    val id: String,     // id
    val title: String,      // タイトル
    val date: String,       // 日付
    val time: String,       // 時刻
    val tag: String? = null     // タグ
)