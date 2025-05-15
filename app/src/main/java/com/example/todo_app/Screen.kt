/**
 * 制作実習Ⅱ 第一期個人制作
 * @author 0J01028 中村蒼
 */

package com.example.todo_app

import androidx.compose.foundation.background
import androidx.compose.material3.*
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
//import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController

// 画面遷移先
enum class Screen {
    Home_Screen,    // ホーム画面
    Create_Screen,    // 編集画面
    TaskList_Screen,    //音投稿準備画面
}

//画面遷移の設定　どこのページへ移動するかnavControllerに定義する
@Composable
fun NavRoute(){
    // NavControllerを定義
    val navController = rememberNavController()

    //NavHostの作成,設定
    NavHost(navController = navController,
        // 最初に表示するページ
        startDestination = Screen.Home_Screen.name
    ) {
        // ルート名：Home_Screen　ホーム画面に遷移
        composable(route = Screen.Home_Screen.name) {
            Home_Screen(navController = navController)
        }

        // ルート名：Create_Screen　作成画面に遷移
        composable(route = Screen.Create_Screen.name) {
            Create_Screen(navController = navController)
        }

        // ルート名：TaskList_Screen　タスク一覧画面に遷移
        composable(route = Screen.TaskList_Screen.name) {
            TaskList_Screen(navController = navController)
        }

    }
}

// トップバー(タイトル)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBar(navController: NavController) {
    // トップバーの設定
    TopAppBar(
        title = { Text("Todoアプリ　＞　ホーム画面") },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = Color(0xFFFEF4F4) // 背景色を#FEF4F4に設定
        )
    )
}

