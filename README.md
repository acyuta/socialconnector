Social Auth Wrapper for Android
=======================

[![](https://jitpack.io/v/acyuta/socialconnector.svg)](https://jitpack.io/#acyuta/socialconnector)

# Available Socials
+ Vk.com
+ Facebook native
+ Facebook Account Kit (as phone)


# Create values for FB authorization
For example values/secrets.xml
```
<?xml version="1.0" encoding="utf-8"?>
<resources>
    <!-- only for VK.com -->
    <integer name="com_vk_sdk_AppId">1231231</integer>
    <!-- only for fb.com -->
    <string name="fb_login_protocol_scheme" translatable="false">fb123123123123123</string>
    <!-- both for fb.com  and fb Account Kit -->
    <string name="facebook_app_id" translatable="false">123123123123123</string>
    <string name="account_kit_client_token" translatable="false">321asd321asd5648asd4as86d21</string>
</resources>
```
# Add to Manifest
```
<!-- Permissions ->>
<uses-permission android:name="android.permission.INTERNET" />

...
        <!-- vk -->
        <activity
            android:name="com.vk.sdk.VKServiceActivity"
            android:label="ServiceActivity"
            android:theme="@style/VK.Transparent" />
        <!-- vk end -->

    <!-- Facebook Values -->
        <meta-data
            android:name="com.facebook.accountkit.ApplicationName"
            android:value="@string/app_name" />

        <meta-data
            android:name="com.facebook.sdk.ApplicationId"
            android:value="@string/facebook_app_id" />

        <meta-data
            android:name="com.facebook.accountkit.ClientToken"
            android:value="@string/account_kit_client_token" />

        <meta-data
            android:name="com.facebook.accountkit.FacebookAppEventsEnabled"
            android:value="false" />

        <activity
            android:name="com.facebook.FacebookActivity"
            android:configChanges="keyboard|keyboardHidden|screenLayout|screenSize|orientation"
            android:label="@string/app_name" />
        <activity
            android:name="com.facebook.CustomTabActivity"
            android:exported="true">
            <intent-filter>
                <action android:name="android.intent.action.VIEW" />
                <category android:name="android.intent.category.DEFAULT" />
                <category android:name="android.intent.category.BROWSABLE" />

                <data android:scheme="@string/fb_login_protocol_scheme" />
            </intent-filter>
        </activity>
        <!-- End Facebook Values -->
```
