package kim.glushkova.socialconnector;

import android.app.Activity;
import android.app.Fragment;

public interface AuthorizedInit extends Authorizer {
    Authorizer initForActivity(Activity activity, AuthenticationCallback callback);

    Authorizer initForFragment(Fragment fragment, AuthenticationCallback callback);

    Authorizer intForFragment(android.support.v4.app.Fragment fragment, AuthenticationCallback callback);
}
