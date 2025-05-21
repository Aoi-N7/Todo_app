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
import android.util.Log

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
        // リストへの追加
        _tasks.value = _tasks.value + task

        // ファイルの更新
        SaveFile(context, _tags.value, _tasks.value)
    }

    // タグ追加
    fun addTag(tag: Tag) {
        _tags.value = _tags.value + tag
    }
}

// タスク情報やタグ情報の保存(内部ストレージにテキストファイル形式で)
fun SaveFile(context: Context, tags: List<Tag>, allTasks: List<Task>) {
    // ログ用のタグ名
    val TAG = "SaveTask"

    try {
        // 内部ストレージに "Todo_app_tasks" ディレクトリを作成（ない場合）
        val dir = File(context.filesDir, "Todo_app_tasks")
        if (!dir.exists()) {
            val created = dir.mkdirs()
            Log.d(TAG, if (created) "tasks ディレクトリを作成しました" else "tasks ディレクトリの作成に失敗しました")
        }

        // "tags.txt"を上書き保存（タグ一覧を常に最新に保つ）
        val tagsFile = File(dir, "tags.txt")
        tagsFile.printWriter().use { out ->
            tags.forEach {
                out.println("${it.id},${it.name}")
            }
        }
        Log.d(TAG, "tags.txt を上書き保存しました（${tags.size} 件）")

        // 各タグごとに対応するタスクを取り出し、個別ファイルに保存("tag_0.txt"的な)
        tags.forEach { tag ->
            // タグIDが一致するタスクの取り出し
            val tasksWithSameTag = allTasks.filter { it.tag.toString() == tag.id }

            // タグIDに対応するファイル名を作成("tag_1.txt"的な)
            val tagFile = File(dir, "tag_${tag.id}.txt")

            // 該当タスクをファイルに上書き保存(CSV形式)
            tagFile.printWriter().use { out ->
                tasksWithSameTag.forEach {
                    out.println("${it.id},${it.title},${it.date},${it.time},${it.tag}")
                }
            }

            // 保存完了ログ
            Log.d(TAG, "tag_${tag.id}.txt にタスクを保存しました（${tasksWithSameTag.size} 件）")
        }

    } catch (e: Exception) {
        // 例外が発生した場合のエラーログ
        Log.e(TAG, "ファイル保存中にエラーが発生しました: ${e.message}")
    }
}




