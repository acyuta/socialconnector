package kim.glushkova.socialconnector;

import android.app.Activity;
import android.app.Fragment;

public class FBAccountAuthorizerEmailBuilder extends AuthorizerBuilder {
    FBAccountAuthorizerEmailBuilder(Activity activity, Fragment fragment, android.support.v4.app.Fragment fragmentSupport, AuthenticationCallback callback) {
        super(activity, fragment, fragmentSupport, callback);
    }


    @Override
    protected AuthorizedInit innerBuild() {
        return new FBAccountEmailAuthorizer();
    }
}
