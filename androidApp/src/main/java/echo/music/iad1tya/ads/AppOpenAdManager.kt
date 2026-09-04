package echo.music.iad1tya.ads

import android.app.Activity
import android.app.Application
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import com.google.android.gms.ads.AdError
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.FullScreenContentCallback
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.appopen.AppOpenAd
import java.util.Date

class AppOpenAdManager(
    private val application: Application,
    private val adUnitId: String,
) : Application.ActivityLifecycleCallbacks,
    DefaultLifecycleObserver {

    private var appOpenAd: AppOpenAd? = null
    private var isLoadingAd = false
    private var isShowingAd = false
    private var loadTime: Long = 0
    private var currentActivity: Activity? = null

    private fun wasLoadTimeLessThanFourHoursAgo(): Boolean {
        val dateDifference = Date().time - loadTime
        val numMilliSecondsPerHour = 3600000
        return dateDifference < numMilliSecondsPerHour * 4
    }

    private fun isAdAvailable(): Boolean = appOpenAd != null && wasLoadTimeLessThanFourHoursAgo()

    fun loadAd() {
        if (isLoadingAd || isAdAvailable()) return
        isLoadingAd = true

        val request = AdRequest.Builder().build()
        AppOpenAd.load(
            application,
            adUnitId,
            request,
            object : AppOpenAd.AppOpenAdLoadCallback() {
                override fun onAdLoaded(ad: AppOpenAd) {
                    appOpenAd = ad
                    isLoadingAd = false
                    loadTime = Date().time
                }

                override fun onAdFailedToLoad(loadAdError: LoadAdError) {
                    isLoadingAd = false
                    Log.w("AppOpenAdManager", "Ad failed to load: ${loadAdError.message}")
                }
            },
        )
    }

    fun showAdIfAvailable() {
        if (isShowingAd) {
            return
        }
        if (!isAdAvailable()) {
            loadAd()
            return
        }

        val activity = currentActivity ?: return

        appOpenAd?.fullScreenContentCallback =
            object : FullScreenContentCallback() {
                override fun onAdDismissedFullScreenContent() {
                    appOpenAd = null
                    isShowingAd = false
                    loadAd()
                }

                override fun onAdFailedToShowFullScreenContent(adError: AdError) {
                    appOpenAd = null
                    isShowingAd = false
                    loadAd()
                }

                override fun onAdShowedFullScreenContent() {
                    isShowingAd = true
                }
            }

        appOpenAd?.show(activity)
    }

    override fun onStart(owner: LifecycleOwner) {
        showAdIfAvailable()
    }

    override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {}
    override fun onActivityStarted(activity: Activity) { currentActivity = activity }
    override fun onActivityResumed(activity: Activity) { currentActivity = activity }
    override fun onActivityPaused(activity: Activity) {}
    override fun onActivityStopped(activity: Activity) {}
    override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {}
    override fun onActivityDestroyed(activity: Activity) {
        if (currentActivity === activity) currentActivity = null
    }

    companion object {
        fun attach(application: Application, adUnitId: String): AppOpenAdManager {
            val manager = AppOpenAdManager(application, adUnitId)
            application.registerActivityLifecycleCallbacks(manager)
            androidx.lifecycle.ProcessLifecycleOwner.get().lifecycle.addObserver(manager)
            manager.loadAd()
            return manager
        }
    }
}
