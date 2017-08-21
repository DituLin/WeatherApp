package atu.com.weatherapp.dcs

import android.text.TextUtils
import android.util.Log
import atu.com.dueroslib.oauth.api.IOauth
import atu.com.dueroslib.oauth.api.OauthPreferenceUtil
import atu.com.weatherapp.App
import atu.com.weatherapp.ui.activity.MainActivity
import org.jetbrains.anko.*

/**
 * Created by atu on 2017/8/19.
 */
class OauthImpl : IOauth {
    override fun getAccessToken(): String = OauthPreferenceUtil.getAccessToken(App.instance)

    override fun authorize() {
        App.instance.startActivity(App.instance.intentFor<MainActivity>().clearTask().newTask())
    }

    override fun isSessionValid(): Boolean {
        val accessToken: String? = accessToken
        val createTime = OauthPreferenceUtil.getCreateTime(App.instance)
        val expires = OauthPreferenceUtil.getExpires(App.instance) + createTime
        return !TextUtils.isEmpty(accessToken) && expires != 0L && System.currentTimeMillis() < expires

    }
}