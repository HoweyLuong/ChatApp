package com.example.chatapp.utilities;

import android.content.Context;
import android.content.SharedPreferences;


/**
 * This class manages the shared preferences in the chat application, allowing
 * storage and retrieval of user session data and other settings in a key-value format.
 */
public class PreferenceManager {
    private final SharedPreferences sharedPreferences;
    /**
     * Constructor that initializes the shared preferences instance.
     *
     * @param context The application context for accessing shared preferences.
     */

    public PreferenceManager (Context context) {
       sharedPreferences = context.getSharedPreferences(Constants.KEY_PREFERENCE_NAME, Context.MODE_PRIVATE);
    }

    /**
     * Stores a boolean value in shared preferences.
     *
     * @param key   The key with which the value is to be associated.
     * @param value The boolean value to store.
     */


    public void putBoolean(String key, Boolean value) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(key,value);
        editor.apply();
    }

    /**
     * Retrieves a boolean value from shared preferences.
     *
     * @param key The key whose associated value is to be returned.
     * @return The boolean value associated with the key, or false if the key does not exist.
     */

   public Boolean getBoolean(String key) {
        return sharedPreferences.getBoolean(key, false);

   }
    /**
     * Stores a string value in shared preferences.
     *
     * @param key   The key with which the value is to be associated.
     * @param value The string value to store.
     */
   public void putString(String key, String value) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(key, value);
        editor.apply();
   }
    /**
     * Retrieves a string value from shared preferences.
     *
     * @param key The key whose associated value is to be returned.
     * @return The string value associated with the key, or null if the key does not exist.
     */
   public String getString(String key) {
        return sharedPreferences.getString(key, null);
   }
    /**
     * Clears all values stored in shared preferences.
     */
   public void clear() {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
   }


}
