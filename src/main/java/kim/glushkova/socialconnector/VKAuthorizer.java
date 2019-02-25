package kim.glushkova.socialconnector;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;

import com.vk.api.sdk.auth.VKAccessToken;
import com.vk.api.sdk.auth.VKAuthCallback;

import org.jetbrains.annotations.NotNull;

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
        com.vk.api.sdk.VK.login(activity);
    }

    private void startForFragment(Fragment fragment, Context context) {
        com.vk.api.sdk.VK.login(fragment.getActivity());
    }

    private void startForFragment(android.support.v4.app.Fragment fragment, Context context) {
        if (fragment.getActivity() != null) {
            com.vk.api.sdk.VK.login(fragment.getActivity());
        } else {
            this.callback.onAuthenticationResult(AuthenticationResult
                    .create(VK)
                    .status(ResultStatus.ERROR)
                    .message("Can't found activity")
                    .build());
        }
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
        if (data == null || !com.vk.api.sdk.VK.onActivityResult(requestCode, resultCode, data, new VKAuthCallback() {
            @Override
            public void onLoginFailed(int i) {
                callback.onAuthenticationResult(AuthenticationResult.create(name())
                        .status(ResultStatus.ERROR)
                        .message("Error code: " + i)
                        .build());
            }

            @Override
            public void onLogin(@NotNull VKAccessToken res) {
                callback.onAuthenticationResult(
                        AuthenticationResult.create(name())
                                .status(ResultStatus.OK)
                                .token(res.getAccessToken())
                                .build()
                );
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
