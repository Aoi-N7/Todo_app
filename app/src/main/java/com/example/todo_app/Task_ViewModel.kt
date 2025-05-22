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
            Task(id = 0, title = "タスク名", date = "1月1日", time = "00:00", tag = 0, state = false),
        )
    )
    val tasks: State<List<Task>> get() = _tasks

    // タグ情報
    private val _tags = mutableStateOf(
        listOf(
            Tag(id = 0, name = "仕事", color = Color(0xFFE3F2FD)),      // 少し薄めの色
            Tag(id = 1, name = "プライベート", color = Color(0xFFE8F5E9))      // 少し薄めの色
        )
    )
    val tags: State<List<Tag>> get() = _tags

    // データの更新(ファイルから読み込み)
    fun loadFromStorage(context: Context) {
        val (loadedTags, loadedTasks) = LoadFiles(context)
        _tags.value = loadedTags
        _tasks.value = loadedTasks
    }

    // タスク追加
    fun addTask(context: Context, task: Task) {
        // リストへの追加
        _tasks.value = _tasks.value + task

        // ファイルの更新
        SaveFile(context, _tags.value, _tasks.value)
    }

    // タグ追加
    fun addTag(context: Context, tag: Tag) {
        // リストへの追加
        _tags.value = _tags.value + tag

        // ファイルの更新
        SaveFile(context, _tags.value, _tasks.value)
    }

    fun deleteItems(context: Context, taskIds: List<Int>, tagIds: List<Int>) {
        // タスクの削除(選択されていないタスクだけを残す)
        _tasks.value = _tasks.value.filterNot { it.id in taskIds }

        // タグの削除(選択されていないタグだけを残す)
        _tags.value = _tags.value.filterNot { it.id in tagIds }

        // ファイルを更新（削除後の状態で保存）
        SaveFile(context, _tags.value, _tasks.value)
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
                out.println("${it.id},${it.name},${it.color.value.toString(16)}")
            }
        }
        Log.d(TAG, "tags.txt を上書き保存しました（${tags.size} 件）")

        // 各タグごとに対応するタスクを取り出し、個別ファイルに保存("tag_0.txt"的な)
        tags.forEach { tag ->
            // タグIDが一致するタスクの取り出し
            val tasksWithSameTag = allTasks.filter { it.tag == tag.id }

            // タグIDに対応するファイル名を作成("tag_1.txt"的な)
            val tagFile = File(dir, "tag_${tag.id}.txt")

            // 該当タスクをファイルに上書き保存(CSV形式)
            tagFile.printWriter().use { out ->
                tasksWithSameTag.forEach {
                    out.println("${it.id},${it.title},${it.date},${it.time},${it.tag},${it.state}")
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

// 保存されたファイルからタスク情報とタグ情報のリストを読み込む関数
fun LoadFiles(context: Context): Pair<List<Tag>, List<Task>> {
    // ログ用のタグ名
    val TAG = "LoaTask"

    // 読み込み先のディレクトリ
    val dir = File(context.filesDir, "Todo_app_tasks")

    // 読み込んだ情報をタスクとタグごとに格納するリスト
    val tags = mutableListOf<Tag>()
    val tasks = mutableListOf<Task>()

    try {
        // タグ一覧ファイル "tags.txt" を読み込む
        val tagsFile = File(dir, "tags.txt")
        if (tagsFile.exists()) {
            tagsFile.forEachLine { line ->
                val parts = line.split(",") // 1行をカンマで分割（形式: id,name）
                if (parts.size >= 2) {
                    val id = parts[0].toInt()   // ID
                    val name = parts[1]     // タグ名
                    // カラー情報
                    val colorHex = parts[2]
                    val color = Color(colorHex.toULong(16))

                    // タグリストへの追加
                    tags.add(Tag(id = id, name = name, color = color))
                }
            }
            Log.d(TAG, "tags.txt を読み込みました（${tags.size} 件）")
        } else {
            Log.w(TAG, "tags.txt が存在しません")
        }

        // 2. 各タグに対応するタスクファイル "tag_{id}.txt" を読み込む
        tags.forEach { tag ->
            val tagFile = File(dir, "tag_${tag.id}.txt")
            if (tagFile.exists()) {
                // タグファイルが存在する場合
                tagFile.forEachLine { line ->
                    val parts = line.split(",") // 1行をカンマで分割
                    if (parts.size >= 5) {
                        val task = Task(
                            id = parts[0].toInt(),  // ID
                            title = parts[1],   // タイトル
                            date = parts[2],    // 日付
                            time = parts[3],    // 時刻
                            tag = parts[4].toInt(), // タグID(Int 型に変換)
                            state = parts[5].toBoolean()    // タスクの状態
                        )

                        // タスクリストへの追加
                        tasks.add(task)
                    }
                }
                Log.d(TAG, "tag_${tag.id}.txt を読み込みました（${tasks.count { it.tag == tag.id }} 件）")
            } else {
                // タグファイルが存在しない場合
                Log.w(TAG, "tag_${tag.id}.txt が存在しません")
            }
        }

    } catch (e: Exception) {
        // 例外が発生した場合はエラーログを出力
        Log.e(TAG, "ファイル読み込み中にエラーが発生しました: ${e.message}")
    }

    // 読み込んだタグとタスクのリストを返す
    return Pair(tags, tasks)
}




