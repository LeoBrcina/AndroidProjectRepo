package hr.algebra.formula1data.provider

import android.content.ContentProvider
import android.content.ContentUris
import android.content.ContentValues
import android.content.UriMatcher
import android.database.Cursor
import android.net.Uri
import hr.algebra.formula1data.db.F1Database

class F1ContentProvider : ContentProvider() {

    companion object {
        const val AUTHORITY = "hr.algebra.formula1data.provider"
        const val DRIVER_STANDINGS_PATH = "driver_standings"
        val CONTENT_URI: Uri = Uri.parse("content://$AUTHORITY/$DRIVER_STANDINGS_PATH")

        private const val DRIVER_STANDINGS = 1

        private val uriMatcher = UriMatcher(UriMatcher.NO_MATCH).apply {
            addURI(AUTHORITY, DRIVER_STANDINGS_PATH, DRIVER_STANDINGS)
        }
    }

    private lateinit var db: F1Database

    override fun onCreate(): Boolean {
        db = F1DatabaseProvider.getDatabase(requireNotNull(context))
        return true
    }

    override fun query(
        uri: Uri,
        projection: Array<out String>?,
        selection: String?,
        selectionArgs: Array<out String>?,
        sortOrder: String?
    ): Cursor? {
        return when (uriMatcher.match(uri)) {
            DRIVER_STANDINGS -> {
                db.driverStandingDao().getAllCursor()
            }
            else -> null
        }
    }

    override fun getType(uri: Uri): String? = null
    override fun insert(uri: Uri, values: ContentValues?): Uri? = null
    override fun update(uri: Uri, values: ContentValues?, selection: String?, selectionArgs: Array<out String>?): Int = 0
    override fun delete(uri: Uri, selection: String?, selectionArgs: Array<out String>?): Int = 0
}
