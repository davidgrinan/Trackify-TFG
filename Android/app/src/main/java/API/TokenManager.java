package API;

import android.content.Context;
import android.content.SharedPreferences;

public class TokenManager {

    private static final String PREF_NAME = "trackify_preferences";
    private static final String TOKEN_KEY = "jwt_token";

    public static void saveToken(Context context, String token) {
        SharedPreferences preferences =
                context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);

        preferences.edit()
                .putString(TOKEN_KEY, token)
                .apply();
    }

    public static String getToken(Context context) {
        SharedPreferences preferences =
                context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);

        return preferences.getString(TOKEN_KEY, null);
    }

    public static void clearToken(Context context) {
        SharedPreferences preferences =
                context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);

        preferences.edit()
                .remove(TOKEN_KEY)
                .apply();
    }
    private static final String USERNAME_KEY = "username";

    public static void saveUsername(Context context, String username) {
        SharedPreferences preferences =
                context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);

        preferences.edit()
                .putString(USERNAME_KEY, username)
                .apply();
    }

    public static String getUsername(Context context) {
        SharedPreferences preferences =
                context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);

        return preferences.getString(USERNAME_KEY, "usuario");
    }

    public static boolean hasToken(Context context) {
        return getToken(context) != null;
    }
}