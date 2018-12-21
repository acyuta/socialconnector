package kim.glushkova.socialconnector;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;

import com.vk.sdk.VKAccessToken;
import com.vk.sdk.VKCallback;
import com.vk.sdk.VKSdk;
import com.vk.sdk.VKServiceActivity;
import com.vk.sdk.api.VKError;

import java.util.ArrayList;

class VKAuthorizer implements Authorizer, AuthorizedInit {
    VKAuthorizer() {
    }

    private ArrayList<String> scopes = new ArrayList<>();
    private AuthenticationCallback callback;
    private Activity activity;
    private Fragment fragment;
    private android.support.v4.app.Fragment fragmentSupport;

    private void startForActivity(Activity activity) {
        VKSdk.login(activity);
    }

    private void startForFragment(Fragment fragment, Context context) {
        VKSdk.login(fragment);
    }

    private void startForFragment(android.support.v4.app.Fragment fragment, Context context) {
        Intent intent = new Intent(context, VKServiceActivity.class);
        intent.putExtra("arg1", VKServiceActivity.VKServiceType.Authorization.name());
        intent.putStringArrayListExtra("arg2", scopes);
        intent.putExtra("arg4", VKSdk.isCustomInitialize());
        fragment.startActivityForResult(intent, VKServiceActivity.VKServiceType.Authorization.getOuterCode());
    }

    @Override
    public VKAuthorizer initForActivity(Activity activity, AuthenticationCallback callback) {
        this.activity = activity;
        this.callback = callback;
        return this;
    }

    @Override
    public VKAuthorizer initForFragment(Fragment fragment, AuthenticationCallback callback) {
        this.fragment = fragment;
        this.callback = callback;
        return this;
    }

    @Override
    public VKAuthorizer intForFragment(android.support.v4.app.Fragment fragment, AuthenticationCallback callback) {
        this.fragmentSupport = fragment;
        this.callback = callback;
        return this;
    }

    @Override
    public void auth() {
        if (SocialAuthorizationManager.isInitialized()) {
            if (activity != null) {
                startForActivity(activity);
            } else if (fragment != null) {
                startForFragment(fragment, fragment.getActivity());
            } else if (fragmentSupport != null) {
                startForFragment(fragmentSupport, fragmentSupport.getContext());
            }
        } else {
            callback.onAuthenticationResult(AuthenticationResult.create(name())
                    .status(ResultStatus.ERROR)
                    .message("VK Sdk not initialized")
                    .build());
        }
    }

    @Override
    public void unregister() {

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (!VKSdk.onActivityResult(requestCode, resultCode, data, new VKCallback<VKAccessToken>() {
            @Override
            public void onResult(VKAccessToken res) {
                // Пользователь успешно авторизовался
                callback.onAuthenticationResult(
                        AuthenticationResult.create(name())
                                .status(ResultStatus.OK)
                                .token(res.accessToken)
                                .build()
                );
            }

            @Override
            public void onError(VKError error) {
                // Произошла ошибка авторизации (например, пользователь запретил авторизацию)
                callback.onAuthenticationResult(AuthenticationResult.create(name())
                        .status(ResultStatus.ERROR)
                        .message(error.toString())
                        .build());
            }
        })) {
            callback.onAuthenticationResult(AuthenticationResult.create(name())
                    .status(ResultStatus.NOT_ANSWERED)
                    .build());
        }
    }

    @Override
    public String name() {
        return Authorizer.VK;
    }

    VKAuthorizer setScopes(ArrayList<String> scopes) {
        this.scopes.addAll(scopes);
        return this;
    }
}
