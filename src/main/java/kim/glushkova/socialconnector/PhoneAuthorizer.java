package kim.glushkova.socialconnector;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;

import com.facebook.accountkit.AccountKitLoginResult;
import com.facebook.accountkit.ui.AccountKitActivity;
import com.facebook.accountkit.ui.AccountKitConfiguration;
import com.facebook.accountkit.ui.LoginType;

class PhoneAuthorizer implements Authorizer, AuthorizedInit {
    PhoneAuthorizer() {
    }

    private static final int APP_REQUEST_CODE = 99;
    private String countryCode = null;
    private boolean readPhoneStateEnabled = false;
    private boolean receiveSMS = false;
    private AuthenticationCallback callback;
    private Activity activity;
    private Fragment fragment;
    private android.support.v4.app.Fragment fragmentSupport;

    @NonNull
    private Intent buildIntent(Context context) {
        final Intent intent = new Intent(context, AccountKitActivity.class);
        AccountKitConfiguration.AccountKitConfigurationBuilder cbuilder =
                new AccountKitConfiguration.AccountKitConfigurationBuilder(
                        LoginType.PHONE,
                        AccountKitActivity.ResponseType.TOKEN
                );
        //additional configuration
        if (countryCode != null)
            cbuilder.setDefaultCountryCode(countryCode);
        cbuilder.setReadPhoneStateEnabled(readPhoneStateEnabled);
        cbuilder.setReceiveSMS(receiveSMS);

        intent.putExtra(
                AccountKitActivity.ACCOUNT_KIT_ACTIVITY_CONFIGURATION,
                cbuilder.build()
        );
        return intent;
    }

    private AuthenticationResult handleActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == APP_REQUEST_CODE && data != null) { // confirm that this response matches your request
            AccountKitLoginResult loginResult = data.getParcelableExtra(AccountKitLoginResult.RESULT_KEY);
            if (loginResult.getError() != null) {
                return AuthenticationResult.create(name())
                        .status(ResultStatus.ERROR)
                        .message(loginResult.getError().getUserFacingMessage())
                        .build();
            } else if (loginResult.wasCancelled()) {
                return AuthenticationResult.create(name())
                        .status(ResultStatus.CANCELED)
                        .build();
            } else {
                if (loginResult.getAccessToken() != null) {
                    return AuthenticationResult.create(name())
                            .status(ResultStatus.OK)
                            .token(loginResult.getAccessToken().getToken())
                            .build();
                } else {
                    return AuthenticationResult.create(name())
                            .status(ResultStatus.NO_TOKEN)
                            .build();
                }
            }
        } else {
            return AuthenticationResult.create(name())
                    .status(ResultStatus.NOT_ANSWERED)
                    .build();
        }
    }

    PhoneAuthorizer setCountryCode(String countryCode) {
        this.countryCode = countryCode;
        return this;
    }

    PhoneAuthorizer setReadPhoneStateEnabled(boolean readPhoneStateEnabled) {
        this.readPhoneStateEnabled = readPhoneStateEnabled;
        return this;
    }

    PhoneAuthorizer setReceiveSMS(boolean receiveSMS) {
        this.receiveSMS = receiveSMS;
        return this;
    }

    @Override
    public PhoneAuthorizer initForActivity(Activity activity, AuthenticationCallback callback) {
        this.callback = callback;
        this.activity = activity;
        return this;
    }

    @Override
    public PhoneAuthorizer initForFragment(Fragment fragment, AuthenticationCallback callback) {
        this.callback = callback;
        this.fragment = fragment;
        return this;
    }

    @Override
    public PhoneAuthorizer intForFragment(android.support.v4.app.Fragment fragment, AuthenticationCallback callback) {
        this.callback = callback;
        this.fragmentSupport = fragment;
        return this;
    }

    @Override
    public void auth() {
        if (activity != null) {
            final Intent intent = buildIntent(activity);
            activity.startActivityForResult(intent, APP_REQUEST_CODE);
        } else if (fragment != null) {
            final Intent intent = buildIntent(fragment.getActivity());
            fragment.startActivityForResult(intent, APP_REQUEST_CODE);
        } else if (fragmentSupport != null) {
            final Intent intent = buildIntent(fragmentSupport.getContext());
            fragmentSupport.startActivityForResult(intent, APP_REQUEST_CODE);
        }

    }

    @Override
    public void unregister() {

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        callback.onAuthenticationResult(handleActivityResult(requestCode, resultCode, data));
    }

    @Override
    public String name() {
        return Authorizer.PHONE;
    }
}
