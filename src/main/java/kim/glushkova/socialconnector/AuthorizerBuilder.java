package kim.glushkova.socialconnector;

import android.app.Activity;
import android.app.Fragment;

abstract class AuthorizerBuilder {

    private final Activity activity;
    private final Fragment fragment;
    private final android.support.v4.app.Fragment fragmentSupport;
    private final AuthenticationCallback callback;

    protected AuthorizerBuilder(Activity activity, Fragment fragment, android.support.v4.app.Fragment fragmentSupport, AuthenticationCallback callback) {
        this.activity = activity;
        this.fragment = fragment;
        this.fragmentSupport = fragmentSupport;
        this.callback = callback;
    }


    protected abstract AuthorizedInit innerBuild();

    public Authorizer build() {
        AuthorizedInit preBuild = innerBuild();
        if (activity != null) {
            return preBuild.initForActivity(activity, callback);
        } else if (fragment != null) {
            return preBuild.initForFragment(fragment, callback);
        } else if (fragmentSupport != null) {
            return preBuild.intForFragment(fragmentSupport, callback);
        } else {
            throw new AuthorizationBuilderException("Authorization must be initialized with not null activity or fragment");
        }
    }
}
