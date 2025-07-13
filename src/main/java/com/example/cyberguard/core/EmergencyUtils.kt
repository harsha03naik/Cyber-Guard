package com.example.cyberguard.core

import android.content.Context
import android.content.Intent
import android.net.Uri

object EmergencyUtils {
    const val CYBERCRIME_PORTAL_URL = "https://cybercrime.gov.in"
    const val CYBERCRIME_PORTAL_URL_HTTP = "http://cybercrime.gov.in"

    fun openCybercrimePortal(context: Context) {
        try {
            android.util.Log.d("EmergencyUtils", "Opening cybercrime portal: $CYBERCRIME_PORTAL_URL")
            var uri = Uri.parse(CYBERCRIME_PORTAL_URL)
            var intent = Intent(Intent.ACTION_VIEW, uri)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            intent.addCategory(Intent.CATEGORY_BROWSABLE)
            val packageManager = context.packageManager
            var resolveInfo = packageManager.resolveActivity(intent, 0)
            android.util.Log.d("EmergencyUtils", "HTTPS ResolveInfo: $resolveInfo")
            if (resolveInfo != null) {
                android.util.Log.d("EmergencyUtils", "Browser found, opening HTTPS URL")
                context.startActivity(intent)
                ToastUtils.showToast(context, "Opening Cybercrime.gov.in", "emergency_portal")
                return
            }
            android.util.Log.d("EmergencyUtils", "HTTPS failed, trying HTTP")
            uri = Uri.parse(CYBERCRIME_PORTAL_URL_HTTP)
            intent = Intent(Intent.ACTION_VIEW, uri)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            intent.addCategory(Intent.CATEGORY_BROWSABLE)
            resolveInfo = packageManager.resolveActivity(intent, 0)
            android.util.Log.d("EmergencyUtils", "HTTP ResolveInfo: $resolveInfo")
            if (resolveInfo != null) {
                android.util.Log.d("EmergencyUtils", "Browser found, opening HTTP URL")
                context.startActivity(intent)
                ToastUtils.showToast(context, "Opening Cybercrime.gov.in", "emergency_portal")
                return
            }
            android.util.Log.w("EmergencyUtils", "No browser found, trying alternative method")
            tryAlternativeBrowser(context, uri)
        } catch (e: Exception) {
            e.printStackTrace()
            android.util.Log.e("EmergencyUtils", "Error opening cybercrime portal", e)
            ToastUtils.showToast(context, "Error opening cybercrime portal", "emergency_error")
        }
    }

    fun tryAlternativeBrowser(context: Context, uri: Uri) {
        try {
            android.util.Log.d("EmergencyUtils", "Trying alternative browser method")
            try {
                val intent = Intent(Intent.ACTION_VIEW, uri)
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                context.startActivity(intent)
                android.util.Log.d("EmergencyUtils", "Alternative method 1 succeeded")
                ToastUtils.showToast(context, "Opening Cybercrime.gov.in", "emergency_portal")
                return
            } catch (e: Exception) {
                android.util.Log.w("EmergencyUtils", "Alternative method 1 failed", e)
            }
            val browserPackages = listOf(
                "com.android.chrome",
                "com.google.android.apps.chrome",
                "com.android.browser",
                "org.mozilla.firefox",
                "com.opera.browser"
            )
            for (packageName in browserPackages) {
                try {
                    val intent = Intent(Intent.ACTION_VIEW, uri)
                    intent.setPackage(packageName)
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                    context.startActivity(intent)
                    android.util.Log.d("EmergencyUtils", "Alternative method 2 succeeded with $packageName")
                    ToastUtils.showToast(context, "Opening Cybercrime.gov.in", "emergency_portal")
                    return
                } catch (e: Exception) {
                    android.util.Log.w("EmergencyUtils", "Failed with $packageName", e)
                }
            }
            try {
                val alternativeUrl = "http://www.cybercrime.gov.in"
                val alternativeUri = Uri.parse(alternativeUrl)
                val intent = Intent(Intent.ACTION_VIEW, alternativeUri)
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                context.startActivity(intent)
                android.util.Log.d("EmergencyUtils", "Alternative method 3 succeeded")
                ToastUtils.showToast(context, "Opening Cybercrime.gov.in", "emergency_portal")
                return
            } catch (e: Exception) {
                android.util.Log.w("EmergencyUtils", "Alternative method 3 failed", e)
            }
            android.util.Log.e("EmergencyUtils", "All methods failed")
            ToastUtils.showToast(context, "Please visit: $CYBERCRIME_PORTAL_URL", "emergency_error")
        } catch (e: Exception) {
            e.printStackTrace()
            android.util.Log.e("EmergencyUtils", "Alternative method failed", e)
            ToastUtils.showToast(context, "Please visit: $CYBERCRIME_PORTAL_URL", "emergency_error")
        }
    }
} 