package kim.glushkova.socialconnector;

import android.app.Activity;
import android.app.Fragment;

public class PhoneAuthrizerBuilder extends AuthorizerBuilder {
    private boolean receiveSMS;
    private boolean readPhoneStateEnabled;
    private String countryCode;

    PhoneAuthrizerBuilder(Activity activity, Fragment fragment, android.support.v4.app.Fragment fragmentSupport, AuthenticationCallback callback) {
        super(activity, fragment, fragmentSupport, callback);
    }

    public PhoneAuthrizerBuilder receiveSMS(boolean receiveSMS) {
        this.receiveSMS = receiveSMS;
        return this;
    }

    public PhoneAuthrizerBuilder readPhoneStateEnabled(boolean readPhoneStateEnabled) {
        this.readPhoneStateEnabled = readPhoneStateEnabled;
        return this;
    }

    public PhoneAuthrizerBuilder countryCode(String countryCode) {
        this.countryCode = countryCode;
        return this;
    }

    @Override
    protected PhoneAuthorizer innerBuild() {
        return new PhoneAuthorizer()
                .setCountryCode(countryCode)
                .setReceiveSMS(receiveSMS)
                .setReadPhoneStateEnabled(readPhoneStateEnabled);
    }
}
