/**
 * 制作実習Ⅱ 第一期個人制作
 * @author 0J01028 中村蒼
 */

package com.example.todo_app

// タスクデータ
data class Task(
    val id: String,     // id
    val title: String,      // タイトル
    val date: String,       // 日付
    val time: String,       // 時刻
    val tag: Int     // タグ
)