package com.newritage.app.data;

import androidx.annotation.NonNull;
import androidx.room.EntityInsertAdapter;
import androidx.room.RoomDatabase;
import androidx.room.coroutines.FlowUtil;
import androidx.room.util.DBUtil;
import androidx.room.util.SQLiteStatementUtil;
import androidx.sqlite.SQLiteStatement;
import java.lang.Class;
import java.lang.Integer;
import java.lang.Long;
import java.lang.NullPointerException;
import java.lang.Object;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import kotlin.Unit;
import kotlin.coroutines.Continuation;
import kotlinx.coroutines.flow.Flow;

@SuppressWarnings({"unchecked", "deprecation", "removal"})
public final class SessionDao_Impl implements SessionDao {
  private final RoomDatabase __db;

  private final EntityInsertAdapter<Session> __insertAdapterOfSession;

  public SessionDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertAdapterOfSession = new EntityInsertAdapter<Session>() {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR REPLACE INTO `sessions` (`id`,`date`,`sessionIndex`,`hasThread`,`startTime`,`endTime`,`durationSeconds`,`avgPressure`,`maxPressure`,`minPressure`,`emotion`,`threadColor`,`threadColorName`,`aiFeedback`,`createdAt`) VALUES (nullif(?, 0),?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SQLiteStatement statement, @NonNull final Session entity) {
        statement.bindLong(1, entity.getId());
        if (entity.getDate() == null) {
          statement.bindNull(2);
        } else {
          statement.bindText(2, entity.getDate());
        }
        statement.bindLong(3, entity.getSessionIndex());
        final int _tmp = entity.getHasThread() ? 1 : 0;
        statement.bindLong(4, _tmp);
        if (entity.getStartTime() == null) {
          statement.bindNull(5);
        } else {
          statement.bindText(5, entity.getStartTime());
        }
        if (entity.getEndTime() == null) {
          statement.bindNull(6);
        } else {
          statement.bindText(6, entity.getEndTime());
        }
        statement.bindLong(7, entity.getDurationSeconds());
        statement.bindDouble(8, entity.getAvgPressure());
        statement.bindDouble(9, entity.getMaxPressure());
        statement.bindDouble(10, entity.getMinPressure());
        if (entity.getEmotion() == null) {
          statement.bindNull(11);
        } else {
          statement.bindText(11, entity.getEmotion());
        }
        if (entity.getThreadColor() == null) {
          statement.bindNull(12);
        } else {
          statement.bindText(12, entity.getThreadColor());
        }
        if (entity.getThreadColorName() == null) {
          statement.bindNull(13);
        } else {
          statement.bindText(13, entity.getThreadColorName());
        }
        if (entity.getAiFeedback() == null) {
          statement.bindNull(14);
        } else {
          statement.bindText(14, entity.getAiFeedback());
        }
        statement.bindLong(15, entity.getCreatedAt());
      }
    };
  }

  @Override
  public Object insert(final Session session, final Continuation<? super Long> $completion) {
    if (session == null) throw new NullPointerException();
    return DBUtil.performSuspending(__db, false, true, (_connection) -> {
      return __insertAdapterOfSession.insertAndReturnId(_connection, session);
    }, $completion);
  }

  @Override
  public Object getSessionsByDate(final String date,
      final Continuation<? super List<Session>> $completion) {
    final String _sql = "SELECT * FROM sessions WHERE date = ? ORDER BY createdAt ASC";
    return DBUtil.performSuspending(__db, true, false, (_connection) -> {
      final SQLiteStatement _stmt = _connection.prepare(_sql);
      try {
        int _argIndex = 1;
        if (date == null) {
          _stmt.bindNull(_argIndex);
        } else {
          _stmt.bindText(_argIndex, date);
        }
        final int _columnIndexOfId = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "id");
        final int _columnIndexOfDate = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "date");
        final int _columnIndexOfSessionIndex = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "sessionIndex");
        final int _columnIndexOfHasThread = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "hasThread");
        final int _columnIndexOfStartTime = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "startTime");
        final int _columnIndexOfEndTime = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "endTime");
        final int _columnIndexOfDurationSeconds = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "durationSeconds");
        final int _columnIndexOfAvgPressure = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "avgPressure");
        final int _columnIndexOfMaxPressure = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "maxPressure");
        final int _columnIndexOfMinPressure = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "minPressure");
        final int _columnIndexOfEmotion = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "emotion");
        final int _columnIndexOfThreadColor = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "threadColor");
        final int _columnIndexOfThreadColorName = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "threadColorName");
        final int _columnIndexOfAiFeedback = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "aiFeedback");
        final int _columnIndexOfCreatedAt = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "createdAt");
        final List<Session> _result = new ArrayList<Session>();
        while (_stmt.step()) {
          final Session _item;
          final long _tmpId;
          _tmpId = _stmt.getLong(_columnIndexOfId);
          final String _tmpDate;
          if (_stmt.isNull(_columnIndexOfDate)) {
            _tmpDate = null;
          } else {
            _tmpDate = _stmt.getText(_columnIndexOfDate);
          }
          final int _tmpSessionIndex;
          _tmpSessionIndex = (int) (_stmt.getLong(_columnIndexOfSessionIndex));
          final boolean _tmpHasThread;
          final int _tmp;
          _tmp = (int) (_stmt.getLong(_columnIndexOfHasThread));
          _tmpHasThread = _tmp != 0;
          final String _tmpStartTime;
          if (_stmt.isNull(_columnIndexOfStartTime)) {
            _tmpStartTime = null;
          } else {
            _tmpStartTime = _stmt.getText(_columnIndexOfStartTime);
          }
          final String _tmpEndTime;
          if (_stmt.isNull(_columnIndexOfEndTime)) {
            _tmpEndTime = null;
          } else {
            _tmpEndTime = _stmt.getText(_columnIndexOfEndTime);
          }
          final int _tmpDurationSeconds;
          _tmpDurationSeconds = (int) (_stmt.getLong(_columnIndexOfDurationSeconds));
          final float _tmpAvgPressure;
          _tmpAvgPressure = (float) (_stmt.getDouble(_columnIndexOfAvgPressure));
          final float _tmpMaxPressure;
          _tmpMaxPressure = (float) (_stmt.getDouble(_columnIndexOfMaxPressure));
          final float _tmpMinPressure;
          _tmpMinPressure = (float) (_stmt.getDouble(_columnIndexOfMinPressure));
          final String _tmpEmotion;
          if (_stmt.isNull(_columnIndexOfEmotion)) {
            _tmpEmotion = null;
          } else {
            _tmpEmotion = _stmt.getText(_columnIndexOfEmotion);
          }
          final String _tmpThreadColor;
          if (_stmt.isNull(_columnIndexOfThreadColor)) {
            _tmpThreadColor = null;
          } else {
            _tmpThreadColor = _stmt.getText(_columnIndexOfThreadColor);
          }
          final String _tmpThreadColorName;
          if (_stmt.isNull(_columnIndexOfThreadColorName)) {
            _tmpThreadColorName = null;
          } else {
            _tmpThreadColorName = _stmt.getText(_columnIndexOfThreadColorName);
          }
          final String _tmpAiFeedback;
          if (_stmt.isNull(_columnIndexOfAiFeedback)) {
            _tmpAiFeedback = null;
          } else {
            _tmpAiFeedback = _stmt.getText(_columnIndexOfAiFeedback);
          }
          final long _tmpCreatedAt;
          _tmpCreatedAt = _stmt.getLong(_columnIndexOfCreatedAt);
          _item = new Session(_tmpId,_tmpDate,_tmpSessionIndex,_tmpHasThread,_tmpStartTime,_tmpEndTime,_tmpDurationSeconds,_tmpAvgPressure,_tmpMaxPressure,_tmpMinPressure,_tmpEmotion,_tmpThreadColor,_tmpThreadColorName,_tmpAiFeedback,_tmpCreatedAt);
          _result.add(_item);
        }
        return _result;
      } finally {
        _stmt.close();
      }
    }, $completion);
  }

  @Override
  public Object countSessionsByDate(final String date,
      final Continuation<? super Integer> $completion) {
    final String _sql = "SELECT COUNT(*) FROM sessions WHERE date = ?";
    return DBUtil.performSuspending(__db, true, false, (_connection) -> {
      final SQLiteStatement _stmt = _connection.prepare(_sql);
      try {
        int _argIndex = 1;
        if (date == null) {
          _stmt.bindNull(_argIndex);
        } else {
          _stmt.bindText(_argIndex, date);
        }
        final Integer _result;
        if (_stmt.step()) {
          final Integer _tmp;
          if (_stmt.isNull(0)) {
            _tmp = null;
          } else {
            _tmp = (int) (_stmt.getLong(0));
          }
          _result = _tmp;
        } else {
          _result = null;
        }
        return _result;
      } finally {
        _stmt.close();
      }
    }, $completion);
  }

  @Override
  public Object getThreadSessionByDate(final String date,
      final Continuation<? super Session> $completion) {
    final String _sql = "SELECT * FROM sessions WHERE date = ? AND hasThread = 1 LIMIT 1";
    return DBUtil.performSuspending(__db, true, false, (_connection) -> {
      final SQLiteStatement _stmt = _connection.prepare(_sql);
      try {
        int _argIndex = 1;
        if (date == null) {
          _stmt.bindNull(_argIndex);
        } else {
          _stmt.bindText(_argIndex, date);
        }
        final int _columnIndexOfId = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "id");
        final int _columnIndexOfDate = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "date");
        final int _columnIndexOfSessionIndex = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "sessionIndex");
        final int _columnIndexOfHasThread = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "hasThread");
        final int _columnIndexOfStartTime = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "startTime");
        final int _columnIndexOfEndTime = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "endTime");
        final int _columnIndexOfDurationSeconds = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "durationSeconds");
        final int _columnIndexOfAvgPressure = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "avgPressure");
        final int _columnIndexOfMaxPressure = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "maxPressure");
        final int _columnIndexOfMinPressure = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "minPressure");
        final int _columnIndexOfEmotion = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "emotion");
        final int _columnIndexOfThreadColor = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "threadColor");
        final int _columnIndexOfThreadColorName = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "threadColorName");
        final int _columnIndexOfAiFeedback = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "aiFeedback");
        final int _columnIndexOfCreatedAt = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "createdAt");
        final Session _result;
        if (_stmt.step()) {
          final long _tmpId;
          _tmpId = _stmt.getLong(_columnIndexOfId);
          final String _tmpDate;
          if (_stmt.isNull(_columnIndexOfDate)) {
            _tmpDate = null;
          } else {
            _tmpDate = _stmt.getText(_columnIndexOfDate);
          }
          final int _tmpSessionIndex;
          _tmpSessionIndex = (int) (_stmt.getLong(_columnIndexOfSessionIndex));
          final boolean _tmpHasThread;
          final int _tmp;
          _tmp = (int) (_stmt.getLong(_columnIndexOfHasThread));
          _tmpHasThread = _tmp != 0;
          final String _tmpStartTime;
          if (_stmt.isNull(_columnIndexOfStartTime)) {
            _tmpStartTime = null;
          } else {
            _tmpStartTime = _stmt.getText(_columnIndexOfStartTime);
          }
          final String _tmpEndTime;
          if (_stmt.isNull(_columnIndexOfEndTime)) {
            _tmpEndTime = null;
          } else {
            _tmpEndTime = _stmt.getText(_columnIndexOfEndTime);
          }
          final int _tmpDurationSeconds;
          _tmpDurationSeconds = (int) (_stmt.getLong(_columnIndexOfDurationSeconds));
          final float _tmpAvgPressure;
          _tmpAvgPressure = (float) (_stmt.getDouble(_columnIndexOfAvgPressure));
          final float _tmpMaxPressure;
          _tmpMaxPressure = (float) (_stmt.getDouble(_columnIndexOfMaxPressure));
          final float _tmpMinPressure;
          _tmpMinPressure = (float) (_stmt.getDouble(_columnIndexOfMinPressure));
          final String _tmpEmotion;
          if (_stmt.isNull(_columnIndexOfEmotion)) {
            _tmpEmotion = null;
          } else {
            _tmpEmotion = _stmt.getText(_columnIndexOfEmotion);
          }
          final String _tmpThreadColor;
          if (_stmt.isNull(_columnIndexOfThreadColor)) {
            _tmpThreadColor = null;
          } else {
            _tmpThreadColor = _stmt.getText(_columnIndexOfThreadColor);
          }
          final String _tmpThreadColorName;
          if (_stmt.isNull(_columnIndexOfThreadColorName)) {
            _tmpThreadColorName = null;
          } else {
            _tmpThreadColorName = _stmt.getText(_columnIndexOfThreadColorName);
          }
          final String _tmpAiFeedback;
          if (_stmt.isNull(_columnIndexOfAiFeedback)) {
            _tmpAiFeedback = null;
          } else {
            _tmpAiFeedback = _stmt.getText(_columnIndexOfAiFeedback);
          }
          final long _tmpCreatedAt;
          _tmpCreatedAt = _stmt.getLong(_columnIndexOfCreatedAt);
          _result = new Session(_tmpId,_tmpDate,_tmpSessionIndex,_tmpHasThread,_tmpStartTime,_tmpEndTime,_tmpDurationSeconds,_tmpAvgPressure,_tmpMaxPressure,_tmpMinPressure,_tmpEmotion,_tmpThreadColor,_tmpThreadColorName,_tmpAiFeedback,_tmpCreatedAt);
        } else {
          _result = null;
        }
        return _result;
      } finally {
        _stmt.close();
      }
    }, $completion);
  }

  @Override
  public Object getSessionsInRange(final String start, final String end,
      final Continuation<? super List<Session>> $completion) {
    final String _sql = "SELECT * FROM sessions WHERE date BETWEEN ? AND ? ORDER BY date ASC, createdAt ASC";
    return DBUtil.performSuspending(__db, true, false, (_connection) -> {
      final SQLiteStatement _stmt = _connection.prepare(_sql);
      try {
        int _argIndex = 1;
        if (start == null) {
          _stmt.bindNull(_argIndex);
        } else {
          _stmt.bindText(_argIndex, start);
        }
        _argIndex = 2;
        if (end == null) {
          _stmt.bindNull(_argIndex);
        } else {
          _stmt.bindText(_argIndex, end);
        }
        final int _columnIndexOfId = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "id");
        final int _columnIndexOfDate = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "date");
        final int _columnIndexOfSessionIndex = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "sessionIndex");
        final int _columnIndexOfHasThread = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "hasThread");
        final int _columnIndexOfStartTime = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "startTime");
        final int _columnIndexOfEndTime = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "endTime");
        final int _columnIndexOfDurationSeconds = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "durationSeconds");
        final int _columnIndexOfAvgPressure = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "avgPressure");
        final int _columnIndexOfMaxPressure = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "maxPressure");
        final int _columnIndexOfMinPressure = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "minPressure");
        final int _columnIndexOfEmotion = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "emotion");
        final int _columnIndexOfThreadColor = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "threadColor");
        final int _columnIndexOfThreadColorName = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "threadColorName");
        final int _columnIndexOfAiFeedback = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "aiFeedback");
        final int _columnIndexOfCreatedAt = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "createdAt");
        final List<Session> _result = new ArrayList<Session>();
        while (_stmt.step()) {
          final Session _item;
          final long _tmpId;
          _tmpId = _stmt.getLong(_columnIndexOfId);
          final String _tmpDate;
          if (_stmt.isNull(_columnIndexOfDate)) {
            _tmpDate = null;
          } else {
            _tmpDate = _stmt.getText(_columnIndexOfDate);
          }
          final int _tmpSessionIndex;
          _tmpSessionIndex = (int) (_stmt.getLong(_columnIndexOfSessionIndex));
          final boolean _tmpHasThread;
          final int _tmp;
          _tmp = (int) (_stmt.getLong(_columnIndexOfHasThread));
          _tmpHasThread = _tmp != 0;
          final String _tmpStartTime;
          if (_stmt.isNull(_columnIndexOfStartTime)) {
            _tmpStartTime = null;
          } else {
            _tmpStartTime = _stmt.getText(_columnIndexOfStartTime);
          }
          final String _tmpEndTime;
          if (_stmt.isNull(_columnIndexOfEndTime)) {
            _tmpEndTime = null;
          } else {
            _tmpEndTime = _stmt.getText(_columnIndexOfEndTime);
          }
          final int _tmpDurationSeconds;
          _tmpDurationSeconds = (int) (_stmt.getLong(_columnIndexOfDurationSeconds));
          final float _tmpAvgPressure;
          _tmpAvgPressure = (float) (_stmt.getDouble(_columnIndexOfAvgPressure));
          final float _tmpMaxPressure;
          _tmpMaxPressure = (float) (_stmt.getDouble(_columnIndexOfMaxPressure));
          final float _tmpMinPressure;
          _tmpMinPressure = (float) (_stmt.getDouble(_columnIndexOfMinPressure));
          final String _tmpEmotion;
          if (_stmt.isNull(_columnIndexOfEmotion)) {
            _tmpEmotion = null;
          } else {
            _tmpEmotion = _stmt.getText(_columnIndexOfEmotion);
          }
          final String _tmpThreadColor;
          if (_stmt.isNull(_columnIndexOfThreadColor)) {
            _tmpThreadColor = null;
          } else {
            _tmpThreadColor = _stmt.getText(_columnIndexOfThreadColor);
          }
          final String _tmpThreadColorName;
          if (_stmt.isNull(_columnIndexOfThreadColorName)) {
            _tmpThreadColorName = null;
          } else {
            _tmpThreadColorName = _stmt.getText(_columnIndexOfThreadColorName);
          }
          final String _tmpAiFeedback;
          if (_stmt.isNull(_columnIndexOfAiFeedback)) {
            _tmpAiFeedback = null;
          } else {
            _tmpAiFeedback = _stmt.getText(_columnIndexOfAiFeedback);
          }
          final long _tmpCreatedAt;
          _tmpCreatedAt = _stmt.getLong(_columnIndexOfCreatedAt);
          _item = new Session(_tmpId,_tmpDate,_tmpSessionIndex,_tmpHasThread,_tmpStartTime,_tmpEndTime,_tmpDurationSeconds,_tmpAvgPressure,_tmpMaxPressure,_tmpMinPressure,_tmpEmotion,_tmpThreadColor,_tmpThreadColorName,_tmpAiFeedback,_tmpCreatedAt);
          _result.add(_item);
        }
        return _result;
      } finally {
        _stmt.close();
      }
    }, $completion);
  }

  @Override
  public Object getSessionsByMonth(final String monthPrefix,
      final Continuation<? super List<Session>> $completion) {
    final String _sql = "SELECT * FROM sessions WHERE date LIKE ? ORDER BY date ASC, createdAt ASC";
    return DBUtil.performSuspending(__db, true, false, (_connection) -> {
      final SQLiteStatement _stmt = _connection.prepare(_sql);
      try {
        int _argIndex = 1;
        if (monthPrefix == null) {
          _stmt.bindNull(_argIndex);
        } else {
          _stmt.bindText(_argIndex, monthPrefix);
        }
        final int _columnIndexOfId = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "id");
        final int _columnIndexOfDate = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "date");
        final int _columnIndexOfSessionIndex = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "sessionIndex");
        final int _columnIndexOfHasThread = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "hasThread");
        final int _columnIndexOfStartTime = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "startTime");
        final int _columnIndexOfEndTime = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "endTime");
        final int _columnIndexOfDurationSeconds = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "durationSeconds");
        final int _columnIndexOfAvgPressure = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "avgPressure");
        final int _columnIndexOfMaxPressure = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "maxPressure");
        final int _columnIndexOfMinPressure = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "minPressure");
        final int _columnIndexOfEmotion = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "emotion");
        final int _columnIndexOfThreadColor = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "threadColor");
        final int _columnIndexOfThreadColorName = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "threadColorName");
        final int _columnIndexOfAiFeedback = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "aiFeedback");
        final int _columnIndexOfCreatedAt = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "createdAt");
        final List<Session> _result = new ArrayList<Session>();
        while (_stmt.step()) {
          final Session _item;
          final long _tmpId;
          _tmpId = _stmt.getLong(_columnIndexOfId);
          final String _tmpDate;
          if (_stmt.isNull(_columnIndexOfDate)) {
            _tmpDate = null;
          } else {
            _tmpDate = _stmt.getText(_columnIndexOfDate);
          }
          final int _tmpSessionIndex;
          _tmpSessionIndex = (int) (_stmt.getLong(_columnIndexOfSessionIndex));
          final boolean _tmpHasThread;
          final int _tmp;
          _tmp = (int) (_stmt.getLong(_columnIndexOfHasThread));
          _tmpHasThread = _tmp != 0;
          final String _tmpStartTime;
          if (_stmt.isNull(_columnIndexOfStartTime)) {
            _tmpStartTime = null;
          } else {
            _tmpStartTime = _stmt.getText(_columnIndexOfStartTime);
          }
          final String _tmpEndTime;
          if (_stmt.isNull(_columnIndexOfEndTime)) {
            _tmpEndTime = null;
          } else {
            _tmpEndTime = _stmt.getText(_columnIndexOfEndTime);
          }
          final int _tmpDurationSeconds;
          _tmpDurationSeconds = (int) (_stmt.getLong(_columnIndexOfDurationSeconds));
          final float _tmpAvgPressure;
          _tmpAvgPressure = (float) (_stmt.getDouble(_columnIndexOfAvgPressure));
          final float _tmpMaxPressure;
          _tmpMaxPressure = (float) (_stmt.getDouble(_columnIndexOfMaxPressure));
          final float _tmpMinPressure;
          _tmpMinPressure = (float) (_stmt.getDouble(_columnIndexOfMinPressure));
          final String _tmpEmotion;
          if (_stmt.isNull(_columnIndexOfEmotion)) {
            _tmpEmotion = null;
          } else {
            _tmpEmotion = _stmt.getText(_columnIndexOfEmotion);
          }
          final String _tmpThreadColor;
          if (_stmt.isNull(_columnIndexOfThreadColor)) {
            _tmpThreadColor = null;
          } else {
            _tmpThreadColor = _stmt.getText(_columnIndexOfThreadColor);
          }
          final String _tmpThreadColorName;
          if (_stmt.isNull(_columnIndexOfThreadColorName)) {
            _tmpThreadColorName = null;
          } else {
            _tmpThreadColorName = _stmt.getText(_columnIndexOfThreadColorName);
          }
          final String _tmpAiFeedback;
          if (_stmt.isNull(_columnIndexOfAiFeedback)) {
            _tmpAiFeedback = null;
          } else {
            _tmpAiFeedback = _stmt.getText(_columnIndexOfAiFeedback);
          }
          final long _tmpCreatedAt;
          _tmpCreatedAt = _stmt.getLong(_columnIndexOfCreatedAt);
          _item = new Session(_tmpId,_tmpDate,_tmpSessionIndex,_tmpHasThread,_tmpStartTime,_tmpEndTime,_tmpDurationSeconds,_tmpAvgPressure,_tmpMaxPressure,_tmpMinPressure,_tmpEmotion,_tmpThreadColor,_tmpThreadColorName,_tmpAiFeedback,_tmpCreatedAt);
          _result.add(_item);
        }
        return _result;
      } finally {
        _stmt.close();
      }
    }, $completion);
  }

  @Override
  public Flow<List<Session>> getAllSessionsFlow() {
    final String _sql = "SELECT * FROM sessions ORDER BY createdAt DESC";
    return FlowUtil.createFlow(__db, false, new String[] {"sessions"}, (_connection) -> {
      final SQLiteStatement _stmt = _connection.prepare(_sql);
      try {
        final int _columnIndexOfId = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "id");
        final int _columnIndexOfDate = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "date");
        final int _columnIndexOfSessionIndex = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "sessionIndex");
        final int _columnIndexOfHasThread = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "hasThread");
        final int _columnIndexOfStartTime = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "startTime");
        final int _columnIndexOfEndTime = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "endTime");
        final int _columnIndexOfDurationSeconds = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "durationSeconds");
        final int _columnIndexOfAvgPressure = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "avgPressure");
        final int _columnIndexOfMaxPressure = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "maxPressure");
        final int _columnIndexOfMinPressure = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "minPressure");
        final int _columnIndexOfEmotion = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "emotion");
        final int _columnIndexOfThreadColor = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "threadColor");
        final int _columnIndexOfThreadColorName = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "threadColorName");
        final int _columnIndexOfAiFeedback = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "aiFeedback");
        final int _columnIndexOfCreatedAt = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "createdAt");
        final List<Session> _result = new ArrayList<Session>();
        while (_stmt.step()) {
          final Session _item;
          final long _tmpId;
          _tmpId = _stmt.getLong(_columnIndexOfId);
          final String _tmpDate;
          if (_stmt.isNull(_columnIndexOfDate)) {
            _tmpDate = null;
          } else {
            _tmpDate = _stmt.getText(_columnIndexOfDate);
          }
          final int _tmpSessionIndex;
          _tmpSessionIndex = (int) (_stmt.getLong(_columnIndexOfSessionIndex));
          final boolean _tmpHasThread;
          final int _tmp;
          _tmp = (int) (_stmt.getLong(_columnIndexOfHasThread));
          _tmpHasThread = _tmp != 0;
          final String _tmpStartTime;
          if (_stmt.isNull(_columnIndexOfStartTime)) {
            _tmpStartTime = null;
          } else {
            _tmpStartTime = _stmt.getText(_columnIndexOfStartTime);
          }
          final String _tmpEndTime;
          if (_stmt.isNull(_columnIndexOfEndTime)) {
            _tmpEndTime = null;
          } else {
            _tmpEndTime = _stmt.getText(_columnIndexOfEndTime);
          }
          final int _tmpDurationSeconds;
          _tmpDurationSeconds = (int) (_stmt.getLong(_columnIndexOfDurationSeconds));
          final float _tmpAvgPressure;
          _tmpAvgPressure = (float) (_stmt.getDouble(_columnIndexOfAvgPressure));
          final float _tmpMaxPressure;
          _tmpMaxPressure = (float) (_stmt.getDouble(_columnIndexOfMaxPressure));
          final float _tmpMinPressure;
          _tmpMinPressure = (float) (_stmt.getDouble(_columnIndexOfMinPressure));
          final String _tmpEmotion;
          if (_stmt.isNull(_columnIndexOfEmotion)) {
            _tmpEmotion = null;
          } else {
            _tmpEmotion = _stmt.getText(_columnIndexOfEmotion);
          }
          final String _tmpThreadColor;
          if (_stmt.isNull(_columnIndexOfThreadColor)) {
            _tmpThreadColor = null;
          } else {
            _tmpThreadColor = _stmt.getText(_columnIndexOfThreadColor);
          }
          final String _tmpThreadColorName;
          if (_stmt.isNull(_columnIndexOfThreadColorName)) {
            _tmpThreadColorName = null;
          } else {
            _tmpThreadColorName = _stmt.getText(_columnIndexOfThreadColorName);
          }
          final String _tmpAiFeedback;
          if (_stmt.isNull(_columnIndexOfAiFeedback)) {
            _tmpAiFeedback = null;
          } else {
            _tmpAiFeedback = _stmt.getText(_columnIndexOfAiFeedback);
          }
          final long _tmpCreatedAt;
          _tmpCreatedAt = _stmt.getLong(_columnIndexOfCreatedAt);
          _item = new Session(_tmpId,_tmpDate,_tmpSessionIndex,_tmpHasThread,_tmpStartTime,_tmpEndTime,_tmpDurationSeconds,_tmpAvgPressure,_tmpMaxPressure,_tmpMinPressure,_tmpEmotion,_tmpThreadColor,_tmpThreadColorName,_tmpAiFeedback,_tmpCreatedAt);
          _result.add(_item);
        }
        return _result;
      } finally {
        _stmt.close();
      }
    });
  }

  @Override
  public Object getDatesWithSessionsByMonth(final String monthPrefix,
      final Continuation<? super List<String>> $completion) {
    final String _sql = "SELECT DISTINCT date FROM sessions WHERE date LIKE ? ORDER BY date ASC";
    return DBUtil.performSuspending(__db, true, false, (_connection) -> {
      final SQLiteStatement _stmt = _connection.prepare(_sql);
      try {
        int _argIndex = 1;
        if (monthPrefix == null) {
          _stmt.bindNull(_argIndex);
        } else {
          _stmt.bindText(_argIndex, monthPrefix);
        }
        final List<String> _result = new ArrayList<String>();
        while (_stmt.step()) {
          final String _item;
          if (_stmt.isNull(0)) {
            _item = null;
          } else {
            _item = _stmt.getText(0);
          }
          _result.add(_item);
        }
        return _result;
      } finally {
        _stmt.close();
      }
    }, $completion);
  }

  @Override
  public Object getTotalActiveDays(final Continuation<? super Integer> $completion) {
    final String _sql = "SELECT COUNT(DISTINCT date) FROM sessions";
    return DBUtil.performSuspending(__db, true, false, (_connection) -> {
      final SQLiteStatement _stmt = _connection.prepare(_sql);
      try {
        final Integer _result;
        if (_stmt.step()) {
          final Integer _tmp;
          if (_stmt.isNull(0)) {
            _tmp = null;
          } else {
            _tmp = (int) (_stmt.getLong(0));
          }
          _result = _tmp;
        } else {
          _result = null;
        }
        return _result;
      } finally {
        _stmt.close();
      }
    }, $completion);
  }

  @Override
  public Object updateEmotion(final long id, final String emotion,
      final Continuation<? super Unit> $completion) {
    final String _sql = "UPDATE sessions SET emotion = ? WHERE id = ?";
    return DBUtil.performSuspending(__db, false, true, (_connection) -> {
      final SQLiteStatement _stmt = _connection.prepare(_sql);
      try {
        int _argIndex = 1;
        if (emotion == null) {
          _stmt.bindNull(_argIndex);
        } else {
          _stmt.bindText(_argIndex, emotion);
        }
        _argIndex = 2;
        _stmt.bindLong(_argIndex, id);
        _stmt.step();
        return Unit.INSTANCE;
      } finally {
        _stmt.close();
      }
    }, $completion);
  }

  @NonNull
  public static List<Class<?>> getRequiredConverters() {
    return Collections.emptyList();
  }
}
