package kim.glushkova.socialconnector;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;

import java.util.Collection;
import java.util.Collections;


class FBAuthorizer implements Authorizer, FacebookCallback<LoginResult>, AuthorizedInit {
    private Activity a;
    private Fragment f;
    private android.support.v4.app.Fragment fs;

    FBAuthorizer() {
    }

    private CallbackManager callbackManager;
    private AuthenticationCallback callback;

    @Override
    public FBAuthorizer initForActivity(Activity activity, AuthenticationCallback callback) {
        this.a = activity;
        register(callback);
        return this;
    }

    @Override
    public FBAuthorizer initForFragment(Fragment fragment, AuthenticationCallback callback) {
        this.f = fragment;
        register(callback);
        return this;
    }

    @Override
    public FBAuthorizer intForFragment(android.support.v4.app.Fragment fragment, AuthenticationCallback callback) {
        register(callback);
        this.fs = fragment;
        return this;
    }

    @Override
    public void auth() {
        Collection<String> perms = Collections.emptyList();
        if (a != null)
            LoginManager.getInstance().logInWithPublishPermissions(a, perms);
        else if (f != null) {
            LoginManager.getInstance().logInWithPublishPermissions(f, perms);
        } else if (fs != null) {
            LoginManager.getInstance().logInWithPublishPermissions(fs, perms);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        callbackManager.onActivityResult(requestCode, resultCode, data);

    }

    @Override
    public String name() {
        return Authorizer.FACEBOOK;
    }

    private void register(AuthenticationCallback callback) {
        callbackManager = CallbackManager.Factory.create();
        this.callback = callback;
        LoginManager.getInstance().registerCallback(callbackManager, this);
    }

    @Override
    public void onSuccess(LoginResult loginResult) {
        unregister();
        callback.onAuthenticationResult(
                AuthenticationResult.create(name())
                        .status(ResultStatus.OK)
                        .token(loginResult.getAccessToken().getToken())
                        .build()
        );
    }

    @Override
    public void unregister() {
        LoginManager.getInstance().unregisterCallback(callbackManager);
    }

    @Override
    public void onCancel() {
        unregister();
        callback.onAuthenticationResult(AuthenticationResult.create(name())
                .status(ResultStatus.CANCELED)
                .build());
    }

    @Override
    public void onError(FacebookException error) {
        unregister();
        callback.onAuthenticationResult(AuthenticationResult.create(name())
                .status(ResultStatus.ERROR)
                .message(error.toString())
                .build());
    }
}
