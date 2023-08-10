package com.overeasy.simplemusicplayer.modules.permission.listener

interface PermissionListener {
    /**
     * 모든 권한 허용
     */
    fun onPermissionGranted()

    /**
     * 일부 권한이 거부되었다.
     *
     * @param deniedPermission 거부된 권한 목록
     */
    fun onPermissionShouldBeGranted(deniedPermission: List<String>)

    /**
     * 일부 권한이 영구적으로 거부되었다.
     *
     * @param deniedPermissions 거부된 권한 목록
     * @param permanentlyDeniedPermissions 영구적으로 거부된 권한 목록
     */
    fun onAnyPermissionsPermanentlyDenied(
        deniedPermissions: List<String>,
        permanentlyDeniedPermissions: List<String>
    )
}