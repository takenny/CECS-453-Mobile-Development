import android.content.Context
import android.preference.PreferenceManager
import android.provider.Settings.Global.putString
import androidx.core.content.edit

private const val KTPREF_SEARCH_QUERY = "searchQuery"
private const val KTPREF_LAST_RESULT_ID = "lastResultId"
private const val KTPREF_IS_POLLING = "isPolling"

object QueryPreferences {
    fun getStoredQuery(context: Context): String {
        val KTprefs = PreferenceManager.getDefaultSharedPreferences(context)
        return KTprefs.getString(KTPREF_SEARCH_QUERY, "")!!
    }
    fun setStoredQuery(context: Context, query: String) {
        PreferenceManager.getDefaultSharedPreferences(context)
            .edit {
                putString(KTPREF_SEARCH_QUERY, query)
            }
    }
    fun getLastResultId(context: Context): String {
        return PreferenceManager.getDefaultSharedPreferences(context)
            .getString(KTPREF_LAST_RESULT_ID, "")!!
    }
    fun setLastResultId(context: Context, lastResultId: String) {
        PreferenceManager.getDefaultSharedPreferences(context).edit {
            putString(KTPREF_LAST_RESULT_ID, lastResultId)
        }
    }

    fun isPolling(context: Context): Boolean {
        return PreferenceManager.getDefaultSharedPreferences(context)
            .getBoolean(KTPREF_IS_POLLING, false)
    }
    fun setPolling(context: Context, isOn: Boolean) {
        PreferenceManager.getDefaultSharedPreferences(context).edit {
            putBoolean(KTPREF_IS_POLLING, isOn)
        }
    }


}