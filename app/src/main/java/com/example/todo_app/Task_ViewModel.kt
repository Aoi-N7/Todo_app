/**
 * 制作実習Ⅱ 第一期個人制作
 * @author 0J01028 中村蒼
 */

package com.example.todo_app

import androidx.compose.runtime.*
import androidx.lifecycle.ViewModel
import androidx.compose.ui.graphics.Color

// アプリ全体で共有したいリストをViewModelを使って管理
class TaskViewModel : ViewModel() {

    // タスク情報
    private val _tasks = mutableStateOf(
        listOf(
            Task(id = "0", title = "タスク名", date = "1月1日", time = "10:21", tag = 1),
            Task(id = "1", title = "誕生日プレゼント決め", date = "4月1日", time = "12:00", tag = 2)
        )
    )
    val tasks: State<List<Task>> get() = _tasks

    // タグ情報
    private val _tags = mutableStateOf(
        listOf(
            Tag(id = "0", name = "仕事", color = Color(0xFFE3F2FD)),
            Tag(id = "1", name = "プライベート", color = Color(0xFFE8F5E9)),
            Tag(id = "2", name = "自治会", color = Color(0xFFF8BBD0))
        )
    )
    val tags: State<List<Tag>> get() = _tags

    // タスク追加
    fun addTask(task: Task) {
        _tasks.value = _tasks.value + task
    }

    // タグ追加
    fun addTag(tag: Tag) {
        _tags.value = _tags.value + tag
    }
}
