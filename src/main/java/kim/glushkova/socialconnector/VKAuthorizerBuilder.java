package kim.glushkova.socialconnector;

import android.app.Activity;
import android.app.Fragment;

import java.util.ArrayList;
import java.util.Collections;

public class VKAuthorizerBuilder extends AuthorizerBuilder {
    private ArrayList<String> scopes;

    VKAuthorizerBuilder(Activity activity, Fragment fragment, android.support.v4.app.Fragment fragmentSupport, AuthenticationCallback callback) {
        super(activity, fragment, fragmentSupport, callback);
        scopes = new ArrayList<>();
    }

    public VKAuthorizerBuilder addScope(String... scope) {
        Collections.addAll(this.scopes, scope);
        return this;
    }

    @Override
    protected AuthorizedInit innerBuild() {
        return new VKAuthorizer().setScopes(scopes);
    }
}
