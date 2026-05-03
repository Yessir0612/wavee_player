package com.vibewave.data.db;

import android.database.Cursor;
import android.os.CancellationSignal;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.room.CoroutinesRoom;
import androidx.room.EntityInsertionAdapter;
import androidx.room.RoomDatabase;
import androidx.room.RoomSQLiteQuery;
import androidx.room.SharedSQLiteStatement;
import androidx.room.util.CursorUtil;
import androidx.room.util.DBUtil;
import androidx.sqlite.db.SupportSQLiteStatement;
import java.lang.Class;
import java.lang.Exception;
import java.lang.Integer;
import java.lang.Object;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Callable;
import javax.annotation.processing.Generated;
import kotlin.Unit;
import kotlin.coroutines.Continuation;
import kotlinx.coroutines.flow.Flow;

@Generated("androidx.room.RoomProcessor")
@SuppressWarnings({"unchecked", "deprecation"})
public final class HistoryDao_Impl implements HistoryDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<HistoryEntity> __insertionAdapterOfHistoryEntity;

  private final SharedSQLiteStatement __preparedStmtOfClear;

  public HistoryDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfHistoryEntity = new EntityInsertionAdapter<HistoryEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR REPLACE INTO `history` (`trackId`,`title`,`artist`,`albumTitle`,`albumArt`,`previewUrl`,`durationSec`,`explicit`,`lastPlayedAt`,`playCount`) VALUES (?,?,?,?,?,?,?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final HistoryEntity entity) {
        statement.bindLong(1, entity.getTrackId());
        statement.bindString(2, entity.getTitle());
        statement.bindString(3, entity.getArtist());
        statement.bindString(4, entity.getAlbumTitle());
        if (entity.getAlbumArt() == null) {
          statement.bindNull(5);
        } else {
          statement.bindString(5, entity.getAlbumArt());
        }
        statement.bindString(6, entity.getPreviewUrl());
        statement.bindLong(7, entity.getDurationSec());
        final int _tmp = entity.getExplicit() ? 1 : 0;
        statement.bindLong(8, _tmp);
        statement.bindLong(9, entity.getLastPlayedAt());
        statement.bindLong(10, entity.getPlayCount());
      }
    };
    this.__preparedStmtOfClear = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "DELETE FROM history";
        return _query;
      }
    };
  }

  @Override
  public Object upsert(final HistoryEntity entity, final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __insertionAdapterOfHistoryEntity.insert(entity);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object clear(final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfClear.acquire();
        try {
          __db.beginTransaction();
          try {
            _stmt.executeUpdateDelete();
            __db.setTransactionSuccessful();
            return Unit.INSTANCE;
          } finally {
            __db.endTransaction();
          }
        } finally {
          __preparedStmtOfClear.release(_stmt);
        }
      }
    }, $completion);
  }

  @Override
  public Flow<List<HistoryEntity>> observeRecent(final int limit) {
    final String _sql = "SELECT * FROM history ORDER BY lastPlayedAt DESC LIMIT ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, limit);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"history"}, new Callable<List<HistoryEntity>>() {
      @Override
      @NonNull
      public List<HistoryEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfTrackId = CursorUtil.getColumnIndexOrThrow(_cursor, "trackId");
          final int _cursorIndexOfTitle = CursorUtil.getColumnIndexOrThrow(_cursor, "title");
          final int _cursorIndexOfArtist = CursorUtil.getColumnIndexOrThrow(_cursor, "artist");
          final int _cursorIndexOfAlbumTitle = CursorUtil.getColumnIndexOrThrow(_cursor, "albumTitle");
          final int _cursorIndexOfAlbumArt = CursorUtil.getColumnIndexOrThrow(_cursor, "albumArt");
          final int _cursorIndexOfPreviewUrl = CursorUtil.getColumnIndexOrThrow(_cursor, "previewUrl");
          final int _cursorIndexOfDurationSec = CursorUtil.getColumnIndexOrThrow(_cursor, "durationSec");
          final int _cursorIndexOfExplicit = CursorUtil.getColumnIndexOrThrow(_cursor, "explicit");
          final int _cursorIndexOfLastPlayedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "lastPlayedAt");
          final int _cursorIndexOfPlayCount = CursorUtil.getColumnIndexOrThrow(_cursor, "playCount");
          final List<HistoryEntity> _result = new ArrayList<HistoryEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final HistoryEntity _item;
            final long _tmpTrackId;
            _tmpTrackId = _cursor.getLong(_cursorIndexOfTrackId);
            final String _tmpTitle;
            _tmpTitle = _cursor.getString(_cursorIndexOfTitle);
            final String _tmpArtist;
            _tmpArtist = _cursor.getString(_cursorIndexOfArtist);
            final String _tmpAlbumTitle;
            _tmpAlbumTitle = _cursor.getString(_cursorIndexOfAlbumTitle);
            final String _tmpAlbumArt;
            if (_cursor.isNull(_cursorIndexOfAlbumArt)) {
              _tmpAlbumArt = null;
            } else {
              _tmpAlbumArt = _cursor.getString(_cursorIndexOfAlbumArt);
            }
            final String _tmpPreviewUrl;
            _tmpPreviewUrl = _cursor.getString(_cursorIndexOfPreviewUrl);
            final int _tmpDurationSec;
            _tmpDurationSec = _cursor.getInt(_cursorIndexOfDurationSec);
            final boolean _tmpExplicit;
            final int _tmp;
            _tmp = _cursor.getInt(_cursorIndexOfExplicit);
            _tmpExplicit = _tmp != 0;
            final long _tmpLastPlayedAt;
            _tmpLastPlayedAt = _cursor.getLong(_cursorIndexOfLastPlayedAt);
            final int _tmpPlayCount;
            _tmpPlayCount = _cursor.getInt(_cursorIndexOfPlayCount);
            _item = new HistoryEntity(_tmpTrackId,_tmpTitle,_tmpArtist,_tmpAlbumTitle,_tmpAlbumArt,_tmpPreviewUrl,_tmpDurationSec,_tmpExplicit,_tmpLastPlayedAt,_tmpPlayCount);
            _result.add(_item);
          }
          return _result;
        } finally {
          _cursor.close();
        }
      }

      @Override
      protected void finalize() {
        _statement.release();
      }
    });
  }

  @Override
  public Flow<List<HistoryEntity>> observeTop(final int limit) {
    final String _sql = "SELECT * FROM history ORDER BY playCount DESC, lastPlayedAt DESC LIMIT ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, limit);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"history"}, new Callable<List<HistoryEntity>>() {
      @Override
      @NonNull
      public List<HistoryEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfTrackId = CursorUtil.getColumnIndexOrThrow(_cursor, "trackId");
          final int _cursorIndexOfTitle = CursorUtil.getColumnIndexOrThrow(_cursor, "title");
          final int _cursorIndexOfArtist = CursorUtil.getColumnIndexOrThrow(_cursor, "artist");
          final int _cursorIndexOfAlbumTitle = CursorUtil.getColumnIndexOrThrow(_cursor, "albumTitle");
          final int _cursorIndexOfAlbumArt = CursorUtil.getColumnIndexOrThrow(_cursor, "albumArt");
          final int _cursorIndexOfPreviewUrl = CursorUtil.getColumnIndexOrThrow(_cursor, "previewUrl");
          final int _cursorIndexOfDurationSec = CursorUtil.getColumnIndexOrThrow(_cursor, "durationSec");
          final int _cursorIndexOfExplicit = CursorUtil.getColumnIndexOrThrow(_cursor, "explicit");
          final int _cursorIndexOfLastPlayedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "lastPlayedAt");
          final int _cursorIndexOfPlayCount = CursorUtil.getColumnIndexOrThrow(_cursor, "playCount");
          final List<HistoryEntity> _result = new ArrayList<HistoryEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final HistoryEntity _item;
            final long _tmpTrackId;
            _tmpTrackId = _cursor.getLong(_cursorIndexOfTrackId);
            final String _tmpTitle;
            _tmpTitle = _cursor.getString(_cursorIndexOfTitle);
            final String _tmpArtist;
            _tmpArtist = _cursor.getString(_cursorIndexOfArtist);
            final String _tmpAlbumTitle;
            _tmpAlbumTitle = _cursor.getString(_cursorIndexOfAlbumTitle);
            final String _tmpAlbumArt;
            if (_cursor.isNull(_cursorIndexOfAlbumArt)) {
              _tmpAlbumArt = null;
            } else {
              _tmpAlbumArt = _cursor.getString(_cursorIndexOfAlbumArt);
            }
            final String _tmpPreviewUrl;
            _tmpPreviewUrl = _cursor.getString(_cursorIndexOfPreviewUrl);
            final int _tmpDurationSec;
            _tmpDurationSec = _cursor.getInt(_cursorIndexOfDurationSec);
            final boolean _tmpExplicit;
            final int _tmp;
            _tmp = _cursor.getInt(_cursorIndexOfExplicit);
            _tmpExplicit = _tmp != 0;
            final long _tmpLastPlayedAt;
            _tmpLastPlayedAt = _cursor.getLong(_cursorIndexOfLastPlayedAt);
            final int _tmpPlayCount;
            _tmpPlayCount = _cursor.getInt(_cursorIndexOfPlayCount);
            _item = new HistoryEntity(_tmpTrackId,_tmpTitle,_tmpArtist,_tmpAlbumTitle,_tmpAlbumArt,_tmpPreviewUrl,_tmpDurationSec,_tmpExplicit,_tmpLastPlayedAt,_tmpPlayCount);
            _result.add(_item);
          }
          return _result;
        } finally {
          _cursor.close();
        }
      }

      @Override
      protected void finalize() {
        _statement.release();
      }
    });
  }

  @Override
  public Object findById(final long id, final Continuation<? super HistoryEntity> $completion) {
    final String _sql = "SELECT * FROM history WHERE trackId = ? LIMIT 1";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, id);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<HistoryEntity>() {
      @Override
      @Nullable
      public HistoryEntity call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfTrackId = CursorUtil.getColumnIndexOrThrow(_cursor, "trackId");
          final int _cursorIndexOfTitle = CursorUtil.getColumnIndexOrThrow(_cursor, "title");
          final int _cursorIndexOfArtist = CursorUtil.getColumnIndexOrThrow(_cursor, "artist");
          final int _cursorIndexOfAlbumTitle = CursorUtil.getColumnIndexOrThrow(_cursor, "albumTitle");
          final int _cursorIndexOfAlbumArt = CursorUtil.getColumnIndexOrThrow(_cursor, "albumArt");
          final int _cursorIndexOfPreviewUrl = CursorUtil.getColumnIndexOrThrow(_cursor, "previewUrl");
          final int _cursorIndexOfDurationSec = CursorUtil.getColumnIndexOrThrow(_cursor, "durationSec");
          final int _cursorIndexOfExplicit = CursorUtil.getColumnIndexOrThrow(_cursor, "explicit");
          final int _cursorIndexOfLastPlayedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "lastPlayedAt");
          final int _cursorIndexOfPlayCount = CursorUtil.getColumnIndexOrThrow(_cursor, "playCount");
          final HistoryEntity _result;
          if (_cursor.moveToFirst()) {
            final long _tmpTrackId;
            _tmpTrackId = _cursor.getLong(_cursorIndexOfTrackId);
            final String _tmpTitle;
            _tmpTitle = _cursor.getString(_cursorIndexOfTitle);
            final String _tmpArtist;
            _tmpArtist = _cursor.getString(_cursorIndexOfArtist);
            final String _tmpAlbumTitle;
            _tmpAlbumTitle = _cursor.getString(_cursorIndexOfAlbumTitle);
            final String _tmpAlbumArt;
            if (_cursor.isNull(_cursorIndexOfAlbumArt)) {
              _tmpAlbumArt = null;
            } else {
              _tmpAlbumArt = _cursor.getString(_cursorIndexOfAlbumArt);
            }
            final String _tmpPreviewUrl;
            _tmpPreviewUrl = _cursor.getString(_cursorIndexOfPreviewUrl);
            final int _tmpDurationSec;
            _tmpDurationSec = _cursor.getInt(_cursorIndexOfDurationSec);
            final boolean _tmpExplicit;
            final int _tmp;
            _tmp = _cursor.getInt(_cursorIndexOfExplicit);
            _tmpExplicit = _tmp != 0;
            final long _tmpLastPlayedAt;
            _tmpLastPlayedAt = _cursor.getLong(_cursorIndexOfLastPlayedAt);
            final int _tmpPlayCount;
            _tmpPlayCount = _cursor.getInt(_cursorIndexOfPlayCount);
            _result = new HistoryEntity(_tmpTrackId,_tmpTitle,_tmpArtist,_tmpAlbumTitle,_tmpAlbumArt,_tmpPreviewUrl,_tmpDurationSec,_tmpExplicit,_tmpLastPlayedAt,_tmpPlayCount);
          } else {
            _result = null;
          }
          return _result;
        } finally {
          _cursor.close();
          _statement.release();
        }
      }
    }, $completion);
  }

  @Override
  public Flow<Integer> observeCount() {
    final String _sql = "SELECT COUNT(*) FROM history";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"history"}, new Callable<Integer>() {
      @Override
      @NonNull
      public Integer call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final Integer _result;
          if (_cursor.moveToFirst()) {
            final int _tmp;
            _tmp = _cursor.getInt(0);
            _result = _tmp;
          } else {
            _result = 0;
          }
          return _result;
        } finally {
          _cursor.close();
        }
      }

      @Override
      protected void finalize() {
        _statement.release();
      }
    });
  }

  @NonNull
  public static List<Class<?>> getRequiredConverters() {
    return Collections.emptyList();
  }
}
