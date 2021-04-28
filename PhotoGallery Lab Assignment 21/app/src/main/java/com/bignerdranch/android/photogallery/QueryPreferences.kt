import android.content.Context
import android.preference.PreferenceManager
import android.provider.Settings.Global.putString
import androidx.core.content.edit

private const val PREF_SEARCH_QUERY = "searchQuery"
object QueryPreferences {
    fun getStoredQuery(context: Context): String {
        val KTprefs = PreferenceManager.getDefaultSharedPreferences(context)
        return KTprefs.getString(PREF_SEARCH_QUERY, "")!!
    }
    fun setStoredQuery(context: Context, query: String) {
        PreferenceManager.getDefaultSharedPreferences(context)
            .edit {
                putString(PREF_SEARCH_QUERY, query)
            }
    }
}