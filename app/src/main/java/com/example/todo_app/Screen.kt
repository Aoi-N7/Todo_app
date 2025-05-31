/**
 * 制作実習Ⅱ 第一期個人制作
 * @author 0J01028 中村蒼
 */

package com.example.todo_app

import android.os.Build
import androidx.annotation.RequiresApi
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
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.navArgument

// 画面遷移先
enum class Screen {
    Home_Screen,    // ホーム画面
    Create_Screen,    // 編集画面
    TaskList_Screen,    //音投稿準備画面
}

//画面遷移の設定　どこのページへ移動するかnavControllerに定義する
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun NavRoute(){
    // NavControllerを定義
    val navController = rememberNavController()

    // ViewModelを定義
    val taskViewModel: TaskViewModel = viewModel()

    //NavHostの作成,設定
    NavHost(navController = navController,
        // 最初に表示するページ
        startDestination = Screen.Home_Screen.name
    ) {
        // ルート名：Home_Screen　ホーム画面に遷移
        composable(route = Screen.Home_Screen.name) {
            Home_Screen(navController, taskViewModel)
        }

        // ルート名：Create_Screen　作成画面に遷移(引数なし)
        composable("Create_Screen") {
            Create_Screen(navController, taskViewModel)
        }

        // ルート名：Create_Screen　作成画面に遷移(引数あり)
        composable(
            route = "Create_Screen/{id}",
            arguments = listOf(navArgument("id") { type = NavType.IntType })
        ) { backStackEntry ->
            val id = backStackEntry.arguments?.getInt("id")
            Create_Screen(navController, taskViewModel, id)
        }


        // ルート名：TaskList_Screen　タスク一覧画面に遷移
        composable(
            route = "TaskList_Screen/{id}",
            arguments = listOf(navArgument("id") { type = NavType.IntType })
        ) { backStackEntry ->
            val id = backStackEntry.arguments?.getInt("id")
            TaskList_Screen(navController, taskViewModel, id)
        }


    }
}

// トップバー(タイトル)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBar(navController: NavController) {
    // トップバーの設定
    TopAppBar(
        title = { Text("Todoアプリ") },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = Color(0xFFFEF4F4) // 背景色を#FEF4F4に設定
        )
    )
}

