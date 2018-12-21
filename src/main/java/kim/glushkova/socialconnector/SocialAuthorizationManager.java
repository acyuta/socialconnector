package kim.glushkova.socialconnector;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;

import com.vk.sdk.VKSdk;

public class SocialAuthorizationManager {

    public static void init(Context context) {
        VKSdk.initialize(context);
    }

    public static Builder getInstance() {
        return new Builder();
    }


    public static class Builder {
        private Builder() {
        }

        public Socials forActivity(Activity activity, AuthenticationCallback callback) {
            return new Socials(activity, callback);
        }

        public Socials forFragment(Fragment fragment, AuthenticationCallback callback) {
            return new Socials(fragment, callback);
        }

        public Socials forFragment(android.support.v4.app.Fragment fragment, AuthenticationCallback callback) {
            return new Socials(fragment, callback);
        }
    }

    public static class Socials {
        private final Activity activity;
        private final Fragment fragment;
        private final android.support.v4.app.Fragment fragmentSupport;
        private final AuthenticationCallback callback;


        private Socials(Activity activity, AuthenticationCallback callback) {
            this.callback = callback;
            this.activity = activity;
            this.fragment = null;
            this.fragmentSupport = null;
        }

        private Socials(Fragment fragment, AuthenticationCallback callback) {
            this.callback = callback;
            this.activity = null;
            this.fragment = fragment;
            this.fragmentSupport = null;
        }

        private Socials(android.support.v4.app.Fragment fragment, AuthenticationCallback callback) {
            this.callback = callback;
            this.activity = null;
            this.fragment = null;
            this.fragmentSupport = fragment;
        }

        public FBAuthorizerBuilder fb() {
            return new FBAuthorizerBuilder(activity, fragment, fragmentSupport, callback);
        }

        public VKAuthorizerBuilder vk() {
            return new VKAuthorizerBuilder(activity, fragment, fragmentSupport, callback);
        }

        public PhoneAuthrizerBuilder phone() {
            return new PhoneAuthrizerBuilder(activity, fragment, fragmentSupport, callback);
        }
    }
}
