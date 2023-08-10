package com.overeasy.simplemusicplayer.modules.permission

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.provider.Settings
import androidx.core.content.ContextCompat
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.listener.multi.BaseMultiplePermissionsListener
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import com.overeasy.simplemusicplayer.modules.permission.listener.PermissionListener

abstract class PermissionHelper(
    private val context: Context
) {
    abstract val permissions: List<String>

    fun requestPermission(listener: PermissionListener) {
        if (!isAllGrantedPermissions(permissions)) {
            requestPermissions(permissions, listener)
        } else {
            if (getNotGrantedPermissions(permissions).isNotEmpty()) {
                requestPermissions(getNotGrantedPermissions(permissions), listener)
            } else return
        }
    }

    fun openPermissionSettings() {
        context.startActivity(Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
            data = Uri.fromParts("package", context.packageName, null)
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        })
    }

    private fun isAllGrantedPermissions(permissions: List<String>): Boolean {
        return permissions.all {
            ContextCompat.checkSelfPermission(context, it) != PackageManager.PERMISSION_DENIED
        }
    }

    private fun getNotGrantedPermissions(permissions: List<String>): List<String> =
        permissions.filter {
            ContextCompat.checkSelfPermission(context, it) == PackageManager.PERMISSION_DENIED
        }

    private fun requestPermissions(
        permissions: Collection<String>,
        listener: PermissionListener
    ) {
        val multiplePermissionsListenerImpl: MultiplePermissionsListener =
            object : BaseMultiplePermissionsListener() {
                override fun onPermissionsChecked(report: MultiplePermissionsReport) {

                    val deniedPermissions = report.deniedPermissionResponses
                        .map { it.permissionName }
                    val permanentlyDeniedPermissions = report.deniedPermissionResponses
                        .filter { it.isPermanentlyDenied }
                        .map { it.permissionName }

                    when {
                        // 모든 권한이 허가
                        report.areAllPermissionsGranted() -> {
                            listener.onPermissionGranted()
                        }
                        // 권한 중에 영구적으로 거부된 권한
                        report.isAnyPermissionPermanentlyDenied -> {
                            listener.onAnyPermissionsPermanentlyDenied(
                                deniedPermissions,
                                permanentlyDeniedPermissions
                            )
                        }
                        // 권한 중에 거부된 권한이 존재한다면
                        else -> {
                            listener.onPermissionShouldBeGranted(deniedPermissions)
                        }
                    }
                    super.onPermissionsChecked(report)
                }
            }

        Dexter.withContext(context)
            .withPermissions(permissions)
            .withListener(multiplePermissionsListenerImpl)
            .check()
    }
}