package kim.glushkova.socialconnector;

import android.content.Intent;


public interface Authorizer {

    String FACEBOOK = "fb";
    String VK = "vk";
    String PHONE = "phone";

    void auth();

    void unregister();

    void onActivityResult(final int requestCode, final int resultCode, final Intent data);
    
    String name();
}
