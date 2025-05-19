/**
 * 制作実習Ⅱ 第一期個人制作
 * @author 0J01028 中村蒼
 */

package com.example.todo_app

import android.content.Context
import androidx.compose.runtime.*
import androidx.lifecycle.ViewModel
import androidx.compose.ui.graphics.Color
import java.io.File

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
    fun addTask(context: Context, task: Task) {
        _tasks.value = _tasks.value + task
    }

    // タグ追加
    fun addTag(tag: Tag) {
        _tags.value = _tags.value + tag
    }
}

// テキストファイルへの保存関数
fun SaveFile(context: Context, task: Task, tags: List<Tag>, allTasks: List<Task>) {
    // 対象のタグを取得（task.tag は Int、tag.id は String なので比較のために変換）
    val tag = tags.find { it.id == task.tag.toString() } ?: return

    // 1. 内部ストレージの "tasks" ディレクトリを取得・作成
    val dir = File(context.filesDir, "tasks")
    if (!dir.exists()) {
        // ディレクトリが存在しない場合は作成
        dir.mkdirs()
    }

    // 2. "tags.txt" ファイルを作成（存在しない場合のみ）
    val tagsFile = File(dir, "tags.txt")
    if (!tagsFile.exists()) {
        tagsFile.printWriter().use { out ->
            // 各タグの ID と名前を 1 行ずつ書き込む（例: 0,仕事）
            tags.forEach {
                out.println("${it.id},${it.name}")
            }
        }
    }

    // 3. 同じタグ ID を持つタスクをすべて取得
    val tasksWithSameTag = allTasks.filter { it.tag.toString() == tag.id }

    // 4. "tag_{id}.txt" ファイルにタスクを上書き保存
    val tagFile = File(dir, "tag_${tag.id}.txt")
    tagFile.printWriter().use { out ->
        // 各タスクを 1 行ずつ CSV 形式で書き込む（例: 1,買い物,4月1日,12:00,2）
        tasksWithSameTag.forEach {
            out.println("${it.id},${it.title},${it.date},${it.time},${it.tag}")
        }
    }
}
