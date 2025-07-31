package ru.itis.t_trips

import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.CompositionLocalProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.compose.rememberNavController
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.EntryPointAccessors
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import ru.itis.t_trips.domain.localdatastorecontract.BasicUserDataStorage
import ru.itis.t_trips.navigation.ApplicationNavHost
import ru.itis.t_trips.navigation_api.LocalNavController
import ru.itis.t_trips.navigation_api.navigator.Navigator
import ru.itis.t_trips.navigation_api.route.AuthenticationPhoneNumberRoute
import ru.itis.t_trips.navigation_api.route.TripsListRoute
import ru.itis.t_trips.ui.theme.TTripsTheme
import ru.itis.t_trips.utils.LocaleHelper
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var navigator: Navigator

    @Inject
    lateinit var basicUserDataStorage: BasicUserDataStorage

    private var startDestination: Any = AuthenticationPhoneNumberRoute

    override fun attachBaseContext(newBase: Context) {
        val appContext = newBase.applicationContext

        val appLocale = runBlocking {
            val entryPoint = EntryPointAccessors.fromApplication(appContext, StorageEntryPoint::class.java)
            entryPoint.basicUserDataStorage().getAppLocale()
        }

        val updatedContext = appLocale?.let { lang ->
            LocaleHelper.updateLocaleWithCode(newBase, lang)
        } ?: newBase

        super.attachBaseContext(updatedContext)
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        if (savedInstanceState == null) {
            var phoneNum: String? = null
            lifecycleScope.launch {
                phoneNum = async {
                    basicUserDataStorage.getUserPhoneNumber()
                }.await()
                    phoneNum?.let {
                    startDestination = TripsListRoute
                }
            }
        }

        setContent {
            TTripsTheme {
                val navController = rememberNavController()
                navigator.setNavController(navController)

                CompositionLocalProvider(
                    LocalNavController provides navController
                ) {
                    ApplicationNavHost(
                        startDestination = startDestination,
                        navController = navController
                    )
                }
            }
        }
    }
}